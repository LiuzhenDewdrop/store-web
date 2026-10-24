import updateManager from './common/updateManager';
import { setInviter } from './utils/auth';

App({
  onLaunch: function () {},
  onShow: function () {
    updateManager();
    // 存储邀请信息
    const options = wx.getEnterOptionsSync();
    if (options && options.query && options.query.inviterId) {
      const inviterId = options.query.inviter_id;
      setInviter(inviterId);
    }
  },
});
