package org.dewdrop.steamhelper.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.entity.SysMenu;
import org.dewdrop.steamhelper.util.constant.CommonConstant;
import org.dewdrop.steamhelper.util.util.CollectionUtil;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.bean.response.SysMenuResp;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class SysMenuService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public List<SysMenu> getListByRole(Integer roleId) {
		if (roleId == CommonConstant.ROLE_ID_SUPER_ADMIN) {
			return mapperSupport.sysMenuMapper.findAll(new SysMenu());
		}
		return mapperSupport.sysMenuMapper.getListByRole(roleId);
	}

	public List<SysMenu> getListByParent(Integer pid, Integer roleId) {
		if (roleId == 1) {
			return mapperSupport.sysMenuMapper.getListByParent(pid);
		}
		return mapperSupport.sysMenuMapper.getListByParentRole(pid, roleId);
	}
	
	public List<SysMenuResp> getLeft(Integer roleId) {
		// 一级菜单
		List<SysMenu> menus = getListByParent(0, roleId);
		if (CollectionUtil.isNotEmpty(menus)) {
			List<SysMenuResp> resultList = new ArrayList<>(menus.size());
			for (SysMenu menu : menus) {
				SysMenuResp parent  = new SysMenuResp();
				parent.setName(menu.getName());
				parent.setpId(menu.getpId());
				parent.setIcon(menu.getIcon());
				// 二级菜单
				List<SysMenu> children = getListByParent(menu.getId(), roleId);
				if(CollectionUtil.isNotEmpty(children)) {
					List<SysMenuResp> childList = new ArrayList<>(children.size());
					for (SysMenu menu1 : children) {
						SysMenuResp child = new SysMenuResp();
						child.setName(menu1.getName());
						child.setUrl(menu1.getUrl());
						child.setIcon(menu1.getIcon());
						childList.add(child);
					}
					parent.setChildren(childList);
				}
				resultList.add(parent);
			}
			return resultList;
		}
		return null;
	}
	
	public PageInfo<SysMenu> allOfPage(SysMenu query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(),layPage.getLimit());
		List<SysMenu> list = mapperSupport.sysMenuMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public boolean saveOrUpd(SysMenu menu, boolean saveFlag) {
		int i;
		menu.setCreateTime(new Date());
		if (saveFlag) {
			i = mapperSupport.sysMenuMapper.insert(menu);
		} else {
			i = mapperSupport.sysMenuMapper.updateByPrimaryKey(menu);
		}
		return i == 1;
	}
	
	public SysMenu selectByPrimaryKey(Integer id) {
		return mapperSupport.sysMenuMapper.selectByPrimaryKey(id);
	}
	
	public void del(Integer id) {
		mapperSupport.sysMenuMapper.deleteByPrimaryKey(id);
	}
	
	public List<SysMenu> getHigherLevel(Integer nowLevel) {
		if (nowLevel == null) {
			return null;
		}
		return mapperSupport.sysMenuMapper.getListByLevel(nowLevel - 1);
	}
	
	public List<SysMenuResp> getTree(Integer roleId) {
		boolean skip = roleId == 1;
		Set<Integer> menuIds = null;
		if (!skip) {
			menuIds = mapperSupport.sysRoleMenuMapper.getMenuIdsByRole(roleId);
		}
		List<SysMenu> level_1 = mapperSupport.sysMenuMapper.getListByParent(0);
		if (CollectionUtil.isEmpty(level_1)) {
			return null;
		}
		List<SysMenuResp> tree = new ArrayList<>(level_1.size());
		for (SysMenu lev_1 : level_1) {
			SysMenuResp lv_1 = SysMenuResp.createTreeNode(lev_1, skip || menuIds.contains(lev_1.getId()));
			List<SysMenu> level_2 = mapperSupport.sysMenuMapper.getListByParent(lev_1.getId());
			if (CollectionUtil.isNotEmpty(level_2)) {
				List<SysMenuResp> children = new ArrayList<>(level_2.size());
				for (SysMenu lev_2 : level_2) {
					SysMenuResp lv_2 = SysMenuResp.createTreeNode(lev_2, skip || menuIds.contains(lev_2.getId()));
					List<SysMenu> level_3 = mapperSupport.sysMenuMapper.getListByParent(lev_2.getId());
					if (CollectionUtil.isNotEmpty(level_3)) {
						List<SysMenuResp> leaf = new ArrayList<>(level_3.size());
						for (SysMenu lev_3 : level_3) {
							leaf.add(SysMenuResp.createTreeNode(lev_3, skip || menuIds.contains(lev_3.getId())));
						}
						lv_2.setChildren(leaf);
					}
					children.add(lv_2);
				}
				lv_1.setChildren(children);
			}
			tree.add(lv_1);
		}
		return tree;
	}
}
