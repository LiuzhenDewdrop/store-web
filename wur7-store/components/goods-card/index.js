Component({
  options: {
    addGlobalClass: true,
  },

  properties: {
    id: {
      type: String,
      value: '',
      observer(id) {
        this.genIndependentID(id);
      },
    },
    data: {
      type: Object,
      observer(data) {
        if (!data) {
          return;
        }
        this.setData({ goods: data });
      },
    },
    currency: {
      type: String,
      value: '¥',
    },
  },

  data: {
    independentID: '',
    goods: { id: '' },
  },

  lifetimes: {
    ready() {
      this.init();
    },
    detached() {
      this.clear();
    },
  },

  pageLifeTimes: {},

  methods: {
    onError(e) {
      console.log('图片加载失败:', e.detail.errMsg);
      // 常见 errMsg:
      // "downloadFile:fail url not in domain list" -> 域名未配置
      // "downloadFile:fail status code 403" -> 服务器防盗链/权限拒绝
      // "downloadFile:fail status code 404" -> 图片路径错误
      // "downloadFile:fail timeout" -> 网络超时
    },

    clickHandle() {
      this.triggerEvent('click', { goods: this.data.goods });
    },

    clickThumbHandle() {
      this.triggerEvent('thumb', { goods: this.data.goods });
    },

    addCartHandle(e) {
      const { id } = e.currentTarget;
      const { id: cardID } = e.currentTarget.dataset;
      this.triggerEvent('add-cart', {
        ...e.detail,
        id,
        cardID,
        goods: this.data.goods,
      });
    },

    genIndependentID(id) {
      let independentID;
      if (id) {
        independentID = id;
      } else {
        independentID = `goods-card-${~~(Math.random() * 10 ** 8)}`;
      }
      this.setData({ independentID });
    },

    init() {
      const { id } = this.properties;
      this.genIndependentID(id);
    },

    clear() {
    },
  },
});
