import { Toast } from 'tdesign-miniprogram';
import { cdnBase } from '../../../config/index';

import { wxItemDetail, wxQrCode, wxCartAdd } from '../../../services/_utils/api';
import { generatePoster } from '../../../utils/poster-generator'
import { isLogin, getLocalUserInfo } from '../../../utils/auth';

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
    navigation: { type: 'fraction' },
    current: 0,
    autoplay: true,
    duration: 500,
    interval: 5000,
    soldUnitNum: 0, // 已售数量
    showSharePopup: false,
    posterPath: '' // 海报
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

  // 加入购物车
  async addCart(itemId, specId, quantity) {
    if (!isLogin()) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请先登录',
      });
      return
    }
    const res = await wxCartAdd(itemId, specId, quantity);
    if (res.code !== '0000') {
      return;
    }
    Toast({
      context: this,
      selector: '#t-toast',
      message: '商品已加入购物车',
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
      const {
        details,
        selectItem,
        buyNum,
       } = this.data;
      this.addCart(details.item.id, selectItem?.id, buyNum);
    }
    this.handlePopupHide();
  },

  async getDetail(spuId, specId, quantity) {
    // 如果是点进来带数据的，需要刷一下数据
    let refresh = !isNaN(specId) && !isNaN(quantity);
    const res = await wxItemDetail(spuId);
    if (res.code !== '0000') {
      Toast({
        title: '查询失败',
        icon: 'error',
        message: res.msg,
      });
      return
    }
    const details = res.data;
    details.images = details.picList.map(a=>a.picUrl);
    this.setData({
      details,
      primaryImage: details.item.itemImage,
      soldUnitNum: details.item.soldUnitNum,
    });
    if (!refresh) {
      const salePrice = details.specList.reduce((min, obj) => {
        return (!min || obj.salePrice < min.salePrice) ? obj : min;
      }, null).salePrice;
      this.setData({
        salePrice: salePrice ? parseInt(salePrice) : 0,
      });
    } else {
      let selectedSku = details.specList.find(item => item.id === specId);
      let selectedAttrStr = ` 件, ${selectedSku.specName}`;
      this.setData({
        buyNum: quantity,
        selectedAttrStr: selectedAttrStr,
        selectItem: selectedSku,
        selectSkuSellsPrice: selectedSku.salePrice*quantity,
        salePrice: selectedSku.salePrice*quantity,
        specImg: selectedSku.picUrl ? selectedSku.picUrl : primaryImage,
      });
    }
  },


  onShareAppMessage() {
    // 未登录不能分享
    if (!isLogin()) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请登录后分享',
        icon: '',
        duration: 5000,
      });
      return new Promise((resolve) => {
        resolve(null); 
      });;
    }
    // 登录的人可以分享出去
    const localUser = getLocalUserInfo();
    const { selectedAttrStr } = this.data;
    let shareSubTitle = '';
    if (selectedAttrStr.indexOf('件') > -1) {
      const count = selectedAttrStr.indexOf('件');
      shareSubTitle = selectedAttrStr.slice(count + 1, selectedAttrStr.length);
    }
    const customInfo = {
      imageUrl: this.data.details.item.itemImage,
      title: this.data.details.item.itemName + shareSubTitle,
      path: `/pages/goods/details/index?spuId=${this.data.spuId}&inviterId=${localUser.id}&specIdNum=${this.data.selectItem.id}&quantity=${this.data.buyNum}`,
    };
    return customInfo;
  },

  onShareTimeline() {
     // 未登录不能分享
     if (!isLogin()) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请登录后分享',
        icon: '',
        duration: 5000,
      });
      return new Promise((resolve) => {
        resolve(null); 
      });
    }
    // 登录的人可以分享出去
    const localUser = getLocalUserInfo();
    let shareSubTitle = '';
    if (selectedAttrStr.indexOf('件') > -1) {
      const count = selectedAttrStr.indexOf('件');
      shareSubTitle = selectedAttrStr.slice(count + 1, selectedAttrStr.length);
    }
    return {
      title: this.data.details.item.itemName + shareSubTitle,
      query: `spuId=${this.data.spuId}&inviterId=${localUser.id}&specIdNum=${this.data.selectItem.id}&quantity=${this.data.buyNum}`
    };
  },

  onShareToTimeline() {
    // 未登录不能分享
    if (!isLogin()) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请登录后分享',
        icon: '',
        duration: 5000,
      });
      return new Promise((resolve) => {
        resolve(null); 
      });
    }
    Toast({
      context: this,
      selector: '#t-toast',
      message: '请点击右上角...分享到朋友圈吧~',
      icon: '',
      duration: 2000,
    });
  },

  openSharePopup() {
    // 未登录不能分享
    if (!isLogin()) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请登录后分享',
        icon: '',
        duration: 5000,
      });
      return new Promise((resolve) => {
        resolve(null); 
      });
    }
    // 有海报就直接发
    if (this.data.posterPath) {
      this.setData({
        showSharePopup: true,
      })
      return
    }
    // 生成海报
    const localUser = getLocalUserInfo();
    this.fetchPoster(localUser.id, this.data.spuId);
  },

  async fetchPoster(inviterId, spuId, specId, quantity) {
    wx.showLoading({ title: '海报生成中...' })
    try {
      // 拉取小程序码
      const res = await wxQrCode(inviterId, spuId, specId, quantity, "pages/goods/detail"); 
      if (res.code !== '0000') {
        Toast({
          title: '查询失败',
          icon: 'error',
          message: res.msg,
        });
        return
      }
      const qrRes = res.data;
      // 把二维码转成本地临时路径避免绘制失败
      const qrLocalPath = await wx.getImageInfo({ src: qrRes });
      const posterRes = await generatePoster('goodsPosterCanvas', this.data.details, qrLocalPath.path);
      this.setData({
        showSharePopup: true,
        posterPath: posterRes.tempFilePath
      })
    } catch (err) {
      Toast({ title: '海报生成失败', icon: 'error' })
    } finally {
      wx.hideLoading()
    }
  },

  closeSharePopup() {
    this.setData({
      showSharePopup: false, 
    });
  },

  // 保存图片
  async onSaveImage() {
    if (!this.data.posterPath) {
      Toast({ title: '图片还未生成', icon: 'none' });
      return;
    }
    // 先请求相册权限
    const authRes = await wx.getSetting()
    if (!authRes.authSetting['scope.writePhotosAlbum']) {
      await wx.authorize({ scope: 'scope.writePhotosAlbum' })
    }
    await wx.saveImageToPhotosAlbum({ filePath: this.data.posterPath })
    Toast({ title: '保存成功' })
  },

  onLoad(query) {
    const { spuId, specId, quantity } = query;
    const specIdNum = parseInt(specId);
    const quantityNum = parseInt(quantity);
    this.setData({
      spuId: spuId,
    });
    this.getDetail(spuId, specIdNum, quantityNum);
  },
});
