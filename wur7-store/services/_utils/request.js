import { config } from '../../config/index';
import { getToken, clearLoginInfo } from '../../utils/auth';
import Toast from 'tdesign-miniprogram/toast/index';

/**
 * 网络请求工具
 * @param {string} url 接口路径
 * @param {string} method 请求方法 GET/POST
 * @param {object} data 请求参数
 */
export function request(url, method = 'GET', data = {}) {
  return new Promise((resolve, reject) => {
    const token = getToken();
    wx.request({
      url: config.apiRoot + url,
      method: method,
      data: data,
      header: {
        'content-type': 'application/json',
        'token': token
      },
      success: (res) => {
        if (res.statusCode === 200) {
          const result = res.data;
          // 未登录或token失效
          if (result.code === '0002' || result.code === '0001') {
            clearLoginInfo();
            Toast({
              context: getCurrentPages()[getCurrentPages().length - 1],
              selector: '#t-toast',
              message: result.msg || '请先登录',
              duration: 1000
            });
            reject(result);
            return;
          }
          // 业务失败
          if (result.code !== '0000' && result.code !== 0) {
            Toast({
              context: getCurrentPages()[getCurrentPages().length - 1],
              selector: '#t-toast',
              message: result.msg || '操作失败',
              duration: 1500
            });
            reject(result);
            return;
          }
          resolve(result);
        } else {
          Toast({
            context: getCurrentPages()[getCurrentPages().length - 1],
            selector: '#t-toast',
            message: '网络请求失败',
            duration: 1000
          });
          reject(res);
        }
      },
      fail: (err) => {
        Toast({
          context: getCurrentPages()[getCurrentPages().length - 1],
          selector: '#t-toast',
          message: '网络连接失败',
          duration: 1000
        });
        reject(err);
      }
    });
  });
}