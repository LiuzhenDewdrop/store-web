import { RequestConfig, request } from './request';

// 微信一键登录
export function wxLogin(code, inviterId) {
  return request('/wx/login', 'POST', {
    code: code,
    inviterId: inviterId,
  });
}

// 退出登录
export function wxLogout() {
  return request('/wx/logout', 'GET');
}

// 首页商品列表
export function wxItemList(params) {
  return request('/wx/item/list', 'POST', params);
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

// 查询商品详情
export function wxQrCode(inviterId, spuId, page) {
  return request('/wx/qrCode', 'POST', {
    inviterId: inviterId,
    spuId: spuId,
    page: page,
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

// 查询购物车列表
export function wxCartList() {
  return request('/wx/cart/list', 'POST');
}
// 添加进购物车
export function wxCartAdd(cart) {
  return request('/wx/cart/add', 'POST', cart);
}
// 删除购物车中商品
export function wxCartDel(cart) {
  return request('/wx/cart/del', 'POST', cart);
}
// 修改购物车商品
export function wxCartUpd(cart) {
  return request('/wx/cart/upd', 'POST', cart);
}