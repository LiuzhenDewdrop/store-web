package com.wur7.store.web.emuns;

public enum SteamUrl {
	STORE("https://store.steampowered.com/app/{appid}"),
	API("https://api.steampowered.com/"),
	IMAGE("https://shared.st.dl.eccdnx.com/store_item_assets/steam/apps/{appid}/"),
	ACHV("https://steamcommunity.com/stats/{appid}/achievements"),
	DLC("https://store.steampowered.com/dlc/{appid}/{name}/ajaxgetfilteredrecommendations/"),
	
	;
	
	SteamUrl(String url) {
		this.url = url;
	}
	
	private final String url;
	
	public String url() {
		return this.url;
	}
	
	public String url(Long appId, String suffix) {
		return this.url.replace("{appid}", appId+"") + suffix;
	}
	
	public String url(Long appId, String name, String suffix) {
		name = name.replaceAll(" ", "_").replaceAll("[^_a-zA-Z0-9]", "");
		if ("".equals(name)) {
			name = "_";
		}
		return this.url.replace("{appid}", appId+"").replace("{name}", name) + suffix;
	}
}
