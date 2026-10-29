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
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.ShopItemPic;
import com.wur7.store.entity.ShopItemSpec;
import com.wur7.store.entity.SysUser;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.bean.request.AppQuery;
import com.wur7.store.web.bean.response.ShopItemDetail;
import com.wur7.store.web.emuns.Category;
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
			int minSalePrice = detail.getSpecList().stream()
					.mapToInt(ShopItemSpec::getSalePrice)
					.min().orElse(0);
			if (item.getItemType() == Category.ACTIVITY.getItemType()
				|| item.getItemType() == Category.COURSE.getItemType()) {
				item.setItemCategory1(null);
				item.setItemCategory2(null);
				item.setItemCategory3(null);
			}
			item.setSalePrice(minSalePrice);
//			item.setItemStatus(ITEM_STATUS.DRAFT.STATUS);
			item.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
			item.setSoldUnitNum(0);
			item.setCreator(operator.getId());
			item.setCreateTime(new Date());
			mapperSupport.shopItemMapper.insert(item);
			Integer itemId = item.getId();
			for (ShopItemSpec spec : detail.getSpecList()) {
				spec.setItemId(itemId);
				spec.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
				mapperSupport.shopItemSpecMapper.insert(spec);
			}
			List<ShopItemPic> picList = detail.getPicList();
			for (ShopItemPic pic : picList) {
				pic.setItemId(itemId);
				pic.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
				pic.setPicType(CommonConstant.PIC_TYPE_SWIPPER);
				mapperSupport.shopItemPicMapper.insert(pic);
			}
			List<ShopItemPic> descList = detail.getDesc();
			for (ShopItemPic desc : descList) {
				desc.setItemId(itemId);
				desc.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
				desc.setPicType(CommonConstant.PIC_TYPE_DETAIL);
				mapperSupport.shopItemPicMapper.insert(desc);
			}
		} else {
//			item.setItemStatus(ITEM_STATUS.DRAFT.STATUS);
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
		List<ShopItemPic> pics = mapperSupport.shopItemPicMapper.getByItem(shopItem.getId(), CommonConstant.PIC_TYPE_SWIPPER);
		ShopItemPic first = new ShopItemPic();
		first.setPicUrl(shopItem.getItemImage());
		pics.add(0, first);
		List<ShopItemPic> details = mapperSupport.shopItemPicMapper.getByItem(shopItem.getId(), CommonConstant.PIC_TYPE_DETAIL);
		return new ShopItemDetail(shopItem, specs, pics, details);
	}
	
	public IReturnBean<ShopItemDetail> detailForApp(Integer id) {
		ShopItem shopItem = mapperSupport.shopItemMapper.selectByPrimaryKey(id);
		if (shopItem == null) {
			return IReturnBean.fail("查不到此商品");
		}
		return IReturnBean.success(genDetail(shopItem));
	}
	
	public IReturnBean<?> del(Integer id) {
		if (id == null) {
			return IReturnBean.fail("未查询到商品");
		}
		ShopItem item = mapperSupport.shopItemMapper.selectByPrimaryKey(id);
		if (item == null) {
			return IReturnBean.fail("未查询到商品");
		}
		ShopItem del = new ShopItem();
		del.setId(id);
		del.setDelStatus(CommonConstant.DEL_STATUS_DEL);
		del.setUpdateTime(new Date());
		mapperSupport.shopItemMapper.updateByPrimaryKeySelective(del);
		return IReturnBean.success();
	}
	
	public IReturnBean<?> operate(Integer id) {
		if (id == null) {
			return IReturnBean.fail("未查询到商品");
		}
		ShopItem item = mapperSupport.shopItemMapper.selectByPrimaryKey(id);
		if (item == null) {
			return IReturnBean.fail("未查询到商品");
		}
		ShopItem record = new ShopItem();
		record.setId(id);
		// 商品状态【1：上架，2：下架，3：草稿】
		// 上架变为下架，下架和草稿都上架
		if (item.getItemStatus() == CommonConstant.ITEM_STATUS_UP) {
			record.setItemStatus(CommonConstant.ITEM_STATUS_DOWN);
		} else {
			record.setItemStatus(CommonConstant.ITEM_STATUS_UP);
		}
		record.setUpdateTime(new Date());
		mapperSupport.shopItemMapper.updateByPrimaryKeySelective(record);
		return IReturnBean.success();
	}
}
