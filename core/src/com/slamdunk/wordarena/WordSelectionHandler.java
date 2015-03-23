package com.slamdunk.wordarena;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.screens.arena.MatchManager;

/**
 * Gère le mot actuellement sélectionné et détermine si une cellule
 * peut être ajoutée au mot.
 */
public class WordSelectionHandler {
	private MatchManager gameManager;
	private List<ArenaCell> selectedCells;
	
	public WordSelectionHandler(MatchManager gameManager) {
		this.gameManager = gameManager;
		selectedCells = new ArrayList<ArenaCell>();
	}
	
	/**
	 * Ajoute la cellule indiquée au mot si elle peut l'être
	 * @return true si la cellule a pu être ajoutée au mot
	 */
	public boolean selectCell(ArenaCell cell) {
		// Vérifie que la cellule n'est pas déjà sélectionnée
		if (cell.getData().selected
		|| selectedCells.contains(cell)) {
			// La cellule est déjà sélectionnée : on souhaite donc
			// la désélectionner, ainsi que les suivantes
			unselectCellsFrom(cell);
			gameManager.setCurrentWord(getCurrentWord());
			return false;
		}
		
		// Vérifie que la cellule peut être sélectionnée après la précédente
		if (!selectedCells.isEmpty()) {
			// Vérifie que la cellule est bien voisine de la dernière sélectionnée
			ArenaCell last = selectedCells.get(selectedCells.size() - 1);
			double distance = last.getData().position.distance(cell.getData().position);
			if (distance >= 2) {
				return false;
			}
			
			// Vérifie qu'il n'y a pas de mur entre les 2
			if (gameManager.hasWall(last, cell)) {
				return false;
			}
		}
		// Si c'est la première lettre du mot, on s'assure qu'elle est dans
		// une zone contrôlée par le joueur ou qu'elle est elle-même
		// contrôlée par le joueur
		else {
			ArenaZone zone = cell.getData().zone;
			Player player = gameManager.getCinematic().getCurrentPlayer();
			Player cellOwner = cell.getData().owner;
			// La cellule est-elle dans une zone du joueur ?
			boolean isInPlayerZone = (zone != null && player.equals(zone.getData().owner));

			// Si la cellule n'appartient pas au joueur...
			if (!player.equals(cellOwner)
			// ... et que ce n'est pas une cellule neutre dans une zone du joueur ...
			&& (!Player.NEUTRAL.equals(cellOwner) || !isInPlayerZone)) {
				// ... alors il est interdit de commencer un mot dessus
				return false;
			}
		}
		
		// Tout est bon, on sélectionne la cellule puis on l'ajoute au mot
		cell.select();
		selectedCells.add(cell);
		gameManager.setCurrentWord(getCurrentWord());
		return true;
	}
	
	private void unselectCellsFrom(ArenaCell cell) {
		int index = selectedCells.indexOf(cell);
		if (index == -1) {
			return;
		}
		
		final int lastCellIndex = selectedCells.size() - 1;
		ArenaCell removed;
		for (int curSelected = lastCellIndex; curSelected >= index; curSelected--) {
			removed = selectedCells.remove(curSelected);
			removed.unselect();
		}
	}

	/**
	 * Réinitialise le mot sélectionné, supprimant toutes
	 * les lettres
	 */
	public void cancel() {
		for (ArenaCell cell : selectedCells) {
			cell.unselect();
		}
		selectedCells.clear();
		gameManager.setCurrentWord("");
	}

	public List<ArenaCell> getSelectedCells() {
		return selectedCells;
	}

	/**
	 * Retourne le mot actuellement sélectionné
	 * @return
	 */
	public String getCurrentWord() {
		StringBuilder word = new StringBuilder();
		for (ArenaCell cell : selectedCells) {
			word.append(cell.getData().letter.label);
		}
		return word.toString();
	}
}
