package com.slamdunk.wordarena.data.game;

import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.enums.PlayerKind;

/**
 * Contient les informations sur le joueur participant à la partie
 */
public class Player {
	public static final Player NEUTRAL;
	static {
		NEUTRAL = new Player(Assets.i18nBundle.get("ui.editor.player.0"), Assets.MARKER_PACK_NEUTRAL);
	}
	
	public String id;
	
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
	
	public Player() {
	}
	
	public Player(String id, String cellPack) {
		this(id, cellPack, -1);
	}
	
	public Player(String id, String cellPack, int place) {
		this.id = id;
		this.place = place;
		this.markerPack = cellPack;
		this.kind = PlayerKind.HUMAN_LOCAL;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null
		&& (obj instanceof Player)) {
			return id.equals(((Player)obj).id);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
