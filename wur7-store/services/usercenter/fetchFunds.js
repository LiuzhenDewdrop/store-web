import { config } from '../../config/index';

const { useMock } = config;

// ---- 生成大数量 mock 数据（用于分页测试） ----
const NICK_WORDS = ['阿明', '小', '大', '王', '李', '张', '阿', '可爱的', '萌萌', '吃货', '大佬', '萌新', '佛系', '老', '隔壁', '楼上', '村口'];
const NICK_TAIL = ['哥', '姐', '酱', '君', '仔', '同学', '先生', '酱', '猫', '狗', '鼠', '牛', '虎', '兔', '龙', '蛇', '马', '羊', '猴', '鸡'];

function pick(arr) {
  return arr[Math.floor(Math.random() * arr.length)];
}

function genMockList(type, totalCount) {
  // type: 'unfund' → payTime；'fund' → receiveTime
  const timeKey = type === 'unfund' ? 'payTime' : 'receiveTime';
  const list = [];
  const now = Date.now();
  for (let i = 0; i < totalCount; i++) {
    const offsetMin = Math.floor(Math.random() * 60 * 24 * 60); // 60天内
    const t = new Date(now - offsetMin * 60 * 1000);
    const pad = (n) => (n < 10 ? '0' + n : n);
    const timeStr = `${t.getFullYear()}-${pad(t.getMonth() + 1)}-${pad(t.getDate())} ${pad(t.getHours())}:${pad(t.getMinutes())}`;
    list.push({
      id: `${type}_${i + 1}`,
      profitAmt: Math.floor(Math.random() * 50000) + 100, // 1元 ~ 501元（分）
      nickname: pick(NICK_WORDS) + pick(NICK_TAIL),
      [timeKey]: timeStr,
    });
  }
  // 按时间倒序（最新在前）
  list.sort((a, b) => (a[timeKey] < b[timeKey] ? 1 : -1));
  return list;
}

const MOCK_DATA = {
  unfund: genMockList('unfund', 68),
  fund: genMockList('fund', 102),
};

function sumAmt(list) {
  return list.reduce((acc, item) => acc + (item.profitAmt || 0), 0);
}

/**
 * 分页获取资金明细列表
 * @param {'unfund'|'fund'} type
 * @param {{pageNum:number,pageSize:number}} params  页码从1开始
 */
function fetchFunds(type, { pageNum = 1, pageSize = 20 } = {}) {
  if (useMock) {
    const source = MOCK_DATA[type] || [];
    const start = (pageNum - 1) * pageSize;
    const rows = source.slice(start, start + pageSize);
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          code: 0,
          data: {
            total: source.length,
            totalAmt: sumAmt(source),
            list: rows,
            pageNum,
            pageSize,
            hasMore: start + rows.length < source.length,
          },
        });
      }, 300);
    });
  }

  // 真实接口占位：接后端时把这里换成 request 调用，返回结构保持一致
  return Promise.reject(new Error('funds 接口未接入'));
}

export const fetchUnfundList = (params) => fetchFunds('unfund', params);
export const fetchFundList = (params) => fetchFunds('fund', params);
