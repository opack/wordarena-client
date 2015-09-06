package com.slamdunk.wordarena.data.arena;

import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.enums.Letters;

import java.util.ArrayList;
import java.util.List;

public class ArenaData {
	public int width;
	public int height;
	
	/**
	 * Contient les différentes cellules de l'arène
	 */
	public CellData[][] cells;
	
	/**
	 * Contient les différentes zones de l'arène
	 */
	public List<ZoneData> zones;
	
	/**
	 * Indique s'il y a un mur entre les 2 cellules indiquées
	 */
	public DoubleEntryArray<CellData, CellData, Boolean> walls;
	
	public Deck<Letters> letterDeck;
	
	/**
	 * Nom de l'arène
	 */
	public String name;
	
	/**
	 * Nom de la skin d'arène
	 */
	public String skin;
	
	public ArenaData() {
		zones = new ArrayList<ZoneData>();
		walls = new DoubleEntryArray<CellData, CellData, Boolean>();
	}
	
	/**
	 * Indique s'il y a un mur entre les 2 cellules spécifiées
	 * @param cell1
	 * @param cell2
	 * @return
	 */
	public boolean hasWall(CellData cell1, CellData cell2) {
		Boolean wall = walls.get(cell1, cell2);
		return wall != null
			&& wall == Boolean.TRUE;
	}

	/**
	 * Ajoute un mur entre cell1 et cell2.
	 * Ce mur est ajouté 2 fois : une fois de cell1 vers cell2, et une fois
	 * dans l'autre sens. Ainsi la recherche de mur via {@link #hasWall(CellActor, CellActor)}
	 * se fait rapidement et sans se préoccuper du sens.
	 * @param cell1
	 * @param cell2
	 */
	public void addWall(CellData cell1, CellData cell2) {
		walls.put(cell1, cell2, Boolean.TRUE);
		walls.put(cell2, cell1, Boolean.TRUE);
	}
	
	/**
	 * Supprime un mur entre cell1 et cell2.
	 * Ce mur est supprimé 2 fois : une fois de cell1 vers cell2, et une fois
	 * dans l'autre sens.
	 * @param cell1
	 * @param cell2
	 */
	public void removeWall(CellData cell1, CellData cell2) {
		walls.remove(cell1, cell2, Boolean.TRUE);
		walls.remove(cell2, cell1, Boolean.TRUE);
	}
	
	/**
	 * Indique si la position existe dans l'arène
	 * @param pos
	 * @return
	 */
	public boolean isValidPos(Point pos) {
		return pos.getX() > -1
			&& pos.getX() < width
			&& pos.getY() > -1
			&& pos.getY() < height;
	}
	
	/**
	 * Indique si la position existe dans l'arène
	 * @param pos
	 * @return
	 */
	public boolean isValidPos(int x, int y) {
		return x > -1
			&& x < width
			&& y > -1
			&& y < height;
	}

	/**
	 * Retourne la zone data correspondant à l'id indiqué
	 * @param zone
	 * @return
	 */
	public ZoneData getZoneData(String id) {
		for (ZoneData zoneData : zones) {
			if (zoneData.id.equals(id)) {
				return zoneData;
			}
		}
		return null;
	}
}
