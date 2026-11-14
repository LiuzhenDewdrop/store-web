package com.wur7.store.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wur7.store.bean.vo.ShopOrderQuery;
import com.wur7.store.bean.vo.ShopOrderResp;
import com.wur7.store.entity.ShopOrder;

public interface ShopOrderMapper {
	
	int deleteByPrimaryKey(Integer id);
	int insert(ShopOrder record);
	int insertSelective(ShopOrder record);
	ShopOrder selectByPrimaryKey(Integer id);
	int updateByPrimaryKeySelective(ShopOrder record);
	int updateByPrimaryKey(ShopOrder record);
	
	// ===== 查询 =====
	ShopOrder selectByOrderNo(@Param("orderNo") String orderNo);
	
	ShopOrder selectByOutTradeNo(@Param("outTradeNo") String outTradeNo);
	
	List<ShopOrder> selectByTransactionId(@Param("transactionId") String transactionId);
	
	List<ShopOrder> selectBySettleId(@Param("settleId") Integer settleId);
	
	List<ShopOrder> selectByCustomer(@Param("customerId") Integer customerId,
									 @Param("orderStatus") Integer orderStatus,
									 @Param("offset") Integer offset,
									 @Param("limit") Integer limit);
	
	long countByCustomer(@Param("customerId") Integer customerId,
						 @Param("orderStatus") Integer orderStatus);
	
	List<ShopOrder> selectByShop(@Param("shopId") Integer shopId,
								 @Param("orderStatus") Integer orderStatus,
								 @Param("offset") Integer offset,
								 @Param("limit") Integer limit);
	
	long countByShop(@Param("shopId") Integer shopId,
					 @Param("orderStatus") Integer orderStatus);
	
	List<ShopOrder> selectExpiredUnpaid(@Param("now") Date now,
										@Param("limit") Integer limit);
	
	List<ShopOrder> selectShippedTimeout(@Param("deadline") Date deadline,
										 @Param("limit") Integer limit);
	
	// ===== 状态流转（乐观锁，update_time 代码赋值）=====
	int updateStatusWithVersion(@Param("id") Integer id,
								@Param("oldStatus") Integer oldStatus,
								@Param("newStatus") Integer newStatus,
								@Param("version") Integer version,
								@Param("updateTime") Date updateTime);
	
	int updatePaid(@Param("id") Integer id,
				   @Param("oldStatus") Integer oldStatus,
				   @Param("version") Integer version,
				   @Param("transactionId") String transactionId,
				   @Param("payType") Integer payType,
				   @Param("paidAmt") Integer paidAmt,
				   @Param("payTime") Date payTime,
				   @Param("updateTime") Date updateTime);
	
	int updateShipped(@Param("id") Integer id,
					  @Param("oldStatus") Integer oldStatus,
					  @Param("version") Integer version,
					  @Param("expressCompanyName") String expressCompanyName,
					  @Param("expressNo") String expressNo,
					  @Param("shipTime") Date shipTime,
					  @Param("updateTime") Date updateTime);
	
	int updateReceived(@Param("id") Integer id,
					   @Param("oldStatus") Integer oldStatus,
					   @Param("version") Integer version,
					   @Param("receiveTime") Date receiveTime,
					   @Param("finishTime") Date finishTime,
					   @Param("updateTime") Date updateTime);
	
	int updatePartialRefund(@Param("id") Integer id,
							@Param("refundAmt") Integer refundAmt,
							@Param("version") Integer version,
							@Param("updateTime") Date updateTime);
	
	int updateFullRefundClose(@Param("id") Integer id,
							  @Param("oldStatus") Integer oldStatus,
							  @Param("version") Integer version,
							  @Param("closeTime") Date closeTime,
							  @Param("updateTime") Date updateTime);
	
	int updateAfterSaleStatus(@Param("id") Integer id,
							  @Param("afterSaleStatus") Integer afterSaleStatus,
							  @Param("version") Integer version,
							  @Param("updateTime") Date updateTime);
	
	int softDelete(@Param("id") Integer id,
				   @Param("customerId") Integer customerId,
				   @Param("updateTime") Date updateTime);
	
	int updateCancel(@Param("id") Integer id,
					 @Param("oldStatus") Integer oldStatus,
					 @Param("newStatus") Integer newStatus,
					 @Param("version") Integer version,
					 @Param("cancelTime") Date cancelTime,
					 @Param("closeTime") Date closeTime,
					 @Param("cancelReason") String cancelReason,
					 @Param("updateTime") Date updateTime);
	List<ShopOrderResp> findPageByCustomerIds(@Param("query") ShopOrderQuery query,
											  @Param("customerIds") List<Integer> customerIds);
	
}