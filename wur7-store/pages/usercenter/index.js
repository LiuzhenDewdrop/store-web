import { fetchUserCenter } from '../../services/usercenter/fetchUsercenter';
import { doWxLogin, isLogin, getLocalUserInfo, clearLoginInfo, setLoginInfo, getToken } from '../../utils/auth';
import { wxProfitOverall, wxQrCode } from '../../services/_utils/api';
import Toast from 'tdesign-miniprogram/toast/index';
import { generateHomePoster } from '../../utils/poster-generator';

const menuData = [
  [
    {
      title: '收货地址',
      tit: '',
      url: '',
      type: 'address',
    },

  ],
  [
    // {
    //   title: '帮助中心',
    //   tit: '',
    //   url: '',
    //   type: 'help-center',
    // },
    {
      title: '客服',
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
  unfundAmt: 0,
  persons: 0,
  fundAmt: 0,
  share_img: 'https://5renqi.com/store-web/image/item/2026-11/06/PGPexp1793931657553.jpg',
  share_poster: 'https://5renqi.com/store-web/image/item/2026-11/06/pmNqve1793933083263.jpg',
  showSharePopup: false,
  posterPath: '', // 生成的海报路径
  support: 'https://5renqi.com/store-web/image/item/2026-11/06/G5ZdwO1793933105195.jpg',
  showSupportQrcode: false,
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
        },
      });
      this.fetchOverall(localUser.userId);
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

  // 拉取三个总账信息
  async fetchOverall(id) {
    const res = await wxProfitOverall(id);
    if (res.code !== '0000') {
      return;
    }
    const data = res.data;
    // 更新本地缓存
    const localUser = getLocalUserInfo();
    setLoginInfo(getToken(), {
      ...localUser, 
      unfundAmt: data.unfundAmt,
      persons: data.personNum,
      fundAmt: data.fundAmt,
    });
    this.setData({
      unfundAmt: data.unfundAmt || 0,
      persons: data.personNum || 0,
      fundAmt: data.fundAmt || 0,
    });
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
      console.error('登录失败1', e);
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
        console.error('登录失败2', e);
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
        this.openSupportQrcode();
        break;
      }
      // case 'help-center': {
      //   Toast({
      //     context: this,
      //     selector: '#t-toast',
      //     message: '你点击了帮助中心',
      //     icon: '',
      //     duration: 1000,
      //   });
      //   break;
      // }
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

  // 打开客服二维码弹窗
  openSupportQrcode() {
    this.setData({ showSupportQrcode: true });
  },

  // 关闭客服二维码弹窗（点击遮罩层时）
  closeSupportQrcode() {
    this.setData({ showSupportQrcode: false });
  },

  // 阻止点击二维码区域冒泡关闭弹窗
  noop() {},

  previewSupport() {
    const url = this.data.support;
    wx.previewImage({ current: url, urls: [url] });
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

    // 待到账
    onClickUnfund() {
      if (!isLogin()) {
        this.handleLogin();
        return;
      }
      wx.navigateTo({ url: '/pages/user/funds/unfund/index' });
    },
  
    // 邀请人数
    onClickPersons() {
      if (!isLogin()) {
        this.handleLogin();
        return;
      }
      wx.navigateTo({ url: '/pages/user/invite-list/index' });
    },
  
    // 已到账
    onClickFund() {
      if (!isLogin()) {
        this.handleLogin();
        return;
      }
      wx.navigateTo({ url: '/pages/user/funds/fund/index' });
    },

    // 分享给朋友
    onShareAppMessage() {
      // 未登录不能分享
      if (!isLogin()) {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '请登录后分享',
          icon: '',
          duration: 2000,
        });
        return new Promise((resolve) => {
          resolve(null); 
        });
      }
      // 登录的人可以分享出去
      const localUser = getLocalUserInfo();
      return {
        title: '欢迎来到商城',
        path: `/pages/home/home?inviterId=${localUser.userId}`,
        imageUrl: this.data.share_img || ''
      };
    },

    // 分享到朋友圈
    onShareTimeline() {
      // 未登录不能分享
      if (!isLogin()) {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '请登录后分享',
          icon: '',
          duration: 2000,
        });
        return new Promise((resolve) => {
          resolve(null); 
        });
      }
      const localUser = getLocalUserInfo();
      return {
        title: '欢迎来到商城',
        query: `inviterId=${localUser.userId}`
      };
    },

    // 朋友圈分享提示
    onShareToTimeline() {
      // 未登录不能分享
      if (!isLogin()) {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '请登录后分享',
          icon: '',
          duration: 2000,
        });
        return;
      }
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请点击右上角...分享到朋友圈吧~',
        icon: '',
        duration: 2000,
      });
    },

    // 打开分享弹窗
    openSharePopup() {
      // 未登录不能分享
      if (!isLogin()) {
        this.handleLogin();
        return;
      }
      // 有海报就直接打开发弹窗
      if (this.data.posterPath) {
        this.setData({
          showSharePopup: true,
        })
        return
      }
      // 没有海报背景图提示
      if (!this.data.share_poster) {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '分享海报正在准备中',
          icon: '',
          duration: 2000,
        });
        return;
      }
      // 生成海报
      const localUser = getLocalUserInfo();
      this.fetchPoster(localUser.userId);
    },

    // 获取并生成海报
    async fetchPoster(inviterId) {
      wx.showLoading({ title: '海报生成中...' })
      try {
        // 拉取小程序码，跳转到首页
        const res = await wxQrCode(inviterId, null, "pages/home/home"); 
        if (res.code !== '0000') {
          return
        }
        const qrRes = res.data;
        // 把二维码转成本地临时路径避免绘制失败
        const qrLocalPath = await wx.getImageInfo({ src: qrRes });
        // 使用share_poster作为海报背景生成海报
        const posterRes = await generateHomePoster('userSharePosterCanvas', this.data.share_poster, qrLocalPath.path);
        this.setData({
          showSharePopup: true,
          posterPath: posterRes.tempFilePath
        })
      } catch (err) {
        console.error('海报生成失败', err);
        Toast({ 
          context: this,
          selector: '#t-toast',
          message: '海报生成失败，请重试',
          icon: 'error' ,
        })
      } finally {
        wx.hideLoading()
      }
    },

    // 关闭分享弹窗
    closeSharePopup() {
      this.setData({
        showSharePopup: false, 
      });
    },

    // 保存图片到相册
    async onSaveImage() {
      if (!this.data.posterPath) {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '图片还未生成，请稍后重试',
          theme: 'warning',
        });
        return;
      }
      // 1. 先确保拿到「保存到相册」权限
      const authed = await this.ensureAlbumAuth();
      if (!authed) {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '未获得相册权限，无法保存',
          theme: 'warning',
        });
        return;
      }

      // 2. 保存本地临时图片到相册
      wx.showLoading({ title: '保存中...', mask: true });
      try {
        await this.saveToAlbum(this.data.posterPath);
        wx.hideLoading();
        Toast({
          context: this,
          selector: '#t-toast',
          message: '图片已保存到相册',
          theme: 'success',
        });
      } catch (err) {
        wx.hideLoading();
        const errMsg = (err && err.errMsg) || '';
        console.error('[onSaveImage] 保存失败:', errMsg);

        // 用户主动取消，不打扰
        if (/cancel/i.test(errMsg)) return;

        // 系统层面关掉了相册权限：引导一次设置页
        if (/auth/i.test(errMsg) && (await this.guideToOpenSetting())) {
          try {
            wx.showLoading({ title: '保存中...', mask: true });
            await this.saveToAlbum(this.data.posterPath);
            wx.hideLoading();
            Toast({
              context: this,
              selector: '#t-toast',
              message: '图片已保存到相册',
              theme: 'success',
            });
            return;
          } catch (retryErr) {
            wx.hideLoading();
            console.error('[onSaveImage] 重试保存失败:', retryErr);
          }
        }

        Toast({
          context: this,
          selector: '#t-toast',
          message: '保存失败，请重试',
          theme: 'error',
        });
      }
    },

    // 确保已获得「保存到相册」权限
    ensureAlbumAuth() {
      const scope = 'scope.writePhotosAlbum';
      return new Promise((resolve) => {
        wx.getSetting({
          success: (res) => {
            const value = (res.authSetting || {})[scope];
            if (value === true) {
              resolve(true);
            } else if (value === false) {
              resolve(this.guideToOpenSetting());
            } else {
              wx.authorize({
                scope,
                success: () => resolve(true),
                fail: () => resolve(this.guideToOpenSetting()),
              });
            }
          },
          fail: () => resolve(false),
        });
      });
    },

    // 引导用户到设置页开启相册权限
    guideToOpenSetting() {
      return new Promise((resolve) => {
        wx.showModal({
          title: '需要相册权限',
          content: '保存图片需要您授权「保存到相册」，请在设置中开启后重试',
          confirmText: '去设置',
          confirmColor: '#FA550F',
          cancelText: '取消',
          success: (modalRes) => {
            if (!modalRes.confirm) {
              resolve(false);
              return;
            }
            wx.openSetting({
              success: (settingRes) => {
                const authed = (settingRes.authSetting || {})['scope.writePhotosAlbum'];
                resolve(!!authed);
              },
              fail: () => resolve(false),
            });
          },
          fail: () => resolve(false),
        });
      });
    },

    // 保存图片到相册
    saveToAlbum(filePath) {
      return new Promise((resolve, reject) => {
        wx.saveImageToPhotosAlbum({
          filePath,
          success: resolve,
          fail: reject,
        });
      });
    },
});
