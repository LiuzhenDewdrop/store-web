package org.steamhelper.web;

import org.dewdrop.steamhelper.entity.ResGame;
import org.dewdrop.steamhelper.util.util.JsonUtil;
import org.dewdrop.steamhelper.web.util.SteamApiUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class JsoupTest {
	
	
	@Test
	public void test() throws Exception {
		// 测试执行
		String url = "https://store.steampowered.com/app/500";
		Connection conn = Jsoup.connect(url);
		Document doc = conn.get();
		System.out.println(doc);
	}
	
	@Test
	public void test1() throws Exception {
		ResGame game = new ResGame();
		game.setAppId(306130L);
		SteamApiUtil.getGemeInfoByPage(game);
		System.out.println(JsonUtil.toJson(game));
	}
}
