function fenToYuan(fen) {
	if (!fen) {
		return 0;
	}
	// 除以100，保留2位小数
	return (fen / 100).toFixed(2);
}

function yuanToFen(yuan) {
	if (!yuan) {
		return 0;
	}
	// 乘以100并四舍五入取整，确保结果是整数
	return Math.round(yuan * 100);
}