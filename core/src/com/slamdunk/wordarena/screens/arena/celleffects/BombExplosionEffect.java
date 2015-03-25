package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellEffects;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;

/**
 * Retirer la possession des adversaires dans les cellules voisines
 */
public class BombExplosionEffect extends CellEffect {
	/**
	 * Liste de travail qui contient les voisins à chaque appel
	 * à applyEffect. Permet d'éviter l'instanciation d'une
	 * nouvelle liste à chaque fois.
	 */
	private List<ArenaCell> tmpNeighbors;
	
	public BombExplosionEffect(CellEffectsManager manager, ArenaCell cell) {
		super(manager, cell);
		tmpNeighbors = new ArrayList<ArenaCell>(8);
	}
	
	@Override
	public CellEffects getEffect() {
		return CellEffects.BOMB_EXPLOSION;
	}

	@Override
	public boolean act(float delta) {
		ArenaCell cell = getCell();
		
		// Vide la liste des voisins
		tmpNeighbors.clear();
		getManager().getArena().getNeighbors4(cell, tmpNeighbors);
		
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
			// TODO Animation et son d'explosion
			
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
		
		return true;
	}
}
