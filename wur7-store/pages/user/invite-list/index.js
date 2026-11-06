import { getLocalUserInfo } from '../../../utils/auth';
import { wxProfitPerson } from '../../../services/_utils/api';
import { fetchInviteList } from '../../../services/usercenter/fetchInviteList';

Page({
  data: {
    customer: 0,
    totalPersons: 0,
    list: [],
    loading: false,
    finished: false,
  },
  onLoad() {
    this.page = { num: 1, size: 20 };
    this.loadList(true);
  },
  onShow() {
    const { id, personNum } = getLocalUserInfo();
    this.setData({
      customer: id,
      totalPersons: personNum,
    })
  },
  onPullDownRefresh() {
    this.loadList(true);
  },
  onReachBottom() {
    this.loadList(false);
  },
  async loadList(reset = false) {
    if (this.data.loading) return;
    if (reset) {
      this.page.num = 1;
      this.setData({ finished: false, list: [] });
    }
    if (this.data.finished) return;
    this.setData({ loading: true });
    if (true) {
      this.mock();
      return
    }

    try {
      const params = {
        offset: (this.page.num-1)*this.page.size,
        pageSize: this.page.size,
        id: this.customer,
      }
      const result = await wxProfitPerson(params);
      if (result.code !== '0000') {
        this.setData({
          loading: false,
        });
        return
      }
      
      const totalCount = result.count;
      if (reset && totalCount === 0) {
        this.setData({
          finished: true,
          loading: false,
        });
        return;
      }

      const data = result.data;
      const list = reset ? data : this.data.list.concat(data);
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
  mock(reset) {
    fetchInviteList({ pageNum: this.page.num, pageSize: this.page.size })
    .then(res => {
      const newList = res.data.list || [];
      const allList = reset ? newList : this.data.list.concat(newList);
      const hasMore = newList.length >= this.page.size && res.data.hasMore !== false;
      this.setData({
        list: allList,
        loading: false,
        finished: !hasMore
      });
      if (hasMore) this.page.num++;
    })
    .catch(() => {
      this.setData({ loading: false });
    })
    .finally(() => {
      wx.stopPullDownRefresh();
    });
  },
});
