package org.dewdrop.steamhelper.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResAccount;
import org.dewdrop.steamhelper.entity.SysRole;
import org.dewdrop.steamhelper.entity.SysUser;
import org.dewdrop.steamhelper.util.constant.CommonConstant;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ResAccountService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<ResAccount> allOfPage(ResAccount query, LayPage layPage, SysUser operator) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		SysRole operatorRole = mapperSupport.sysRoleMapper.selectByPrimaryKey(operator.getRoleId());
		if (operatorRole.getLevel() < CommonConstant.ROLE_NORMAL_LINE) {
			query.setUserId(operator.getId());
		}
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
}
