// dayjs入口文件，解决小程序路径引用问题
import dayjs from '../miniprogram_npm/dayjs/index';
// 导入中文语言包
import './dayjs/locale/zh-cn';
// 默认使用中文
dayjs.locale('zh-cn');

export default dayjs;