package org.dewdrop.steamhelper.web.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class LayTree implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String text;
	// open,closed
	private String state = "open";
	private boolean checked = false;
	private Object attributes;
	private List<LayTree> children;
	private String iconCls;
	private Integer pid;

}
