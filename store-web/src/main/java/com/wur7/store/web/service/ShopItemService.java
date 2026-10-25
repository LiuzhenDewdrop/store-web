package com.wur7.store.web.service;

import java.io.IOException;
import java.util.ArrayList;
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
import com.wur7.store.web.bean.response.ShopItemDetail;
import com.wur7.store.web.bean.request.AppQuery;
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
	
	public IReturnBean<String> uploadImage(MultipartFile file) throws IOException {
		IReturnBean<String> saveFile = fileService.saveFile("item", true, file, null);
		if (saveFile.isNotSuccess()) {
			return saveFile;
		}
		String localUrl = saveFile.getData();
		return IReturnBean.success(localUrl);
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
			List<ShopItemSpec>  specList = detail.getSpecList();
			for (ShopItemSpec spec : specList) {
				spec.setItemId(itemId);
				mapperSupport.shopItemSpecMapper.insert(spec);
			}
			List<ShopItemPic> picList = detail.getPicList();
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
			List<ShopItemSpec>  specList = detail.getSpecList();
			// todo 图片
			List<ShopItemPic> picList = detail.getPicList();
			
		}
		return IReturnBean.success();
	}
	
	public IReturnBean<List<ShopItemDetail>> listForApp(AppQuery query) {
		long total = mapperSupport.shopItemMapper.count(null, query.getItemType());
		List<ShopItem> itemList = mapperSupport.shopItemMapper.queryForApp(null, query.getItemType(), (query.getPageNum()-1)* query.getPageSize(), query.getPageSize());
		List<ShopItemDetail> list = new ArrayList<>(itemList.size());
		for (ShopItem shopItem : itemList) {
			list.add(genDetail(shopItem));
		}
		return IReturnBean.success(total, list);
	}
	
	public IReturnBean<List<ShopItemDetail>> queryForApp(AppQuery query) {
		long total = mapperSupport.shopItemMapper.count(query.getKeyword(), null);
		List<ShopItem> itemList = mapperSupport.shopItemMapper.queryForApp(query.getKeyword(), null, (query.getPageNum()-1)* query.getPageSize(), query.getPageSize());
		List<ShopItemDetail> list = new ArrayList<>(itemList.size());
		for (ShopItem shopItem : itemList) {
			list.add(genDetail(shopItem));
		}
		return IReturnBean.success(total, list);
	}
	
	private ShopItemDetail genDetail(ShopItem shopItem) {
		List<ShopItemSpec> specs = mapperSupport.shopItemSpecMapper.getByItem(shopItem.getId());
		List<ShopItemPic> pics = mapperSupport.shopItemPicMapper.getByItem(shopItem.getId());
		return new ShopItemDetail(shopItem, specs, pics);
	}
	
	public IReturnBean<ShopItemDetail> detailForApp(Integer id) {
		ShopItem shopItem = mapperSupport.shopItemMapper.selectByPrimaryKey(id);
		if (shopItem == null) {
			return IReturnBean.fail("查不到此商品");
		}
		return IReturnBean.success(genDetail(shopItem));
	}
}
