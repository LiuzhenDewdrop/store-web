package org.dewdrop.steamhelper.web.emuns;

import org.dewdrop.steamhelper.bean.IReturnBean;

public enum SteamApi {
	PLAYER__GET_OWNED_GAMES("IPlayerService/GetOwnedGames/v1/"),
	USER_STATS__GET_PLAYER_ACHIEVEMENTS("ISteamUserStats/GetPlayerAchievements/v1/"),
	USER_STATS_GET_GLOBAL_ACHIEVEMENT_PERCENTAGES_FOR_APP("ISteamUserStats/GetGlobalAchievementPercentagesForApp/v2/"),
	;
	
	private final String path;
	
	SteamApi(String path) {
		this.path = path;
	}
	
	public String api() {
		return SteamUrl.API.url() + path;
	}
	
	public <T> IReturnBean<T> apiFail(String stage) {
		return IReturnBean.fail( "STEAM接口结果解析失败:"+ this.name() + ":" + stage);
	}
}
