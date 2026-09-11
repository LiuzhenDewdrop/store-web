package com.wur7.store.enums;

import com.wur7.store.constant.CommonConstant;
import com.wur7.store.util.util.StringUtil;

/**
 * @class:  DLC_TYPE
 * @description: DLC类型【1：流程/地图/角色相关；2：支持者包或皮肤；3：音轨；4：图集；0：未分类】
 * @see CommonConstant#DICT_GROUP_DLC_TYPE
 * @see CommonConstant#DICT_GROUP_DLC_ACHV
 * @author: L.zhen
 * @date:   2025/12/31 21:47
 */
public enum DLC_TYPE {
	
	/**
	 * DLC类型 DLC_TYPE 【未分类】 0
	 */
	INIT(CommonConstant.DLC_TYPE_INIT, CommonConstant.DLC_ACHV_INIT),
	/**
	 * DLC类型 DLC_TYPE 【流程/地图/角色】 1
	 */
	CORE(CommonConstant.DLC_TYPE_CORE, CommonConstant.DLC_ACHV_INIT),
	/**
	 * DLC类型 DLC_TYPE 【支持者包或皮肤】 2
	 */
	SUPPORT(CommonConstant.DLC_TYPE_SUPPORT, CommonConstant.DLC_ACHV_NO),
	/**
	 * DLC类型 DLC_TYPE 【数字附加】 3
	 */
	DIGITAL(CommonConstant.DLC_TYPE_DIGITAL, CommonConstant.DLC_ACHV_NO),
	;
	
	public final int type;
	public final String achv;
	
	DLC_TYPE(int type, String achv) {
		this.type = type;
		this.achv = achv;
	}
	
	private static final String[] CORE_ARR = {"season pass", "character", "角色", "weapon"};
	private static final String[] DIGITAL_ARR = {"digital", " ost", "soundtrack", "原声", "音轨", "artbook", "art book", "图集", "图画集", "paint "};
	private static final String[] SUPPORT_ARR = {"supporter", "支持者", "skin", "皮肤", "costume"};
	
	/**
	 * @title  parseName
	 * @description 解析DLC名称，初步推测类型
	 * @param dlcName
	 * @return
	 */
	public static DLC_TYPE parseName(String dlcName) {
		if (StringUtil.isBlank(dlcName)) {
			return INIT;
		}
		String name = dlcName.trim().toLowerCase();
		if (StringUtil.containsAny(name, DIGITAL_ARR)) {
			return DIGITAL;
		}
		if (StringUtil.containsAny(name, SUPPORT_ARR)) {
			return SUPPORT;
		}
		if (StringUtil.containsAny(name, CORE_ARR)) {
			return CORE;
		}
		return INIT;
	}
}
