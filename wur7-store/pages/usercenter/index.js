import { fetchUserCenter } from '../../services/usercenter/fetchUsercenter';
import { doWxLogin, isLogin, getLocalUserInfo, clearLoginInfo } from '../../utils/auth';
import { wxLogout } from '../../services/login/index';
import Toast from 'tdesign-miniprogram/toast/index';

const menuData = [
  [
    {
      title: '收货地址',
      tit: '',
      url: '',
      type: 'address',
    },
    {
      title: '优惠券',
      tit: '',
      url: '',
      type: 'coupon',
    },
    {
      title: '积分',
      tit: '',
      url: '',
      type: 'point',
    },
  ],
  [
    {
      title: '帮助中心',
      tit: '',
      url: '',
      type: 'help-center',
    },
    {
      title: '客服热线',
      tit: '',
      url: '',
      type: 'service',
      icon: 'service',
    },
  ],
];

const orderTagInfos = [
  {
    title: '待付款',
    iconName: 'wallet',
    orderNum: 0,
    tabType: 5,
    status: 1,
  },
  {
    title: '待发货',
    iconName: 'deliver',
    orderNum: 0,
    tabType: 10,
    status: 1,
  },
  {
    title: '待收货',
    iconName: 'package',
    orderNum: 0,
    tabType: 40,
    status: 1,
  },
  {
    title: '待评价',
    iconName: 'comment',
    orderNum: 0,
    tabType: 60,
    status: 1,
  },
  {
    title: '退款/售后',
    iconName: 'exchang',
    orderNum: 0,
    tabType: 0,
    status: 1,
  },
];

const getDefaultData = () => ({
  showMakePhone: false,
  userInfo: {
    avatarUrl: '',
    nickName: '未登录',
    phoneNumber: '',
  },
  menuData,
  orderTagInfos,
  customerServiceInfo: {},
  currAuthStep: 1, // 1=未登录 2=已登录
  showKefu: true,
  versionNo: '',
});

Page({
  data: getDefaultData(),

  onLoad() {
    this.getVersionInfo();
  },

  onShow() {
    this.getTabBar().init();
    this.init();
  },
  onPullDownRefresh() {
    this.init();
  },

  init() {
    this.checkLoginStatus();
  },

  // 检查登录状态
  checkLoginStatus() {
    if (isLogin()) {
      // 已登录，获取用户信息
      const localUser = getLocalUserInfo();
      this.setData({
        currAuthStep: 2,
        userInfo: {
          avatarUrl: localUser.avatarUrl || '',
          nickName: localUser.userName || '微信用户',
          phoneNumber: localUser.phoneNumber || ''
        }
      });
      this.fetUseriInfoHandle();
    } else {
      // 未登录
      this.setData({
        currAuthStep: 1,
        userInfo: {
          avatarUrl: '',
          nickName: '未登录',
          phoneNumber: ''
        }
      });
      wx.stopPullDownRefresh();
    }
  },

  // 微信一键登录
  async handleLogin() {
    if (isLogin()) return;
    
    wx.showLoading({ title: '登录中...' });
    try {
      // 先获取用户信息授权，再登录
      const userInfoRes = await wx.getUserProfile({
        desc: '用于完善会员资料'
      });
      const userInfo = {
        nickName: userInfoRes.userInfo.nickName,
        avatarUrl: userInfoRes.userInfo.avatarUrl,
        gender: userInfoRes.userInfo.gender
      };
      const loginData = await doWxLogin(userInfo);
      wx.hideLoading();
      Toast({
        context: this,
        selector: '#t-toast',
        message: '登录成功',
        theme: 'success',
        duration: 1000
      });
      this.checkLoginStatus();
    } catch (e) {
      wx.hideLoading();
      console.error('登录失败', e);
      // 如果用户拒绝授权，使用默认信息登录
      try {
        await doWxLogin();
        wx.hideLoading();
        Toast({
          context: this,
          selector: '#t-toast',
          message: '登录成功',
          theme: 'success',
          duration: 1000
        });
        this.checkLoginStatus();
      } catch (err) {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '登录失败，请重试',
          duration: 2000
        });
      }
    }
  },

  fetUseriInfoHandle() {
    fetchUserCenter().then(({ userInfo, countsData, orderTagInfos: orderInfo, customerServiceInfo }) => {
      // eslint-disable-next-line no-unused-expressions
      menuData?.[0].forEach((v) => {
        countsData.forEach((counts) => {
          if (counts.type === v.type) {
            // eslint-disable-next-line no-param-reassign
            v.tit = counts.num;
          }
        });
      });
      const info = orderTagInfos.map((v, index) => ({
        ...v,
        ...orderInfo[index],
      }));
      this.setData({
        menuData,
        orderTagInfos: info,
        customerServiceInfo,
      });
      wx.stopPullDownRefresh();
    });
  },

  onClickCell({ currentTarget }) {
    const { type } = currentTarget.dataset;
    
    // 未登录先登录
    if (!isLogin() && type !== 'service' && type !== 'help-center') {
      this.handleLogin();
      return;
    }

    switch (type) {
      case 'address': {
        wx.navigateTo({ url: '/pages/user/address/list/index' });
        break;
      }
      case 'service': {
        this.openMakePhone();
        break;
      }
      case 'help-center': {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '你点击了帮助中心',
          icon: '',
          duration: 1000,
        });
        break;
      }
      case 'point': {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '你点击了积分菜单',
          icon: '',
          duration: 1000,
        });
        break;
      }
      case 'coupon': {
        wx.navigateTo({ url: '/pages/coupon/coupon-list/index' });
        break;
      }
      default: {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '未知跳转',
          icon: '',
          duration: 1000,
        });
        break;
      }
    }
  },

  jumpNav(e) {
    const status = e.detail.tabType;

    if (status === 0) {
      wx.navigateTo({ url: '/pages/order/after-service-list/index' });
    } else {
      wx.navigateTo({ url: `/pages/order/order-list/index?status=${status}` });
    }
  },

  jumpAllOrder() {
    wx.navigateTo({ url: '/pages/order/order-list/index' });
  },

  openMakePhone() {
    this.setData({ showMakePhone: true });
  },

  closeMakePhone() {
    this.setData({ showMakePhone: false });
  },

  call() {
    wx.makePhoneCall({
      phoneNumber: this.data.customerServiceInfo.servicePhone,
    });
  },

  gotoUserEditPage() {
    const { currAuthStep } = this.data;
    if (currAuthStep === 2) {
      // 已登录，跳转个人资料页
      wx.navigateTo({ url: '/pages/user/person-info/index' });
    } else {
      // 未登录，触发登录
      this.handleLogin();
    }
  },

  getVersionInfo() {
    const versionInfo = wx.getAccountInfoSync();
    const { version, envVersion = __wxConfig } = versionInfo.miniProgram;
    this.setData({
      versionNo: envVersion === 'release' ? version : envVersion,
    });
  },
});
