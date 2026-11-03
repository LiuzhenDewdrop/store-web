import { getLocalUserInfo } from '../../../../utils/auth';
import { wxProfitList } from '../../../../services/_utils/api';
import { fetchFundList } from '../../../../services/usercenter/fetchFunds';

Page({
  page: {
    size: 20,
    num: 1,
  },

  data: {
    title: '已到账',
    customer: 0,
    totalAmt: 0,
    list: [],
    loading: false,
    finished: false,
  },

  onLoad() {
    wx.setNavigationBarTitle({ title: this.data.title });
  },

  onShow() {
    const { id, fundAmt } = getLocalUserInfo();
    this.setData({
      customer: id,
      totalAmt: fundAmt,
    })
    this.refresh();
  },

  onPullDownRefresh() {
    this.refresh().finally(() => wx.stopPullDownRefresh());
  },

  onReachBottom() {
    if (this.data.loading || this.data.finished) return;
    this.loadMore();
  },

  refresh() {
    this.page.num = 1;
    this.setData({ finished: false, list: [], totalAmt: 0 });
    return this.loadMore(true);
  },

  async loadMore(isFirst = false) {
    this.setData({ loading: true });
    // if (true) {
    //   return  this.mockData(isFirst);
    // }
    try {
      const params = {
        offset: (this.page.num-1)*this.page.size,
        pageSize: this.page.size,
        intValue: 2,
        id: this.customer
      }
      const result = await wxProfitList(params);
      if (result.code !== '0000') {
        this.setData({
          loading: false,
        });
        return
      }
      
      const totalCount = result.count;
      if (isFirst && totalCount === 0) {
        this.setData({
          finished: true,
          loading: false,
        });
        return;
      }

      const data = result.data;
      const list = isFirst ? data : this.data.list.concat(data);
      this.page.num += 1;
      this.setData({
        list: list,
        loading: false,
        finished: totalCount <= list.length,
      });
    } catch (error) {
      this.setData({
        loading: false,
      });
    }
  },

  mockData(isFirst) {
    fetchFundList({
      pageNum: this.page.num,
      pageSize: this.page.size,
    })
      .then((res) => {
        const d = res.data || {};
        const rows = d.list || [];
        this.page.num += 1;
        this.setData({
          list: isFirst ? rows : this.data.list.concat(rows),
          // totalAmt: d.totalAmt || 0,
          loading: false,
          finished: !d.hasMore,
        });
      })
      .catch(() => {
        this.setData({ loading: false });
      });
  }
});
