package org.dewdrop.steamhelper.util;

import org.dewdrop.steamhelper.util.util.HttpUtil;
import org.junit.Test;

public class HttpUtilTest {
	
	@Test
	public void test() throws Exception {
		// 测试执行
		String result = HttpUtil.post("https://steamcommunity.com/stats/TitanQuestAnniversaryEdition/achievements?l=schinese", "");
		System.out.println("result:");
		System.out.println();
		System.out.println(result);
	}
}
