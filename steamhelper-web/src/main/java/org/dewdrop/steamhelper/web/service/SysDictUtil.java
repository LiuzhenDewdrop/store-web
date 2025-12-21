package org.dewdrop.steamhelper.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import org.dewdrop.steamhelper.entity.SysDict;
import org.dewdrop.steamhelper.mapper.SysDictMapper;
import org.dewdrop.steamhelper.util.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SysDictUtil {
	
	private static Map<String, SysDict> sysDictMap = new HashMap<>();
	
	@Resource
	public SysDictMapper sysDictMapper;
	
	@PostConstruct
	public void init() {
		sysDictMap = new HashMap<>();
		List<SysDict> list = sysDictMapper.selectAll();
		if (list != null && list.size() > 0) {
			for (SysDict dict : list) {
				sysDictMap.put(dict.getDictKey(), dict);
			}
		}
	}
	
	public static String getValue(String key) {
		SysDict dict = sysDictMap.get(key);
		if (dict == null) {
			return null;
		}
		return dict.getDictValue();
	}
	
	public boolean putDict(String key, String value) {
		if (StringUtil.isBlank(key) || StringUtil.isBlank(value)) {
			return false;
		}
		SysDict dict = sysDictMap.get(key);
		if (dict != null && value.equals(dict.getDictValue())) {
			return true;
		}
		if (dict == null || StringUtil.isBlank(dict.getDictValue())) {
			SysDict record = new SysDict();
			record.setDictKey(key);
			record.setDictValue(value);
			sysDictMapper.insert(record);
		} else {
			dict.setDictValue(value);
			sysDictMapper.updateByPrimaryKey(dict);
		}
		init();
		return true;
	}
}
