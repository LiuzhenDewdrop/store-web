package com.wur7.store.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wur7.store.entity.SysItemCategory;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class SysItemCategoryService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public List<SysItemCategory> findByPid(Integer pId) {
		return mapperSupport.sysItemCategoryMapper.findByPid(pId);
	}
}
