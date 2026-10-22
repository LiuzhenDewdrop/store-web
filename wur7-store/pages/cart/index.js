import Dialog from 'tdesign-miniprogram/dialog/index';
import Toast from 'tdesign-miniprogram/toast/index';
import { fetchCartGroupData } from '../../services/cart/cart';

Page({
  data: {
    storeGoods: [],
    isNotEmpty: false,
    isAllSelected: false,
    selectedGoodsCount: 0,
    totalAmount: 0,
  },

  // 调用自定义tabbar的init函数，使页面与tabbar激活状态保持一致
  onShow() {
    this.getTabBar().init();
  },

  onLoad() {
    this.getCartGroupData().then((res) => {
      const { storeGoods } = this.data; 
      let selectedGoodsCount = 0;
      let totalAmount = 0;
      let isEmpty = true;
      const carts = res.data;
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
            selectedGoodsCount += goods.cart.quantity;
            totalAmount += goods.cart.quantity * goods.spec.salePrice;
          } else {
            store.shortageGoodsList.push(goods);
          }
          return goods.cart.itemStatus === 1;
        });
        if (store.list.length > 0) {
          isEmpty = false;
        }
        storeGoods.push(store);
      }
      this.setData({ 
        storeGoods,
        isNotEmpty: !isEmpty,
        totalAmount: totalAmount, 
        selectedGoodsCount: selectedGoodsCount,
       });
    });
  },

  refreshData() {
    
  },

  findGoods(spuId, skuId) {
    let currentStore;
    let currentActivity;
    let currentGoods;
    const { storeGoods } = this.data;
    for (const store of storeGoods) {
      for (const activity of store.promotionGoodsList) {
        for (const goods of activity.goodsPromotionList) {
          if (goods.spuId === spuId && goods.skuId === skuId) {
            currentStore = store;
            currentActivity = currentActivity;
            currentGoods = goods;
            return {
              currentStore,
              currentActivity,
              currentGoods,
            };
          }
        }
      }
    }
    return {
      currentStore,
      currentActivity,
      currentGoods,
    };
  },

  // 注：实际场景时应该调用接口获取购物车数据
  getCartGroupData() {
    const { storeGoods } = this.data;
    if (storeGoods.length == 0) {
      return fetchCartGroupData();
    }
    return Promise.resolve({ data: storeGoods });
  },

  // 全选门店
  selectStoreService({ storeId, isSelected }) {
    const currentStore = this.data.storeGoods.find((s) => s.storeId === storeId);
    currentStore.isSelected = isSelected;
    currentStore.list.forEach((goods) => {
        goods.cart.selectStatus = isSelected;
      });
    return Promise.resolve();
  },

  // 加购数量变更
  changeQuantityService({ spuId, skuId, quantity }) {
    this.findGoods(spuId, skuId).currentGoods.quantity = quantity;
    return Promise.resolve();
  },

  // 删除加购商品
  deleteGoodsService({ spuId, skuId }) {
    function deleteGoods(group) {
      for (const gindex in group) {
        const goods = group[gindex];
        if (goods.spuId === spuId && goods.skuId === skuId) {
          group.splice(gindex, 1);
          return gindex;
        }
      }
      return -1;
    }
    const { storeGoods } = this.data;
    for (const store of storeGoods) {
        if (deleteGoods(store.list) > -1) {
          return Promise.resolve();
      }
      if (deleteGoods(store.shortageGoodsList) > -1) {
        return Promise.resolve();
      }
    }
    return Promise.reject();
  },

  // 选一个
  onGoodsSelect(e) {
    const {
      goods,
      isSelected,
    } = e.detail;
    const { storeGoods } = this.data;
    const store = storeGoods.find(item => item.storeId === goods.item.shopId);
    const selectedGoods = store.list.find(item => item.item.id === goods.item.id);
    selectedGoods.cart.selectStatus = isSelected ? 1 : 0;
    // todo api
    // success
    let selectedGoodsCount = this.data.selectedGoodsCount;
    let totalAmount = this.data.totalAmount;
    if (isSelected) {
      selectedGoodsCount += selectedGoods.cart.quantity;
      totalAmount += selectedGoods.cart.quantity * selectedGoods.spec.salePrice;
    } else {
      selectedGoodsCount -= selectedGoods.cart.quantity;
      totalAmount -= selectedGoods.cart.quantity * selectedGoods.spec.salePrice;
    }
   
    this.setData({
      storeGoods,
      selectedGoodsCount: selectedGoodsCount,
      totalAmount: totalAmount,
    })
  },

  // 选店铺所有
  onStoreSelect(e) {
    const {
      store: { storeId },
      isSelected,
    } = e.detail;
    const currentStore = this.data.storeGoods.find((s) => s.storeId === storeId);
    currentStore.isSelected = isSelected;
    currentStore.list.forEach((goods) => {
        goods.cart.selectStatus = isSelected;
      });
    return Promise.resolve();
  },

  // 改变数量，直接触发选中
  onQuantityChange(e) {
    const {
      goods: { spuId, skuId },
      quantity,
    } = e.detail;
    const { currentGoods } = this.findGoods(spuId, skuId);
    // 加购数量超过库存数量
    this.changeQuantityService({ spuId, skuId, quantity }).then(() => this.refreshData());
  },

  goGoodsDetail(e) {
    const { item, spec, cart } = e.detail.goods;
    wx.navigateTo({
      // url: `/pages/goods/details/index?spuId=${item.id}&specId=${spec.id}&quantity=${cart.quantity}&tab=2`,
      url: `/pages/goods/details/index?spuId=0&specId=${spec.id}&quantity=${cart.quantity}&tab=2`,
    });
  },

  onGoodsDelete(e) {
    const {
      goods: { spuId, skuId },
    } = e.detail;
    Dialog.confirm({
      content: '确认删除该商品吗?',
      confirmBtn: '确定',
      cancelBtn: '取消',
    }).then(() => {
      this.deleteGoodsService({ spuId, skuId }).then(() => {
        Toast({ context: this, selector: '#t-toast', message: '商品删除成功' });
        this.refreshData();
      });
    });
  },

  onSelectAll(event) {
    const { isAllSelected } = event?.detail ?? {};
    Toast({
      context: this,
      selector: '#t-toast',
      message: `${isAllSelected ? '取消' : '点击'}了全选按钮`,
    });
    // 调用接口改变全选
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
