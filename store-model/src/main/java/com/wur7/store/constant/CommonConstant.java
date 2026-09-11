package com.wur7.store.constant;

import com.wur7.store.enums.DLC_TYPE;

public class CommonConstant {
	
	public static final String URL_STEAM_ICON = "https://media.st.dl.eccdnx.com/steamcommunity/public/images/apps/";
	public static final String URL_STEAM_IMAGE = "https://shared.st.dl.eccdnx.com/store_item_assets/steam/apps/";

	// 字典组group
	/**
	 * 字典组 DICT_GROUP 【角色】 ROLE
	 */
	public static final String DICT_GROUP_ROLE = "ROLE";
	/**
	 * 字典组 DICT_GROUP 【平台】 PLATFORM
	 */
	public static final String DICT_GROUP_PLATFORM = "PLATFORM";
	/**
	 * 字典组 DICT_GROUP 【游戏状态】 GAME_STATUS
	 * {@link CommonConstant#GAME_STATUS_NORMAL},
	 * {@link CommonConstant#GAME_STATUS_OFFLINE},
	public static final String DICT_GROUP_GAME_STATUS = "GAME_STATUS";
	/**
	 * 字典组 DICT_GROUP 【成就状态】 ACHV_STATUS
	 * {@link CommonConstant#ACHV_STATUS_NORMAL},
	 * {@link CommonConstant#ACHV_STATUS_NONE},
	 * {@link CommonConstant#ACHV_STATUS_LEARNING},
	 * {@link CommonConstant#ACHV_STATUS_LIMITED}
	 */
	public static final String DICT_GROUP_ACHV_STATUS = "ACHV_STATUS";
	/**
	 * 字典组 DICT_GROUP 【DLC类型】 DLC_TYPE
	 * {@link CommonConstant#DLC_TYPE_INIT},
	 * {@link CommonConstant#DLC_TYPE_CORE},
	 * {@link CommonConstant#DLC_TYPE_SUPPORT},
	 * {@link CommonConstant#DLC_TYPE_OST},
	 * {@link CommonConstant#DLC_TYPE_ART}
	 */
	public static final String DICT_GROUP_DLC_TYPE = "DLC_TYPE";
	/**
	 * 字典组 DICT_GROUP 【DLC是否影响成就】 DLC_ACHV
	 * {@link CommonConstant#DLC_ACHV_INIT},
	 * {@link CommonConstant#DLC_ACHV_YES},
	 * {@link CommonConstant#DLC_ACHV_NO}
	 */
	public static final String DICT_GROUP_DLC_ACHV = "DLC_ACHV";

	// 平台编号
	/**
	 * 平台编号 PLATFORM 【STEAM】 1
	 */
	public static final int PLATFORM_STEAM = 1;
	
	// 游戏状态【1：正常；2：下架】
	/**
	 * 游戏状态 GAME_STATUS 【正常】 1
	 * {@link CommonConstant#DICT_GROUP_ACHV_STATUS}
	 */
	public static final int GAME_STATUS_NORMAL = 1;
	/**
	 * 游戏状态 GAME_STATUS 【下架】 2
	 * {@link CommonConstant#DICT_GROUP_ACHV_STATUS}
	 */
	public static final int GAME_STATUS_OFFLINE = 2;
	
	// 成就状态【1：正常；2：无成就；3：Steam 正在了解该游戏 ；4：个人资料功能受限】
	/**
	 * 成就状态 ACHV_STATUS 【正常】 1
	 * {@link CommonConstant#DICT_GROUP_ACHV_STATUS}
	 */
	public static final int ACHV_STATUS_NORMAL = 1;
	/**
	 * 成就状态 ACHV_STATUS 【无成就】 2
	 * {@link CommonConstant#DICT_GROUP_ACHV_STATUS}
	 */
	public static final int ACHV_STATUS_NONE = 2;
	/**
	 * 成就状态 ACHV_STATUS 【Steam 正在了解该游戏】 3
	 * {@link CommonConstant#DICT_GROUP_ACHV_STATUS}
	 */
	public static final int ACHV_STATUS_LEARNING = 3;
	/**
	 * 成就状态 ACHV_STATUS 【个人资料功能受限】 4
	 * {@link CommonConstant#DICT_GROUP_ACHV_STATUS}
	 */
	public static final int ACHV_STATUS_LIMITED = 4;
	
	// dlc类型【1：流程/地图/角色相关；2：支持者包或皮肤；3：音轨；4：图集；0：未分类】
	/**
	 * DLC_TYPE 【未分类】 0
	 * {@link CommonConstant#DICT_GROUP_DLC_TYPE},
	 * {@link DLC_TYPE#INIT}
	 */
	public static final int DLC_TYPE_INIT =	0;
	/**
	 * DLC_TYPE 【流程/地图/角色】 1
	 * {@link CommonConstant#DICT_GROUP_DLC_TYPE},
	 * {@link DLC_TYPE#CORE}
	 */
	public static final int DLC_TYPE_CORE = 1;
	/**
	 * DLC_TYPE 【支持者包或皮肤】 2
	 * {@link CommonConstant#DICT_GROUP_DLC_TYPE},
	 * {@link DLC_TYPE#SUPPORT}
	 */
	public static final int DLC_TYPE_SUPPORT = 2;
	/**
	 * DLC_TYPE 【数字附加】 3
	 * {@link CommonConstant#DICT_GROUP_DLC_TYPE},
	 * {@link DLC_TYPE#DIGITAL}
	 */
	public static final int DLC_TYPE_DIGITAL = 3;
	
	// 是否影响游戏成就【Y/N/I】（是/否/初始）
	/**
	 * DLC成就 DLC_ACHV 【初始化】 "I"
	 * {@link CommonConstant#DICT_GROUP_DLC_ACHV},
	 * {@link DLC_TYPE#INIT},
	 * {@link DLC_TYPE#CORE}
	 */
	public static final String DLC_ACHV_INIT = "I";
	/**
	 * DLC成就 DLC_ACHV 【影响成就】 "Y"
	 * {@link CommonConstant#DICT_GROUP_DLC_ACHV}
	 */
	public static final String DLC_ACHV_YES = "Y";
	/**
	 * DLC成就 DLC_ACHV 【不涉及成就】 "N"
	 * {@link CommonConstant#DICT_GROUP_DLC_ACHV},
	 * {@link DLC_TYPE#SUPPORT},
	 * {@link DLC_TYPE#OST},
	 * {@link DLC_TYPE#ART}
	 */
	public static final String DLC_ACHV_NO = "N";

	public static final String INIT = "INIT";
	public static final String COMPLETE = "COMPLETE";

	public static final String ENABLE = "ENABLE";
	public static final String DISABLE = "DISABLE";

	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";

	public static final String Y = "Y";
	public static final String N = "N";

	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String FAIL = "FAIL";


	// 特殊的角色ID
	/**
	 * 特殊的角色ID ROLE_ID 【SUPER_ADMIN】 1
	 */
    public static final int ROLE_ID_SUPER_ADMIN = 1;
	/**
	 * 特殊的角色ID ROLE_ID 【访客】 2
	 */
    public static final int ROLE_ID_GUEST = 2;
	/**
	 * 特殊的角色ID ROLE_ID 【普通用户上限】 500
	 */
	public static final int ROLE_ID_NORMAL_LINE = 500;
}
