package com.wur7.store.web.emuns;

import com.wur7.store.entity.SysItemCategory;

public enum Category {
	ACTIVITY(1000001, 0, "活动", 1),
	COURSE(1000002, 0, "课程", 2),
	;
	private final int id;
	private final int pid;
	private final String name;
	
	private final int itemType;
	
	Category(int id, int pid, String name, int itemType) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.itemType = itemType;
	}
	
	public int getId() {
		return id;
	}
	
	public int getPid() {
		return pid;
	}
	
	public String getName() {
		return name;
	}
	
	public int getItemType() {
		return itemType;
	}
	
	public static Category fromId(int id) {
		for (Category cat : values()) {
			if (cat.id == id) {
				return cat;
			}
		}
		return null;
	}
	
	public static SysItemCategory toEntity(Category cat) {
		SysItemCategory entity = new SysItemCategory();
		entity.setId(cat.id);
		entity.setpId(cat.pid);
		entity.setCategoryName(cat.name);
		return entity;
	}
}
