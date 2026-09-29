import { updateNickname } from '../../../services/usercenter/fetchPerson';
import { getLocalUserInfo, setLoginInfo, getToken } from '../../../utils/auth';
import Toast from 'tdesign-miniprogram/toast/index';

Page({
  data: {
    nameValue: '',
  },
  onLoad(options) {
    const { name } = options;
    this.setData({
      nameValue: decodeURIComponent(name || ''),
    });
  },
  onSubmit() {
    const nickname = this.data.nameValue.trim();
    if (!nickname) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '昵称不能为空',
        theme: 'warning',
      });
      return;
    }
    if (nickname.length > 15) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '昵称最多15个字',
        theme: 'warning',
      });
      return;
    }
    Toast({
      context: this,
      selector: '#t-toast',
      message: '保存中...',
      theme: 'loading',
      duration: 500,
    });
    updateNickname(nickname).then(() => {
      // 更新本地缓存
      const localUser = getLocalUserInfo();
      setLoginInfo(getToken(), { ...localUser, userName: nickname, nickname });
      Toast({
        context: this,
        selector: '#t-toast',
        message: '修改成功',
        theme: 'success',
        duration: 1000,
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1000);
    }).catch(() => {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '修改失败，请重试',
        theme: 'error',
      });
    });
  },
  onNameInput(e) {
    this.setData({
      nameValue: e.detail.value,
    });
  },
  clearContent() {
    this.setData({
      nameValue: '',
    });
  },
});
