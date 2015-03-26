package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.ReturnCodes;
import com.slamdunk.wordarena.screens.arena.MatchCinematic.GameCinematicListener;
import com.slamdunk.wordarena.screens.arena.celleffects.CellEffectsApplicationFinishedListener;
import com.slamdunk.wordarena.screens.arena.celleffects.CellEffectsManager;

/**
 * Gère la partie
 */
public class MatchManager implements GameCinematicListener, CellEffectsApplicationFinishedListener{
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	private String arenaPlanFile;
	
	private GameStates state;
	private int nbZones;
	
	private WordSelectionHandler wordSelectionHandler;
	private WordValidator wordValidator;
	private MatchCinematic cinematic;
	private CellEffectsManager cellEffectsManager;
	
	public MatchManager() {		
		wordSelectionHandler = new WordSelectionHandler(this);
		wordValidator = new WordValidator();
		cinematic = new MatchCinematic();
		cinematic.setListener(this);
	}
	
	public WordSelectionHandler getWordSelectionHandler() {
		return wordSelectionHandler;
	}
	
	public MatchCinematic getCinematic() {
		return cinematic;
	}

	public void prepareGame(ArenaScreen screen, String arenaPlanFile, Array<Player> playersList) {
		arena = screen.getArena();
		ui = screen.getUI();
		
		cellEffectsManager = arena.getCellEffectsManager();
		cellEffectsManager.setListener(this);
		
		this.arenaPlanFile = arenaPlanFile;
		cinematic.init(playersList);
		
		// Charge l'arène
		loadArena();
		
		// Pas de mot et affichage des boutons en conséquence
		setCurrentWord("");
		
		// Démarre le jeu
		changeState(GameStates.READY);
	}

	/**
	 * Vérifie si le mot est valide, ajoute des points au score
	 * le cas échéant et choisit d'autres lettres sur le mot
	 * sélectionné.
	 */
	public void validateWord() {
		ReturnCodes result = wordValidator.validate(wordSelectionHandler.getCurrentWord());
		String word = wordValidator.getLastValidatedWord();
		switch (result) {
		
		case OK:
			Player player = cinematic.getCurrentPlayer();
			List<ArenaCell> selectedCells = wordSelectionHandler.getSelectedCells();
			
			// Affiche le mot joué
			ui.setInfo(Assets.i18nBundle.format("ui.arena.wordPlayed", player.name, word));
			
			// Déclenche les effets sur les cellules
			cellEffectsManager.applyEffects(selectedCells, player, arena.getData());
			
			// Bloquer la saisie pour empêcher que le joueur ne joue de nouveau pendant les animations
			arena.enableCellSelection(false);
			
			// Raz du mot sélectionné
			cancelWord();
			
			break;
			
		case WORD_ALREADY_PLAYED:
			ui.setInfo(Assets.i18nBundle.format("ui.arena.alreadyPlayed", word));
			
			// Les boutons de validation doivent rester affichés
			ui.showWordValidationButtons(true);
			
			break;
			
		case WORD_UNKNOWN:
			ui.setInfo(Assets.i18nBundle.format("ui.arena.unknownWord", word));
			
			// Les boutons de validation doivent rester affichés
			ui.showWordValidationButtons(true);
			
			break;
			
		}
	}

	/**
	 * Appelée par ArenaZone lorsqu'une zone change de propriétaire.
	 * @param oldOwner
	 * @param newOwner
	 */
	public void zoneChangedOwner(Player oldOwner, Player newOwner) {
		if (!Player.NEUTRAL.equals(newOwner)) {
			// Le joueur a une zone de plus
			newOwner.nbZonesOwned++;
			
			// Ajout des points uniquement pendant la partie, et pas pendant
			// le chargement de l'arène par exemple
			if (state == GameStates.RUNNING
			&& oldOwner != newOwner) {
				ScoreHelper.onZoneConquest(newOwner, oldOwner);
			}
		}
		
		// Regarde si le joueur qui a perdu la zone a perdu sa
		// dernière zone, et donc le round.
		if (!Player.NEUTRAL.equals(oldOwner)) {
			// Ce joueur a perdu une zone
			oldOwner.nbZonesOwned--;
			
			// Si c'était sa dernière zone, il perd le round
			if (oldOwner.nbZonesOwned <= 0) {
				cinematic.endRound();
			}
		}
		
		// Met à jour l'UI si le jeu tourne. Sinon, on a sûrement
		// été appelé pendant la création de l'arène. On ne met
		// donc pas à jour l'UI.
		if (state == GameStates.RUNNING) {
//			ui.updateStats();
		}
	}

	/**
	 * Réinitialise les lettres sélectionnées
	 */
	public void cancelWord() {
		wordSelectionHandler.cancel();
	}
	
	/**
	 * Met le jeu en pause s'il était en train de tourner
	 */
	public void pause() {
		if (state == GameStates.RUNNING) {
			changeState(GameStates.PAUSED);
		}
	}
	
	/**
	 * Change l'état actuel du jeu et met à jour l'IHM
	 * @param newState
	 */
	public void changeState(GameStates newState) {
		if (newState == state) {
			return;
		}
		
		// Mise à jour de l'UI
		state = newState;
		ui.present(newState);
		arena.showLetters(newState == GameStates.RUNNING);
		
		if (newState == GameStates.RUNNING) {
			// Re-masque ou ré-affiche les boutons en fonction de l'état de la partie
			setCurrentWord(wordSelectionHandler.getCurrentWord());
		} else if (newState == GameStates.PAUSED) {
			ui.updateStats();
		}
	}
	
	public void loadArena() {
		// Réinitialise les scores
		for (Player player : cinematic.getPlayers()) {
			player.score = 0;
			player.nbZonesOwned = 0;
			player.nbWordsPlayed = 0;
		}
		
		// Réinitialise le sélecteur et le validateur de mots
		wordSelectionHandler.cancel();
		wordValidator.clearAlreadyPlayedList();
		
		// Charge l'arène
		arena.buildArena(arenaPlanFile, this);
		arena.showLetters(false);
		nbZones = arena.getData().zones.size();
		cinematic.setArenaData(arena.getData());
		
		// Met à jour l'UI
		ui.setArenaName(arena.getData().name);
		ui.setInfo("");
	}
	
	public int getNbZones() {
		return nbZones;
	}

	public void refreshStartingZone() {
		Player curPlayer = cinematic.getCurrentPlayer();
		
		// Change les lettres de la zone de départ
		arena.refreshStartingZone(curPlayer);
		
		// Supprime le mot éventuellement sélectionné
		wordSelectionHandler.cancel();
		
		// Affiche un message de confirmation
		ui.setInfo(Assets.i18nBundle.format("ui.arena.redrewLetters", curPlayer.name));
		
		// Le score du joueur est modifié
		ScoreHelper.onRefreshStartingZone(curPlayer);
		
		// Termine le tour de ce joueur
		cinematic.endMove();
	}
	
	/**
	 * Met à jour l'affichage du mot actuellement sélectionné
	 */
	public void setCurrentWord(String word) {
		if (ui != null) {
			boolean emptyWord = word.isEmpty();
			ui.setCurrentWord(word);
			ui.showWordValidationButtons(!emptyWord);
			ui.showRefreshStartingZoneButton(cinematic.getCurrentPlayer().nbWordsPlayed == 0 && emptyWord);
		}
	}

	public void requestBack() {
		switch (state) {
		case RUNNING:
			changeState(GameStates.PAUSED);
			break;
		case PAUSED:
			changeState(GameStates.RUNNING);
			break;
		case READY:
		case GAME_OVER:
		case ROUND_OVER:
			// On ne fait rien dans ces cas-là : l'utilisateur doit choisir une action
			break;
		}
	}

	/**
	 * Vérifie s'il y a un mur entre les 2 cellules, ou dans un coin qui empêcherait
	 * de passer d'une cellule à l'autre
	 * @param last
	 * @param cell
	 * @return
	 */
	public boolean hasWall(ArenaCell cell1, ArenaCell cell2) {
		return arena.getData().hasWall(cell1, cell2);
	}
	
	/**
	 * Passe au round suivant
	 */
	public void nextRound() {
		cinematic.nextRound();
		
		// Réinitialise l'arène
		loadArena();
		// Démarre le jeu
		changeState(GameStates.RUNNING);
	}

	@Override
	public void onRoundEnd(Player roundWinner) {
		if (roundWinner == null) {
			// Egalité
			ui.setRoundWinner("Egalité ! Personne ne gagne ce round !");
		} else {
			// Victoire
			ui.setRoundWinner(roundWinner.name + " gagne le round !");
		}
		changeState(GameStates.ROUND_OVER);
		
		// Cache les lettres de l'arène
		arena.showLetters(false);
	}
	
	@Override
	public void onGameEnd(Player gameWinner) {
		// Affichage du gagnant
		if (gameWinner == null) {
			ui.setGameWinner("Egalité parfaite ! Personne ne gagne cette partie !");
		} else {
			ui.setGameWinner(gameWinner.name + " gagne la partie !");
		}
		changeState(GameStates.GAME_OVER);
		
		// Cache les lettres de l'arène
		arena.showLetters(false);
	}
	
	@Override
	public void onPlayerChange(Player currentPlayer) {
		ui.setCurrentPlayer(currentPlayer, cinematic.getCurrentTurn(), cinematic.getNbTurnsPerRound(), cinematic.getCurrentRound());
		
		// Affiche le bouton de rafraîchissement des lettres de la zone de départ.
		// On ne peut rafraîchir la zone de départ qu'au premier coup du round.
		ui.showRefreshStartingZoneButton(currentPlayer.nbWordsPlayed == 0);
		
		switch (currentPlayer.kind) {
		case CPU:
			// TODO Si le joueur est un CPU, on le fait jouer
			arena.enableCellSelection(false);
			break;
		case HUMAN_LOCAL:
			// TODO Le prochain joueur joue sur ce terminal. On affiche une boîte de dialogue qu'il devra fermer pour débuter son tour.
			arena.enableCellSelection(true);
			break;
		case HUMAN_NET:
			// TODO Le prochain joueur joue sur le net. On désactive la saisie sur ce terminal.
			arena.enableCellSelection(false);
			break;
		}
	}

	/**
	 * Cette méthode est appelée après la validation d'un mot, une fois que tous les
	 * effets visuels et de gameplay ont été appliqués.
	 */
	@Override
	public void onEffectApplicationFinished(Player player, List<ArenaCell> processedCells) {
		// Toutes les cellules passent sous la domination du joueur
		arena.setCellsOwner(processedCells, player);
		
		// Le score du joueur est modifié
		ScoreHelper.onValidWord(player, processedCells);
		
		// Le joueur a joué un coup. C'est bon à savoir pour les stats
		// et pour autoriser ou non le refresh de la zone de départ
		player.nbWordsPlayed++;
		
		// Termine le tour de ce joueur
		cinematic.endMove();
	}
}
