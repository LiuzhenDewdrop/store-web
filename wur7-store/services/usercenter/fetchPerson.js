import { config } from '../../config/index';
import { request } from '../_utils/request';

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
