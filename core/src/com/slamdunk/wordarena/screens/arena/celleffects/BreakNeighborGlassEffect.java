package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.ArenaCell;
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
	
	public BreakNeighborGlassEffect(CellEffectsManager manager, ArenaCell cell) {
		super(manager, cell);
		tmpNeighbors = new ArrayList<ArenaCell>(8);
	}
	
	@Override
	public CellEffects getEffect() {
		return CellEffects.BREAK_NEIGHBOR_GLASS;
	}
	
	@Override
	public boolean act(float delta) {
		// Vide la liste des voisins
		tmpNeighbors.clear();
		getManager().getArena().getNeighbors4(getCell(), tmpNeighbors);
		
		// Parcours chaque voisin pour voir s'il est en verre
		for (ArenaCell neighbor : tmpNeighbors) {
			if (neighbor.getData().type != CellTypes.G) {
				continue;
			}
			// TODO Animation et son d'explosion de verre (ou de glace ?)
			
			// La cellule touchée change de type et devient une cellule normale
			neighbor.getData().type = CellTypes.L;
			
			// Mise à jour de l'apparence de la cellule
			neighbor.updateDisplay();
		}
		
		return true;
	}
}
