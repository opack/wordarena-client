package com.slamdunk.wordarena.data.game;

import com.slamdunk.wordarena.enums.GameTypes;
import com.slamdunk.wordarena.enums.Objectives;

import java.util.Date;

public class GameHeader {
	/**
	 * Le type de la partie
	 */
	public GameTypes gameType;

	/**
	 * L'objectif de la partie, càd les règles appliquées à la partie
	 */
	public Objectives objective;
	
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
	 * Date/heure de la dernière mise à jour de la partie
	 */
	public Date lastUpdateTime;
}
