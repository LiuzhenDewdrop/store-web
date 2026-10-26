import { wxItemList, wxCartAdd } from '../../services/_utils/api';
import { isLogin } from '../../utils/auth';

import Toast from 'tdesign-miniprogram/toast/index';

Page({
  data: {
    imgSrcs: [],
    tabList: [],
    goodsList: [],
    goodsListLoadStatus: 0,
    pageLoading: false,
    current: 1,
    autoplay: true,
    duration: '500',
    interval: 5000,
    navigation: { type: 'dots' },
    swiperImageProps: { mode: 'scaleToFill' },
    hasLoaded: false,
  },

  goodListPagination: {
    index: 1,
    num: 20,
  },

  privateData: {
    tabIndex: 1,
  },

  onShow() {
    this.getTabBar().init();
  },

  onLoad() {
    this.init();
  },

  onReachBottom() {
    if (this.data.goodsListLoadStatus === 0) {
      this.loadGoodsList();
    }
  },

  onPullDownRefresh() {
    this.init();
  },

  init() {
    this.loadHomePage();
  },

  loadHomePage() {
    wx.stopPullDownRefresh();

    this.setData({
      pageLoading: true,
    });
    this.fetchHome().then(({ swiper, tabList }) => {
      this.setData({
        tabList,
        imgSrcs: swiper,
        pageLoading: false,
      });
      this.loadGoodsList(true);
    });
  },

  fetchHome() {
    const { delay } = require('../../services/_utils/delay');
    const { genSwiperImageList } = require('../../model/swiper');
    return delay().then(() => {
      return {
        swiper: genSwiperImageList(),
        tabList: [
          {
            text: '活动',
            key: 1,
          },
          {
            text: '课程',
            key: 2,
          },
          {
            text: '商品',
            key: 3,
          },
        ],
        activityImg: `https://we-retail-static-1300977798.cos.ap-guangzhou.myqcloud.com/retail-mp/activity/banner.png`,
      };
    });
  },

  tabChangeHandle(e) {
    this.privateData.tabIndex = e.detail.value;
    this.loadGoodsList(true);
  },

  onReTry() {
    this.loadGoodsList();
  },

  async loadGoodsList(fresh = false) {
    if (fresh) {
      wx.pageScrollTo({
        scrollTop: 0,
      });
    }
    this.setData({ goodsListLoadStatus: 1 });
    let pageIndex = 1;
    if (!fresh) {
      pageIndex = this.goodListPagination.index + 1;
    }
    try {
      const params = {
        pageNum: pageIndex,
        pageSize: this.goodListPagination.num,
        itemType: this.privateData.tabIndex,
      };
      const result = await wxItemList(params);
      if (result.code !== '0000') {
        this.setData({
          goodsListLoadStatus: 3,
          hasLoaded: true,
        });
        wx.showToast({
          title: '查询失败',
          icon: 'error',
          message: result.msg,
        });
        return
      }
      const nextList = result.data;
      if (nextList.length) {
        nextList.forEach((item) => {
          item.spuId = item.item.id;
          item.thumb = item.item.itemImage;
          item.title = item.item.itemName;
          item.price = item.item.salePrice
        });
      }
      this.setData({
        goodsList: fresh ? nextList : this.data.goodsList.concat(nextList),
        goodsListLoadStatus: 0,
        hasLoaded: true,
      });
      this.goodListPagination.index = pageIndex;
    } catch (err) {
      this.setData({ goodsListLoadStatus: 3 });
    }
  },

  goodListClickHandle(e) {
    const { index } = e.detail;
    const { spuId } = this.data.goodsList[index];
    wx.navigateTo({
      url: `/pages/goods/details/index?spuId=${spuId}&tab=${this.privateData.tabIndex}`,
    });
  },

  async onAddCart(e) {
    if (!isLogin()) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请先登录',
      });
      return
    }
    const {
      goods: { item: {id} },
    } = e.detail;
    const res = await wxCartAdd(id);
    if (res.code !== '0000') {
      return;
    }
    Toast({
      context: this,
      selector: '#t-toast',
      message: '商品已加入购物车',
    });
  },

  navToSearchPage() {
    wx.navigateTo({ url: '/pages/goods/search/index' });
  },

});
