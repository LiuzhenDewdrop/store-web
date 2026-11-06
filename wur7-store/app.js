import updateManager from './common/updateManager';
import { setInviter, getInviter } from './utils/auth';

App({
  onLaunch: function () {},
  onShow: function () {
    updateManager();
    
    // 存储邀请信息
    if (!getInviter()) {
      const options = wx.getEnterOptionsSync();
      const query = (options && options.query) || {};
      let inviterId = 0;
      // 1) 转发卡片 / 带参路径：?inviterId=123
      if (query.inviterId) {
        inviterId = query.inviterId;
      }
      // 2) 小程序码：参数在 scene 里（URL 编码），形如 inviterId=123&spuId=null
      if (!inviterId && query.scene) {
        const scene = decodeURIComponent(query.scene);
        scene.split('&').forEach((pair) => {
          const [key, value] = pair.split('=');
          if (key === 'inviterId') inviterId = value;
        });
      }
      if (inviterId) {
        setInviter(inviterId);
      }
    }
  },
});