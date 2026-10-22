import { mockIp, mockReqId } from '../utils/mock';

export function genCartGroupData() {
  const resp = {
    data: [[
                {
                  cart: {
                    selectStatus: 1,
                    quantity: 2,
                    itemStatus: 1,
                  },
                  uid: '88888888205468',
                  
                  spuId: '12',
                  skuId: '135691622',
                  item: {
                    id: 1,
                    selectStatus: 1,
                    quantity: 1,
                    shopId: 1001,
                    shopName: '云Mall深圳旗舰店',
                    itemName: '腾讯极光盒子4智能网络电视机顶盒6K千兆网络机顶盒4K高分辨率',
                  },
                  spec:{
                      picUrl: 'https://tdesign.gtimg.com/miniprogram/template/retail/goods/dz-3a.png',
                      specName: '经典套装',
                      salePrice: '9900',
                  },
                },
                {
                  uid: '88888888205468',
                  
                  spuId: '18',
                  skuId: '135681631',
                  cart: {
                    selectStatus: 1,
                    quantity: 1,
                    itemStatus: 1,
                  },
                  item: {
                    id: 100001,
                    shopId: 1001,
                    shopName: '云Mall深圳旗舰店',
                    itemName: '白色短袖连衣裙荷叶边裙摆宽松韩版休闲纯白清爽优雅连衣裙',
                  },
                  spec:{
                      id: 1000012,
                      picUrl: 'https://tdesign.gtimg.com/miniprogram/template/retail/goods/nz-09a.png',
                      specName: '颜色:米色荷叶边,尺码:M',
                      salePrice: '29800',
                  },
                },
                {
                  uid: '88888888205468',
                  
                  spuId: '13',
                  skuId: '135698362',
                  cart: {
                    selectStatus: 1,
                    quantity: 14,
                    itemStatus: 1,
                  },
                  item: {
                    id: 10,
                    shopId: 1001,
                    shopName: '云Mall深圳旗舰店',
                    itemName: '带帽午休毯虎年款多功能加厚加大加绒简约多功能午休毯连帽披肩',
                  },
                  spec:{
                      picUrl: 'https://tdesign.gtimg.com/miniprogram/template/retail/goods/muy-3a.png',
                      specName: '颜色浅灰色尺码M',
                      salePrice: '29900',
                  },
                },
                {
                  uid: '88888888205468',
                  
                  spuId: '7',
                  skuId: '135681625',
                  cart: {
                    selectStatus: 1,
                    quantity: 1,
                    itemStatus: 1,
                  },
                  item: {
                    id: 16,
                    shopId: 1001,
                    shopName: '云Mall深圳旗舰店',
                    itemName: '不锈钢刀叉勺套装家用西餐餐具ins简约耐用不锈钢金色银色可选',
                  },
                  spec:{
                      picUrl: 'https://tdesign.gtimg.com/miniprogram/template/retail/goods/gh-2b.png',
                      specName: '颜色:奶黄色,数量:六件套',
                      salePrice: '29900',
                  },
                },
                {
                  uid: '88888888205468',
                  
                  spuId: '11',
                  skuId: '135691629',
                  cart: {
                    selectStatus: 0,
                    quantity: 1,
                    itemStatus: 2,
                  },
                  item: {
                    id: 10041,
                    shopId: 1001,
                    shopName: '云Mall深圳旗舰店',
                    itemName: '运动连帽拉链卫衣休闲开衫长袖多色运动细绒面料运动上衣',
                  },
                  spec:{
                      picUrl: 'https://tdesign.gtimg.com/miniprogram/template/retail/goods/nz-17a.png',
                      specName: '颜色军绿色尺码S',
                      salePrice: '25900',
                  },
                },
                {
                  uid: '88888888205468',
                  
                  spuId: '5',
                  skuId: '135691635',
                  cart: {
                    selectStatus: 0,
                    quantity: 1,
                    itemStatus: 1,
                  },
                  item: {
                    id: 911,
                    shopId: 1001,
                    shopName: '云Mall深圳旗舰店',
                    itemName: '迷你便携高颜值蓝牙无线耳机立体声只能触控式操作简约立体声耳机',
                  },
                  spec:{
                      picUrl: 'https://tdesign.gtimg.com/miniprogram/template/retail/goods/dz-2a.png',
                      specName: '颜色黑色类型简约款',
                      salePrice: '29000',
                  },
                },
      ],
    ],
    code: 'Success',
    msg: null,
    requestId: mockReqId(),
    clientIp: mockIp(),
    rt: 269,
    success: true,
  };
  return resp;
}
