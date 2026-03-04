package org.dewdrop.steamhelper.web.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGameClassification;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResGameClfService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public List<ResGameClassification> getAll() {
		return mapperSupport.resGameClassificationMapper.findAll();
	}
	
	public Map<String, List<ResGameClassification>> getSeries() {
		return mapperSupport.resGameClassificationMapper.findAll().stream().map(clf -> new ResGameClassification(clf.getId(), clf.getSeries(), clf.getSubSeries())).collect(Collectors.groupingBy(ResGameClassification::getSeries));
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
		// 清理涉及游戏表中的类型： 交给mysql外键约束
		return IReturnBean.success();
	}
}
