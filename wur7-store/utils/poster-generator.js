/**
 * 微信小程序海报生成通用工具
 * 基于Canvas 2D实现，兼容TDesign零售项目基础库2.6.5+要求
 */
export default class PosterGenerator {
  constructor(canvasId, ctx) {
    this.canvasId = canvasId
    this.ctx = ctx
    this.dpr = wx.getWindowInfo().pixelRatio
  }

  // 高清画布初始化
  async init(canvasWidth, canvasHeight) {
    const query = wx.createSelectorQuery()
    const res = await new Promise(resolve => {
      query.select(`#${this.canvasId}`)
        .fields({ node: true, size: true })
        .exec(resolve)
    })
    this.canvas = res[0].node
    this.ctx = this.canvas.getContext('2d')
    
    // 按设备像素比放大画布，解决生成图片模糊问题
    this.canvas.width = canvasWidth * this.dpr
    this.canvas.height = canvasHeight * this.dpr
    this.ctx.scale(this.dpr, this.dpr)
    return this
  }

  // 预加载图片，自动转换为小程序本地Image对象
  async preloadImage(imgUrl) {
    // 如果是网络图片，先调用wx.getImageInfo转成本地临时路径
    if (/^https?:\/\//.test(imgUrl)) {
      const imgInfo = await wx.getImageInfo({ src: imgUrl })
      const img = this.canvas.createImage()
      img.src = imgInfo.path
      await new Promise((resolve, reject) => {
        img.onload = resolve
        img.onerror = reject
      })
      return img
    }
    // 本地静态图片直接载入
    const img = this.canvas.createImage()
    img.src = imgUrl
    await new Promise(resolve => img.onload = resolve)
    return img
  }

  // 自动换行绘制长文本
  drawWrapText(text, x, y, maxWidth, lineHeight) {
    const ctx = this.ctx
    let curY = y
    let curLineText = ''
    for(let i=0; i<text.length; i++) {
      const testText = curLineText + text[i]
      const metrics = ctx.measureText(testText)
      if(metrics.width > maxWidth && i>0) {
        ctx.fillText(curLineText, x, curY)
        curLineText = text[i]
        curY += lineHeight
      } else {
        curLineText = testText
      }
    }
    ctx.fillText(curLineText, x, curY)
    return curY + lineHeight
  }

  // 导出海报临时图片路径
  async exportTempFilePath() {
    return await wx.canvasToTempFilePath({
      canvas: this.canvas,
      quality: 1,
    })
  }
}

// 海报固定尺寸：宽度750px，高度1100px
const POSTER_SIZE = { width: 750, height: 1100 }

export async function generatePoster(canvasId, goodsInfo, qrCode) {
  const generator = new PosterGenerator(canvasId)
  await generator.init(POSTER_SIZE.width, POSTER_SIZE.height)
  const ctx = generator.ctx

  // 1. 绘制背景
  const gradient = ctx.createLinearGradient(0, 0, POSTER_SIZE.width, POSTER_SIZE.height)
  gradient.addColorStop(0, '#4c1d95')
  gradient.addColorStop(0.5, '#7c3aed')
  gradient.addColorStop(1, '#a78bfa')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, POSTER_SIZE.width, POSTER_SIZE.height)

  // 2. 加载并绘制商品主图
  const goodsImage = await generator.preloadImage(goodsInfo.item.itemImage)
  ctx.drawImage(goodsImage, 50, 120, 650, 450)
  // 3. 绘制商品标题
  ctx.font = 'bold 40px Noto Sans SC'
  ctx.fillStyle = '#ffffff'
  const y = generator.drawWrapText(goodsInfo.item.itemName, 50, 620, 650, 60)

  // 4. 绘制商品价格
  ctx.font = 'bold 56px Noto Sans SC'
  ctx.fillStyle = '#fef08a'
  const pArr = priceFmt(goodsInfo.item.salePrice);
  ctx.fillText(`¥${pArr[0]}.${pArr[1]}`, 50, y+20)

  // 5. 绘制底部区域：白色卡片 + 二维码
  ctx.fillStyle = '#ffffff'
  ctx.roundRect(0, 820, POSTER_SIZE.width, 280, [24,24,0,0])
  ctx.fill()

  // 绘制引导文案
  ctx.font = 'bold 36px Noto Sans SC'
  ctx.fillStyle = '#1f2937'
  ctx.fillText('长按扫码立即购买', 60, 890)
  ctx.font = '24px Noto Sans SC'
  ctx.fillStyle = '#6b7280'
  ctx.fillText('来自乐活益家专属分享', 60, 940)

  // 绘制小程序二维码
  const qrImg = await generator.preloadImage(qrCode)
  ctx.drawImage(qrImg, 550, 850, 140, 140)
  // 导出生成完成的海报
  return await generator.exportTempFilePath()
}

function priceFmt(priceInt) {
  let price = parseFloat(priceInt);
  const pArr = ['0', '00'];
  if (isNaN(price)) {
    return pArr
  }
  const isMinus = price < 0;
  if (isMinus) {
    price = -price;
  }
  price = Math.round(price * 10 ** 8) / 10 ** 8; // 恢复精度丢失
  price = Math.ceil(price); // 向上取整
  pArr[0] = price >= 100 ? `${price}`.slice(0, -2) : '0';
  pArr[1] = `${price + 100}`.slice(-2);
  return pArr;
}
