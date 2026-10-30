package com.wur7.store.constant;

public class CommonConstant {
	
	public static final int YES = 1;
	public static final int NO = 0;
	
	// 公共
	
	// `del_status` int(11) DEFAULT NULL COMMENT '删除状态【1：已删除，0：未删除】',
	public static final int DEL_STATUS_DEL = 1;
	public static final int DEL_STATUS_EXIST = 0;
	
	// `item_status` int(11) DEFAULT NULL COMMENT '商品状态【1：上架，2：下架】',
	public final static int ITEM_STATUS_UP = 1;
	public final static int ITEM_STATUS_DOWN = 2;
	
	// shop_item
	// `model_type` int(11) DEFAULT NULL COMMENT '型号类别【1：单一型号规格，2：多种型号规格】',
	public final static int MODEL_TYPE_SINGLE = 1;
	public final static int MODEL_TYPE_MULTI = 2;
	
	
	// shop_cart
	// `select_status` int(11) DEFAULT NULL COMMENT '选中状态【1：选中，2：未选中】',
	public final static int SELECT_STATUS_YES = 1;
	public final static int SELECT_STATUS_NO = 0;
	
	// shop_item_pic
	// `pic_type` int(11) DEFAULT NULL COMMENT '图片类型【1：轮播图，2：详情图】',
	public final static int PIC_TYPE_SWIPPER = 1;
	public final static int PIC_TYPE_DETAIL = 2;
	
	// 字典组group
	/**
	 * 字典组 DICT_GROUP 【角色】 ROLE
	 */
	public static final String DICT_GROUP_ROLE = "ROLE";


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
}
