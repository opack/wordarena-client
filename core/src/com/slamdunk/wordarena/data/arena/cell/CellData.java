package com.slamdunk.wordarena.data.arena.cell;

import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

/**
 * Représente les données d'une cellule
 */
public class CellData {
	/**
	 * Le type de la cellule
	 */
	public CellTypes type;
	
	/**
	 * Lettre initialement contenue dans la cellule, telle qu'indiquée dans le plan
	 */
	public String planLetter;
	
	/**
	 * La lettre actuellement contenue dans la cellule
	 */
	public Letters letter;
	
	/**
	 * Indique dans quel état se trouve la cellule (contrôle, dans une zone de contrôle ou neutre)
	 */
	public CellStates state;
	
	/**
	 * Indique si la cellule est actuellement sélectionnée dans un mot
	 */
	public boolean selected;
	
	/**
	 * La position à laquelle se trouve la cellule
	 * dans l'arène
	 */
	public final Point position;
	
	/**
	 * Indice (parmi la liste des joueurs de cette
	 * partie) du joueur qui possède la cellule
	 */
	public int ownerPlace;
	
	/**
	 * Indique la puissance de cette cellule
	 */
	public int power;
	
	/**
	 * Indique dans quelle zone se trouve cette cellule
	 */
	public String zone;
	
	public CellData() {
		letter = Letters.A;
		state = CellStates.CONTROLED;
		selected = false;
		position = new Point(0, 0);
		ownerPlace = PlayerData.NEUTRAL.place;
		power = 1;
		zone = ZoneActor.NONE.getData().id;
	}
}
