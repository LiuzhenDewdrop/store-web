Component({
  
  externalClasses: ['custom-class'],

  properties: {
    category: {
      type: Array,
    },
    itemList: {
      type: Array,
    },
    initActive: {
      type: Array,
      value: [],
      observer(newVal, oldVal) {
        if (newVal[0] !== oldVal[0]) {
          this.setActiveKey(newVal[0]);
        }
      },
    },
  },
  data: {
    activeKey: 0,
  },
  attached() {
    if (this.properties.initActive && this.properties.initActive.length > 0) {
      this.setData({
        activeKey: this.properties.initActive[0],
      });
    }
  },
  methods: {
    changeCatetory(event) {
      this.setActiveKey(event.detail.index).then(() => {
        this.triggerEvent('changeCatetory', this.data.activeKey);
      });
      
    },
    handleAddCart(event) {
        this.triggerEvent('handleAddCart', event.currentTarget.dataset.id);
    },
    gotoGoodsDetail(e) {
      const clickId = e.detail.goods.item.id;
      wx.navigateTo({
        url: `/pages/goods/details/index?spuId=${clickId}`,
      });
    },
    chooseItem(event) {
      const { item } = event.currentTarget.dataset;
      this.triggerEvent('chooseItem', {
        item,
      });
    },
    setActiveKey(key) {
      return new Promise((resolve) => {
        this.setData(
          {
            activeKey: key,
          },
          () => {
            resolve();
          },
        );
      });
    },
  },
});
