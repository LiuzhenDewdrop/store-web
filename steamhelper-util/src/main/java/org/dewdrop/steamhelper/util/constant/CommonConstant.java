package org.dewdrop.steamhelper.util.constant;

public class CommonConstant {
	
	public static final String URL_STEAM_API = "https://api.steampowered.com/";
	public static final String URL_STEAM_IMAGE = "https://shared.st.dl.eccdnx.com/store_item_assets/steam/apps/";
	public static String getUrlSteamImage(Long appid, String suffix) {
		return URL_STEAM_IMAGE + appid + "/" + suffix;
	}
	
	
	public static final String DICT_GROUP_ROLE = "ROLE";
	public static final String DICT_GROUP_PLATFORM = "PLATFORM";

	public static final String INIT = "INIT";
	public static final String COMPLETE = "COMPLETE";

	public static final String ENABLE = "ENABLE";
	public static final String DISABLE = "DISABLE";

	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";

	public static final String Y = "Y";
	public static final String N = "N";

	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String FAIL = "FAIL";


    public static final int ROLE_ID_SUPER_ADMIN = 1;
    public static final int ROLE_ID_GUEST = 2;
	public static final int ROLE_NORMAL_LINE = 500;
}
