import { Toast } from 'tdesign-miniprogram';

import { wxCategoryBase, wxCategoryDetail } from '../../services/_utils/api';

import { getCategoryList } from '../../services/good/fetchCategoryList';
Page({
  data: {
    categoryList: [],
    list: [],
  },
  async init() {
    try {
      const res = await wxCategoryBase();
      if (res.code !== '0000') {
        return
      }
      const categoryList = res.data;
      this.setData({
        categoryList,
      });
      this.selectCatetory(categoryList[0].id);
    } catch (error) {
      console.error('err:', error);
    }
  },
  onShow() {
    this.getTabBar().init();
  },
  onLoad() {
    this.init(true);
  },
  changeCatetory(e) {
    const {categoryList} = this.data;
    this.selectCatetory(categoryList[e.detail].id);
  },
  async selectCatetory(id) {
    const res = await wxCategoryDetail(id);
    if (res.code !== '0000') {
      return
    }
    const list = res.data;
    if (list.length) {
      list.forEach((item) => {
        if (item.list.length) {
          item.list.forEach((subItem) => {
            subItem.spuId = subItem.id;
            subItem.thumb = subItem.itemImage;
            subItem.title = subItem.itemName;
            subItem.price = subItem.salePrice;
          });
        }
      });
    }

    this.setData({
      list,
    });
  },
  chooseItem(e) {
    const spuId = e.detail.item.id;
    wx.navigateTo({
      url: `/pages/goods/details/index?spuId=${spuId}`,
    });

  },
  handleAddCart(e) {
    // e.detail 就是id
    Toast({
      context: this,
      selector: '#t-toast',
      message: '点击加购',
    });
  }
});
