package org.dewdrop.steamhelper.web.util;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.constant.CommonConstant;
import org.dewdrop.steamhelper.entity.ResAccount;
import org.dewdrop.steamhelper.entity.ResGame;
import org.dewdrop.steamhelper.entity.ResGameAchievement;
import org.dewdrop.steamhelper.entity.ResGameDlc;
import org.dewdrop.steamhelper.enums.DLC_TYPE;
import org.dewdrop.steamhelper.util.bean.CodeMsgBean;
import org.dewdrop.steamhelper.util.util.HttpUtil;
import org.dewdrop.steamhelper.web.emuns.SteamApi;
import org.dewdrop.steamhelper.web.emuns.SteamUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SteamApiUtil {
	
	/**
	 * 名称	类型	是否必需	描述
	 * key	string	✔	Steamworks Web API 用户验证密钥。
	 * steamid	uint64	✔	要查询的玩家。
	 * include_appinfo	bool	✔	如果我们需要各游戏的更多信息（如名称、图标等），为 true。						经测试，默认false
	 * include_played_free_games	bool	✔	默认不包含免费游戏。 若设置，将返回用户玩过的免费游戏。
	 * appids_filter	uint32	✔	若设置，将结果集限制在传入的应用。												经测试，非必填
	 *
	 * 如果您能看到玩家拥有游戏的详情，则返回该玩家所拥有游戏的列表。
	 */
	public static IReturnBean<JSONArray> player_getOwnedGames(Map<String, Object> param) throws Exception {
		CodeMsgBean resp = HttpUtil.get(SteamApi.PLAYER__GET_OWNED_GAMES.api(), param);
		if (resp.getCode() != HttpURLConnection.HTTP_OK) {
			return SteamApi.PLAYER__GET_OWNED_GAMES.apiFail("code=" +  resp.getCode());
		}
		JSONObject response = JSONObject.parseObject(resp.getMsg()).getJSONObject("response");
		if (response == null) {
			return SteamApi.PLAYER__GET_OWNED_GAMES.apiFail("response");
		}
		JSONArray arr = response.getJSONArray("games");
		if (arr == null || arr.isEmpty()) {
			return SteamApi.PLAYER__GET_OWNED_GAMES.apiFail("games");
		}
		return IReturnBean.success(arr);
	}
	
	
	/**
	 * 获取商城的页面信息
	 * 依靠值传递
	 * @param game
	 * @return
	 * @throws Exception
	 */
	public static IReturnBean<?> getGemeInfoByPage(ResGame game) throws Exception {
		String url = SteamUrl.STORE.url(game.getAppId(), "?l=schinese");
		log.info("url:{}", url);
		if (!HttpUtil.check(url, null)) {
			return SteamApi.PLAYER__GET_OWNED_GAMES.apiFail("check");
		}
		Document doc = Jsoup.connect(url).timeout(5000).get();
		if (doc == null) {
			return SteamApi.PLAYER__GET_OWNED_GAMES.apiFail("Jsoup");
		}
		// chn name
		Element appHubAppName = doc.getElementById("appHubAppName");
		if (appHubAppName == null) {
			game.setGameStatus(CommonConstant.GAME_STATUS_OFFLINE);
			return SteamApi.PLAYER__GET_OWNED_GAMES.apiFail("该游戏已下架");
		}
		game.setGameStatus(CommonConstant.GAME_STATUS_NORMAL);
		String chnName = appHubAppName.text();
		game.setChnName(chnName);
		// dlc num
		// 这里判没有是准的，判有很麻烦，直接后边用接口吧
		Element dlcElm = doc.getElementById("gameAreaDLCSection");
		if (dlcElm == null) {
			game.setDlcNum(0);
		} else {
			game.setDlcNum(-1);
		}
		// achv num
		// 这里判没有是准的，判有很麻烦，直接后边用接口吧
		Element achvBlock = doc.getElementById("achievement_block");
		if (achvBlock == null) {
			game.setAchieveNum(0);
//		} else {
//			String blockTitle = achvBlock.getElementsByClass("block_title").text().trim();;
//			if (blockTitle.equals("可用点数商店物品") || blockTitle.equals("Points Shop Items Available")) {
//				game.setAchieveNum(0);
//			} else {
//				String achvNumStr = blockTitle.replace("Includes", "").replace("Steam Achievements", "")
//						.replace("包括", "").replace("项 Steam 成就", "").trim();
//				game.setAchieveNum(Integer.parseInt(achvNumStr));
//			}
			game.setAchieveStatus(CommonConstant.ACHV_STATUS_NONE);
		} else {
			game.setAchieveNum(-1);
			// achv status
			Elements learningAbout = doc.getElementById("category_block").getElementsByClass("game_area_details_specs_ctn learning_about");
			if (learningAbout.isEmpty()) {
				game.setAchieveStatus( CommonConstant.ACHV_STATUS_NORMAL);
			} else {
				String learningAboutText = learningAbout.get(0).getElementsByClass("label").text();
				switch (learningAboutText) {
					case "Profile Features Limited":
					case "个人资料功能受限":
						game.setAchieveStatus(CommonConstant.ACHV_STATUS_LIMITED);
						break;
					case "Steam is learning about this game":
					case "Steam 正在了解该游戏":
						game.setAchieveStatus(CommonConstant.ACHV_STATUS_LEARNING);
						break;
					default:
						game.setAchieveStatus(CommonConstant.ACHV_STATUS_NONE);
						break;
				}
			}
		}
		// releaseDate
		String releaseDate = doc.getElementById("glanceMidCtn").getElementsByClass("date").text();
		game.setReleaseDate(releaseDate);
		// headerSuffix
		String headerImgUrl = doc.getElementById("gameHeaderImageCtn").getElementsByTag("img").attr("src");
		game.setHeaderSuffix(getSuffix(headerImgUrl, game.getAppId()));
		return IReturnBean.success();
	}
	
	public static void getCapsuleSuffix(ResGame game) throws Exception {
		String url = SteamUrl.ACHV.url(game.getAppId(), "?l=schinese");
		if (!HttpUtil.check(url, null)) {
			log.info("没有找到合适的图片,appid={},imageType={}", game.getAppId(), "capsule");
			return;
		}
		Document doc = Jsoup.connect(url).get();
		if (doc == null) {
			log.info("没有找到合适的图片,appid={},imageType={}", game.getAppId(), "capsule");
			return;
		}
		String imgUrl = doc.getElementsByClass("gameLogo").get(0).getElementsByTag("img").attr("src");
		game.setCapsuleSuffix(getSuffix(imgUrl, game.getAppId()));
	}
	
	public static void checkGameSuffix(ResGame game) throws Exception {
		// libSuffix
		IReturnBean<String> checkResult = checkSuffix(game.getAppId(), "library_600x900");
		if (checkResult.isSuccess()) {
			game.setLibSuffix(checkResult.getData());
		}
		// heroSuffix
		checkResult = checkSuffix(game.getAppId(), "library_hero");
		if (checkResult.isSuccess()) {
			game.setHeroSuffix(checkResult.getData());
		}
	}
	
	private static IReturnBean<String> checkSuffix(Long appId, String suffixType) throws Exception {
		String suffix1 = "/" + suffixType + "_schinese.jpg";
		String suffix2 = "/" + suffixType + ".jpg";
		boolean checkResult = HttpUtil.check(SteamUrl.IMAGE.url(appId, suffix1), null);
		if (checkResult) {
			return IReturnBean.success(suffix1);
		}
		checkResult = HttpUtil.check(SteamUrl.IMAGE.url(appId, suffix2), null);
		if (checkResult) {
			return IReturnBean.success(suffix2);
		}
		log.info("没有找到合适的图片,appid={},imageType={}", appId, suffix2);
		return IReturnBean.fail();
	}
	
	private static String getSuffix(String url, Long appId) {
		String appid = "/" + appId;
		int index = url.indexOf("?");
		int start = url.indexOf(appid) + appid.length();
		return index > -1 ? url.substring(start, index) : url.substring(start);
	}
	
	public static IReturnBean<List<ResGameDlc>> getDlcInfoByPage(ResGame game) throws Exception {
		if (game.getGameStatus() != CommonConstant.GAME_STATUS_NORMAL) {
			return IReturnBean.fail("失败原因：game状态");
		}
		if (game.getDlcNum() == 0) {
			// -1代表有值需要查但是还没查，0代表确认没有dlc，正数代表之前有多少
			return IReturnBean.fail("失败原因：dlc数量");
		}
		String url = SteamUrl.DLC.url(game.getAppId(), game.getEngName(), "?count=100000&l=schinese");
		CodeMsgBean resp = HttpUtil.get(url, null);
		if (resp.getCode() != HttpURLConnection.HTTP_OK) {
			return IReturnBean.fail("查询DLC失败");
		}
		JSONObject jObj = JSONObject.parseObject(resp.getMsg());
		if (jObj.getIntValue("success") != 1) {
			return IReturnBean.fail("查询DLC失败");
		}
		int totalCount = jObj.getIntValue("total_count");
		// 商城里游戏页面可能有N/A的dlc，这个准确
		game.setDlcNum(totalCount);
		if (totalCount == 0) {
			return IReturnBean.fail("查询steam显示无dlc");
		}
		Document doc = Jsoup.parse(jObj.getString("results_html"));
		if (doc == null) {
			return IReturnBean.fail("查无dlc");
		}
		// dlc
		List<ResGameDlc> dlcList = new ArrayList<>(totalCount);
		List<Element> elemList = doc.getElementsByClass("recommendation").asList();
		for (Element elem : elemList) {
			ResGameDlc dlc = new ResGameDlc();
//			dlc.setId();
			dlc.setGameId(game.getId());
			String imgUrl = elem.getElementsByTag("img").attr("src");
			dlc.setDlcId(getAppid(imgUrl));
			dlc.setHeaderSuffix(getSuffix(imgUrl, dlc.getDlcId()));
			dlc.setDlcName(elem.getElementsByClass("color_created").text());
			dlc.setDlcDesc(elem.getElementsByClass("recommendation_desc").text());
			dlc.setReleaseDate(elem.getElementsByClass("curator_review_date").text());
			DLC_TYPE d = DLC_TYPE.parseName(dlc.getDlcName());
			dlc.setDlcType(d.type);
			dlc.setAchievement(d.achv);
			dlcList.add(dlc);
		}
		return IReturnBean.success(dlcList);
	}
	
	public static IReturnBean<List<ResGameAchievement>> getAchvInfo(ResGame game, ResAccount acc) throws Exception {
		if (game.getAchieveStatus() == null || game.getAchieveStatus() == CommonConstant.ACHV_STATUS_NONE) {
			return IReturnBean.fail("game的成就状态错误");
		}
		Map<String, Object> param = new HashMap<>(5);
		param.put("key", acc.getAccountToken());
		param.put("steamid", acc.getPlatformUserId());
		param.put("appid", game.getAppId());
		param.put("l", "english");
		IReturnBean<JSONArray> engResult = userStats_getPlayerAchievements(param);
		if (engResult.isNotSuccess()) {
			return IReturnBean.fail(engResult.getMsg());
		}
		JSONArray arrEng = engResult.getData();
		int total = arrEng.size();
		game.setAchieveNum(total);
		List<ResGameAchievement> list = new ArrayList<>(total);
		Map<String, Integer> apinameIndexMap = new HashMap<>(total);
		Map<String, Integer> achvNameIndexMap = new HashMap<>(total);
		for (int i = 0; i < total; i++) {
			JSONObject obj = arrEng.getJSONObject(i);
			ResGameAchievement achv = new ResGameAchievement();
//			achv.setId();
			achv.setGameId(game.getId());
			achv.setApiName(obj.getString("apiname"));
//			achv.setChnName();
//			achv.setChnDesc();
			achv.setEngName(obj.getString("name"));
			achv.setEngDesc(obj.getString("description"));
//			achv.setLockImgSuffix();
//			achv.setUnlockImgSuffix();
//			achv.setFinishPercent();
//			achv.setPercentRank();
			list.add(achv);
			apinameIndexMap.put(achv.getApiName(), i);
			achvNameIndexMap.put(achv.getEngName(), i);
		}
		param.put("l", "schinese");
		IReturnBean<JSONArray> chnResult = userStats_getPlayerAchievements(param);
		if (chnResult.isSuccess()) {
			JSONArray arrChn = chnResult.getData();
			int totalChn = arrChn.size();
			for (int i = 0; i < totalChn; i++) {
				JSONObject obj = arrChn.getJSONObject(i);
				String apiname = obj.getString("apiname");
				Integer index = apinameIndexMap.get(apiname);
				if (index != null) {
					ResGameAchievement achv = list.get(index);
					achv.setChnName(obj.getString("name"));
					achv.setChnDesc(obj.getString("description"));
				} else {
					ResGameAchievement achv = new ResGameAchievement();
					achv.setGameId(game.getId());
					achv.setApiName(obj.getString("apiname"));
					achv.setChnName(obj.getString("name"));
					achv.setChnDesc(obj.getString("description"));
					apinameIndexMap.put(achv.getApiName(), list.size());
					list.add(achv);
				}
			}
		}
		Map<String, Object> gameidMap = new HashMap<>(1);
		gameidMap.put("gameid", game.getAppId());
		IReturnBean<JSONArray> achvPercentResult = userStats_getGlobalAchievementPercentagesForApp(gameidMap);
		if (achvPercentResult.isSuccess()) {
			JSONArray percentArr = achvPercentResult.getData();
			int length = percentArr.size();
			for (int i = 0; i < length; i++) {
				JSONObject obj = percentArr.getJSONObject(i);
				String name = obj.getString("name");
				Integer index = apinameIndexMap.get(name);
				if (index != null) {
					ResGameAchievement achv = list.get(index);
					achv.setFinishPercent(obj.getString("percent"));
					achv.setPercentRank(i+1);
				}
			}
		}
		getAchvInfoByPage(game, achvNameIndexMap, list);
		return IReturnBean.success(list);
	}
	
	/**
	 * 名称	类型	是否必需	描述
	 * key	string	✔	Steamworks Web API 发行商验证密钥。
	 * appid	uint32	✔	要获取全局统计的 AppID。
	 * count	uint32	✔	要获取数据的统计的数量。																经测试，非必填
	 * name[0]	string	✔	要获取数据的统计的名称。																经测试，非必填
	 * startdate	uint32		每日合计的开始日期（unix 时间戳）
	 * enddate	uint32		每日合计的结束日期（unix 时间戳）
	 *
	 * 为指定应用获取全局统计数据百分比。
	 * {@code 经测试，steamid为必填项}
	 * {@code 经测试，l为可选项schinese/english，且仅当有该参数时结果才返回名称和描述}
	 */
	private static IReturnBean<JSONArray> userStats_getPlayerAchievements(Map<String, Object> param) throws Exception {
		SteamApi current = SteamApi.USER_STATS__GET_PLAYER_ACHIEVEMENTS;
		CodeMsgBean resp = HttpUtil.get(current.api(), param);
		if (resp.getCode() != HttpURLConnection.HTTP_OK) {
			return current.apiFail("code=" +  resp.getCode());
		}
		JSONObject response = JSONObject.parseObject(resp.getMsg()).getJSONObject("playerstats");
		if (response == null) {
			return current.apiFail("playerstats");
		}
		JSONArray arr = response.getJSONArray("achievements");
		if (arr == null || arr.isEmpty()) {
			return current.apiFail("achievements");
		}
		return IReturnBean.success(arr);
	}
	
	/**
	 * 名称	类型	是否必需	描述
	 * gameid	uint64	✔	要获取成就百分比的 GameID。
	 *
	 * 为指定应用获取全局成就百分比。
	 */
	private static IReturnBean<JSONArray> userStats_getGlobalAchievementPercentagesForApp(Map<String, Object> param) throws Exception {
		SteamApi current = SteamApi.USER_STATS_GET_GLOBAL_ACHIEVEMENT_PERCENTAGES_FOR_APP;
		CodeMsgBean resp = HttpUtil.get(current.api(), param);
		if (resp.getCode() != HttpURLConnection.HTTP_OK) {
			return current.apiFail("code=" +  resp.getCode());
		}
		JSONObject response = JSONObject.parseObject(resp.getMsg()).getJSONObject("achievementpercentages");
		if (response == null) {
			return current.apiFail("achievementpercentages");
		}
		JSONArray arr = response.getJSONArray("achievements");
		if (arr == null || arr.isEmpty()) {
			return current.apiFail("achievements");
		}
		return IReturnBean.success(arr);
	}
	
	private static IReturnBean<?> getAchvInfoByPage(ResGame game, Map<String, Integer> achvNameIndexMap, List<ResGameAchievement> list) throws Exception {
		String url = SteamUrl.ACHV.url(game.getAppId(), "?l=english");
		if (!HttpUtil.check(url, null)) {
			log.info("查询成就页面失败,appid={}, url不通", game.getAppId());
			return IReturnBean.fail("查询成就页面失败");
		}
		Document doc = Jsoup.connect(url).get();
		if (doc == null) {
			log.info("查询成就页面失败,appid={}, doc=null", game.getAppId());
			return IReturnBean.fail("查询成就页面失败");
		}
		Elements rows = doc.getElementsByClass("achieveRow ");
		if (rows.isEmpty()) {
			log.info("页面无成就,appid={}, rows is empty", game.getAppId());
			return IReturnBean.fail("页面无成就");
		}
		for (Element row : rows) {
			String imgUrl = row.getElementsByTag("img").attr("src");
			String name = row.getElementsByTag("h3").text();
			Integer index = achvNameIndexMap.get(name);
			if (index != null) {
				list.get(index).setUnlockImgSuffix(getSuffix(imgUrl, game.getAppId()));
			}
		}
		return IReturnBean.success();
	}
	
	private static Long getAppid(String url) {
		int start = url.indexOf("/apps/") + 6;
		String s = url.substring(start);
		int end = s.indexOf("/");
		return Long.parseLong(s.substring(0, end));
	}
}
