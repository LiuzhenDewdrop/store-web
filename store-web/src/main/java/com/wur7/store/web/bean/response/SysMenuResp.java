package com.wur7.store.web.bean.response;

import java.io.Serializable;
import java.util.List;

import com.wur7.store.entity.SysMenu;

import lombok.Data;

@Data
public class SysMenuResp extends SysMenu implements Serializable {

	private boolean checked;
	private boolean spread = true;
	private List<SysMenuResp> children;
	
	public static SysMenuResp createTreeNode(SysMenu m, boolean checked) {
		SysMenuResp node = new SysMenuResp();
		node.setId(m.getId());
		node.setName(m.getName() + " (" + m.getPermission() + ")");
		node.setChecked(checked);
		return node;
	}
}
