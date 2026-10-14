import { config } from '../../config/index';

/** 获取商品列表 */
function mockFetchGood(tab, ID = 0) {
  const { delay } = require('../_utils/delay');
  const { getGoodItem } = require('../../model/goods');

  return delay().then(() => getGoodItem(tab, ID));
}

/** 获取商品列表 */
export function fetchGood(tab, ID = 0) {
  if (config.useMock) {
    return mockFetchGood(tab, ID);
  }
  return new Promise((resolve) => {
    resolve('real api');
  });
}
