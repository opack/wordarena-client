package com.slamdunk.wordarena.screens.arena;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.PlayerData;

/**
 * Gère le mot actuellement sélectionné et détermine si une cellule
 * peut être ajoutée au mot.
 */
public class WordSelectionHandler {
	private MatchManager matchManager;
	private List<CellActor> selectedCells;
	
	public WordSelectionHandler(MatchManager matchManager) {
		this.matchManager = matchManager;
		selectedCells = new ArrayList<CellActor>();
	}

	/**
	 * Ajoute la cellule indiquée au mot si elle peut l'être
	 * @return true si la cellule a pu être ajoutée au mot
	 */
	public boolean selectCell(CellActor cell) {
		// Vérifie que la cellule n'est pas déjà sélectionnée
		if (cell.getData().selected
		|| selectedCells.contains(cell)) {
			// La cellule est déjà sélectionnée : on souhaite donc
			// la désélectionner, ainsi que les suivantes
			unselectCellsFrom(cell);
			matchManager.setCurrentWord(getCurrentWord());
			return false;
		}
		
		// Vérifie que la cellule peut être sélectionnée après la précédente
		if (!selectedCells.isEmpty()) {
			// Vérifie que la cellule est bien voisine de la dernière sélectionnée
			CellActor last = selectedCells.get(selectedCells.size() - 1);
			double distance = last.getData().position.distance(cell.getData().position);
			if (distance >= 2) {
				return false;
			}
			
			// Vérifie qu'il n'y a pas de mur entre les 2
			if (matchManager.hasWall(last, cell)) {
				return false;
			}
		}
		// Si c'est la première lettre du mot, on s'assure qu'elle est dans
		// une zone contrôlée par le joueur ou qu'elle est elle-même
		// contrôlée par le joueur
		else {
			ZoneData zoneData = matchManager.getArenaData().getZoneData(cell.getData().zone);
			PlayerData player = matchManager.getCinematic().getCurrentPlayer();
			int cellOwner = cell.getData().ownerPlace;
			
			// La cellule est-elle dans une zone du joueur ?
			boolean isInPlayerZone = (zoneData != null && player.place == zoneData.ownerPlace);

			// Si la cellule n'appartient pas au joueur...
			if (player.place != cellOwner
			// ... et que ce n'est pas une cellule neutre dans une zone du joueur ...
			&& (!PlayerData.isNeutral(cellOwner) || !isInPlayerZone)) {
				// ... alors il est interdit de commencer un mot dessus
				return false;
			}
		}
		
		// Tout est bon, on sélectionne la cellule puis on l'ajoute au mot
		cell.select(true);
		selectedCells.add(cell);
		matchManager.setCurrentWord(getCurrentWord());
		return true;
	}
	
	private void unselectCellsFrom(CellActor cell) {
		int index = selectedCells.indexOf(cell);
		if (index == -1) {
			return;
		}
		
		final int lastCellIndex = selectedCells.size() - 1;
		CellActor removed;
		for (int curSelected = lastCellIndex; curSelected >= index; curSelected--) {
			removed = selectedCells.remove(curSelected);
			removed.select(false);
		}
	}

	/**
	 * Réinitialise le mot sélectionné, supprimant toutes
	 * les lettres
	 */
	public void cancel() {
		for (CellActor cell : selectedCells) {
			cell.select(false);
		}
		selectedCells.clear();
		matchManager.setCurrentWord("");
	}

	public List<CellActor> getSelectedCells() {
		return selectedCells;
	}

	/**
	 * Retourne le mot actuellement sélectionné
	 * @return
	 */
	public String getCurrentWord() {
		StringBuilder word = new StringBuilder();
		for (CellActor cell : selectedCells) {
			word.append(cell.getData().letter.label);
		}
		return word.toString();
	}
}
