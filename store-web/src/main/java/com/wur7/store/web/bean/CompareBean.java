package com.wur7.store.web.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CompareBean<T> {
	private List<T> add;
	private List<T> upd;
	private List<T> del;
	
	public CompareBean() {
		this.add = new ArrayList<>();
		this.upd = new ArrayList<>();
		this.del = new ArrayList<>();
	}
	
//	public static CompareBean<ResGameDlc> compare(List<ResGameDlc> old, List<ResGameDlc> in) {
//		CompareBean<ResGameDlc> result = new CompareBean<>();
//		if (CollectionUtil.isEmpty(old)) {
//			result.add = in;
//			return result;
//		}
//		if (CollectionUtil.isEmpty(in)) {
//			result.del = old;
//			return result;
//		}
//		Map<Long, ResGameDlc> inMap = in.stream().collect(Collectors.toMap(ResGameDlc::getDlcId, d -> d));
//		for (ResGameDlc o : old) {
//			ResGameDlc i = inMap.remove(o.getDlcId());
//			if (i == null) {
//				// 以前有，现在没有，就删
//				result.del.add(o);
//			} else {
//				// 一直有，就看要不要更
//				boolean flag = false;
//				ResGameDlc updRecord = new ResGameDlc();
//				if (!StringUtil.equals(o.getDlcName(), i.getDlcName())) {
//					updRecord.setDlcName(i.getDlcName());
//					flag = true;
//				}
//				if (!StringUtil.equals(o.getDlcDesc(), i.getDlcDesc())) {
//					updRecord.setDlcDesc(i.getDlcDesc());
//					flag = true;
//				}
//				if (!StringUtil.equals(o.getHeaderSuffix(), i.getHeaderSuffix())) {
//					updRecord.setHeaderSuffix(i.getHeaderSuffix());
//					flag = true;
//				}
//				if (!StringUtil.equals(o.getReleaseDate(), i.getReleaseDate())) {
//					updRecord.setReleaseDate(i.getReleaseDate());
//					flag = true;
//				}
//				if (flag) {
//					updRecord.setId(o.getId());
//					result.upd.add(updRecord);
//				}
//			}
//		}
//		result.add = new ArrayList<>(inMap.values());
//		return result;
//	}
	
}
