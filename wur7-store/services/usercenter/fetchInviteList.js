import { delay } from '../_utils/delay';

const useMock = true;

function genMockInviteData(totalCount) {
  const nicknames = ['小明', '阿杰', '乐乐', '大壮', '晓雨', '老司机', '菜鸟玩家', '隔壁老王', '张同学', '李阿姨', '王小明', '张三', '李四', '王五', '赵六', '小周', '小陈'];
  const list = [];
  for (let i = 0; i < totalCount; i++) {
    const minutesAgo = i * (60 + Math.random() * 1200);
    const d = new Date(Date.now() - minutesAgo * 60 * 1000);
    const timeStr = `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;
    list.push({
      id: `invite_${i}`,
      nickname: nicknames[Math.floor(Math.random() * nicknames.length)],
      createTime: timeStr
    });
  }
  return list;
}

const inviteTotal = 56;
const inviteListAll = genMockInviteData(inviteTotal);

export function fetchInviteList(params = {}) {
  if (useMock) {
    const { pageNum = 1, pageSize = 20 } = params;
    return delay(300).then(() => {
      const start = (pageNum - 1) * pageSize;
      const end = start + pageSize;
      const slice = inviteListAll.slice(start, end);
      return {
        code: 0,
        data: {
          totalPersons: inviteTotal,
          list: slice,
          pageNum,
          pageSize,
          hasMore: end < inviteTotal
        }
      };
    });
  }
  // TODO: 替换为真实接口
  // return request({ url: '/api/xxx', method: 'get', data: params });
}
