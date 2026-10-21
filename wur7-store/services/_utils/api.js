import { RequestConfig, request } from './request';

// 微信一键登录
export function wxLogin(code) {
  return request('/wx/login', 'POST', {
    code: code
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

// 查询商品详情
export function wxItemDetail(id) {
  return request('/wx/item/detail', 'POST', {
    id: id
  });
}

// 查询一级分类
export function wxCategoryBase() {
  return request('/wx/category/base', 'POST');
}

// 查询一级分类下辖内容
export function wxCategoryDetail(id) {
  return request('/wx/category/detail', 'POST', {
    id: id
  });
}