package org.dewdrop.steamhelper.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.SysDict;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class SysDictService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public void putDict(SysDict dict) {
		SysDict d = mapperSupport.sysDictMapper.selectOne(dict.getDictGroup(), dict.getDictKey());
		if (d == null) {
			mapperSupport.sysDictMapper.insert(dict);
		} else {
			dict.setId(d.getId());
			mapperSupport.sysDictMapper.updateByPrimaryKey(dict);
		}
		SysDictUtil.putDict(dict);
	}
	
	public void delDict(SysDict dict) {
		SysDict d = mapperSupport.sysDictMapper.selectOne(dict.getDictGroup(), dict.getDictKey());
		if (d == null) {
			return ;
		}
		mapperSupport.sysDictMapper.deleteByPrimaryKey(d.getId());
		SysDictUtil.del(d);
	}
	
	public PageInfo<SysDict> allOfPage(SysDict query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<SysDict> list = mapperSupport.sysDictMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(SysDict dict, boolean saveFlag) {
		int i;
		if (saveFlag) {
			i = mapperSupport.sysDictMapper.insert(dict);
		} else {
			i = mapperSupport.sysDictMapper.updateByPrimaryKey(dict);
		}
		return i == 1 ? IReturnBean.success() : IReturnBean.fail();
	}
	
	public SysDict selectByPrimaryKey(Integer id) {
		return mapperSupport.sysDictMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		mapperSupport.sysDictMapper.deleteByPrimaryKey(id);
		return IReturnBean.success();
	}
}
