package org.dewdrop.steamhelper.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResAccount;
import org.dewdrop.steamhelper.entity.ResGame;
import org.dewdrop.steamhelper.constant.CommonConstant;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ResGameService {
	
	@Resource
	private ResAccountService resAccountService;
	@Resource
	private SteamService steamService;
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<ResGame> allOfPage(ResGame query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ResGame> list = mapperSupport.resGameMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(ResGame game, boolean saveFlag) {
		int i;
		if (saveFlag) {
			i = mapperSupport.resGameMapper.insert(game);
		} else {
			i = mapperSupport.resGameMapper.updateByPrimaryKey(game);
		}
		return i == 1 ? IReturnBean.success() : IReturnBean.fail();
	}
	
	public ResGame selectByPrimaryKey(Integer id) {
		return mapperSupport.resGameMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		mapperSupport.resGameMapper.deleteByPrimaryKey(id);
		return IReturnBean.success();
	}
	
	public IReturnBean<?> auto(Integer userId, Integer platformId) throws Exception {
		if (platformId == null) {
			return IReturnBean.error("未选择平台");
		}
		ResAccount acc = resAccountService.getUserMainAccount(userId, platformId);
		if (acc == null) {
			return IReturnBean.fail("您未设置该平台的主账户");
		}
		IReturnBean result = IReturnBean.success();
		switch (platformId) {
			case CommonConstant.PLATFORM_STEAM:
				result = steamService.auto(acc);
				break;
			default:
				break;
		}
		return result;
	}
}
