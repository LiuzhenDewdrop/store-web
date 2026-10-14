import { RequestConfig, request } from './request';

// 微信一键登录
export function wxLogin(code) {
  return request('/wx/login', 'POST', {
    code:code
  });
}

// 获取登录用户信息
export function getWxUserInfo() {
  return request('/wx/info', 'GET');
}

// 退出登录
export function wxLogout() {
  return request('/wx/logout', 'GET');
}

// 查询商品
export function wxItemQuery(params) {
  return request('/wx/item/query', 'POST', params);
}