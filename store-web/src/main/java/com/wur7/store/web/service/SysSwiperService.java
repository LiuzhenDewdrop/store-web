package com.wur7.store.web.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wur7.store.bean.IReturnBean;
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
	
	public IReturnBean<List<SysSwiper>> listForApp() {
		return IReturnBean.success(mapperSupport.sysSwiperMapper.listForApp());
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
		// todo
		return IReturnBean.success();
	}
}
