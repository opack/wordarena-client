package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.enums.CellEffects;
import com.slamdunk.wordarena.enums.CellTypes;

/**
 * Effet qui consiste à casser le verre des cellules voisines
 */
public class BreakNeighborGlassEffect extends CellEffect {
	/**
	 * Liste de travail qui contient les voisins à chaque appel
	 * à applyEffect. Permet d'éviter l'instanciation d'une
	 * nouvelle liste à chaque fois.
	 */
	private List<ArenaCell> tmpNeighbors;
	
	private List<ArenaCell> brokenCells;
	
	private AnimationDrawer drawer;
	
	public BreakNeighborGlassEffect(CellEffectsManager manager, ArenaCell cell) {
		super(manager, cell);
	}
	
	@Override
	public CellEffects getEffect() {
		return CellEffects.BREAK_NEIGHBOR_GLASS;
	}
	
	@Override
	protected boolean init() {
		// Récupère les voisins
		tmpNeighbors = new ArrayList<ArenaCell>(4);
		getManager().getArena().getNeighbors4(getCell(), tmpNeighbors);
		
		brokenCells = new ArrayList<ArenaCell>(4);
		 
		// Parcours chaque voisin pour voir s'il est en verre
		for (ArenaCell neighbor : tmpNeighbors) {
			if (neighbor.getData().type != CellTypes.G) {
				continue;
			}

			// On jouera une animation de verre brisé sur cette cellule
			brokenCells.add(neighbor);
		}
		
		// TODO Démarre l'animation du verre qui se brise et joue un son
		// TODO Ajouter des particules ?
		drawer = new AnimationDrawer();
		drawer.setAnimation(Assets.breakGlassAnim, true, false);
		
		return true;
	}
	
	@Override
	public boolean perform(float delta) {
		// Déroule l'animation
		drawer.updateTime(delta);
		
		// Vérifie si l'animation est terminée et met à jour l'arène
		if (drawer.isAnimationFinished()) {
			updateArena();
		}
		
		return true;
	}
	
	@Override
	protected void render(Batch batch, float parentAlpha) {
		// Dessine l'animation sur chaque cellule cassée
		for (ArenaCell neighbor : brokenCells) {
			drawer.draw(neighbor, batch);
		}
	}

	private void updateArena() {
		// Chaque cellule cassée devient une cellule normale
		for (ArenaCell neighbor : brokenCells) {
			// La cellule touchée change de type et devient une cellule normale
			neighbor.getData().type = CellTypes.L;
			
			// Mise à jour de l'apparence de la cellule
			neighbor.updateDisplay();
		}
	}
}
