Component({
  properties: {
    list: { type: Array, value: [] },
    loading: { type: Boolean, value: false },
    finished: { type: Boolean, value: false },
    emptyText: { type: String, value: '暂无记录' }
  },
  observers: {
    'list': function(list) {
      if (!list || list.length === 0) return;
      const rows = list.map(item => ({
        ...item,
        nickname: item.nickname || '匿名用户',
        timeText: item.createTime || '--'
      }));
      this.setData({ rows });
    }
  },
  data: {
    rows: []
  }
});
