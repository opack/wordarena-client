package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.Player;

/**
 * Un effet par défaut qui ne fait rien à part filtrer les cellules reçues
 * pour ne garder que celles de certains types, et conserver le joueur
 * et l'arène pour utilisation future
 */
public abstract class DefaultCellEffect implements CellEffect {
	private List<ArenaCell> targetCells;
	private Player player;
	private ArenaData arena;
	
	public DefaultCellEffect() {
		targetCells = new ArrayList<ArenaCell>();
	}

	@Override
	public boolean init(List<ArenaCell> cells, Player player, ArenaData arena) {
		this.player = player;
		this.arena = arena;
		
		// Filtre les cellules reçues pour ne conserver que celles sur
		// lesquelles cet effet peut être appliqué
		targetCells.clear();
		for (ArenaCell cell : cells) {
			if (isCellTargetable(cell)) {
				targetCells.add(cell);
			}
		}
		
		return true;
	}

	/**
	 * Indique si la cellule peut être la cible de cet effet. Cette méthode
	 * sert surtout dans la méthode init() pour initialiser la liste
	 * des cellules cibles (getTargetedCells()).
	 * Note : au moment où cette méthode est appélée, les champs player
	 * et arena sont renseignés
	 * @param cell
	 * @return
	 */
	protected abstract boolean isCellTargetable(ArenaCell cell);

	public List<ArenaCell> getTargetCells() {
		return targetCells;
	}

	public Player getPlayer() {
		return player;
	}

	public ArenaData getArena() {
		return arena;
	}
}
