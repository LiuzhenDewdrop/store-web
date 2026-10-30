/**
 * 兼容补丁：给不支持 roundRect 的 canvas 上下文补上该方法
 * 支持环境直接用原生，不支持则用 arcTo 手写圆角路径
 * @param {CanvasRenderingContext2D} ctx
 */
export function patchRoundRect(ctx) {
  if (typeof ctx.roundRect === 'function') return ctx; // 支持就什么都不做

  ctx.roundRect = function (x, y, w, h, radii) {
    // 按 CSS border-radius 规则，把 radii 统一成 [左上, 右上, 右下, 左下]
    let r;
    if (Array.isArray(radii)) {
      switch (radii.length) {
        case 1: r = [radii[0], radii[0], radii[0], radii[0]]; break;
        case 2: r = [radii[0], radii[1], radii[0], radii[1]]; break;
        case 3: r = [radii[0], radii[1], radii[2], radii[1]]; break;
        default: r = [radii[0], radii[1], radii[2], radii[3] === undefined ? radii[1] : radii[3]];
      }
    } else if (typeof radii === 'number') {
      r = [radii, radii, radii, radii];
    } else {
      r = [0, 0, 0, 0];
    }

    // 半径容错：不能为负，且不能超过短边一半，否则图形会溢出
    const max = Math.min(w, h) / 2;
    const [tl, tr, br, bl] = r.map(v => Math.min(Math.max(Number(v) || 0, 0), max));

    this.moveTo(x + tl, y);
    this.lineTo(x + w - tr, y);
    this.arcTo(x + w, y, x + w, y + tr, tr);
    this.lineTo(x + w, y + h - br);
    this.arcTo(x + w, y + h, x + w - br, y + h, br);
    this.lineTo(x + bl, y + h);
    this.arcTo(x, y + h, x, y + h - bl, bl);
    this.lineTo(x, y + tl);
    this.arcTo(x, y, x + tl, y, tl);
    this.closePath();
    return this;
  };
  return ctx;
}
