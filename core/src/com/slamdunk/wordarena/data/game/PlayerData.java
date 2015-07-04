package com.slamdunk.wordarena.data.game;

import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.enums.PlayerKind;

/**
 * Contient les informations sur le joueur participant à la partie
 */
public class PlayerData {
	
	public static final PlayerData NEUTRAL;
	
	static {
		NEUTRAL = new PlayerData(Assets.i18nBundle.get("ui.editor.player.0"), Assets.MARKER_PACK_NEUTRAL, -1);
	}

	/**
	 * Identifiant unique du joueur
	 */
	public String name;
	
	/**
	 * Indique l'ordre de jeu, donc l'indice du joueur
	 * dans la liste des joueurs de la partie
	 */
	public int place;
	
	/**
	 * Donne le type de joueur (CPU, humain à distance, humain en local)
	 */
	public PlayerKind kind;
	
	public String markerPack;
	
	public int score;
	
	public int nbRoundsWon;
	public int nbZonesOwned;
	public int nbWordsPlayed;
	
	public PlayerData() {
	}
	
	public PlayerData(String id, String cellPack) {
		this(id, cellPack, -1);
	}
	
	public PlayerData(String id, String cellPack, int place) {
		this.name = id;
		this.place = place;
		this.markerPack = cellPack;
		this.kind = PlayerKind.HUMAN_LOCAL;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null
		&& (obj instanceof PlayerData)) {
			return name.equals(((PlayerData)obj).name);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public boolean isNeutral() {
		return this.place == PlayerData.NEUTRAL.place;
	}
	
	public static boolean isNeutral(int place) {
		return place == PlayerData.NEUTRAL.place;
	}
}
