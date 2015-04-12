package com.slamdunk.wordarena.data.game;

public class GameCinematic {
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
	 * Indique si la partie est terminée
	 */
	public boolean gameOver;
}
