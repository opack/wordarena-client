package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.slamdunk.toolkit.lang.MaxValueFinder;
import com.slamdunk.wordarena.data.arena.ArenaData;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.Player;

/**
 * Gère la cinématique du jeu et détermine qui est le prochain joueur à jouer
 */
public class MatchCinematic {
	public interface GameCinematicListener {
		/**
		 * Appelée lorsque le joueur courant change
		 * @param currentPlayer Player dont c'est à présent le tour
		 */
		void onPlayerChange(Player currentPlayer);
		
		/**
		 * Appelée lorsqu'un tour s'achève
		 */
		void onRoundEnd(Player roundWinner);
		
		/**
		 * Appelée lorsque la partie s'achève
		 */
		void onGameEnd(Player gameWinner);
	}
	
	private static final int TURNS_PER_ROUND = 5;
	private static final int WINNING_ROUNDS_PER_GAME = 2;
	
	/**
	 * Nombre de rounds maximum. Si on arrive à ce dernier round, on fait tout pour
	 * éviter une égalité : le gagnant est alors celui qui a le plus de zones, ou de
	 * cellules, ou de score.
	 */
	private static final int MAX_NB_ROUNDS_PER_GAME = 3;
	
	private List<Player> players;
	private ArenaData arenaData;
	
	private int firstPlayer;
	private int curPlayer;
	
	private int curTurn;
	private int nbTurnsPerRound;
	
	private int curRound;
	private int nbWinningRoundsPerGame;
	
	private boolean gameOver;
	
	private GameCinematicListener listener;
	
	public MatchCinematic() {
		nbTurnsPerRound = TURNS_PER_ROUND;
		nbWinningRoundsPerGame = WINNING_ROUNDS_PER_GAME;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}

	public void setArenaData(ArenaData arenaData) {
		this.arenaData = arenaData;
	}

	public void setListener(GameCinematicListener listener) {
		this.listener = listener;
	}


	public List<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
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
		curPlayer = playerIndex;
		
		if (listener != null) {
			listener.onPlayerChange(getCurrentPlayer());
		}
	}
	
	public int getFirstPlayer() {
		return firstPlayer;
	}

	public Player getCurrentPlayer() {
		return players.get(curPlayer);
	}
	
	/**
	 * Retourne le tour courant à afficher. Au premier tour
	 * cette méthode retourne donc 1 et pas 0. 
	 * @return
	 */
	public int getCurrentTurn() {
		return curTurn + 1;
	}
	
	/**
	 * Retourne le round courant à afficher. Au premier round
	 * cette méthode retourne donc 1 et pas 0. 
	 * @return
	 */
	public int getCurrentRound() {
		return curRound + 1;
	}
	

	public boolean isLastRound() {
		return curRound == MAX_NB_ROUNDS_PER_GAME - 1;
	}

	public void init(List<Player> playersList) {
		this.players = playersList;
		
		firstPlayer = 0;
		setCurrentPlayer(0);
		curTurn = 0;
		curRound = 0;
		
		gameOver = false;
	}
	
	public void init(GameData gameData) {
		this.players = gameData.players;
		
		firstPlayer = gameData.firstPlayer;
		setCurrentPlayer(gameData.curPlayer);
		curTurn = gameData.curTurn;
		curRound = gameData.curRound;
		
		gameOver = gameData.gameOver;
		
		setArenaData(gameData.arena);
	}
	
	/**
	 * Termine le coup du joueur actuel et passe au joueur suivant
	 */
	public void endMove() {
		// Le joueur suivant est celui juste après
		curPlayer = (curPlayer + 1) % players.size();
		
		// Si tout le monde a joué, on a fini un tour
		if (curPlayer == firstPlayer) {
			endTurn();
		}
		
		// Affiche le prochain joueur à jouer
		setCurrentPlayer(curPlayer);
	}
	
	/**
	 * Termine le tour, c'est-à-dire que chaque joueur a joué
	 * son coup.
	 */
	private void endTurn() {
		curTurn ++;
		if (curTurn == TURNS_PER_ROUND) {
			endRound();
		}
	}
	
	/**
	 * Termine le round actuel. Cette action peut avoir pour conséquence de terminer le jeu.
	 * @param roundWinner
	 */
	public void endRound() {
		// Détermine le gagnant du round
		Player winner = computeRoundWinner();
		
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
	private Player computeRoundWinner() {
		MaxValueFinder<Player> maxValueFinder = new MaxValueFinder<Player>();
		maxValueFinder.setIgnoredValue(Player.NEUTRAL);
		
		// Recherche le joueur ayant le plus de zones
		for (ZoneData zone : arenaData.zones) {
			maxValueFinder.addValue(zone.owner);
		}
		Player winner = maxValueFinder.getMaxValue();
		
		// Si on arrive à ce dernier round, on fait tout pour
		// éviter une égalité : le gagnant est alors celui qui a le plus de zones, ou de
		// cellules, ou de score.
		if (winner == null
		&& isLastRound()) {
			// S'il y a égalité, le gagnant est celui occupant le plus de terrain,
			// en tenant compte de la puissance de chaque cellule
			maxValueFinder.reset();
			CellData cellData;
			for (int y = 0; y < arenaData.height; y++) {
				for (int x = 0; x < arenaData.width; x++) {
					cellData = arenaData.cells[x][y];
					maxValueFinder.addValue(cellData.owner, cellData.power);
				}
			}
			winner = maxValueFinder.getMaxValue();
			
			// Si on entre ici, c'est qu'on a une égalité parfaite : même nombre de zones
			// et même nombre de cases occupées. On départage les joueurs au score.
			if (winner == null) {
				maxValueFinder.reset();
				for (Player player : getPlayers()) {
					maxValueFinder.addValue(player, player.score);
				}
				winner = maxValueFinder.getMaxValue();
			}
		}
		
		// Toujours pas de gagnant ? On a une égalité parfaite (très improbable)
		if (winner == null) {
			return null;
		}
		
		// Ajoute 1 round au joueur gagnant
		winner.nbRoundsWon++;
		return winner;
	}
	
	/**
	 *Teste si la fFin de la parti est arrivéee
	 * @param roundWinner
	 */
	public boolean endGame(Player roundWinner) {
		Player gameWinner = null;
		
		if (roundWinner != null
		&& roundWinner.nbRoundsWon >= nbWinningRoundsPerGame) {
			
			// Fin de partie car le gagnant du round a gagné assez de rounds
			gameWinner = roundWinner;
			
		} else if (curRound >= MAX_NB_ROUNDS_PER_GAME - 1) {
			
			// Fin de partie car on a joué le nombre max de rounds. Il faut
			// donc déterminer le gagnant en comptant le nombre de rounds
			// que chacun a gagné.
			MaxValueFinder<Player> maxValueFinder = new MaxValueFinder<Player>();
			for (Player player : players) {
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
		gameOver = true;
		return true;
	}
	
	/**
	 * Passe au round suivant
	 */
	public void nextRound() {
		// On passe au round suivant s'il n'y a pas de vainqueur
		curRound ++;
		// On commence le premier tour de jeu
		curTurn = 0;
		
		// Le joueur qui débute le nouveau round n'est pas le même que celui
		// du round précédent.
		firstPlayer = (firstPlayer + 1) % players.size();
		setCurrentPlayer(firstPlayer);
	}
}
