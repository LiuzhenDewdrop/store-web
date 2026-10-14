import { cdnBase } from '../config/index';
const imgPrefix = cdnBase;

const defaultDesc = [`${imgPrefix}/goods/details-1.png`];

const allGoods = [
  {
    spuId: '101001',
    storeId: '1000',
    title: '计算机专业必学网课和证书推荐',
    primaryImage: 'https://t8.baidu.com/it/u=663471659,699964424&fm=3031&app=3031&size=r3,4&q=100&n=0&g=11n&f=PNG&fmt=auto&maxorilen2heic=2000000',
    images: ['https://t8.baidu.com/it/u=663471659,699964424&fm=3031&app=3031&size=r3,4&q=100&n=0&g=11n&f=PNG&fmt=auto&maxorilen2heic=2000000'
    ],
    video: null,
    salePrice: 40800,
    soldNum: 1024,
    specList: [
      {
        id: '1010011',
        specName: '数据结构',
        picUrl: "",
        salePrice: 40800,
      },
      {
        id: '1010012',
        specName: '计算机组成原理',
        picUrl: "",
        salePrice: 40800,
      },
      {
        id: '1010013',
        specName: '操作系统',
        picUrl: "",
        salePrice: 40800,
      },
      {
        id: '1010014',
        specName: '计算机网络',
        picUrl: "",
        salePrice: 40800,
      },
    ],
    limitInfo: [
      {
        text: '限购999件',
      },
    ],
    desc: [
      'https://t8.baidu.com/it/u=663471659,699964424&fm=3031&app=3031&size=r3,4&q=100&n=0&g=11n&f=PNG&fmt=auto&maxorilen2heic=2000000',
    ],
  },
  {
    spuId: '101002',
    storeId: '1000',
    title: '一建二建网课',
    primaryImage: 'https://t13.baidu.com/it/u=2072101363,3424254789&fm=224&app=112&f=JPEG?w=500&h=500',
    images: ['https://t13.baidu.com/it/u=2072101363,3424254789&fm=224&app=112&f=JPEG?w=500&h=500'
    ],
    video: null,
    salePrice: 500000,
    soldNum: 500,
    specList: [
      {
        id: '1010021',
        specName: '一建',
        picUrl: "",
        salePrice: 1000000,
      },
      {
        id: '1010022',
        specName: '二建',
        picUrl: "",
        salePrice: 500000,
      },
    ],
    limitInfo: [
      {
        text: '限购999件',
      },
    ],
    desc: [
      'https://t13.baidu.com/it/u=2072101363,3424254789&fm=224&app=112&f=JPEG?w=500&h=500',
    ],
  },
  {
    spuId: '101003',
    storeId: '1000',
    title: '初/中级经济师网课',
    primaryImage: 'https://t13.baidu.com/it/u=497790841,2164316838&fm=224&app=112&f=JPEG?w=500&h=500',
    images: ['https://t13.baidu.com/it/u=497790841,2164316838&fm=224&app=112&f=JPEG?w=500&h=500'
    ],
    video: null,
    salePrice: 500000,
    soldNum: 350,
    specList: [
      {
        id: '1010031',
        specName: '初级',
        picUrl: "",
        salePrice: 500000,
      },
      {
        id: '1010032',
        specName: '中级',
        picUrl: "",
        salePrice: 1000000,
      },
    ],
    limitInfo: [
      {
        text: '限购999件',
      },
    ],
    desc: [
      'https://t13.baidu.com/it/u=497790841,2164316838&fm=224&app=112&f=JPEG?w=500&h=500',
    ],
  },
  {
    spuId: '101004',
    storeId: '1000',
    title: '注册会计师网课',
    primaryImage: 'https://t13.baidu.com/it/u=704832135,3993084695&fm=224&app=112&f=JPEG?w=500&h=500',
    images: ['https://t13.baidu.com/it/u=704832135,3993084695&fm=224&app=112&f=JPEG?w=500&h=500'
    ],
    video: null,
    salePrice: 500000,
    soldNum: 300,
    specList: [
      {
        id: '1010041',
        specName: '注册会计师',
        picUrl: "",
        salePrice: 500000,
      },
    ],
    limitInfo: [
      {
        text: '限购999件',
      },
    ],
    desc: [
      'https://t13.baidu.com/it/u=704832135,3993084695&fm=224&app=112&f=JPEG?w=500&h=500',
    ],
  },
  {
    spuId: '101006',
    storeId: '1000',
    title: 'python全套课程',
    primaryImage: 'https://t14.baidu.com/it/u=526159569,4124330018&fm=224&app=112&f=JPEG?w=500&h=500',
    images: ['https://t14.baidu.com/it/u=526159569,4124330018&fm=224&app=112&f=JPEG?w=500&h=500'
    ],
    video: null,
    salePrice: 1000000,
    soldNum: 100,
    specList: [
      {
        id: '1010061',
        specName: 'python全套课程',
        picUrl: "",
        salePrice: 1000000,
      },
    ],
    limitInfo: [
      {
        text: '限购999件',
      },
    ],
    desc: [
      'https://t14.baidu.com/it/u=526159569,4124330018&fm=224&app=112&f=JPEG?w=500&h=500',
    ],
  },
];

/**
 * @param {string} id
 * @param {number} [available] 库存, 默认1
 */
export function genCourse(id, available = 1) {
  const item = allGoods[id % allGoods.length];
  return {
    ...item,
    spuId: `${id}`,
    available: available,
    desc: item?.desc || defaultDesc,
    images: item?.images || [item?.primaryImage],
  };
}
