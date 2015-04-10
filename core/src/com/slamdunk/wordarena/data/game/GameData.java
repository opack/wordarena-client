package com.slamdunk.wordarena.data.game;

import java.util.Date;
import java.util.List;

import com.slamdunk.wordarena.data.arena.ArenaData;
import com.slamdunk.wordarena.enums.GameTypes;
import com.slamdunk.wordarena.enums.Objectives;

/**
 * Regroupe les données d'une partie en cours
 */
public class GameData {
	
	/**
	 * Identifiant unique de la partie
	 */
	public int id;
	
	/**
	 * Date/heure de création de la partie
	 */
	public Date createTime;
	
	/**
	 * Date/heure de début du partie.
	 * Si null, alors la partie n'a pas encore débuté et des joueurs peuvent la rejoindre.
	 */
	public Date startTime;
	
	/**
	 * Date où la partie prend fin (dépend du temps de jeu choisi et de la date du dernier coup)
	 */
	public Date endTime;
	
	/**
	 * Le type de la partie
	 */
	public GameTypes gameType;

	/**
	 * L'objectif de la partie, càd les règles appliquées à la partie
	 */
	public Objectives objective;
	
	/**
	 * Round courant
	 */
	public int curRound;
	
	/**
	 * Tour courant
	 */
	public int curTurn;
	
	/**
	 * Joueur dont c'est le tour. Correspond à un indice du tableau players
	 */
	public int curPlayer;
	
	/**
	 * Joueur ayant débuté ce round. Correspond à un indice du tableau players
	 */
	public int firstPlayer;
	
	/**
	 * Liste des joueurs participant à la partie, dans l'ordre de jeu
	 */
	public List<Player> players;
	
	/**
	 * Indique si la partie est terminée
	 */
	public boolean gameOver;
	
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
}
