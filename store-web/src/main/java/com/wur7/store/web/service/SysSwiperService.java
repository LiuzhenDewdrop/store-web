package com.wur7.store.web.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.SysSwiper;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class SysSwiperService {
	
	@Resource
	private FileService fileService;
	@Resource
	private MapperSupport mapperSupport;
	
	public IReturnBean<List<SysSwiper>> list() {
		return IReturnBean.success(mapperSupport.sysSwiperMapper.list());
	}
	
	public IReturnBean<String> uploadImage(MultipartFile file) throws IOException {
		IReturnBean<String> saveFile = fileService.saveFile("swiper", true, file, null);
		if (saveFile.isNotSuccess()) {
			return saveFile;
		}
		String localUrl = saveFile.getData();
		return IReturnBean.success(localUrl);
	}
	
	public IReturnBean<?> update(List<SysSwiper> list) {
		List<SysSwiper> swipersOld = mapperSupport.sysSwiperMapper.list();
		Set<Integer> swipersOldSet = swipersOld.stream().map(SysSwiper::getId).collect(Collectors.toSet());
		// 区分 增/删/改 (不变归入改)
		for (SysSwiper swiper : list) {
			swiper.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
			if (swiper.getId() == null) {
				// 增
				mapperSupport.sysSwiperMapper.insert(swiper);
			} else {
				// 改
				if (!swipersOldSet.contains(swiper.getId())) {
					continue;
				}
				mapperSupport.sysSwiperMapper.updateByPrimaryKeySelective(swiper);
				swipersOldSet.remove(swiper.getId());
			}
		}
		// 剩下的都删掉
		for (Integer id : swipersOldSet) {
			SysSwiper swiper = new SysSwiper();
			swiper.setId(id);
			swiper.setDelStatus(CommonConstant.DEL_STATUS_DEL);
			mapperSupport.sysSwiperMapper.updateByPrimaryKeySelective(swiper);
		}
		return IReturnBean.success();
	}
}
