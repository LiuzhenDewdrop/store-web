package com.wur7.store.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopCart;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.ShopItemPic;
import com.wur7.store.entity.ShopItemSpec;
import com.wur7.store.entity.SysUser;
import com.wur7.store.util.util.CollectionUtil;
import com.wur7.store.util.util.StringUtil;
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
	public IReturnBean<?> save(ShopItemDetail detail, SysUser operator) {
		ShopItem item = detail.getItem();
		item.setShopId(operator.getId());
		item.setShopName(operator.getUserName());
		if (CollectionUtil.isNotEmpty(detail.getSpecList())) {
			int minSalePrice = detail.getSpecList().stream()
					.mapToInt(ShopItemSpec::getSalePrice)
					.min().orElse(0);
			item.setSalePrice(minSalePrice);
		}
		if (item.getItemType() == Category.ACTIVITY.getItemType()
				|| item.getItemType() == Category.COURSE.getItemType()) {
			item.setItemCategory1(null);
			item.setItemCategory2(null);
			item.setItemCategory3(null);
		}
		item.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
		item.setSoldUnitNum(0);
		item.setCreator(operator.getId());
		item.setCreateTime(new Date());
		mapperSupport.shopItemMapper.insert(item);
		Integer itemId = item.getId();
		for (ShopItemSpec spec : detail.getSpecList()) {
			spec.setItemId(itemId);
			// 没图就用封面图
			if (StringUtil.isBlank(spec.getPicUrl())) {
				spec.setPicUrl(item.getItemImage());
			}
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
		return IReturnBean.success();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> update(ShopItemDetail detail, SysUser operator) {
		ShopItem item = detail.getItem();
		if (item == null || item.getId() == null) {
			return IReturnBean.fail("查无此商品");
		}
		Integer itemId = item.getId();
		ShopItem oldItem = mapperSupport.shopItemMapper.selectByPrimaryKey(itemId);
		if (oldItem == null || oldItem.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("查无此商品");
		}
		if (oldItem.getShopId() != operator.getId()) {
			return IReturnBean.fail("只能修改自己的商品");
		}
		item.setUpdateTime(new Date());
		if (CollectionUtil.isNotEmpty(detail.getSpecList())) {
			int minSalePrice = detail.getSpecList().stream()
					.mapToInt(ShopItemSpec::getSalePrice)
					.min().orElse(0);
			item.setSalePrice(minSalePrice);
		}
		if (item.getItemType() == Category.ACTIVITY.getItemType()
				|| item.getItemType() == Category.COURSE.getItemType()) {
			item.setItemCategory1(null);
			item.setItemCategory2(null);
			item.setItemCategory3(null);
		}
		item.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
		mapperSupport.shopItemMapper.updateByPrimaryKeySelective(item);
		// 规格
		List<ShopItemSpec> specsOld = mapperSupport.shopItemSpecMapper.getByItem(itemId, null);
		Set<Integer> specsOldSet = specsOld.stream().map(ShopItemSpec::getId).collect(Collectors.toSet());
		// 区分 增/删/改 (不变归入改)
		for (ShopItemSpec spec : detail.getSpecList()) {
			spec.setItemId(itemId);
			// 没图就用封面图
			if (StringUtil.isBlank(spec.getPicUrl())) {
				spec.setPicUrl(item.getItemImage());
			}
			spec.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
			if (spec.getId() == null) {
				// 增
				mapperSupport.shopItemSpecMapper.insert(spec);
			} else {
				// 改
				if (!specsOldSet.contains(spec.getId())) {
					continue;
				}
				mapperSupport.shopItemSpecMapper.updateByPrimaryKeySelective(spec);
				specsOldSet.remove(spec.getId());
			}
		}
		// 剩下的都删掉
		for (Integer id : specsOldSet) {
			ShopItemSpec spec = new ShopItemSpec();
			spec.setId(id);
			spec.setDelStatus(CommonConstant.DEL_STATUS_DEL);
			mapperSupport.shopItemSpecMapper.updateByPrimaryKeySelective(spec);
		}
		
		
		
		// todo 图片
		List<ShopItemPic> picList = detail.getPicList();
		// todo 详情图
		List<ShopItemPic> desc = detail.getDesc();
		
		return IReturnBean.success();
	}
		
		public IReturnBean<List<ShopItemDetail>> listForApp(AppQuery query) {
		long total = mapperSupport.shopItemMapper.count(null, query.getItemType());
		List<ShopItem> itemList = mapperSupport.shopItemMapper.queryForApp(null, query.getItemType(), (query.getPageNum()-1)* query.getPageSize(), query.getPageSize());
		List<ShopItemDetail> list = new ArrayList<>(itemList.size());
		for (ShopItem shopItem : itemList) {
			list.add(genDetailForApp(shopItem));
		}
		return IReturnBean.success(total, list);
	}
	
	public IReturnBean<List<ShopItemDetail>> queryForApp(AppQuery query) {
		long total = mapperSupport.shopItemMapper.count(query.getKeyword(), null);
		List<ShopItem> itemList = mapperSupport.shopItemMapper.queryForApp(query.getKeyword(), null, (query.getPageNum()-1)* query.getPageSize(), query.getPageSize());
		List<ShopItemDetail> list = new ArrayList<>(itemList.size());
		for (ShopItem shopItem : itemList) {
			list.add(genDetailForApp(shopItem));
		}
		return IReturnBean.success(total, list);
	}
	
	private ShopItemDetail genDetailForApp(ShopItem shopItem) {
		List<ShopItemSpec> specs = mapperSupport.shopItemSpecMapper.getByItem(shopItem.getId(), CommonConstant.ITEM_STATUS_UP);
		List<ShopItemPic> pics = mapperSupport.shopItemPicMapper.getByItem(shopItem.getId(), CommonConstant.PIC_TYPE_SWIPPER);
		ShopItemPic first = new ShopItemPic();
		first.setPicUrl(shopItem.getItemImage());
		pics.add(0, first);
		List<ShopItemPic> details = mapperSupport.shopItemPicMapper.getByItem(shopItem.getId(), CommonConstant.PIC_TYPE_DETAIL);
		return new ShopItemDetail(shopItem, specs, pics, details);
	}

	public IReturnBean<ShopItemDetail> detailForApp(Integer id) {
		if (id == null) {
			return IReturnBean.fail("查不到此商品");
		}
		ShopItem shopItem = mapperSupport.shopItemMapper.selectByPrimaryKey(id);
		if (shopItem == null || shopItem.getDelStatus() == CommonConstant.DEL_STATUS_DEL
			|| shopItem.getItemStatus() == CommonConstant.ITEM_STATUS_DOWN) {
			return IReturnBean.fail("查不到此商品");
		}
		return IReturnBean.success(genDetailForApp(shopItem));
	}
	
	public IReturnBean<ShopItemDetail> detail(Integer id) {
		if (id == null) {
			return IReturnBean.fail("查不到此商品");
		}
		ShopItem shopItem = mapperSupport.shopItemMapper.selectByPrimaryKey(id);
		if (shopItem == null || shopItem.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("查不到此商品");
		}
		List<ShopItemSpec> specs = mapperSupport.shopItemSpecMapper.getByItem(shopItem.getId(), null);
		List<ShopItemPic> pics = mapperSupport.shopItemPicMapper.getByItem(shopItem.getId(), CommonConstant.PIC_TYPE_SWIPPER);
		List<ShopItemPic> details = mapperSupport.shopItemPicMapper.getByItem(shopItem.getId(), CommonConstant.PIC_TYPE_DETAIL);
		return IReturnBean.success(new ShopItemDetail(shopItem, specs, pics, details));
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
		// 删除商品的同时，其下辖的spec、detail/pic、cart都删除
		List<ShopItemSpec> specs = mapperSupport.shopItemSpecMapper.getByItem(id, null);
		for (ShopItemSpec spec : specs) {
			spec.setDelStatus(CommonConstant.DEL_STATUS_DEL);
			mapperSupport.shopItemSpecMapper.updateByPrimaryKeySelective(spec);
		}
		List<ShopItemPic> pics = mapperSupport.shopItemPicMapper.getByItem(id, null);
		for (ShopItemPic pic : pics) {
			pic.setDelStatus(CommonConstant.DEL_STATUS_DEL);
			mapperSupport.shopItemPicMapper.updateByPrimaryKeySelective(pic);
		}
		List<ShopCart> carts = mapperSupport.shopCartMapper.getByItem(id);
		for (ShopCart cart : carts) {
			cart.setDelStatus(CommonConstant.DEL_STATUS_DEL);
//			cart.setUpdateTime(new Date());		非用户操作，应该是不影响用户那边的排序
			mapperSupport.shopCartMapper.updateByPrimaryKeySelective(cart);
		}
		return IReturnBean.success();
	}
	
	public IReturnBean<?> operate(Integer id) {
		if (id == null) {
			return IReturnBean.fail("未查询到商品");
		}
		ShopItem item = mapperSupport.shopItemMapper.selectByPrimaryKey(id);
		if (item == null || item.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("未查询到商品");
		}
		ShopItem record = new ShopItem();
		record.setId(id);
		// 商品状态【1：上架，2：下架，3：草稿】
		// 上架变为下架，下架和草稿都变为上架
		int itemStatus = item.getItemStatus() == CommonConstant.ITEM_STATUS_UP
				? CommonConstant.ITEM_STATUS_DOWN : CommonConstant.ITEM_STATUS_UP;
		record.setItemStatus(itemStatus);
		record.setUpdateTime(new Date());
		mapperSupport.shopItemMapper.updateByPrimaryKeySelective(record);
		// 上/下架商品的同时，其下辖的cart同步(spec另外单独更新，不在这里)
		List<ShopCart> carts = mapperSupport.shopCartMapper.getByItem(id);
		for (ShopCart cart : carts) {
			cart.setItemStatus(itemStatus);
//			cart.setUpdateTime(new Date());		非用户操作，应该是不影响用户那边的排序
			mapperSupport.shopCartMapper.updateByPrimaryKeySelective(cart);
		}
		return IReturnBean.success();
	}
}
