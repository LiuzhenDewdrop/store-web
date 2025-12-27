package org.dewdrop.steamhelper.web.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGameClassification;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResClassificationService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public List<ResGameClassification> getAll() {
		return mapperSupport.resGameClassificationMapper.findAll();
	}
	
	public List<String> getSeries() {
		return mapperSupport.resGameClassificationMapper.findAll().stream().map(ResGameClassification::getSeries).collect(Collectors.toList());
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(ResGameClassification clz, boolean saveFlag) {
		int i;
		if (saveFlag) {
			i = mapperSupport.resGameClassificationMapper.insert(clz);
		} else {
			i = mapperSupport.resGameClassificationMapper.updateByPrimaryKey(clz);
		}
		return i == 1 ? IReturnBean.success() : IReturnBean.fail();
	}
	
	public ResGameClassification selectByPrimaryKey(Integer id) {
		return mapperSupport.resGameClassificationMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		mapperSupport.resGameClassificationMapper.deleteByPrimaryKey(id);
		// todo 清涉及游戏表中的类型
		return IReturnBean.success();
	}
}
