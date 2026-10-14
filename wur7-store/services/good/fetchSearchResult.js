/* eslint-disable no-param-reassign */
import { config } from '../../config/index';

/** 获取搜索历史 */
function mockSearchResult(params) {
  const { delay } = require('../_utils/delay');
  const { getSearchResult } = require('../../model/search');

  const data = getSearchResult(params);

  if (data.spuList.length) {
    data.spuList.forEach((item) => {
      item.spuId = item.spuId;
      item.thumb = item.primaryImage;
      item.title = item.title;
      item.price = item.salePrice;
      item.originPrice = item.maxLinePrice;
      if (item.spuTagList) {
        item.tags = item.spuTagList.map((tag) => ({ title: tag.title }));
      } else {
        item.tags = [];
      }
    });
  }
  return delay().then(() => {
    return data;
  });
}

/** 获取搜索历史 */
export function getSearchResult(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: config.apiRoot + '/wx/item/query',
      method: 'POST',
      data: params,
      header: {
        'content-type': 'application/json'
      },
      success: (res) => {
        if (res.data.code === '0000') {
          resolve(res.data) 
        } else {
          reject(res.data)        
        }
      },
      fail:(err) => reject(res.data)
    })
  })




    // todo delete
    // if (config.useMock) {
    //   return mockSearchResult(params);
    // }
  
}
