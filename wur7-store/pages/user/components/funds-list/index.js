Component({
  options: {
    multipleSlots: false,
    styleIsolation: 'shared',
  },
  properties: {
    list: {
      type: Array,
      value: [],
    },
    // 时间字段名：待到账 = payTime，已到账 = receiveTime
    timeKey: {
      type: String,
      value: 'payTime',
    },
    loading: {
      type: Boolean,
      value: false,
    },
    finished: {
      type: Boolean,
      value: false,
    },
    emptyText: {
      type: String,
      value: '暂无记录',
    },
  },
  data: {
    rows: [],
  },
  observers: {
    'list, timeKey': function (list, timeKey) {
      const rows = (list || []).map((item) => ({
        ...item,
        _time: item[timeKey] || '',
      }));
      this.setData({ rows });
    },
  },
  methods: {},
});
