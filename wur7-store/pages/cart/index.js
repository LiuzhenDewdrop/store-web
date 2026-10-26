import Dialog from 'tdesign-miniprogram/dialog/index';
import Toast from 'tdesign-miniprogram/toast/index';

import { wxCartList, wxCartAdd, wxCartDel, wxCartUpd, wxCartSel } from '../../services/_utils/api'

Page({
  data: {
    storeGoods: [],
    isNotEmpty: false,
    isAllSelected: false,
    selectedGoodsCount: 0,
    totalAmount: 0,
  },
  
  onShow() {
    // 调用自定义tabbar的init函数，使页面与tabbar激活状态保持一致
    this.getTabBar().init();
    this.getCartGroupData();
  },

  onLoad() {
  },

  // 获取购物车数据
  async getCartGroupData() {
    const res = await wxCartList();
    if (res.code !== '0000') {
      return
    }
    const carts = res.data;
    let selectedGoodsCount = 0;
    let totalAmount = 0;
    let isEmpty = true;
    const storeGoods = [];
    let isAllSelected = true;
    for (const cart of carts) {
      const store = {};
      store.isSelected = true; // 该门店已加购商品是否全选
      store.storeName = cart[0].item.shopName;
      store.storeId = cart[0].item.shopId;
      store.shortageGoodsList = []; 
      store.list = cart.filter((goods) => {
        // 统计是否全选
        if (goods.cart.selectStatus === 0) {
          store.isSelected = false;
        }
        if (goods.cart.itemStatus === 1) {
          if (goods.cart.selectStatus === 1) {
            selectedGoodsCount += goods.cart.quantity;
            totalAmount += goods.cart.quantity * goods.spec.salePrice;
          }
        } else {
          store.shortageGoodsList.push(goods);
        }
        return goods.cart.itemStatus === 1;
      });
      if (store.list.length > 0) {
        isEmpty = false;
      }
      if (!store.isSelected) {
        isAllSelected = false;
      }
      storeGoods.push(store);
    }
    this.setData({ 
      storeGoods,
      isNotEmpty: !isEmpty,
      totalAmount: totalAmount, 
      selectedGoodsCount: selectedGoodsCount,
      isAllSelected: isAllSelected,
    });
  },

  // 全选
  async onSelectAll(e) {
    const { isAllSelected } = e.detail;
    const params = {
      selectAll: 1,
      selectStatus: isAllSelected ? 0: 1,
    }
    const res = await wxCartSel(params);
    if (res.code !== '0000') {
      return
    }
    this.getCartGroupData();
  },

   // 选店铺所有
  async onStoreSelect(e) {
    const {
      store,
      isSelected,
    } = e.detail;
    const params = {
      selectAll: 0,
      cartIds: [],
      selectStatus: isSelected ? 1: 0,
    }
    for (const item of store.list) {
      params.cartIds.push(item.cart.id);
    }
    const res = await wxCartSel(params);
    if (res.code !== '0000') {
      return
    }
    this.getCartGroupData();
  },

  // 选一个
  async onGoodsSelect(e) {
    const {
      goods: { cart: {id} },
      isSelected,
    } = e.detail;
    const params = {
      id: id,
      selectStatus: isSelected ? 1: 0,
    }
    const res = await wxCartUpd(params);
    if (res.code !== '0000') {
      return
    }
    this.getCartGroupData();
  },

  // 改变数量，直接触发选中
  async onQuantityChange(e) {
    const {
      goods: { cart: {id} },
      quantity,
    } = e.detail;
    const params = {
      id: id,
      quantity: quantity,
    }
    const res = await wxCartUpd(params);
    if (res.code !== '0000') {
      return
    }
    this.getCartGroupData();
  },

  // 删除
  async onGoodsDelete(e) {
    const {
      goods: { cart: {id} },
    } = e.detail;
    Dialog.confirm({
      content: '确认删除该商品吗?',
      confirmBtn: '确定',
      cancelBtn: '取消',
    }).then(async() => {
      const res = await wxCartDel(id);
      if (res.code !== '0000') {
        return
      }
      Toast({ context: this, selector: '#t-toast', message: '商品删除成功' });
      this.getCartGroupData();
    });
  },

  goGoodsDetail(e) {
    const { item, spec, cart } = e.detail.goods;
    wx.navigateTo({
      url: `/pages/goods/details/index?spuId=${item.id}&specId=${spec.id}&quantity=${cart.quantity}`,
    });
  },

  onToSettle() {
    const goodsRequestList = [];
    this.data.storeGoods.forEach((store) => {
      store.list.forEach((m) => {
          if (m.cart.selectStatus == 1) {
            goodsRequestList.push(m);
          }
      });
    });
    wx.setStorageSync('order.goodsRequestList', JSON.stringify(goodsRequestList));
    wx.navigateTo({ url: '/pages/order/order-confirm/index?type=cart' });
  },
  onGotoHome() {
    wx.switchTab({ url: '/pages/home/home' });
  },
});
