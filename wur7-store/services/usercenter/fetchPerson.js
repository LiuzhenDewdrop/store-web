import { config } from '../../config/index';
import { request } from '../_utils/request';

/** 获取个人中心信息 - mock */
function mockFetchPerson() {
  const { delay } = require('../_utils/delay');
  const { genSimpleUserInfo } = require('../../model/usercenter');
  const { genAddress } = require('../../model/address');
  const address = genAddress(0);
  return delay().then(() => ({
    ...genSimpleUserInfo(),
    address: {
      provinceName: address.provinceName,
      provinceCode: address.provinceCode,
      cityName: address.cityName,
      cityCode: address.cityCode,
    },
  }));
}

/** 获取个人中心信息 - 真实接口 */
export function fetchPerson() {
  // if (config.useMock) {
  //   return mockFetchPerson();
  // }
  return request('/wx/info', 'GET').then(res => {
    if (res.code === '0000' || res.code === 0) {
      const data = res.data || {};
      // 字段兼容转换
      return {
        avatarUrl: data.avatarUrl || data.avatar || '',
        nickName: data.nickname || data.userName || '微信用户',
        gender: data.gender || 0,
        phoneNumber: data.phone || data.phoneNumber || '',
      };
    }
    return Promise.reject(res);
  });
}

/** 更新用户昵称 */
export function updateNickname(nickname) {
  return request('/wx/update', 'POST', { nickname });
}

/** 更新用户性别 */
export function updateGender(gender) {
  return request('/wx/update', 'POST', { gender });
}

/** 上传头像 */
export function uploadAvatar(filePath) {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('WX_LOGIN_TOKEN') || '';
    wx.uploadFile({
      url: config.apiRoot + '/wx/uploadAvatar',
      filePath: filePath,
      name: 'file',
      header: {
        'token': token
      },
      success: (res) => {
        try {
          const data = JSON.parse(res.data);
          if (data.code === '0000' || data.code === 0) {
            resolve(data.data);
          } else {
            reject(data);
          }
        } catch (e) {
          reject(e);
        }
      },
      fail: reject
    });
  });
}

/** 绑定手机号 */
export function bindPhone(code) {
  return request('/wx/bindPhone', 'POST', { code });
}
