package com.wur7.store.web.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.ShopItemPic;
import com.wur7.store.entity.ShopItemSpec;
import com.wur7.store.entity.SysUser;
import com.wur7.store.enums.ITEM_STATUS;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.bean.ShopItemDetail;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ShopItemService {
	
	@Resource
	private FileService fileService;
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<ShopItem> allOfPage(ShopItem query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ShopItem> list = mapperSupport.shopItemMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	public IReturnBean<String> uploadImage(MultipartFile file, String path) throws IOException {
		IReturnBean<String> saveFile = fileService.saveFile("item", true, file, null);
		if (saveFile.isNotSuccess()) {
			return saveFile;
		}
		String localUrl = saveFile.getData();
		return IReturnBean.success(path + localUrl);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> saveOrUpd(ShopItemDetail detail, boolean saveFlag, SysUser operator) {
		ShopItem item = detail.getItem();
		if (saveFlag) {
			item.setShopId(operator.getId());
			item.setShopName(operator.getUserName());
			item.setCreator(operator.getId());
			item.setCreateTime(new Date());
			item.setItemStatus(ITEM_STATUS.DRAFT.STATUS);
			mapperSupport.shopItemMapper.insert(item);
			Integer itemId = item.getId();
			List<ShopItemSpec>  specList = detail.getSpecs();
			for (ShopItemSpec spec : specList) {
				spec.setItemId(itemId);
				mapperSupport.shopItemSpecMapper.insert(spec);
			}
			List<ShopItemPic> picList = detail.getPics();
			for (ShopItemPic pic : picList) {
				pic.setItemId(itemId);
				mapperSupport.shopItemPicMapper.insert(pic);
			}
		} else {
			item.setItemStatus(ITEM_STATUS.DRAFT.STATUS);
			item.setUpdateTime(new Date());
			// todo item其它字段
			mapperSupport.shopItemMapper.updateByPrimaryKeySelective(item);
			// todo 规格
			List<ShopItemSpec>  specList = detail.getSpecs();
			// todo 图片
			List<ShopItemPic> picList = detail.getPics();
			
		}
		return IReturnBean.success();
	}
}
