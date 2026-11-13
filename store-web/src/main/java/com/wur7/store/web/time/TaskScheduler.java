package com.wur7.store.web.time;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import com.wur7.store.entity.ScheduledTask;
import com.wur7.store.mapper.ScheduledTaskMapper;
import com.wur7.store.util.util.IpUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskScheduler {
	
	private static final long SCAN_INTERVAL = 60_000L;      // 1 分钟扫一次
	private static final long LOCK_SECONDS  = 5 * 60L;     // 锁 5 分钟
	
	@Resource
	private ScheduledTaskMapper scheduledTaskMapper;
	
	@Resource
	private List<BaseScheduledTask> taskList;
	
	private Map<String, BaseScheduledTask> taskMap = new ConcurrentHashMap<>();
	
	private final ExecutorService executor = Executors.newFixedThreadPool(4);
	private volatile boolean running = true;
	private Thread scanThread;
	
	@PostConstruct
	public void start() {
		for (BaseScheduledTask t : taskList) {
			taskMap.put(t.getClass().getName(), t);   // task_key 用全类名
		}
		scanThread = new Thread(this::scanLoop, "task-scheduler");
		scanThread.setDaemon(true);
		scanThread.start();
		log.info("TaskScheduler started, tasks={}", taskMap.keySet());
	}
	
	@PreDestroy
	public void stop() {
		running = false;
		executor.shutdown();
	}
	
	private void scanLoop() {
		while (running) {
			try {
				List<ScheduledTask> list = scheduledTaskMapper.selectEnabled();
				Date now = new Date();
				for (ScheduledTask task : list) {
					if (task.getNextExecTime() == null || !task.getNextExecTime().after(now)) {
						dispatch(task);
					}
				}
			} catch (Exception e) {
				log.error("TaskScheduler scan error:", e);
			}
			try {
				Thread.sleep(SCAN_INTERVAL);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
	
	private void dispatch(ScheduledTask task) throws SocketException, UnknownHostException {
		BaseScheduledTask runner = taskMap.get(task.getTaskKey());
		if (runner == null) {
			log.warn("no BaseScheduledTask bean for taskKey={}", task.getTaskKey());
			return;
		}
		String owner = IpUtil.getLocalIP();
		Date lockUntil = new Date(System.currentTimeMillis() + LOCK_SECONDS * 1000);
		int locked = scheduledTaskMapper.tryLock(task.getTaskKey(), owner, lockUntil);
		if (locked == 0) {
			return;
		}
		executor.submit(() -> {
			try {
				runner.run();
			} catch (Exception e) {
				log.error("task execute error, taskKey=" + task.getTaskKey(), e);
			} finally {
				Date last = new Date();
				Date next = calcNext(task.getCron(), last);
				scheduledTaskMapper.finishTask(task.getTaskKey(), last, next);
			}
		});
	}
	
	private Date calcNext(String cron, Date from) {
		try {
			return new CronSequenceGenerator(cron).next(from);
		} catch (Exception e) {
			log.error("invalid cron: " + cron, e);
			return new Date(from.getTime() + 60_000L);
		}
	}
}