package com.wur7.store.web.controller.wx;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.CommonReq;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.bean.response.ProfitOverallVO;
import com.wur7.store.web.bean.response.ProfitShareResp;
import com.wur7.store.web.service.ShopProfitShareService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/profit")
public class WxProfitShareController extends WxBasicController {
	
	@Resource
	private ShopProfitShareService shopProfitShareService;
	
	/**
	 * @title  list
	 * @description 查询三个总账信息
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/overall")
	public IReturnBean<ProfitOverallVO> overall(HttpServletRequest request, @RequestBody CommonReq req) {
		try {
			ShopCustomer loginUser = getWxLoginUser(request);
			if (loginUser == null || loginUser.getId() == null
				|| req == null || req.getId() == null
				|| loginUser.getId() != req.getId()) {
				return IReturnBean.unlogin();
			}
			return shopProfitShareService.overall(req);
		} catch (Exception e) {
			log.error("查询三个总账信息 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  list
	 * @description 查询收益明细列表
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list")
	public IReturnBean<List<ProfitShareResp>> list(HttpServletRequest request, @RequestBody CommonReq req) {
		try {
			ShopCustomer loginUser = getWxLoginUser(request);
			if (loginUser == null || loginUser.getId() == null
					|| req == null || req.getId() == null
					|| loginUser.getId() != req.getId()) {
				return IReturnBean.unlogin();
			}
			return shopProfitShareService.list(req);
		} catch (Exception e) {
			log.error("查询收益明细列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  person
	 * @description 查询受邀请人列表
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/person")
	public IReturnBean<List<ShopCustomer>> person(HttpServletRequest request, @RequestBody CommonReq req) {
		try {
			ShopCustomer loginUser = getWxLoginUser(request);
			if (loginUser == null || loginUser.getId() == null
					|| req == null || req.getId() == null
					|| loginUser.getId() != req.getId()) {
				return IReturnBean.unlogin();
			}
			return shopProfitShareService.person(req);
		} catch (Exception e) {
			log.error("查询受邀请人列表 error:", e);
			return IReturnBean.error();
		}
	}
	

}
