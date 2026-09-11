package com.wur7.store.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.SysDict;
import com.wur7.store.util.util.StringUtil;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class SysDictService {
	
	@Resource
	private MapperSupport mapperSupport;
	
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
		if (dict == null || StringUtil.isBlank(dict.getDictGroup())
				|| StringUtil.isBlank(dict.getDictKey()) || StringUtil.isBlank(dict.getDictValue())
				|| dict.getDictSort() == null) {
			return IReturnBean.fail("字典字段均不能为空");
		}
		int i;
		SysDict queryDict = null;
		if (saveFlag) {
			queryDict = SysDictUtil.getDict(dict.getDictGroup(), dict.getDictKey());
			if (queryDict != null) {
				return IReturnBean.fail("已存在同group同key的字典项");
			}
			i = mapperSupport.sysDictMapper.insert(dict);
		} else {
			SysDict dbDict = mapperSupport.sysDictMapper.selectByPrimaryKey(dict.getId());
			queryDict = SysDictUtil.getDict(dbDict.getDictGroup(), dbDict.getDictKey());
			i = mapperSupport.sysDictMapper.updateByPrimaryKey(dict);
		}
		if (i != 1) {
			return IReturnBean.fail();
		}
		if (!saveFlag && queryDict != null) {
			if (!StringUtil.equals(queryDict.getDictGroup(), dict.getDictGroup())
			|| !StringUtil.equals(queryDict.getDictKey(), dict.getDictKey())) {
				// 改group或改key，需要先删掉再添加
				SysDictUtil.del(queryDict);
			}
		}
		SysDictUtil.putDict(dict);
		return IReturnBean.success();
	}
	
	public SysDict selectByPrimaryKey(Integer id) {
		return mapperSupport.sysDictMapper.selectByPrimaryKey(id);
	}
	
	public IReturnBean<?> del(Integer id) {
		mapperSupport.sysDictMapper.deleteByPrimaryKey(id);
		return IReturnBean.success();
	}
}
