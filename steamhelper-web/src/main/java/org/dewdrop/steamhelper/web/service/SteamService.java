package org.dewdrop.steamhelper.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.constant.CommonConstant;
import org.dewdrop.steamhelper.entity.ResAccount;
import org.dewdrop.steamhelper.entity.ResGame;
import org.dewdrop.steamhelper.entity.ResGameDlc;
import org.dewdrop.steamhelper.util.util.StringUtil;
import org.dewdrop.steamhelper.web.bean.SteamGameBean;
import org.dewdrop.steamhelper.web.mapper.MapperSupport;
import org.dewdrop.steamhelper.web.util.SteamApiUtil;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SteamService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public IReturnBean<?> auto(ResAccount acc) throws Exception {
		if (StringUtil.isBlank(acc.getAccountToken()) || null == acc.getPlatformUserId()) {
			return IReturnBean.fail("您的主账户尚未设置必要参数信息");
		}
		Map<String, Object> getOwnedGamesParam = new HashMap<>(4);
		getOwnedGamesParam.put("key", acc.getAccountToken());
		getOwnedGamesParam.put("steamid", acc.getPlatformUserId());
		getOwnedGamesParam.put("include_appinfo", true);
		getOwnedGamesParam.put("include_played_free_games", true);
		IReturnBean<JSONArray> getOwnGamesResult = SteamApiUtil.player_getOwnedGames(getOwnedGamesParam);
		if (getOwnGamesResult.isNotSuccess()) {
			return getOwnGamesResult;
		}
		JSONArray arr = getOwnGamesResult.getData();
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			ResGame game = new ResGame();
			game.setPlatformId(CommonConstant.PLATFORM_STEAM);
			game.setAppId(obj.getLong("appid"));
			game.setEngName(obj.getString("name"));
			game.setIconSuffix("/" + obj.getString("img_icon_url") + ".jpg");
			
			IReturnBean<SteamGameBean> gameResult = refresh(game, acc);
			if (gameResult.isSuccess()) {
				// todo
				
			}
		}
		return IReturnBean.success();
	}
	
	public IReturnBean<SteamGameBean> refresh(ResGame game, ResAccount acc) throws Exception {
//		SteamApiUtil.getAchvInfo(game, acc);
		ResGame dbGame = mapperSupport.resGameMapper.queryByAppid(game.getPlatformId(), game.getAppId());
		if (dbGame != null) {
			// 库里有
			if (CommonConstant.GAME_STATUS_OFFLINE == dbGame.getGameStatus()) {
				// 下架游戏不处理（steam接口不能直接根据游戏id获取信息）
				return IReturnBean.success();
			}
			SteamApiUtil.getGemeInfoByPage(game);
			if (!Objects.equals(game.getDlcNum(), dbGame.getDlcNum())) {
				refreshDlc(game, dbGame);
			}
			ResGameDlc queryDlc = new ResGameDlc();
			queryDlc.setGameId(dbGame.getId());
			List<ResGameDlc> dbDlcList = mapperSupport.resGameDlcMapper.findAll(queryDlc);
			
		}
		// 库里没有
		SteamApiUtil.getGemeInfoByPage(game);
//			game.setId();
//			game.setSeries();
//			game.setClfId();
			/**
			 * @see SteamService#auto(ResAccount)
			 */
//			game.setPlatformId(CommonConstant.PLATFORM_STEAM);
//			game.setAppId(obj.getLong("appid"));
//			game.setEngName(obj.getString("name"));
//			game.setIconSuffix("/" + obj.getString("img_icon_url") + ".jpg");

			/**
			 * @see SteamApiUtil#getGemeInfoByPage(ResGame)
			 */
//			game.setChnName();
//			game.setAchieveStatus();
//			game.setAchieveNum();
//			game.setReleaseDate();
//			game.setHeaderSuffix();
			
			/**
			 * @see SteamService#getCapsuleSuffix(ResGame)
			 */
//			game.setCapsuleSuffix()
//		if (game.getAchieveNum() > 0) {
//			getCapsuleSuffix(game);
//		}
			/**
			 * @see SteamService#checkGameSuffix(ResGame)
			 */
//			game.setLibSuffix();
//			game.setHeroSuffix();
//			checkGameSuffix(game);
			// todo insert
//			game.setDlcNum();
		
			return IReturnBean.success();
//		}
//
//			// todo 更新字段
//			return IReturnBean.success();
//		}
//		return IReturnBean.success();
	}
	
	public IReturnBean<?> refreshDlc(ResGame g, ResGame dbGame) throws Exception {
		ResGameDlc queryDlc = new ResGameDlc();
		queryDlc.setGameId(dbGame.getId());
		List<ResGameDlc> dbDlcList = mapperSupport.resGameDlcMapper.findAll(queryDlc);
		switch (g.getDlcNum()) {
			case 0:
				// 最新结果是0个，就删掉现有的
				for (ResGameDlc dbDlc : dbDlcList) {
					mapperSupport.resGameDlcMapper.deleteByPrimaryKey(dbDlc.getId());
				}
				ResGame upd = new ResGame();
				upd.setId(dbGame.getId());
				upd.setDlcNum(0);
				mapperSupport.resGameMapper.updateByPrimaryKeySelective(upd);
				break;
			case -1:
				SteamApiUtil.getDlcInfoByPage(g);
				break;
			default:
				break;
		}
		
		
		
		return IReturnBean.success();
	}
	
	public IReturnBean<?> refreshAchv() {
		
		return IReturnBean.success();
	}
	
}
