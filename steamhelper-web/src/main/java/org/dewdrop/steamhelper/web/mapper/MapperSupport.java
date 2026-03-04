package org.dewdrop.steamhelper.web.mapper;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.mapper.ResAccountMapper;
import org.dewdrop.steamhelper.mapper.ResGameAchievementMapper;
import org.dewdrop.steamhelper.mapper.ResGameClassificationMapper;
import org.dewdrop.steamhelper.mapper.ResGameDlcMapper;
import org.dewdrop.steamhelper.mapper.ResGameMapper;
import org.dewdrop.steamhelper.mapper.SysDictMapper;
import org.dewdrop.steamhelper.mapper.SysMenuMapper;
import org.dewdrop.steamhelper.mapper.SysRoleMapper;
import org.dewdrop.steamhelper.mapper.SysRoleMenuMapper;
import org.dewdrop.steamhelper.mapper.SysUserMapper;
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
	@Resource
	public ResGameMapper resGameMapper;
	@Resource
	public ResGameDlcMapper resGameDlcMapper;
	@Resource
	public ResGameAchievementMapper resGameAchievementMapper;
}
