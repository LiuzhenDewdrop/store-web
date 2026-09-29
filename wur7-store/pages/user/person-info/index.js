import { fetchPerson } from '../../../services/usercenter/fetchPerson';
import { phoneEncryption } from '../../../utils/util';
import { getLocalUserInfo, clearLoginInfo } from '../../../utils/auth';
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
        name: '男',
        code: '1',
      },
      {
        name: '女',
        code: '2',
      },
    ],
    typeVisible: false,
    genderMap: ['', '男', '女'],
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
      this.setData({
        personInfo: {
          avatarUrl: localUser.avatarUrl || '',
          nickName: localUser.userName || '微信用户',
          gender: localUser.gender || 0,
          phoneNumber: phoneEncryption(localUser.phoneNumber || ''),
        }
      });
    }
    this.fetchData();
  },
  fetchData() {
    fetchPerson().then((personInfo) => {
      // 合并本地登录信息和mock数据
      const localUser = getLocalUserInfo();
      const finalInfo = {
        ...personInfo,
        avatarUrl: localUser?.avatarUrl || personInfo.avatarUrl,
        nickName: localUser?.userName || personInfo.nickName,
        gender: localUser?.gender || personInfo.gender,
        phoneNumber: phoneEncryption(localUser?.phoneNumber || personInfo.phoneNumber || ''),
      };
      this.setData({
        personInfo: finalInfo,
      });
    });
  },
  onClickCell({ currentTarget }) {
    const { dataset } = currentTarget;
    const { nickName } = this.data.personInfo;

    switch (dataset.type) {
      case 'gender':
        this.setData({
          typeVisible: true,
        });
        break;
      case 'name':
        wx.navigateTo({
          url: `/pages/user/name-edit/index?name=${nickName}`,
        });
        break;
      case 'avatarUrl':
        this.toModifyAvatar();
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
  onConfirm(e) {
    const { value } = e.detail;
    this.setData(
      {
        typeVisible: false,
        'personInfo.gender': value,
      },
      () => {
        Toast({
          context: this,
          selector: '#t-toast',
          message: '设置成功',
          theme: 'success',
        });
      },
    );
  },
  onGetPhoneNumber(e) {
    if (e.detail.errMsg === 'getPhoneNumber:ok') {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '手机号获取成功',
        theme: 'success',
      });
      // 后续对接后端接口更新用户手机号
    }
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
  async toModifyAvatar() {
    try {
      const tempFilePath = await new Promise((resolve, reject) => {
        wx.chooseImage({
          count: 1,
          sizeType: ['compressed'],
          sourceType: ['album', 'camera'],
          success: (res) => {
            const { path, size } = res.tempFiles[0];
            if (size <= 10485760) {
              resolve(path);
            } else {
              reject({ errMsg: '图片大小超出限制，请重新上传' });
            }
          },
          fail: (err) => reject(err),
        });
      });
      const tempUrlArr = tempFilePath.split('/');
      const tempFileName = tempUrlArr[tempUrlArr.length - 1];
      Toast({
        context: this,
        selector: '#t-toast',
        message: `已选择图片-${tempFileName}`,
        theme: 'success',
      });
    } catch (error) {
      if (error.errMsg === 'chooseImage:fail cancel') return;
      Toast({
        context: this,
        selector: '#t-toast',
        message: error.errMsg || error.msg || '修改头像出错了',
        theme: 'error',
      });
    }
  },
});
