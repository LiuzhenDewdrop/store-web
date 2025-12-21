package org.dewdrop.steamhelper.web.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.SysRole;
import org.dewdrop.steamhelper.entity.SysUser;
import org.dewdrop.steamhelper.enums.IReturnEnum;
import org.dewdrop.steamhelper.util.constant.CommonConstant;
import org.dewdrop.steamhelper.util.util.MD5Util;
import org.dewdrop.steamhelper.util.util.StringUtil;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class SysUserService {
	
	@Resource
	private FileService fileService;
	
	@Resource
	private MapperSupport mapperSupport;
	
	public SysUser findLoginUser(String name) {
		return mapperSupport.sysUserMapper.findLoginUser(name, CommonConstant.ENABLE);
	}
	
	public PageInfo<SysUser> allOfPage(SysUser query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(),layPage.getLimit());
		List<SysUser> list = mapperSupport.sysUserMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(SysUser user, boolean saveFlag, SysUser operator) {
		int i;
		String loginName = StringUtil.trimToNull(user.getLoginName());
		String userName = StringUtil.trimToNull(user.getUserName());
		if (StringUtil.isBlank(loginName) || StringUtil.isBlank(userName)) {
			return IReturnBean.fail("登录账号和用户昵称均不能为空");
		}
		user.setLoginName(loginName);
		user.setUserName(userName);
		if (null != user.getPhoneNo() && StringUtil.isBlank(user.getPhoneNo())) {
			return IReturnBean.fail("电话号码格式错误");
		}
		if (null != user.getEmail() && StringUtil.isBlank(user.getEmail())) {
			return IReturnBean.fail("电子邮箱格式错误");
		}
		user.setPhoneNo(StringUtil.trim(user.getPhoneNo()));
		user.setEmail(StringUtil.trim(user.getEmail()));
		if (!checkInfo(user)) {
			return IReturnBean.fail("用户信息重复");
		}
		user.setCreator(operator.getId());
		user.setCreateTime(new Date());
		if (saveFlag) {
			user.setPassword(MD5Util.md5("123456"));
			user.setRoleId(2);
			user.setStatus(CommonConstant.ENABLE);
			i = mapperSupport.sysUserMapper.insert(user);
		} else {
			// 校验权限
			if (checkRole(user, operator)) {
				return IReturnBean.get(IReturnEnum.INADEQUATE_PERMISSIONS);
			}
			user.setPassword(null);
			i = mapperSupport.sysUserMapper.updateByPrimaryKeySelective(user);
		}
		return i == 1 ? IReturnBean.success(): IReturnBean.fail();
	}
	
	public SysUser selectByPrimaryKey(Integer id) {
		return mapperSupport.sysUserMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id, SysUser operator) {
		// 校验权限
		SysUser user = mapperSupport.sysUserMapper.selectByPrimaryKey(id);
		if (checkRole(user, operator)) {
			return IReturnBean.get(IReturnEnum.INADEQUATE_PERMISSIONS);
		}
		mapperSupport.sysUserMapper.deleteByPrimaryKey(id);
		return IReturnBean.success();
	}
	
	public boolean checkInfo(SysUser user) {
		SysUser sysUser = null;
		if (StringUtil.isNotBlank(user.getLoginName())) {
			sysUser = mapperSupport.sysUserMapper.findLoginUser(user.getLoginName(), null);
			if (sysUser != null) {
				return false;
			}
		}
		if (StringUtil.isNotBlank(user.getUserName())) {
			sysUser = mapperSupport.sysUserMapper.findLoginUser(user.getUserName(), null);
			if (sysUser != null) {
				return false;
			}
		}
		if (StringUtil.isNotBlank(user.getPhoneNo())) {
			sysUser = mapperSupport.sysUserMapper.findLoginUser(user.getPhoneNo(), null);
			if (sysUser != null) {
				return false;
			}
		}
		if (StringUtil.isNotBlank(user.getEmail())) {
			sysUser = mapperSupport.sysUserMapper.findLoginUser(user.getEmail(), null);
			return sysUser == null;
		}
		return true;
	}
	
	public boolean checkRole(SysUser user, SysUser operator) {
		SysRole userRole = mapperSupport.sysRoleMapper.selectByPrimaryKey(user.getRoleId());
		SysRole operatorRole = mapperSupport.sysRoleMapper.selectByPrimaryKey(operator.getRoleId());
		return operatorRole.getLevel() > userRole.getLevel();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> updPwd(SysUser user, String newPassword, SysUser operator) {
		if(!operator.getUserName().equals(user.getUserName())){
			return IReturnBean.fail("禁止修改他人账户密码");
		}
		if(!operator.getPassword().equals(MD5Util.md5(user.getPassword()))){
			return IReturnBean.fail("原始密码错误");
		}
		SysUser upd = new SysUser();
		upd.setId(user.getId());
		upd.setPassword(MD5Util.md5(newPassword));
		mapperSupport.sysUserMapper.updateByPrimaryKeySelective(upd);
		return IReturnBean.success();
	}
	
	public IReturnBean<String> updAvatar(SysUser user, MultipartFile file) throws IOException {
		IReturnBean<String> saveFile = fileService.saveFile("avatar", file);
		if (saveFile.isNotSuccess()) {
			return saveFile;
		}
		String localUrl = saveFile.getData();
		SysUser upd = new SysUser();
		upd.setId(user.getId());
		upd.setAvatarType(1);
		upd.setAvatar(localUrl);
		mapperSupport.sysUserMapper.updateByPrimaryKeySelective(upd);
		return IReturnBean.success(localUrl);
	}
	
}
