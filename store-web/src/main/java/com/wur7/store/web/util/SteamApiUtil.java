package com.wur7.store.web.util;

import java.net.HttpURLConnection;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.util.bean.CodeMsgBean;
import com.wur7.store.util.util.HttpUtil;
import com.wur7.store.web.emuns.SteamApi;
import com.wur7.store.web.emuns.SteamUrl;

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
	
	private static Long getAppid(String url) {
		int start = url.indexOf("/apps/") + 6;
		String s = url.substring(start);
		int end = s.indexOf("/");
		return Long.parseLong(s.substring(0, end));
	}
}
