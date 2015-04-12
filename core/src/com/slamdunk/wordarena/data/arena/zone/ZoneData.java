package com.slamdunk.wordarena.data.arena.zone;

import com.slamdunk.wordarena.data.game.PlayerData;

/**
 * Contient les données logiques d'une zone
 */
public class ZoneData {
	/**
	 * Identifiant de la zone, correspondant à ce qui
	 * est indiqué dans le fichier descriptif properties
	 */
	public final String id;
	
	/**
	 * Joueur qui possède la zone
	 * @param edge
	 */
	public int ownerPlace;
	
	/**
	 * Indique si la zone est en surbrillance
	 */
	public boolean highlighted;
	
	public ZoneData(String id) {
		this.id = id;
		ownerPlace = PlayerData.NEUTRAL.place;
	}
}
