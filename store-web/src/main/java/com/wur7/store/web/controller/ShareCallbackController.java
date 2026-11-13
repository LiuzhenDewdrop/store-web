package com.wur7.store.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.entity.ShopProfitShareRecord;
import com.wur7.store.web.mapper.MapperSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/callback")
public class ShareCallbackController {
	
	@Resource
	private MapperSupport mapperSupport;
	
	@RequestMapping("/wechat/share")
	public String shareCallback(@RequestBody Map<String, Object> body) {
		try {
			// TODO 验签、解密
			String settleNo = (String) body.get("settle_no");
			String channelSettleNo = (String) body.get("channel_settle_no");
			Integer status = (Integer) body.get("status");
			List<ShopProfitShareRecord> records = mapperSupport.shopProfitShareRecordMapper.selectBySettleNo(settleNo);
			for (ShopProfitShareRecord r : records) {
				if (status == 1) {
					mapperSupport.shopProfitShareRecordMapper.updateShareStatusToFund(
							r.getId(), new Date(), r.getVersion());
				}
			}
			return "SUCCESS";
		} catch (Exception e) {
			log.error("分账回调 error:", e);
			return "FAIL";
		}
	}
}