Component({
  options: {
    addGlobalClass: true,
    multipleSlots: true, // 在组件定义时的选项中启用多slot支持
  },

  properties: {
    show: {
      type: Boolean,
      value: false,
    },
    value: {
      type: String,
      value: '',
    },
    thumbMode: {
      type: String,
      value: 'aspectFit',
    },
    zIndex: {
      type: Number,
      value: 99,
    },
    itemName: {
      type: String,
      observer(newVal) {
        this.setData({ 'goods.item.itemName': newVal });
      },
    },
    salePrice: {
      type: Number,
      value: '',
      observer(newVal) {
        this.setData({ 'goods.spec.salePrice': newVal });
      },
    },
    picUrl: {
      type: String,
      value: '',
      observer(newVal) {
        this.setData({ 'goods.spec.picUrl': newVal });
      },
    },
    specName: {
      type: String,
      value: '',
      observer(newVal) {
        this.setData({ 'goods.spec.specName': newVal });
      },
    },
    itemStatus: {
      type: Number,
      value: '',
      observer(newVal) {
        this.setData({ 'goods.cart.itemStatus': newVal });
      },
    },
  },

  data: {
    goods: {
      item: {},
      spec: {},
      cart: {},
    },
  },
  methods: {
    onClose() {
      this.triggerEvent('close');
    },

    onCloseOver() {
      this.triggerEvent('closeover');
    },
  },
});
