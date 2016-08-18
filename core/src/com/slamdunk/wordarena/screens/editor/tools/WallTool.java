package com.slamdunk.wordarena.screens.editor.tools;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;

import java.util.Collection;

public class WallTool extends EditorTool<CellActor> {
	public static final String NAME = WallTool.class.getName();

	private ArenaOverlay arena;
	
	public void setArena(ArenaOverlay arena) {
		this.arena = arena;
	}
	
	@Override
	public void apply(CellActor cell) {
		// Récupère la cellule déjà sélectionnée
		CellActor firstCell = getValue();
		
		// Vérifie si on vient de choisir de nouveau la même
		// cellule. Dans ce cas, on la désélectionne.
		if (firstCell == cell) {
			setValue(null);
			return;
		}
		
		// Vérifie si on vient de choisir la première cellule
		if (firstCell == null) {
			setValue(cell);
			return;
		}
		
		// Si on arrive ici, c'est qu'on a choisi la seconde cellule.
		// Vérifie que les 2 cellules ne sont pas en diagonale
		int cell1X = firstCell.getData().position.getX();
		int cell1Y = firstCell.getData().position.getY();
		int cell2X = cell.getData().position.getX();
		int cell2Y = cell.getData().position.getY();
		if (cell1X != cell2X
		&& cell1Y != cell2Y) {
			// Ni le X ni le Y n'est en commun : ces cellules ne sont pas alignées
			setValue(null);
			return;
		}
		
		// Vérifie que les 2 cellules sont adjacentes
		if (Math.abs(cell2X - cell1X) != 1
		&& Math.abs(cell2Y - cell1Y) != 1) {
			// Ni le delta X ni le delta Y n'est à 1 : ces cellules sont écartées
			setValue(null);
			return;
		}
		
		// Les 2 cellules sont valides, on crée ou supprime le mur
		if (arena.hasWall(firstCell, cell)) {
			arena.removeWall(firstCell, cell);
		} else {
			arena.addWall(firstCell, cell);
		}
		setValue(null);
	}

	@Override
	public void apply(Collection<CellActor> cells) {
		for (CellActor cell : cells) {
			apply(cell);
		}
	}
}
