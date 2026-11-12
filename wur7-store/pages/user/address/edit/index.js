import Toast from 'tdesign-miniprogram/toast/index';

import { wxDeliveryAdd, wxDeliveryUpd, wxDeliveryDetail } from '../../../../services/_utils/api'

import { fetchDeliveryAddress } from '../../../../services/address/fetchAddress';

const innerPhoneReg = '^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[0-8]|8\\d|9\\d)\\d{8}$';
const innerNameReg = '^[a-zA-Z\\d\\u4e00-\\u9fa5]+$';
const labelsOptions = [
  { id: 0, name: '家' },
  { id: 1, name: '公司' },
  { id: 2, name: '学校' },
  { id: 3, name: '亲友' },
];

Page({
  options: {
    multipleSlots: true,
  },
  externalClasses: ['theme-wrapper-class'],
  data: {
    locationState: {
      id: '',
      tagIndex: null,
      addressTag: '',
      detailAddress: '',
      isDefault: false,
      defaultStatus: 0,
      name: '',
      phone: '',
      provinceCode: '',
      provinceName: '',
      isEdit: false,
      isOrderDetail: false,
      isOrderSure: false,
    },
    labels: labelsOptions,
    submitActive: false,
    visible: false,
    labelValue: '',
    columns: 3,
  },
  privateData: {
    verifyTips: '',
  },
  onLoad(options) {
    const { id } = options;
    this.setData({
      'locationState.id': id,
    });
  },
  onUnload() {
  },

  onShow() {
    const { id } = this.data.locationState;
    if (id) {
      this.getAddressDetail(id);
    }
  },

  async getAddressDetail(id) {
    const res = await wxDeliveryDetail(id);
    if (res.code !== '0000') {
      return;
    }
    this.setData({ locationState: res.data }, () => {
      const { isLegal, tips } = this.onVerifyInputLegal();
      this.setData({
        submitActive: isLegal,
      });
      this.privateData.verifyTips = tips;
    });
  },

  onInputValue(e) {
    const { item } = e.currentTarget.dataset;
      const { value = '' } = e.detail;
      this.setData(
        {
          [`locationState.${item}`]: value,
        },
        () => {
          const { isLegal, tips } = this.onVerifyInputLegal();
          this.setData({
            submitActive: isLegal,
          });
          this.privateData.verifyTips = tips;
        },
      );
  },

  onPickLabels(e) {
    const { item } = e.currentTarget.dataset;
    const {
      locationState: { tagIndex = undefined },
      labels = [],
    } = this.data;
    let payload = {
      tagIndex: item,
      addressTag: labels[item].name,
    };
    if (item === tagIndex) {
      payload = { tagIndex: null, addressTag: '' };
    }
    this.setData({
      'locationState.tagIndex': payload.tagIndex,
    });
    this.triggerEvent('triggerUpdateValue', payload);
  },

  onCheckDefaultAddress({ detail }) {
    const { value } = detail;
    this.setData({
      'locationState.isDefault': value,
      'locationState.defaultStatus': value?1:0,
    });
  },

  onVerifyInputLegal() {
    const { name, phone, detailAddress } = this.data.locationState;
    const prefixPhoneReg = String(this.properties.phoneReg || innerPhoneReg);
    const prefixNameReg = String(this.properties.nameReg || innerNameReg);
    const nameRegExp = new RegExp(prefixNameReg);
    const phoneRegExp = new RegExp(prefixPhoneReg);

    if (!name || !name.trim()) {
      return {
        isLegal: false,
        tips: '请填写收货人',
      };
    }
    if (!nameRegExp.test(name)) {
      return {
        isLegal: false,
        tips: '收货人仅支持输入中文、英文（区分大小写）、数字',
      };
    }
    if (!phone || !phone.trim()) {
      return {
        isLegal: false,
        tips: '请填写手机号',
      };
    }
    if (!phoneRegExp.test(phone)) {
      return {
        isLegal: false,
        tips: '请填写正确的手机号',
      };
    }
    if (!detailAddress || !detailAddress.trim()) {
      return {
        isLegal: false,
        tips: '请完善详细地址',
      };
    }
    if (detailAddress && detailAddress.trim().length > 100) {
      return {
        isLegal: false,
        tips: '详细地址不能超过100个字符',
      };
    }
    return {
      isLegal: true,
      tips: '添加成功',
    };
  },

  // builtInSearch({ code, name }) {
  //   return new Promise((resolve, reject) => {
  //     wx.getSetting({
  //       success: (res) => {
  //         if (res.authSetting[code] === false) {
  //           wx.showModal({
  //             title: `获取${name}失败`,
  //             content: `获取${name}失败，请在【右上角】-小程序【设置】项中，将【${name}】开启。`,
  //             confirmText: '去设置',
  //             confirmColor: '#FA550F',
  //             cancelColor: '取消',
  //             success(res) {
  //               if (res.confirm) {
  //                 wx.openSetting({
  //                   success(settingRes) {
  //                     if (settingRes.authSetting[code] === true) {
  //                       resolve();
  //                     } else {
  //                       console.warn('用户未打开权限', name, code);
  //                       reject();
  //                     }
  //                   },
  //                 });
  //               } else {
  //                 reject();
  //               }
  //             },
  //             fail() {
  //               reject();
  //             },
  //           });
  //         } else {
  //           resolve();
  //         }
  //       },
  //       fail() {
  //         reject();
  //       },
  //     });
  //   });
  // },

  // onSearchAddress() {
  //   this.builtInSearch({ code: 'scope.userLocation', name: '地址位置' }).then(() => {
  //     // todo chooseLocation
  //   });
  // },
  async formSubmit() {
    const { submitActive } = this.data;
    if (!submitActive) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: this.privateData.verifyTips,
        icon: '',
        duration: 1000,
      });
      return;
    }
    const { locationState } = this.data;
    if (locationState.id) {
      const res = await wxDeliveryUpd(locationState);
      if (res.code !== '0000') {
        return;
      }
    } else {
      const res = await wxDeliveryAdd(locationState);
      if (res.code !== '0000') {
        return;
      }
    }
    wx.navigateBack({ delta: 1 });
  },
});
