package com.wur7.store.constant;

public class CommonConstant {
	
	// ==================== 公共 ====================
	public static final int YES = 1;
	public static final int NO = 0;
	
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String FAIL = "FAIL";
	
	public static final String DEFAULT_NICKNAME = "微信用户";
	
	// `del_status` int(11) DEFAULT NULL COMMENT '删除状态【1：已删除，0：未删除】',
	public static final int DEL_STATUS_DEL = 1;
	public static final int DEL_STATUS_EXIST = 0;
	
	// `item_status` int(11) DEFAULT NULL COMMENT '商品状态【1：上架，2：下架】',
	public final static int ITEM_STATUS_UP = 1;
	public final static int ITEM_STATUS_DOWN = 2;
	
	public static final String ENABLE = "ENABLE";
	public static final String DISABLE = "DISABLE";

	
	// ==================== shop_item ====================
	// `commission_type` int(11) DEFAULT NULL COMMENT '佣金类别【1：给推荐人分佣，0：不给推荐人分佣】',
	public final static int COMMISSION_TYPE_INVITER_SHARE = 1;
	public final static int COMMISSION_TYPE_INVITER_UNSHARE = 0;
	// `sell_status` int(11) DEFAULT NULL COMMENT '销售状态【0：正常，+1：已购买，+10：售罄，+100：截止】',
	public final static int SELL_STATUS_NORMAL = 0;
	public final static int SELL_STATUS_BUY = 1;
	public final static int SELL_STATUS_SOLDOUT = 10;
	public final static int SELL_STATUS_EXPIRED = 100;
	
	
	// ==================== shop_cart ====================
	// `select_status` int(11) DEFAULT NULL COMMENT '选中状态【1：选中，2：未选中】',
	public final static int SELECT_STATUS_YES = 1;
	public final static int SELECT_STATUS_NO = 0;
	
	// ==================== shop_item_pic ====================
	// `pic_type` int(11) DEFAULT NULL COMMENT '图片类型【1：轮播图，2：详情图】',
	public final static int PIC_TYPE_SWIPER = 1;
	public final static int PIC_TYPE_DETAIL = 2;
	
	// ==================== shop_profit_share_record ====================
	// `share_status` int(11) DEFAULT NULL COMMENT '分润状态【1：待到账，2：已到账，3：已撤销】',
	public final static int SHARE_STATUS_UNFUND = 1;
	public final static int SHARE_STATUS_FUND   = 2;
	public final static int SHARE_STATUS_CANCEL = 3;
	
	// `beneficiary_type` int(11) COMMENT '受益人类型【1：推广人，2：上级代理，3：平台，4：商家】',
	public static final int BENEFICIARY_TYPE_PROMOTER = 1;
	public static final int BENEFICIARY_TYPE_AGENT    = 2;
	public static final int BENEFICIARY_TYPE_PLATFORM = 3;
	public static final int BENEFICIARY_TYPE_SHOP     = 4;
	
	// `share_type` int(11) COMMENT '分润类型【1：按比例，2：固定额】',
	public static final int SHARE_TYPE_RATE  = 1;
	public static final int SHARE_TYPE_FIXED = 2;
	
	// ==================== shop_order ====================
	// `order_status` int(11) NOT NULL DEFAULT '0' COMMENT '订单状态【0：待付款，10：待发货，20：待收货，31：正常完成，32：用户取消，33：超时关闭，34：全额退款关闭】',
	public static final int ORDER_STATUS_UNPAID      = 0;
	public static final int ORDER_STATUS_UNSHIPPED   = 10;
	public static final int ORDER_STATUS_UNRECEIVED  = 20;
	public static final int ORDER_STATUS_FINISHED    = 31;
	public static final int ORDER_STATUS_CANCELED    = 32;
	public static final int ORDER_STATUS_TIMEOUT     = 33;
	public static final int ORDER_STATUS_FULL_REFUND = 34;
	
	// `pay_status` int(11) NOT NULL DEFAULT '0' COMMENT '支付状态【0：未支付，1：已支付，2：部分退款，3：全额退款】',
	public static final int PAY_STATUS_UNPAID      = 0;
	public static final int PAY_STATUS_PAID        = 1;
	public static final int PAY_STATUS_PART_REFUND = 2;
	public static final int PAY_STATUS_FULL_REFUND = 3;
	
	// `after_sale_status` int(11) NOT NULL DEFAULT '0' COMMENT '售后状态【0：无，1：申请中，2：退款中，3：退款完成，4：售后关闭】',
	public static final int AFTER_SALE_STATUS_NONE      = 0;
	public static final int AFTER_SALE_STATUS_APPLYING  = 1;
	public static final int AFTER_SALE_STATUS_REFUNDING = 2;
	public static final int AFTER_SALE_STATUS_REFUNDED  = 3;
	public static final int AFTER_SALE_STATUS_CLOSED    = 4;
	
	// `pay_type` int(11) DEFAULT NULL COMMENT '支付方式【1：微信支付】',
	public static final int PAY_TYPE_WECHAT = 1;
	
	// `invoice_type` int(11) NOT NULL DEFAULT '0' COMMENT '发票类型【0：不开，1：电子普票，2：专票】',
	public static final int INVOICE_TYPE_NONE       = 0;
	public static final int INVOICE_TYPE_ELECTRONIC = 1;
	public static final int INVOICE_TYPE_SPECIAL    = 2;
	
	// ==================== shop_order_item ====================
	// `refund_status` int(11) NOT NULL DEFAULT '0' COMMENT '行退款状态【0：无，1：申请中，2：退款中，3：已退款，4：售后关闭】',
	public static final int ITEM_REFUND_STATUS_NONE      = 0;
	public static final int ITEM_REFUND_STATUS_APPLYING  = 1;
	public static final int ITEM_REFUND_STATUS_REFUNDING = 2;
	public static final int ITEM_REFUND_STATUS_REFUNDED  = 3;
	public static final int ITEM_REFUND_STATUS_CLOSED    = 4;
	
	// ==================== shop_order_status_log ====================
	// `action` int(11) NOT NULL COMMENT '业务动作【1：创建，2：支付，3：发货，4：收货，5：取消，6：超时关闭，7：部分退款，8：全额退款，9：关闭】',
	public static final int ORDER_ACTION_CREATE      = 1;
	public static final int ORDER_ACTION_PAY         = 2;
	public static final int ORDER_ACTION_SHIP        = 3;
	public static final int ORDER_ACTION_RECEIVE     = 4;
	public static final int ORDER_ACTION_CANCEL      = 5;
	public static final int ORDER_ACTION_TIMEOUT     = 6;
	public static final int ORDER_ACTION_PART_REFUND = 7;
	public static final int ORDER_ACTION_FULL_REFUND = 8;
	public static final int ORDER_ACTION_CLOSE       = 9;
	
	// `operator_type` int(11) NOT NULL COMMENT '操作人类型【1：用户，2：商家，3：系统，4：平台】',
	public static final int OPERATOR_TYPE_USER     = 1;
	public static final int OPERATOR_TYPE_SHOP     = 2;
	public static final int OPERATOR_TYPE_SYSTEM   = 3;
	public static final int OPERATOR_TYPE_PLATFORM = 4;
	
	// `source` int(11) NOT NULL DEFAULT '0' COMMENT '触发来源【0：未知，1：微信回调，2：后台接口，3：定时任务，4：退款接口，5：小程序端，6：平台端】',
	public static final int SOURCE_UNKNOWN         = 0;
	public static final int SOURCE_WECHAT_CALLBACK = 1;
	public static final int SOURCE_ADMIN_API       = 2;
	public static final int SOURCE_TIMER           = 3;
	public static final int SOURCE_REFUND_API      = 4;
	public static final int SOURCE_MINI_APP        = 5;
	public static final int SOURCE_PLATFORM        = 6;
	
	// ==================== shop_order_settle ====================
	// `status` int(11) NOT NULL DEFAULT '0' COMMENT '结算单状态【0：待支付，1：已支付，2：已关闭】',
	public static final int SETTLE_STATUS_UNPAID = 0;
	public static final int SETTLE_STATUS_PAID   = 1;
	public static final int SETTLE_STATUS_CLOSED = 2;
	
	/** 是否终态订单（31/32/33/34） */
	public static boolean isOrderFinalStatus(int orderStatus) {
		return orderStatus >= ORDER_STATUS_FINISHED;
	}
	
	// ==================== shop_after_sale ====================
	// `after_sale_type` int(11) COMMENT '售后类型【1：仅退款，2：退货退款】',
	public static final int AFTER_SALE_TYPE_REFUND_ONLY = 1;
	public static final int AFTER_SALE_TYPE_RETURN     = 2;
	
	// `audit_status` int(11) COMMENT '审核状态【1：待审核，2：已同意，3：已拒绝】',
	public static final int AUDIT_STATUS_PENDING  = 1;
	public static final int AUDIT_STATUS_AGREED   = 2;
	public static final int AUDIT_STATUS_REJECTED = 3;


	public static final String INIT = "INIT";
	public static final String COMPLETE = "COMPLETE";

	


	// 特殊的角色ID
	/**
	 * 特殊的角色ID ROLE_ID 【SUPER_ADMIN】 1
	 */
    public static final int ROLE_ID_SUPER_ADMIN = 1;
    public static final int ROLE_ID_SHOP = 2;
}
