import Dialog from 'tdesign-miniprogram/dialog/index';
import Toast from 'tdesign-miniprogram/toast/index';

import { wxDeliveryList, wxDeliveryDel } from '../../../../services/_utils/api'

import { fetchDeliveryAddressList } from '../../../../services/address/fetchAddress';

import { resolveAddress, rejectAddress } from '../../../../services/address/list';

const labelsOptions = [
  { id: 0, name: '家' },
  { id: 1, name: '公司' },
  { id: 2, name: '学校' },
  { id: 3, name: '亲友' },
];

Page({
  data: {
    id: 0,  // 从结算那边过来的时候，已经选中的地址
    addressList: [],
    isOrderSure: false,
  },

  /** 选择模式 */
  selectMode: false,
  /** 是否已经选择地址，不置为true的话页面离开时会触发取消选择行为 */
  hasSelect: false,

  onLoad(query) {
    const { selectMode = '', isOrderSure = '', id = '' } = query;
    this.setData({
      isOrderSure: !!isOrderSure,
      id,
    });
    this.selectMode = !!selectMode;
  },

  onShow() {
    this.getAddressList();
  },

  onUnload() {
    if (this.selectMode && !this.hasSelect) {
      rejectAddress();
    }
  },

  async getAddressList() {
    const { id } = this.data;
    const res = await wxDeliveryList();
    if (!res || res.code !== '0000') {
      return;
    }
    const addressList = res.data;
    for (const addr of addressList) {
      if (id && id == addr.id) {
        addr.checked = true;
      }
      addr.tag = labelsOptions[addr.tagIndex].name;
    }
    this.setData({ addressList });
  },

  selectHandle({ detail }) {
    if (this.selectMode) {
      this.hasSelect = true;
      resolveAddress(detail);
      wx.navigateBack({ delta: 1 });
    } else {
      this.editAddressHandle({ detail });
    }
  },

  editAddressHandle({ detail }) {
    const { id } = detail || {};
    wx.navigateTo({ 
      url: `/pages/user/address/edit/index?id=${id}` 
    });
  },

  createHandle() {
    wx.navigateTo({ url: '/pages/user/address/edit/index' });
  },

  async deleteAddressHandle(event) {
    const { id } = event.currentTarget.dataset;
    try {
      await Dialog.confirm({
        content: '确认删除该地址吗?',
        confirmBtn: '确定',
        cancelBtn: '取消',
      });
      // TDesign Dialog.confirm 确认后 resolve，取消 reject
    } catch (e) {
      // 用户点取消，直接返回
      return;
    }
  
    const res = await wxDeliveryDel(id);
    if (!res || res.code !== '0000') {
      return;
    }
    Toast({ 
      context: this, 
      selector: '#t-toast', 
      message: '地址删除成功' 
    });
    this.getAddressList();

  }
});
