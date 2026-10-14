import Toast from 'tdesign-miniprogram/toast/index';
import { fetchGood } from '../../../services/good/fetchGood';

import { cdnBase } from '../../../config/index';

const imgPrefix = `${cdnBase}/`;

const recLeftImg = `${imgPrefix}common/rec-left.png`;
const recRightImg = `${imgPrefix}common/rec-right.png`;
const obj2Params = (obj = {}, encode = false) => {
  const result = [];
  Object.keys(obj).forEach((key) => result.push(`${key}=${encode ? encodeURIComponent(obj[key]) : obj[key]}`));

  return result.join('&');
};

Page({
  data: {
    recLeftImg,
    recRightImg,
    details: {},
    goodsTabArray: [
      {
        name: '商品',
        value: '', // 空字符串代表置顶
      },
      {
        name: '详情',
        value: 'goods-page',
      },
    ],
    storeLogo: `${imgPrefix}common/store-logo.png`,
    storeName: '5renqi旗舰店',
    jumpArray: [
      {
        title: '首页',
        url: '/pages/home/home',
        iconName: 'home',
      },
      {
        title: '购物车',
        url: '/pages/cart/index',
        iconName: 'cart',
        showCartNum: true,
      },
    ],
    isStock: true,
    cartNum: 0,
    soldout: false,
    buttonType: 1,
    buyNum: 1,
    selectedAttrStr: '',
    primaryImage: '',
    specImg: '',
    isSpuSelectPopupShow: false,
    buyType: 0,
    outOperateStatus: false, // 是否外层加入购物车
    operateType: 0,
    selectSkuSellsPrice: 0,
    salePrice: 0,
    spuId: '',
    tab: 0,
    navigation: { type: 'fraction' },
    current: 0,
    autoplay: true,
    duration: 500,
    interval: 5000,
    soldNum: 0, // 已售数量
  },

  handlePopupHide() {
    this.setData({
      isSpuSelectPopupShow: false,
    });
  },

  showSkuSelectPopup(type) {
    this.setData({
      buyType: type || 0,
      outOperateStatus: type >= 1,
      isSpuSelectPopupShow: true,
    });
  },

  buyItNow() {
    this.showSkuSelectPopup(1);
  },

  toAddCart() {
    this.showSkuSelectPopup(2);
  },

  toNav(e) {
    const { url } = e.detail;
    wx.switchTab({
      url: url,
    });
  },

  showCurImg(e) {
    const { index } = e.detail;
    const { images } = this.data.details;
    wx.previewImage({
      current: images[index],
      urls: images, // 需要预览的图片http链接列表
    });
  },

  onPageScroll({ scrollTop }) {
    const goodsTab = this.selectComponent('#goodsTab');
    goodsTab && goodsTab.onScroll(scrollTop);
  },

  chooseSpecItem(e) {
    // const { specList } = this.data.details;
    const { selectedSku } = e.detail;
    // this.setData({
    //   isAllSelectedSku,
    // });
    const { primaryImage } = this.data;
    let selectedAttrStr = ` 件, ${selectedSku.specName}`;
    this.selectSpecsName(selectedAttrStr);
    this.setData({
      selectItem: selectedSku,
      selectSkuSellsPrice: selectedSku.salePrice*this.data.buyNum,
      salePrice: selectedSku.salePrice*this.data.buyNum,
      specImg: selectedSku.picUrl ? selectedSku.picUrl : primaryImage,
    });
  },

  changeNum(e) {
    this.setData({
      buyNum: e.detail.buyNum,
      salePrice: e.detail.buyNum*this.data.selectItem.salePrice,
      selectSkuSellsPrice: e.detail.buyNum*this.data.selectItem.salePrice,
    });
  },

  selectSpecsName(selectSpecsName) {
    if (selectSpecsName) {
      this.setData({
        selectedAttrStr: selectSpecsName,
      });
    } else {
      this.setData({
        selectedAttrStr: '',
      });
    }
  },



  addCart() {
    // todo 加入购物车
    Toast({
      context: this,
      selector: '#t-toast',
      message: '已加入购物车',
      icon: '',
      duration: 1000,
    });
  },

  gotoBuy(type) {
    const { buyNum } = this.data;
    this.handlePopupHide();
    const query = {
      quantity: buyNum,
      storeId: '1',
      spuId: this.data.spuId,
      goodsName: this.data.details.title,
      skuId: type === 1 ? this.data.skuList[0].skuId : this.data.selectItem.skuId,
      price: this.data.details.salePrice,
      primaryImage: this.data.details.primaryImage,
      spuId: this.data.details.spuId,
      thumb: this.data.details.primaryImage,
      title: this.data.details.title,
    };
    let urlQueryStr = obj2Params({
      goodsRequestList: JSON.stringify([query]),
    });
    urlQueryStr = urlQueryStr ? `?${urlQueryStr}` : '';
    const path = `/pages/order/order-confirm/index${urlQueryStr}`;
    wx.navigateTo({
      url: path,
    });
  },

  specsConfirm() {
    const { buyType } = this.data;
    if (buyType === 1) {
      this.gotoBuy();
    } else {
      this.addCart();
    }
    // this.handlePopupHide();
  },

 


  getDetail(spuId, tab) {
    Promise.all([fetchGood(tab, spuId)]).then((res) => {
      const [details] = res;
      const { primaryImage, salePrice, soldNum } = details;
      this.setData({
        details,
        salePrice: salePrice ? parseInt(salePrice) : 0,
        primaryImage,
        soldNum,
      });
    });
  },


  onShareAppMessage() {
    // 自定义的返回信息
    const { selectedAttrStr } = this.data;
    let shareSubTitle = '';
    if (selectedAttrStr.indexOf('件') > -1) {
      const count = selectedAttrStr.indexOf('件');
      shareSubTitle = selectedAttrStr.slice(count + 1, selectedAttrStr.length);
    }
    const customInfo = {
      imageUrl: this.data.details.primaryImage,
      title: this.data.details.title + shareSubTitle,
      path: `/pages/goods/details/index?spuId=${this.data.spuId}`,
    };
    return customInfo;
  },


  onLoad(query) {
    const { spuId, tab } = query;
    const tabIndex = parseInt(tab);
    this.setData({
      spuId: spuId,
      tab: tabIndex,
    });
    this.getDetail(spuId, tabIndex);
  },
});
