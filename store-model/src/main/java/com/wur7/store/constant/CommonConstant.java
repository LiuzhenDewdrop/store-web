package com.wur7.store.constant;

public class CommonConstant {
	
	public static final int YES = 1;
	public static final int NO = 0;
	
	public static final String DEFAULT_NICKNAME = "微信用户";
	
	// 公共
	
	// `del_status` int(11) DEFAULT NULL COMMENT '删除状态【1：已删除，0：未删除】',
	public static final int DEL_STATUS_DEL = 1;
	public static final int DEL_STATUS_EXIST = 0;
	
	// `item_status` int(11) DEFAULT NULL COMMENT '商品状态【1：上架，2：下架】',
	public final static int ITEM_STATUS_UP = 1;
	public final static int ITEM_STATUS_DOWN = 2;
	
	// shop_item
	// `commission_type` int(11) DEFAULT NULL COMMENT '佣金类别【1：给推荐人分佣，0：不给推荐人分佣】',
	public final static int COMMISSION_TYPE_INVITER_SHARE = 1;
	public final static int COMMISSION_TYPE_INVITER_UNSHARE = 0;
	// `sell_status` int(11) DEFAULT NULL COMMENT '销售状态【0：正常，+1：已购买，+10：售罄，+100：截止】',
	public final static int SELL_STATUS_NORMAL = 0;
	public final static int SELL_STATUS_BUY = 1;
	public final static int SELL_STATUS_SOLDOUT = 10;
	public final static int SELL_STATUS_EXPIRED = 100;
	
	
	// shop_cart
	// `select_status` int(11) DEFAULT NULL COMMENT '选中状态【1：选中，2：未选中】',
	public final static int SELECT_STATUS_YES = 1;
	public final static int SELECT_STATUS_NO = 0;
	
	// shop_item_pic
	// `pic_type` int(11) DEFAULT NULL COMMENT '图片类型【1：轮播图，2：详情图】',
	public final static int PIC_TYPE_SWIPER = 1;
	public final static int PIC_TYPE_DETAIL = 2;
	
	// shop_profit_share_record
	// `share_status` int(11) DEFAULT NULL COMMENT '分润状态【1：待到账，2：已到账，3：已撤销】',
	public final static int SHARE_STATUS_UNFUND = 1;
	public final static int SHARE_STATUS_FUND = 2;
	public final static int SHARE_STATUS_CANCEL = 3;
	
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
    public static final int ROLE_ID_SHOP = 2;
}
