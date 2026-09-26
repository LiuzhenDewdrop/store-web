package com.wur7.store.web.mapper;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wur7.store.mapper.ShopItemMapper;
import com.wur7.store.mapper.ShopItemPicMapper;
import com.wur7.store.mapper.ShopItemSpecMapper;
import com.wur7.store.mapper.SysDictMapper;
import com.wur7.store.mapper.SysItemCategoryMapper;
import com.wur7.store.mapper.SysMenuMapper;
import com.wur7.store.mapper.SysRoleMapper;
import com.wur7.store.mapper.SysRoleMenuMapper;
import com.wur7.store.mapper.SysUserMapper;

@Component
public class MapperSupport {
	
	@Resource
	public SysUserMapper sysUserMapper;
	@Resource
	public SysMenuMapper sysMenuMapper;
	@Resource
	public SysRoleMapper sysRoleMapper;
	@Resource
	public SysRoleMenuMapper sysRoleMenuMapper;
	@Resource
	public SysDictMapper sysDictMapper;
	@Resource
	public SysItemCategoryMapper sysItemCategoryMapper;
	@Resource
	public ShopItemMapper shopItemMapper;
	@Resource
	public ShopItemSpecMapper shopItemSpecMapper;
	@Resource
	public ShopItemPicMapper shopItemPicMapper;
}
