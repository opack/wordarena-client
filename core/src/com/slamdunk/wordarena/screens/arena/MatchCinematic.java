package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.slamdunk.toolkit.lang.MaxValueFinder;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.PlayerData;

/**
 * Gère la cinématique du jeu et détermine qui est le prochain joueur à jouer
 */
public class MatchCinematic {
	public interface GameCinematicListener {
		/**
		 * Appelée lorsque le joueur courant change
		 * @param currentPlayer Player dont c'est à présent le tour
		 */
		void onPlayerChange(PlayerData currentPlayer);
		
		/**
		 * Appelée lorsqu'un tour s'achève
		 */
		void onRoundEnd(PlayerData roundWinner);
		
		/**
		 * Appelée lorsque la partie s'achève
		 */
		void onGameEnd(PlayerData gameWinner);
	}
	
	private static final int TURNS_PER_ROUND = 5;
	private static final int WINNING_ROUNDS_PER_GAME = 2;
	
	/**
	 * Nombre de rounds maximum. Si on arrive à ce dernier round, on fait tout pour
	 * éviter une égalité : le gagnant est alors celui qui a le plus de zones, ou de
	 * cellules, ou de score.
	 */
	private static final int MAX_NB_ROUNDS_PER_GAME = 3;
	
	private GameData gameData;
	
	private int nbTurnsPerRound;
	private int nbWinningRoundsPerGame;
	
	private GameCinematicListener listener;
	
	public MatchCinematic() {
		nbTurnsPerRound = TURNS_PER_ROUND;
		nbWinningRoundsPerGame = WINNING_ROUNDS_PER_GAME;
	}
	
	public boolean isGameOver() {
		return gameData.cinematic.gameOver;
	}

	public void setListener(GameCinematicListener listener) {
		this.listener = listener;
	}


	public List<PlayerData> getPlayers() {
		return gameData.players;
	}
	
	public void setPlayers(List<PlayerData> players) {
		gameData.players = players;
	}

	public int getNbTurnsPerRound() {
		return nbTurnsPerRound;
	}

	public void setNbTurnsPerRound(int nbTurnsPerRound) {
		this.nbTurnsPerRound = nbTurnsPerRound;
	}

	public int getNbWinningRoundsPerGame() {
		return nbWinningRoundsPerGame;
	}

	public void setNbWinningRoundsPerGame(int nbWinningRoundsPerGame) {
		this.nbWinningRoundsPerGame = nbWinningRoundsPerGame;
	}

	public void setCurrentPlayer(int playerIndex) {
		gameData.cinematic.curPlayer = playerIndex;
		
		if (listener != null) {
			listener.onPlayerChange(getCurrentPlayer());
		}
	}
	
	public int getFirstPlayer() {
		return gameData.cinematic.firstPlayer;
	}

	public PlayerData getCurrentPlayer() {
		return gameData.players.get(gameData.cinematic.curPlayer);
	}
	
	/**
	 * Retourne le tour courant à afficher. Au premier tour
	 * cette méthode retourne donc 1 et pas 0. 
	 * @return
	 */
	public int getCurrentTurn() {
		return gameData.cinematic.curTurn + 1;
	}
	
	/**
	 * Retourne le round courant à afficher. Au premier round
	 * cette méthode retourne donc 1 et pas 0. 
	 * @return
	 */
	public int getCurrentRound() {
		return gameData.cinematic.curRound + 1;
	}
	

	public boolean isLastRound() {
		return gameData.cinematic.curRound == MAX_NB_ROUNDS_PER_GAME - 1;
	}

	public void init(GameData gameData) {
		this.gameData = gameData;
		
		// Affiche le prochain joueur à jouer
		setCurrentPlayer(gameData.cinematic.curPlayer);
	}
	
	/**
	 * Termine le coup du joueur actuel et passe au joueur suivant
	 */
	public void endMove() {
		// Le joueur suivant est celui juste après
		gameData.cinematic.curPlayer = (gameData.cinematic.curPlayer + 1) % gameData.players.size();
		
		// Si tout le monde a joué, on a fini un tour
		if (gameData.cinematic.curPlayer == gameData.cinematic.firstPlayer) {
			endTurn();
		}
		
		// Affiche le prochain joueur à jouer
		setCurrentPlayer(gameData.cinematic.curPlayer);
	}
	
	/**
	 * Termine le tour, c'est-à-dire que chaque joueur a joué
	 * son coup.
	 */
	private void endTurn() {
		gameData.cinematic.curTurn ++;
		if (gameData.cinematic.curTurn == TURNS_PER_ROUND) {
			endRound();
		}
	}
	
	/**
	 * Termine le round actuel. Cette action peut avoir pour conséquence de terminer le jeu.
	 * @param roundWinner
	 */
	public void endRound() {
		// Détermine le gagnant du round
		PlayerData winner = computeRoundWinner();
		
		// Teste si le jeu est fini
		if (endGame(winner)) {
			return;
		}
		
		// Fin de round
		if (listener != null) {
			listener.onRoundEnd(winner);
		}
	}
	
	/**
	 * Détermine le gagnant du round
	 * @return
	 */
	private PlayerData computeRoundWinner() {
		MaxValueFinder<Integer> maxValueFinder = new MaxValueFinder<Integer>();
		maxValueFinder.setIgnoredValue(PlayerData.NEUTRAL.place);
		
		// Recherche le joueur ayant le plus de zones
		for (ZoneData zone : gameData.arena.zones) {
			maxValueFinder.addValue(zone.ownerPlace);
		}
		Integer winner = maxValueFinder.getMaxValue();
		
		// Si on arrive à ce dernier round, on fait tout pour
		// éviter une égalité : le gagnant est alors celui qui a le plus de zones, ou de
		// cellules, ou de score.
		if (winner == null
		&& isLastRound()) {
			// S'il y a égalité, le gagnant est celui occupant le plus de terrain,
			// en tenant compte de la puissance de chaque cellule
			maxValueFinder.reset();
			CellData cellData;
			for (int y = 0; y < gameData.arena.height; y++) {
				for (int x = 0; x < gameData.arena.width; x++) {
					cellData = gameData.arena.cells[x][y];
					maxValueFinder.addValue(cellData.ownerPlace, cellData.power);
				}
			}
			winner = maxValueFinder.getMaxValue();
			
			// Si on entre ici, c'est qu'on a une égalité parfaite : même nombre de zones
			// et même nombre de cases occupées. On départage les joueurs au score.
			if (winner == null) {
				maxValueFinder.reset();
				for (PlayerData player : getPlayers()) {
					maxValueFinder.addValue(player.place, player.score);
				}
				winner = maxValueFinder.getMaxValue();
			}
		}
		
		// Toujours pas de gagnant ? On a une égalité parfaite (très improbable)
		if (winner == null) {
			return null;
		}
		
		// Ajoute 1 round au joueur gagnant
		PlayerData winnerPlayer = gameData.players.get(winner);
		winnerPlayer.nbRoundsWon++;
		return winnerPlayer;
	}
	
	/**
	 *Teste si la fFin de la parti est arrivéee
	 * @param roundWinner
	 */
	public boolean endGame(PlayerData roundWinner) {
		PlayerData gameWinner = null;
		
		if (roundWinner != null
		&& roundWinner.nbRoundsWon >= nbWinningRoundsPerGame) {
			
			// Fin de partie car le gagnant du round a gagné assez de rounds
			gameWinner = roundWinner;
			
		} else if (gameData.cinematic.curRound >= MAX_NB_ROUNDS_PER_GAME - 1) {
			
			// Fin de partie car on a joué le nombre max de rounds. Il faut
			// donc déterminer le gagnant en comptant le nombre de rounds
			// que chacun a gagné.
			MaxValueFinder<PlayerData> maxValueFinder = new MaxValueFinder<PlayerData>();
			for (PlayerData player : gameData.players) {
				maxValueFinder.addValue(player, player.nbRoundsWon);
			}
			
			// L'un des deux joueurs a gagné plus de rounds que l'autre :
			// il est déclaré vainqueur de la partie
			gameWinner = maxValueFinder.getMaxValue();
			
			// Si gameWinner == null, c'est qu'on est dans le cas où
			// on a fait 3 rounds et que les 2 joueurs ont gagné autant de
			// rounds l'un que l'autre. On a donc une égalité
		} else {
			// La partie n'est pas terminée
			return false;
		}

		// La partie est terminée
		if (listener != null) {
			listener.onGameEnd(gameWinner);
		}
		gameData.cinematic.gameOver = true;
		return true;
	}
	
	/**
	 * Passe au round suivant
	 */
	public void nextRound() {
		// On passe au round suivant s'il n'y a pas de vainqueur
		gameData.cinematic.curRound ++;
		// On commence le premier tour de jeu
		gameData.cinematic.curTurn = 0;
		
		// Le joueur qui débute le nouveau round n'est pas le même que celui
		// du round précédent.
		gameData.cinematic.firstPlayer = (gameData.cinematic.firstPlayer + 1) % gameData.players.size();
		setCurrentPlayer(gameData.cinematic.firstPlayer);
	}
}
