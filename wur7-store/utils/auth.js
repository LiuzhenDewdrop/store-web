import { wxLogin, getWxUserInfo } from '../services/login/index';

const TOKEN_KEY = 'WX_LOGIN_TOKEN';
const USER_INFO_KEY = 'WX_USER_INFO';

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
            const result = await wxLogin(res.code);
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

// 检查登录状态，未登录则自动登录
export async function checkLogin() {
  if (isLogin()) {
    // 已登录，尝试从后端拉取最新用户信息
    try {
      const result = await getWxUserInfo();
      if (result.code === 0) {
        setLoginInfo(getToken(), result.data);
        return result.data;
      } else {
        // token失效，清除登录信息
        clearLoginInfo();
      }
    } catch (e) {
      console.log('检查登录状态失败', e);
    }
  }
  // 未登录或token失效，自动登录
  return await doWxLogin();
}