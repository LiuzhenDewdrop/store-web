import { getCategoryList } from '../../services/good/fetchCategoryList';
Page({
  data: {
    list: [],
  },
  async init() {
    try {
      const result = await getCategoryList();
      this.setData({
        list: result,
      });
    } catch (error) {
      console.error('err:', error);
    }
  },

  onShow() {
    this.getTabBar().init();
  },
  onChange(e) {
    // e.detail.item
    // const { index } = e.detail;
    // const { spuId } = this.data.goodsList[index];
    // wx.navigateTo({
    //   url: `/pages/goods/details/index?spuId=${spuId}`,
    // });
    wx.navigateTo({
      url: '/pages/goods/list/index',
    });
  },
  onLoad() {
    this.init(true);
  },
});
