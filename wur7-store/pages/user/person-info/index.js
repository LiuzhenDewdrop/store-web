import { fetchPerson, updateGender, uploadAvatar, bindPhone } from '../../../services/usercenter/fetchPerson';
import { phoneEncryption } from '../../../utils/util';
import { getLocalUserInfo, clearLoginInfo, setLoginInfo, getToken } from '../../../utils/auth';
import { wxLogout } from '../../../services/login/index';
import Toast from 'tdesign-miniprogram/toast/index';

Page({
  data: {
    personInfo: {
      avatarUrl: '',
      nickName: '',
      gender: 0,
      phoneNumber: '',
    },
    showUnbindConfirm: false,
    pickerOptions: [
      {
        name: '保密',
        code: '0',
      },
      {
        name: '男',
        code: '1',
      },
      {
        name: '女',
        code: '2',
      },
    ],
    typeVisible: false,
    genderMap: ['保密', '男', '女'],
    // 原始未脱敏手机号
    rawPhone: '',
  },
  onLoad() {
    this.init();
  },
  onShow() {
    this.init();
  },
  init() {
    // 优先从本地存储读取登录用户信息
    const localUser = getLocalUserInfo();
    if (localUser) {
      const phone = localUser.phoneNumber || localUser.phone || '';
      this.setData({
        rawPhone: phone,
        personInfo: {
          avatarUrl: localUser.avatarUrl || localUser.avatar || '',
          nickName: localUser.nickName || localUser.userName || localUser.nickname || '微信用户',
          gender: localUser.gender || 0,
          phoneNumber: phoneEncryption(phone),
        }
      });
    }
    // 调用真实接口刷新用户信息
    this.fetchData();
  },
  fetchData() {
    fetchPerson().then((personInfo) => {
      // 合并本地登录信息和后端返回
      const localUser = getLocalUserInfo();
      const finalInfo = {
        ...personInfo,
        avatarUrl: personInfo.avatarUrl || localUser?.avatarUrl || localUser?.avatar || '',
        nickName: personInfo.nickName || localUser?.userName || localUser?.nickname || '微信用户',
        gender: personInfo.gender !== undefined ? personInfo.gender : (localUser?.gender || 0),
      };
      const rawPhone = personInfo.phoneNumber || localUser?.phoneNumber || localUser?.phone || '';
      finalInfo.phoneNumber = phoneEncryption(rawPhone);
      this.setData({
        personInfo: finalInfo,
        rawPhone: rawPhone,
      });
      // 更新本地缓存
      setLoginInfo(getToken(), {
        ...localUser,
        avatarUrl: finalInfo.avatarUrl,
        avatar: finalInfo.avatarUrl,
        userName: finalInfo.nickName,
        nickname: finalInfo.nickName,
        gender: finalInfo.gender,
        phoneNumber: rawPhone,
        phone: rawPhone,
      });
    }).catch((err) => {
      console.log('获取用户信息失败', err);
    });
  },
  onClickCell({ currentTarget }) {
    const { dataset } = currentTarget;
    switch (dataset.type) {
      case 'gender':
        this.setData({
          typeVisible: true,
        });
        break;
      case 'name':
        // 跳转到独立的昵称编辑页面，把当前昵称传过去
        wx.navigateTo({
          url: `/pages/user/name-edit/index?name=${encodeURIComponent(this.data.personInfo.nickName)}`,
        });
        break;
      default: {
        break;
      }
    }
  },
  onClose() {
    this.setData({
      typeVisible: false,
    });
  },
  // 性别选择确认
  onConfirm(e) {
    const { value } = e.detail;
    const gender = parseInt(value);
    Toast({
      context: this,
      selector: '#t-toast',
      message: '保存中...',
      theme: 'loading',
      duration: 500,
    });
    updateGender(gender).then(() => {
      this.setData(
        {
          typeVisible: false,
          'personInfo.gender': gender,
        },
        () => {
          // 更新本地缓存
          const localUser = getLocalUserInfo();
          setLoginInfo(getToken(), { ...localUser, gender });
          Toast({
            context: this,
            selector: '#t-toast',
            message: '设置成功',
            theme: 'success',
          });
        },
      );
    }).catch(() => {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '设置失败，请重试',
        theme: 'error',
      });
    });
  },
  // 最新头像选择回调：微信基础库2.21.2+支持chooseAvatar
  onChooseAvatar(e) {
    const { avatarUrl } = e.detail;
    if (!avatarUrl) {
      return;
    }
    Toast({
      context: this,
      selector: '#t-toast',
      message: '上传中...',
      theme: 'loading',
      duration: 10000,
    });
    // 上传头像到服务器
    uploadAvatar(avatarUrl).then((serverUrl) => {
      this.setData({
        'personInfo.avatarUrl': serverUrl,
      });
      // 更新本地缓存
      const localUser = getLocalUserInfo();
      setLoginInfo(getToken(), { ...localUser, avatarUrl: serverUrl, avatar: serverUrl });
      Toast({
        context: this,
        selector: '#t-toast',
        message: '头像修改成功',
        theme: 'success',
      });
    }).catch((err) => {
      console.error('头像上传失败', err);
      Toast({
        context: this,
        selector: '#t-toast',
        message: err.message || '头像上传失败',
        theme: 'error',
      });
    });
  },
  // 最新手机号获取回调：e.detail.code，后端调用微信接口换取手机号
  onGetPhoneNumber(e) {
    if (e.detail.errMsg !== 'getPhoneNumber:ok') {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '您取消了授权',
        theme: 'warning',
      });
      return;
    }
    const { code } = e.detail;
    if (!code) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '获取手机号失败，请重试',
        theme: 'error',
      });
      return;
    }
    Toast({
      context: this,
      selector: '#t-toast',
      message: '绑定中...',
      theme: 'loading',
      duration: 10000,
    });
    bindPhone(code).then((phone) => {
      this.setData({
        rawPhone: phone,
        'personInfo.phoneNumber': phoneEncryption(phone),
      });
      // 更新本地缓存
      const localUser = getLocalUserInfo();
      setLoginInfo(getToken(), { ...localUser, phoneNumber: phone, phone });
      Toast({
        context: this,
        selector: '#t-toast',
        message: '手机号绑定成功',
        theme: 'success',
      });
    }).catch((err) => {
      console.error('绑定手机号失败', err);
      Toast({
        context: this,
        selector: '#t-toast',
        message: err.message || '绑定失败，请重试',
        theme: 'error',
      });
    });
  },
  openUnbindConfirm() {
    this.setData({
      showUnbindConfirm: true,
    });
  },
  onCloseUnbind() {
    this.setData({
      showUnbindConfirm: false,
    });
  },
  onConfirmUnbind() {
    // 退出登录
    wxLogout().then(() => {
      clearLoginInfo();
      this.setData({
        showUnbindConfirm: false,
      });
      Toast({
        context: this,
        selector: '#t-toast',
        message: '已退出登录',
        theme: 'success',
        duration: 1000
      });
      setTimeout(() => {
        wx.reLaunch({ url: '/pages/home/home' });
      }, 1000);
    }).catch(() => {
      clearLoginInfo();
      this.setData({
        showUnbindConfirm: false,
      });
      wx.reLaunch({ url: '/pages/home/home' });
    });
  },
});
