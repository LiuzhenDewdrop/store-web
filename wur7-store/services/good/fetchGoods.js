import { config } from '../../config/index';

/** 获取商品列表 */
function mockFetchGoodsList(tab, pageIndex = 1, pageSize = 20) {
  const { delay } = require('../_utils/delay');
  const { getGoodsList } = require('../../model/goods');
  return delay().then(() =>
    getGoodsList(tab, pageIndex, pageSize).map((item) => {
      return {
        spuId: item.spuId,
        thumb: item.primaryImage,
        title: item.title,
        price: item.salePrice,
        originPrice: item.maxLinePrice,
      };
    }),
  );
}

/** 获取商品列表 */
export function fetchGoodsList(tab, pageIndex = 1, pageSize = 20) {
  if (config.useMock) {
    return mockFetchGoodsList(tab, pageIndex, pageSize);
  }
  return new Promise((resolve) => {
    resolve('real api');
  });
}
