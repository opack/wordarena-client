package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.ArenaData;
import com.slamdunk.wordarena.data.game.GameCache;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.MatchCinematic.GameCinematicListener;
import com.slamdunk.wordarena.screens.arena.WordValidator.WordValidationListener;
import com.slamdunk.wordarena.screens.arena.celleffects.CellEffectsApplicationFinishedListener;
import com.slamdunk.wordarena.screens.arena.celleffects.CellEffectsManager;

/**
 * Gère la partie
 */
public class MatchManager implements GameCinematicListener, CellEffectsApplicationFinishedListener, WordValidationListener {
	
	private GameCache cache;
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	private GameStates state;
	
	private WordSelectionHandler wordSelectionHandler;
	private WordValidator wordValidator;
	private MatchCinematic cinematic;
	private CellEffectsManager cellEffectsManager;
	
	/**
	 * Indique que le joueur est en phase de sélection des lettres de son mot.
	 * Pendant cette phase, le score n'est pas modifié par la conquête et la
	 * perte de zone, et la perte de la dernière zone ne provoque pas la fin
	 * du round.
	 */
	private boolean selectingLetters;
	
	public MatchManager() {		
		wordSelectionHandler = new WordSelectionHandler(this);
		wordValidator = new WordValidator(this);
		cinematic = new MatchCinematic();
		cinematic.setListener(this);
	}
	
	public GameCache getCache() {
		return cache;
	}

	public WordSelectionHandler getWordSelectionHandler() {
		return wordSelectionHandler;
	}
	
	public MatchCinematic getCinematic() {
		return cinematic;
	}
	
	public ArenaData getArenaData() {
		return arena.getData();
	}
	
	public void init(ArenaOverlay arena, ArenaUI ui, GameCache cache) {
		// Attache ce manager au Screen
		this.arena = arena;
		arena.setMatchManager(this);
		this.ui = ui;
		cellEffectsManager = arena.getCellEffectsManager();
		cellEffectsManager.setListener(this);
		
		// Récupère le cache puis initialise l'environnement à partir de ses données
		this.cache = cache;
		GameData game = cache.getData();
		
		// Charge l'arène
		selectingLetters = false;
		arena.loadArena(game.arena);
		arena.enableCellSelection(false);
		arena.showLetters(false);
		
		// Prépare la cinématique du jeu
		cinematic.init(game);
		
		// Initialise le sélecteur et le validateur de mots
		wordSelectionHandler.cancel();
		wordValidator.init(game);
		
		// Met à jour l'UI
		ui.init(game, cinematic.getNbWinningRoundsPerGame());
		
		// TODO Rejouer le dernier coup
		// TODO Charger les chats
		
		selectingLetters = !game.cinematic.gameOver;

		// Démarre le jeu
		if (game.cinematic.gameOver) {
			changeState(GameStates.GAME_OVER);
		} else {
			changeState(GameStates.READY);
		}
	}
	
	/**
	 * Demande la validation du mot actuellement sélectionné
	 */
	public void validateWord() {
		// TODO Affiche une animation d'attente
		
		// Demande la validation
		wordValidator.validate(wordSelectionHandler.getCurrentWord(), cinematic.getCurrentPlayer().place);
	}

	/**
	 * Appelée par ArenaZone lorsqu'une zone change de propriétaire.
	 * @param oldOwner
	 * @param newOwner
	 */
	public void zoneChangedOwner(int oldOwnerPlace, int newOwnerPlace) {
		// Met à jour l'UI si le jeu tourne. Sinon, on a sûrement
		// été appelé pendant la création de l'arène. On ne met
		// donc pas à jour l'UI.
		if (state == GameStates.RUNNING) {
			ui.updateZoneMarkers(arena.getData().zones);
		}
		
		// Si le joueur est en train de sélectionner des lettres, alors
		// on ne prend pas en compte ce changement de propriétaire de zone
		if (selectingLetters) {
			return;
		}
		
		PlayerData oldOwner = getPlayer(oldOwnerPlace);
		PlayerData newOwner = getPlayer(newOwnerPlace);
		if (!newOwner.isNeutral()) {
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
		if (!oldOwner.isNeutral()) {
			// Ce joueur a perdu une zone
			oldOwner.nbZonesOwned--;
			
			// Si c'était sa dernière zone, il perd le round
			if (oldOwner.nbZonesOwned <= 0) {
				cinematic.endRound();
			}
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
		
		arena.enableCellSelection(newState == GameStates.RUNNING);
		arena.showLetters(newState == GameStates.RUNNING);
		
		if (newState == GameStates.RUNNING) {
			// Re-masque ou ré-affiche les boutons en fonction de l'état de la partie
			setCurrentWord(wordSelectionHandler.getCurrentWord());
			
			// Continue l'application des effets
			cellEffectsManager.setPaused(false);
		} else if (newState == GameStates.PAUSED) {
			// Met en pause l'application des effets
			cellEffectsManager.setPaused(true);
			
			// Met à jour les statistiques qui seront affichées
			ui.updateStats();
		}
	}
	
	/**
	 * Réinitialise l'arène pour un nouveau round notamment,
	 * en rechargeant l'état initial tel que définit dans le
	 * plan et en réinitialisant les scores et possessions
	 * des joueurs.
	 */
	public void resetArena() {
		// Réinitialise les scores
		for (PlayerData player : cinematic.getPlayers()) {
			player.score = 0;
			player.nbZonesOwned = 0;
			player.nbWordsPlayed = 0;
		}
		
		// Réinitialise le sélecteur et le validateur de mots
		wordSelectionHandler.cancel();
		wordValidator.clearAlreadyPlayedList();
		
		// Charge l'arène
		arena.buildArena("arenas/" + cache.getData().arena.name + ".json");
		arena.enableCellSelection(false);
		arena.showLetters(false);
		cache.getData().arena = arena.getData();
		
		// Met à jour l'UI
		ui.init(cache.getData(), cinematic.getNbWinningRoundsPerGame());
		
		selectingLetters = true;
	}
	
	public void refreshStartingZone() {
		PlayerData curPlayer = cinematic.getCurrentPlayer();
		
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
		
		// TODO Met à jour l'état de la partie dans le cache et auprès du serveur
		cache.save();
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
	public boolean hasWall(CellActor cell1, CellActor cell2) {
		return arena.getData().hasWall(cell1.getData(), cell2.getData());
	}
	
	/**
	 * Passe au round suivant
	 */
	public void nextRound() {
		cinematic.nextRound();
		
		// Réinitialise l'arène
		resetArena();
		
		// TODO Met à jour l'état de la partie dans le cache et auprès du serveur
		cache.save();
		
		// Démarre le jeu
		changeState(GameStates.RUNNING);
	}

	@Override
	public void onRoundEnd(PlayerData roundWinner) {
		ui.setRoundWinner(roundWinner);

		changeState(GameStates.ROUND_OVER);
		
		// Cache les lettres de l'arène et interdit la sélection de cases
		arena.enableCellSelection(false);
		arena.showLetters(false);
	}
	
	@Override
	public void onGameEnd(PlayerData gameWinner) {
		// Affichage du gagnant
		ui.setGameWinner(gameWinner);
		
		changeState(GameStates.GAME_OVER);
		
		// Cache les lettres de l'arène et interdit la sélection de cases
		arena.enableCellSelection(false);
		arena.showLetters(false);
		
		// TODO Met à jour l'état de la partie dans le cache et auprès du serveur
		cache.save();
	}
	
	@Override
	public void onPlayerChange(PlayerData currentPlayer) {
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
	public void onEffectApplicationFinished(PlayerData player, List<CellActor> processedCells) {
		selectingLetters = false;
		
		// Toutes les cellules passent sous la domination du joueur
		arena.setCellsOwner(processedCells, player);
		
		// Le score du joueur est modifié
		ScoreHelper.onValidWord(player, processedCells);
		
		// Le joueur a joué un coup. C'est bon à savoir pour les stats
		// et pour autoriser ou non le refresh de la zone de départ
		player.nbWordsPlayed++;
		
		// Termine le tour de ce joueur
		cinematic.endMove();
		
		// TODO Met à jour l'état de la partie dans le cache et auprès du serveur
		cache.save();
		
		selectingLetters = true;
	}
	
	@Override
	public void onWordValidated(String word) {
		// TODO Arrête l'animation d'attente
		
		PlayerData player = cinematic.getCurrentPlayer();
		List<CellActor> selectedCells = wordSelectionHandler.getSelectedCells();
		
		// Affiche le mot joué
		ui.setInfo(Assets.i18nBundle.format("ui.arena.wordValidation.valid", player.name, word));
		ui.addPlayedWord(player, word);
		
		// Déclenche les effets sur les cellules
		cellEffectsManager.applyEffects(selectedCells, player, arena);
		
		// Bloquer la saisie pour empêcher que le joueur ne joue de nouveau pendant les animations
		arena.enableCellSelection(false);
		
		// Raz du mot sélectionné
		cancelWord();
	}

	@Override
	public void onWordAlreadyPlayed(String word) {
		// TODO Arrête l'animation d'attente
		
		// Informe l'utilisateur
		ui.setInfo(Assets.i18nBundle.format("ui.arena.wordValidation.alreadyPlayed", word));
		
		// Les boutons de validation doivent rester affichés
		ui.showWordValidationButtons(true);
	}

	@Override
	public void onWordUnknown(String word) {
		// TODO Arrête l'animation d'attente
		
		// Informe l'utilisateur
		ui.setInfo(Assets.i18nBundle.format("ui.arena.wordValidation.unknownWord", word));
		
		// Les boutons de validation doivent rester affichés
		ui.showWordValidationButtons(true);
	}

	@Override
	public void onWordValidationFailed(String word) {
		// TODO Arrête l'animation d'attente
		
		// Informe l'utilisateur
		ui.setInfo(Assets.i18nBundle.format("ui.arena.wordValidation.failure", word));
		
		// Les boutons de validation doivent rester affichés
		ui.showWordValidationButtons(true);
	}

	/**
	 * Retourne le joueur à la place indiquée, ou le joueur neutre
	 * si place == -1
	 * @param oldOwnerPlace
	 * @return
	 */
	public PlayerData getPlayer(int place) {
		if (PlayerData.isNeutral(place)) {
			return PlayerData.NEUTRAL;
		}
		return cache.getData().players.get(place);
	}
}
