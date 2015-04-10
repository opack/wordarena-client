package com.slamdunk.wordarena.data.game;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.toolkit.world.point.Point;

/**
 * Un coup joué
 */
public class GameMove {
	/**
	 * Joueur qui a joué ce dernier coup
	 */
	public String playerId;

	/**
	 * Liste ordonnée des cellules sélectionnées lors du dernier mot
	 */
	public List<Point> cells;
	
	public GameMove() {
		cells = new ArrayList<Point>();
	}
}
