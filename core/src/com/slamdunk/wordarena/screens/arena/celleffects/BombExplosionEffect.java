package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;

public class BombExplosionEffect extends DefaultCellEffect {
	/**
	 * Liste de travail qui contient les voisins à chaque appel
	 * à applyEffect. Permet d'éviter l'instanciation d'une
	 * nouvelle liste à chaque fois.
	 */
	private List<ArenaCell> tmpNeighbors;
	
	private AnimationDrawer drawer;
	
	@Override
	protected boolean isCellTargetable(ArenaCell cell) {
		return cell.getData().type == CellTypes.B;
	}
	
	@Override
	public boolean init(List<ArenaCell> cells, Player player, ArenaData arena) {
		super.init(cells, player, arena);
		
		// S'il n'y a pas de bombe, il n'y a rien à faire
		if (getTargetCells().isEmpty()) {
			return false;
		}
		
		// Prépare le tableau de voisins qui servira à la fin
		tmpNeighbors = new ArrayList<ArenaCell>(8);
		
		// Démarre l'animation de l'explosion
		drawer = new AnimationDrawer();
		drawer.setAnimation(Assets.explosionAnim, true, false);
		
		// TODO Joue un son d'explosion
		
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
		
		// L'animation (et donc l'effet) n'est pas terminé
		return false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (ArenaCell cell : getTargetCells()) {
			drawer.draw(cell, batch);
		}
	}

	/**
	 * Met à jour l'arène après l'explosion
	 */
	private void updateArena() {
		for (ArenaCell cell : getTargetCells()) {
			// Récupère les voisins
			tmpNeighbors.clear();
			getArena().getNeighbors4(cell, tmpNeighbors);
			
			// Récupère l'owner de la cellule où se trouve la bombe
			CellData cellData = cell.getData();
			Player owner = cellData.owner;
			
			// Parcours chaque voisin pour voir s'il appartient à l'owner de la cellule
			CellData neighborData;
			for (ArenaCell neighbor : tmpNeighbors) {
				neighborData = neighbor.getData();
				
				// Si le voisin appartient ou joueur ou n'est pas possédé, on ne fait rien
				if (owner.equals(neighborData.owner)
				|| neighborData.state != CellStates.OWNED) {
					continue;
				}
				
				// La cellule perd son owner
				neighborData.owner = Player.NEUTRAL;
				neighborData.state = CellStates.CONTROLED;
				
				// Mise à jour de l'apparence de la cellule
				neighbor.updateDisplay();
			}
			
			// La cellule n'est plus une bombe car elle a explosé
			cellData.type = CellTypes.L;
			
			// Mise à jour de l'apparence de la cellule
			cell.updateDisplay();
		}
	}
}
