package org.dewdrop.steamhelper.web.bean;

import java.util.List;

import org.dewdrop.steamhelper.entity.ResGame;
import org.dewdrop.steamhelper.entity.ResGameAchievement;
import org.dewdrop.steamhelper.entity.ResGameDlc;

import lombok.Data;

@Data
public class SteamGameBean {

	private ResGame game;
	private List<ResGameDlc> dlc;
	private List<ResGameAchievement> achv;
}
