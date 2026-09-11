package com.wur7.store.web.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class LayPage implements Serializable {

	/**当前页**/
	private int page;
	/**每页显示总记录数**/
	private int limit;
	
	/**每页的开始记录数**/
	private int start;
	
	protected long totalCount = -1L;
	
	protected long totalSize = 0;

	public int getStart() {
		this.start = (page - 1) * limit;
		return start;
	}
	
	public long getTotalSize() {
		if (this.totalCount < 0L) {
			return -1L;
		}
		long count = this.totalCount / this.limit;
		if (this.totalCount % this.limit > 0L) {
			count += 1L;
		}
		return count;
	}
}
