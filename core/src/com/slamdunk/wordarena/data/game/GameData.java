package com.slamdunk.wordarena.data.game;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.data.arena.ArenaData;

/**
 * Regroupe les données d'une partie en cours
 */
public class GameData {
	
	/**
	 * Entête de la partie, contenant des infos générales
	 */
	public GameHeader header;
	
	/**
	 * Cinématique de la partie (round courant, joueur dont c'est le tour...)
	 */
	public GameCinematic cinematic;
	
	/**
	 * Liste des joueurs participant à la partie, dans l'ordre de jeu
	 */
	public List<PlayerData> players;
	
	/**
	 * JSON de l'arène au moment où la partie a été sauvegardée
	 */
	public ArenaData arena;
	
	/**
	 * Liste des mots déjà joués au cours de cette partie
	 */
	public List<WordPlayed> wordsPlayed;
	
	/**
	 * Dernier coup joué
	 */
	public GameMove lastMove;
	
	/**
	 * Messages échangés entre les joueurs
	 */
	public List<GameChat> chats;

	/**
	 * Instancie un GameData avec tous les objets permettant de le manipuler sans
	 * risque de NullPointerException (cinematic, wordsPlayed, lastMove et chats).
	 * Ces créations ne sont pas faites dans le constructeur afin d'éviter de les
	 * faire une fois de trop lors du chargement de données depuis un fichier JSON.
	 */
	public static GameData create() {
		GameData gameData = new GameData();
		gameData.header = new GameHeader();
		gameData.cinematic = new GameCinematic();
		gameData.players = new ArrayList<PlayerData>();
		// Le champ arena est toujours construit dynamiquement grâce à un builder ou au chargement JSON
		gameData.wordsPlayed = new ArrayList<WordPlayed>();
		gameData.lastMove = new GameMove();
		gameData.chats = new ArrayList<GameChat>();
		return gameData;
	}
}
