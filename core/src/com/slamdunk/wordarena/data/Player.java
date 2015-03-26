package com.slamdunk.wordarena.data;

import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.enums.PlayerKind;

/**
 * Contient les informations sur le joueur participant à la partie
 */
public class Player {
	public static final Player NEUTRAL;
	static {
		NEUTRAL = new Player(0, Assets.i18nBundle.get("ui.editor.player.0"), Assets.MARKER_PACK_NEUTRAL);
	}
	
	public int uid;
	public String name;
	
	/**
	 * Donne le type de joueur (CPU, humain à distance, humain en local)
	 */
	public PlayerKind kind;
	
	public String markerPack;
	
	public int score;
	
	public int nbRoundsWon;
	public int nbZonesOwned;
	public int nbWordsPlayed;
	
	public Player() {
	}
	
	public Player(int uid, String name, String cellPack) {
		this.uid = uid;
		this.name = name;
		this.markerPack = cellPack;
		this.kind = PlayerKind.HUMAN_LOCAL;
	}
	
	@Override
	public int hashCode() {
		return uid + name.hashCode() * 31;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null
		&& (obj instanceof Player)) {
			return uid == ((Player)obj).uid;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
