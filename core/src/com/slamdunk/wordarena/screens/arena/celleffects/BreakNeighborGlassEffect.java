package com.slamdunk.wordarena.screens.arena.celleffects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;

import java.util.ArrayList;
import java.util.List;

public class BreakNeighborGlassEffect extends DefaultCellEffect {
	/**
	 * Liste de travail qui contient les voisins à chaque appel
	 * à applyEffect. Permet d'éviter l'instanciation d'une
	 * nouvelle liste à chaque fois.
	 */
	private List<CellActor> tmpNeighbors;
	
	/**
	 * Cellules dont le verre sera brisé, donc où il faudra
	 * jouer une animation de verre brisé
	 */
	private List<CellActor> brokenCells;
	
	private AnimationDrawer drawer;
	
	public BreakNeighborGlassEffect() {
		tmpNeighbors = new ArrayList<CellActor>(4);
		brokenCells = new ArrayList<CellActor>();
		drawer = new AnimationDrawer();
	}
	
	@Override
	protected boolean isCellTargetable(CellActor cell) {
		// Toute cellule sélectionnée peut avoir dans son entourage
		// une cellule en verre à briser
		return true;
	}
	
	@Override
	public boolean init(List<CellActor> cells, PlayerData player, ArenaOverlay arena) {
		super.init(cells, player, arena);

		brokenCells.clear();
		
		for (CellActor cell : getTargetCells()) {
			// Récupère les voisins
			tmpNeighbors.clear();
			getArena().getNeighbors4(cell, tmpNeighbors);
			
			// Parcours chaque voisin pour voir s'il est en verre
			for (CellActor neighbor : tmpNeighbors) {
				if (neighbor.getData().type != CellTypes.G) {
					continue;
				}
	
				// On jouera une animation de verre brisé sur cette cellule
				brokenCells.add(neighbor);
			}
		}
		
		// S'il n'y a pas de verre à casser, on ne fait rien
		if (brokenCells.isEmpty()) {
			return false;
		}
		
		// TODO Démarre l'animation du verre qui se brise
		drawer.setAnimation(Assets.breakGlassAnim, true, false);
		
		// TODO Jouer un son de verre brisé
		// TODO Ajouter des particules ?
		
		return true;
	}
	
	@Override
	public boolean act(float delta) {
		// Déroule l'animation
		drawer.updateTime(delta);
		
		// Vérifie si l'animation est terminée et met à jour l'arène
		if (drawer.isAnimationFinished()) {
			updateArena();
			return true;
		}
		
		return false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Dessine l'animation sur chaque cellule cassée
		for (CellActor brokenCell : brokenCells) {
			drawer.draw(brokenCell, batch);
		}
	}
	
	/**
	 * Met à jour l'arène après que les cellules aient été cassées
	 */
	private void updateArena() {
		// Chaque cellule cassée devient une cellule normale
		for (CellActor brokenCell : brokenCells) {
			// La cellule touchée change de type et devient une cellule normale
			brokenCell.getData().type = CellTypes.L;
			
			// Mise à jour de l'apparence de la cellule
			brokenCell.updateDisplay();
		}
	}

}
