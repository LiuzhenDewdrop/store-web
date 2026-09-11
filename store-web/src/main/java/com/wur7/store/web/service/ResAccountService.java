package com.wur7.store.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ResAccount;
import com.wur7.store.entity.SysUser;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ResAccountService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<ResAccount> allOfPage(ResAccount query, LayPage layPage, SysUser operator) {
		query.setUserId(operator.getId());
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ResAccount> list = mapperSupport.resAccountMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(ResAccount acc, boolean saveFlag) {
		int i;
		if (saveFlag) {
			i = mapperSupport.resAccountMapper.insert(acc);
		} else {
			i = mapperSupport.resAccountMapper.updateByPrimaryKey(acc);
		}
		return i == 1 ? IReturnBean.success() : IReturnBean.fail();
	}
	
	public ResAccount selectByPrimaryKey(Integer id) {
		return mapperSupport.resAccountMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		mapperSupport.resAccountMapper.deleteByPrimaryKey(id);
		return IReturnBean.success();
	}
	
	public ResAccount getUserMainAccount(Integer userId, Integer platformId) {
		if (userId == null || platformId == null) {
			return null;
		}
		return mapperSupport.resAccountMapper.getUserMainAccount(userId, platformId);
	}
}
