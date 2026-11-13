package com.wur7.store.web.time.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wur7.store.web.service.ShopOrderService;
import com.wur7.store.web.time.BaseScheduledTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderTimeoutCloseTask implements BaseScheduledTask {
	
	@Resource
	private ShopOrderService shopOrderService;
	
	@Override
	public void execute(String... params) throws Exception {
		shopOrderService.closeExpiredUnpaid(200);
	}
}