import { config } from '../../config/index';

/** 获取商品列表 */
function mockFetchGoodsList(tab, pageIndex = 1, pageSize = 20) {
  const { delay } = require('../_utils/delay');
  const { getGoodsList } = require('../../model/goods');
  return delay().then(() =>
    getGoodsList(tab, pageIndex, pageSize).map((item) => {
      const minPrice = item.specList.reduce((min, obj) => {
        return (!min || obj.salePrice < min.salePrice) ? obj : min;
      }, null).salePrice;
      return {
        spuId: item.spuId,    // todo 似乎没用
        thumb: item.item.itemImage,
        title: item.item.itemName,
        price: minPrice,
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
