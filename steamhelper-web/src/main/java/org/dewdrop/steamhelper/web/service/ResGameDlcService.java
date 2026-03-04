package org.dewdrop.steamhelper.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGameDlc;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ResGameDlcService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<ResGameDlc> allOfPage(ResGameDlc query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ResGameDlc> list = mapperSupport.resGameDlcMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(ResGameDlc dlc, boolean saveFlag) {
		int i;
		if (saveFlag) {
			i = mapperSupport.resGameDlcMapper.insert(dlc);
		} else {
			i = mapperSupport.resGameDlcMapper.updateByPrimaryKey(dlc);
		}
		return i == 1 ? IReturnBean.success() : IReturnBean.fail();
	}
	
	public ResGameDlc selectByPrimaryKey(Integer id) {
		return mapperSupport.resGameDlcMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		mapperSupport.resGameDlcMapper.deleteByPrimaryKey(id);
		return IReturnBean.success();
	}
}
