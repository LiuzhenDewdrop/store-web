import { wxLogin } from '../services/_utils/api';

const TOKEN_KEY = 'WX_LOGIN_TOKEN';
const USER_INFO_KEY = 'WX_USER_INFO';
const INVITER_ID = 'INVITER_ID';

// 存储邀请人
export function setInviter(inviterId) {
  if (getInviter()) {
    return;
  }
  wx.setStorageSync(INVITER_ID, inviterId);
}

export function getInviter() {
  return wx.getStorageSync(INVITER_ID) || 0;
}

// 存储登录信息
export function setLoginInfo(token, userInfo) {
  wx.setStorageSync(TOKEN_KEY, token);
  wx.setStorageSync(USER_INFO_KEY, userInfo);
}

// 获取token
export function getToken() {
  return wx.getStorageSync(TOKEN_KEY) || '';
}

// 获取本地存储的用户信息
export function getLocalUserInfo() {
  return wx.getStorageSync(USER_INFO_KEY) || null;
}

// 清除登录信息
export function clearLoginInfo() {
  wx.removeStorageSync(TOKEN_KEY);
  wx.removeStorageSync(USER_INFO_KEY);
}

// 判断是否已登录
export function isLogin() {
  return !!getToken();
}

// 执行微信一键登录
export function doWxLogin() {
  return new Promise((resolve, reject) => {
    wx.login({
      success: async (res) => {
        if (res.code) {
          try {
            const result = await wxLogin(res.code, getInviter());
            if (result.code === '0000') {
              setLoginInfo(result.data.token, result.data);
              resolve(result.data);
            } else {
              reject(new Error(result.message || '登录失败'));
            }
          } catch (e) {
            reject(e);
          }
        } else {
          reject(new Error('获取微信登录code失败'));
        }
      },
      fail: (err) => {
        reject(err);
      }
    });
  });
}
