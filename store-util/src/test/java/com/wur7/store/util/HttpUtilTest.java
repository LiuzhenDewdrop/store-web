package com.wur7.store.util;

import com.wur7.store.util.bean.CodeMsgBean;
import com.wur7.store.util.util.HttpUtil;
import org.junit.Test;

public class HttpUtilTest {
	
	@Test
	public void test() throws Exception {
		// 测试执行
		CodeMsgBean result = HttpUtil.post("https://steamcommunity.com/stats/TitanQuestAnniversaryEdition/achievements?l=schinese", "");
		System.out.println("result:");
		System.out.println();
		System.out.println(result);
		boolean checkResult = HttpUtil.check("https://shared.st.dl.eccdnx.com/store_item_assets/steam/apps/292030/header_schinese.jpg", null);
		System.out.println("result:");
		System.out.println();
		System.out.println(checkResult);
	}
	
}
