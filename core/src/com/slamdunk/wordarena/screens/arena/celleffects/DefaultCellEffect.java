package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;

/**
 * Un effet par défaut qui ne fait rien à part filtrer les cellules reçues
 * pour ne garder que celles de certains types, et conserver le joueur
 * et l'arène pour utilisation future
 */
public abstract class DefaultCellEffect implements CellEffect {
	private List<CellActor> targetCells;
	private PlayerData player;
	private ArenaOverlay arena;
	
	public DefaultCellEffect() {
		targetCells = new ArrayList<CellActor>();
	}

	@Override
	public boolean init(List<CellActor> cells, PlayerData player, ArenaOverlay arena) {
		this.player = player;
		this.arena = arena;
		
		// Filtre les cellules reçues pour ne conserver que celles sur
		// lesquelles cet effet peut être appliqué
		targetCells.clear();
		for (CellActor cell : cells) {
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
	protected abstract boolean isCellTargetable(CellActor cell);

	public List<CellActor> getTargetCells() {
		return targetCells;
	}

	public PlayerData getPlayer() {
		return player;
	}

	public ArenaOverlay getArena() {
		return arena;
	}
}
