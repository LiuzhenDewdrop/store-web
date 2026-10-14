/* eslint-disable no-param-reassign */
/* eslint-disable no-nested-ternary */
import Toast from 'tdesign-miniprogram/toast/index';

Component({
  options: {
    multipleSlots: true,
    addGlobalClass: true,
  },

  properties: {
    src: {
      type: String,
    },
    title: String,
    show: {
      type: Boolean,
      value: false,
    },
    limitBuyInfo: {
      type: String,
      value: '',
    },
    limitMaxCount: {
      type: Number,
      value: 999,
    },
    limitMinCount: {
      type: Number,
      value: 1,
    },
    specList: {
      type: Array,
      value: [],
      observer(specList) {
        if (specList && specList.length > 0) {
          this.initData();
        }
      },
    },
    outOperateStatus: {
      type: Boolean,
      value: false,
    },
    hasAuth: {
      type: Boolean,
      value: false,
    },
    count: {
      type: Number,
      value: 1,
      observer(count) {
        this.setData({
          buyNum: count,
        });
      },
    },
  },

  initStatus: false,
  selectedSku: {},
  selectSpecObj: {},

  data: {
    buyNum: 1,
    isAllSelectedSku: false,
  },

  methods: {
    initData() {
      const { specList } = this.properties;
      const selectedSku = {};
      specList.forEach((item) => {
        selectedSku[item.specId] = '';
      });
      this.setData({
        specList,
      });
      this.selectSpecObj = {};
      this.selectedSku = {};
      this.initStatus = true;
    },

    flatten(input) {
      const stack = [...input];
      const res = [];
      while (stack.length) {
        const next = stack.pop();
        if (Array.isArray(next)) {
          stack.push(...next);
        } else {
          res.push(next);
        }
      }
      return res.reverse();
    },

    getIntersection(array, nextArray) {
      return array.filter((item) => nextArray.includes(item));
    },

    toChooseItem(e) {
      const specId = e.currentTarget.dataset.id;
      const { specList } = this.properties;
      let selectedSku = {};
      specList.forEach((item) => {
        if (item.id === specId) {
          item.isSelected = true;
          selectedSku = item;
        } else {
          item.isSelected = false;
        }
      });
      this.setData({
        specList,
      });
      this.selectedSku = selectedSku;
      this.triggerEvent('change', {
        specList,
        selectedSku,
      });
    },

    handlePopupHide() {
      this.triggerEvent('closeSpecsPopup', {
        show: false,
      });
    },

    specsConfirm() {
      this.triggerEvent('specsConfirm');
    },

    addCart() {
      this.triggerEvent('addCart');
    },

    buyNow() {
      const { isAllSelectedSku } = this.data;
      this.triggerEvent('buyNow', {
        isAllSelectedSku,
      });
    },

    handleBuyNumChange(e) {
      const buyNum = e.detail.value;
      this.setData({
        buyNum: buyNum,
      });
      this.triggerEvent('changeNum', {
        buyNum,
      });
    },
  },
});
