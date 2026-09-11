package com.wur7.store.web.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.SysDict;
import com.wur7.store.entity.SysRole;
import com.wur7.store.entity.SysRoleMenu;
import com.wur7.store.entity.SysUser;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class SysRoleService {
	
	@Resource
	private SysDictService  sysDictService;
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<SysRole> allOfPage(SysRole query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<SysRole> list = mapperSupport.sysRoleMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(SysRole role, boolean saveFlag, SysUser operator) {
		SysRole opRole = mapperSupport.sysRoleMapper.selectByPrimaryKey(operator.getRoleId());
		int i;
		if (saveFlag) {
			i = mapperSupport.sysRoleMapper.insert(role);
		} else {
			i = mapperSupport.sysRoleMapper.updateByPrimaryKey(role);
		}
		return i == 1 ? IReturnBean.success() : IReturnBean.fail();
	}
	
	public SysRole selectByPrimaryKey(Integer id) {
		return mapperSupport.sysRoleMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		if (id == CommonConstant.ROLE_ID_SUPER_ADMIN || id == CommonConstant.ROLE_ID_GUEST) {
			return IReturnBean.fail("不能删除该角色");
		}
		SysRole role = mapperSupport.sysRoleMapper.selectByPrimaryKey(id);
		mapperSupport.sysRoleMapper.deleteByPrimaryKey(id);
		SysDict sysDict = new SysDict();
//		sysDict.setId();
		sysDict.setDictGroup(CommonConstant.DICT_GROUP_ROLE);
		sysDict.setDictKey(role.getRoleName());
//		sysDict.setDictValue();
//		sysDict.setDictSort();
		sysDictService.delDict(sysDict);
		return IReturnBean.success();
	}
	
	public IReturnBean<?> grant(Integer roleId, List<Integer> menuIds, SysUser operator) {
		// 判断授权资格
		if (roleId == CommonConstant.ROLE_ID_SUPER_ADMIN) {
			return IReturnBean.fail("超级管理员无需授权");
		}
		SysRole creatorRole = mapperSupport.sysRoleMapper.selectByPrimaryKey(operator.getRoleId());
		SysRole desRole = mapperSupport.sysRoleMapper.selectByPrimaryKey(roleId);
		// 授权
		mapperSupport.sysRoleMenuMapper.delByRoleId(roleId);
		for (Integer menuId : menuIds) {
			SysRoleMenu rm = new SysRoleMenu();
			rm.setRoleId(roleId);
			rm.setMenuId(menuId);
			rm.setCreator(operator.getId());
			rm.setCreateTime(new Date());
			mapperSupport.sysRoleMenuMapper.insert(rm);
		}
		return IReturnBean.success();
	}
	
	public List<SysRole> findAll() {
		return mapperSupport.sysRoleMapper.findAll(null);
	}
}
