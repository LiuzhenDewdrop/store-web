
Page({
  data: {
  },

  onShow() {
  },

  close() {
    this.setData({ dialogShow: false });
  },

  handleSubmit(e) {
    const searchValue = e.detail.value;
    if (!searchValue || searchValue.length === 0) return;
    wx.navigateTo({
      url: `/pages/goods/result/index?searchValue=${searchValue}`,
    });
  },
});
