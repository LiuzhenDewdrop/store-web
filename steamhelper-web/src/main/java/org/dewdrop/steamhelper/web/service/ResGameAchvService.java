package org.dewdrop.steamhelper.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGameAchievement;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ResGameAchvService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<ResGameAchievement> allOfPage(ResGameAchievement query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ResGameAchievement> list = mapperSupport.resGameAchievementMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(ResGameAchievement achv, boolean saveFlag) {
		int i;
		if (saveFlag) {
			i = mapperSupport.resGameAchievementMapper.insert(achv);
		} else {
			i = mapperSupport.resGameAchievementMapper.updateByPrimaryKey(achv);
		}
		return i == 1 ? IReturnBean.success() : IReturnBean.fail();
	}
	
	public ResGameAchievement selectByPrimaryKey(Integer id) {
		return mapperSupport.resGameAchievementMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		mapperSupport.resGameAchievementMapper.deleteByPrimaryKey(id);
		return IReturnBean.success();
	}
}
