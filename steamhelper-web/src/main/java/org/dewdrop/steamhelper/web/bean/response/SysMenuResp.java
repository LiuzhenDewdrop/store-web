package org.dewdrop.steamhelper.web.bean.response;

import java.io.Serializable;
import java.util.List;

import org.dewdrop.steamhelper.entity.SysMenu;

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
