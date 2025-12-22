package org.dewdrop.steamhelper.web.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.dewdrop.steamhelper.entity.SysDict;
import org.dewdrop.steamhelper.mapper.SysDictMapper;
import org.dewdrop.steamhelper.util.util.CollectionUtil;
import org.dewdrop.steamhelper.util.util.JsonUtil;
import org.dewdrop.steamhelper.util.util.StringUtil;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SysDictUtil {
	
	private static Map<String, SortedSet<SysDict>> sysDictMap = new HashMap<>();
	
	@Resource
	public SysDictMapper sysDictMapper;
	
	@PostConstruct
	public void init() {
		sysDictMap = new HashMap<>();
		List<SysDict> list = sysDictMapper.findAll(new SysDict());
		if (CollectionUtil.isEmpty(list)) {
			return ;
		}
		for (SysDict dict : list) {
			if (sysDictMap.containsKey(dict.getDictGroup())) {
				sysDictMap.get(dict.getDictGroup()).add(dict);
			} else {
				SortedSet<SysDict> group = createSortedSet();
				group.add(dict);
				sysDictMap.put(dict.getDictGroup(), group);
			}
		}
		log.info("数据字典加载完毕：{}", JsonUtil.toJson(sysDictMap));
	}
	
	public static Map<String, SortedSet<SysDict>> getDict() {
		return sysDictMap;
	}
	
	public static SortedSet<SysDict> getGroup(String group) {
		return sysDictMap.get(group);
	}
	
	public static SysDict getDict(String group, String key) {
		SortedSet<SysDict> groupSet = getGroup(group);
		if (groupSet == null) {
			return null;
		}
		for (SysDict sysDict : groupSet) {
			if (sysDict.getDictKey().equals(key)) {
				return sysDict;
			}
		}
		return  null;
	}
	
	public static String getValue(String group, String key) {
		SysDict dict = getDict(group, key);
		if (dict == null) {
			return null;
		}
		return dict.getDictValue();
	}
	
	/**
	 * @title  putDict
	 * @description
	 * @param dict
	 * @return
	 */
	public static void putDict(SysDict dict) {
		if (dict == null || dict.getId() == null
			|| StringUtil.isBlank(dict.getDictGroup()) || StringUtil.isBlank(dict.getDictKey())
			|| StringUtil.isBlank(dict.getDictValue()) || dict.getDictSort() == null) {
			return;
		}
		SortedSet<SysDict> g = sysDictMap.get(dict.getDictGroup());
		if (g == null) {
			g = createSortedSet();
			g.add(dict);
			sysDictMap.put(dict.getDictGroup(), g);
			return;
		}
		g.add(dict);
	}
	
	public static void del(SysDict dict) {
		SortedSet<SysDict> g = sysDictMap.get(dict.getDictGroup());
		SysDict d = getDict(dict.getDictGroup(), dict.getDictKey());
		g.remove(d);
		if (g.isEmpty()) {
			sysDictMap.remove(dict.getDictGroup());
		}
	}
	
	private static SortedSet<SysDict> createSortedSet() {
		return new TreeSet<SysDict>(Comparator.comparing(SysDict::getDictSort)){
			@Override
			public String toString() {
				return super.toString();
			}
		};
	}
}
