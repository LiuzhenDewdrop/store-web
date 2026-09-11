package com.wur7.store.web.mapper;

import javax.annotation.Resource;

import com.wur7.store.mapper.ResAccountMapper;
import com.wur7.store.mapper.ResGameClassificationMapper;
import com.wur7.store.mapper.SysDictMapper;
import com.wur7.store.mapper.SysMenuMapper;
import com.wur7.store.mapper.SysRoleMapper;
import com.wur7.store.mapper.SysRoleMenuMapper;
import com.wur7.store.mapper.SysUserMapper;

import org.springframework.stereotype.Component;

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
	public ResAccountMapper resAccountMapper;
	@Resource
	public ResGameClassificationMapper resGameClassificationMapper;
}
