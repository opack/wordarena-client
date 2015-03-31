package com.slamdunk.wordarena.actors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.slamdunk.toolkit.lang.MaxValueFinder;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.data.ZoneBorderBuilder;
import com.slamdunk.wordarena.data.ZoneData;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.screens.arena.MatchManager;

/**
 * Représente une zone.
 * Une zone pointe les bordures des cellules afin de changer d'un coup
 * toutes les couleurs.
 */
public class ArenaZone extends Group {
	public static final ArenaZone NONE = new ArenaZone(null, "-");
	public static final float BORDER_POS_OFFSET = 0f;
	
	private ZoneData data;
	
	private MatchManager gameManager;
	
	/**
	 * Cellules uniques de la zone, rangées par position
	 */
	private Map<Point, ArenaCell> cells;
	
	private List<ZoneEdge> edges;
	
	public ArenaZone(MatchManager gameManager, String id) {
		this.gameManager = gameManager;

		data = new ZoneData(id);
		cells = new HashMap<Point, ArenaCell>();
		edges = new ArrayList<ZoneEdge>();
	}
	
	public Collection<ArenaCell> getCells() {
		return cells.values();
	}

	public void addCell(ArenaCell cell) {
		cells.put(cell.getData().position, cell);
	}
	
	public void removeCell(ArenaCell cell) {
		cells.remove(cell.getData().position);
		// Si la cellule était simplement sous contrôle, elle est désormais neutre
		CellData data = cell.getData();
		if (data.state == CellStates.CONTROLED) {
			data.state = CellStates.OWNED;
			data.owner = Player.NEUTRAL;
		}
	}
	
	public void update() {
		// Affecte la zone à chaque cellule
		for (ArenaCell cell : cells.values()) {
			cell.getData().zone = this;
		}
		
		// Dessine une bordure autour de la zone, sauf si c'est la zone NONE
		// qui contient justement les cellules sans zone
		clear();
		if (this != NONE) {
			// Crée les bordures qui entourent la zone
			ZoneBorderBuilder zoneBorderBuilder = new ZoneBorderBuilder(cells, edges);
			zoneBorderBuilder.build(data.owner, data.highlighted);
			
			// Ajoute les bordures à la zone
			for (ZoneEdge edge : edges) {
				addActor(edge);
			}
			
			// Choisit l'owner de la zone
			updateOwner();
		}
		
//		// Met à jour la liste des côtés
//		edges.clear();
//		for (ArenaCell cell : cells.values()) {
//			// Affecte la zone à chaque cellule
//			cell.getData().zone = this;
//		
//			// Ajoute les côtés uniques dans la liste
//			checkEdge(cell, Borders.LEFT, -1, 0);
//			checkEdge(cell, Borders.TOP, 0, +1);
//			checkEdge(cell, Borders.RIGHT, +1, 0);
//			checkEdge(cell, Borders.BOTTOM, 0, -1);
//		}
//		
//		// Crée les lignes pour dessiner ces côtés
//		clear();
//		for (ZoneEdge edge : edges) {
//			edge.update(data.highlighted);
//			edge.setScaling(Scaling.stretch);
//			edge.setAlign(Align.center);
//			edge.setSize(edge.getPrefWidth(), edge.getPrefHeight());
//			
//			ActorHelper.alignInside(edge.getData().border, edge.getData().p1, BORDER_POS_OFFSET, edge);
//			addActor(edge);
//		}
//		
//		// Choisit l'owner de la zone
//		updateOwner();
	}
	
	/**
	 * Met la zone en surbrillance ou non
	 * @param highlighted
	 */
	public void highlight(boolean highlighted) {
		data.highlighted = highlighted;
		MarkerPack pack = Assets.markerPacks.get(data.owner.markerPack);
		for (ZoneEdge edge : edges) {
			edge.updateDisplay(pack, highlighted);
		}
	}
	
	public ZoneData getData() {
		return data;
	}

	private void setOwner(Player newOwner) {
		// Changement de l'owner
		Player oldOwner = data.owner;
		data.owner = newOwner;
		
		// Change l'image des cellules de la zone
		CellData cellData;
		for (ArenaCell cell : cells.values()) {
			cellData = cell.getData();
			
			// Une cellule passe sous le contrôle du joueur si elle est dans la zone et :
			//    - soit neutre
			//    - soit sous le simple contrôle d'un adversaire
			if (Player.NEUTRAL.equals(cellData.owner)
			|| cellData.state != CellStates.OWNED) {
				cellData.owner = newOwner;
				cellData.state = CellStates.CONTROLED;
			}
			
			// Met à jour l'image
			cell.updateDisplay();
		}
		
		// Change les bordures de zone
		if (!edges.isEmpty()) {
			MarkerPack pack = Assets.markerPacks.get(newOwner.markerPack);
			for (ZoneEdge edge : edges) {
				edge.updateDisplay(pack, data.highlighted);
			}
		}
		
		// Changement d'owner ? Avertit le game manager pour la mise à jour du score
		if (!oldOwner.equals(newOwner)
		&& gameManager != null) {
			gameManager.zoneChangedOwner(oldOwner, newOwner);
		}
	}
	
	/**
	 * Détermine le propriétaire de la zone en fonction du propriétaire
	 * des cellules
	 */
	public void updateOwner() {
		// Zone none ? Personne ne peut la posséder
		if (this == NONE) {
			return;
		}
				
		MaxValueFinder<Player> occupations = new MaxValueFinder<Player>();
		occupations.setIgnoredValue(Player.NEUTRAL);
		occupations.setValueIfDraw(Player.NEUTRAL);
		
		// Compte le nombre de cellules occupées par chaque joueur
		CellData cellData;
		for (ArenaCell cell : cells.values()) {
			cellData = cell.getData();
			// Ajout de la puissance de la cellule à celle de ce joueur
			if (cellData.state == CellStates.OWNED) {
				occupations.addValue(cellData.owner, cellData.power);
			}
		}
		
		// Détermine qui occupe le plus de cellules
		Player newOwner = occupations.getMaxValue();
		
		// Change le propriétaire de la zone
		setOwner(newOwner);
	}
	
	@Override
	public String toString() {
		return data.id;
	}
}
