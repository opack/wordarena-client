package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.game.PlayerData;

/**
 * Classe utilitaire simplifiant le calcul des scores
 * @author didem93n
 *
 */
public class ScoreHelper {
	private static final int BONUS1_MIN_LENGTH = 5;
	private static final int BONUS1_POINTS = 2;
	private static final int BONUS2_MIN_LENGTH = 8;
	private static final int BONUS2_POINTS = 3;
	private static final int BONUS3_MIN_LENGTH = 10;
	private static final int BONUS3_POINTS = 5;
	
	private static final int SCORE_ZONE_STEALED = 5;
	private static final int SCORE_ZONE_GAINED = 3;
	
	private static final int MALUS_REFRESH_STARTING_ZONE = 5;
	
	/**
	 * Donne le score pour ce mot
	 * @param list
	 * @return
	 */
	public static void onValidWord(PlayerData player, List<CellActor> cells) {
		// Mot validé : 1pt * cell.power
		int score = 0;
		for (CellActor cell : cells) {
			score += cell.getData().power;
		}
		
		final int wordLength = cells.size();
		// Bonus "Grandiose"  (10+ lettres)
		if (wordLength >= BONUS3_MIN_LENGTH) {
			score += BONUS3_POINTS;
		}
		//Bonus "Sensationnel" (8-9 lettres)
		else if (wordLength >= BONUS2_MIN_LENGTH) {
			score += BONUS2_POINTS;
		}
		// Bonus "Extra" (5-7 lettres)
		else if (wordLength >= BONUS1_MIN_LENGTH) {
			score += BONUS1_POINTS;
		}

		player.score += score;
	}

	/**
	 * Met à jour le score du joueur newOwner suite à la prise d'une zone.
	 * @param newOwner Le joueur qui vient de prendre une zone
	 * @param oldOwner Le joueur qui vient de perdre la zone. null si la zone
	 * n'appartenait à personne.
	 */
	public static void onZoneConquest(PlayerData newOwner, PlayerData oldOwner) {
		// Si la zone appartenait à un adversaire, le joueur
		// gagne plus de points
		if (oldOwner != null
		&& !oldOwner.isNeutral()) {
			newOwner.score += SCORE_ZONE_STEALED;
		} else {
			newOwner.score += SCORE_ZONE_GAINED;
		}
	}

	public static void onRefreshStartingZone(PlayerData player) {
		player.score -= MALUS_REFRESH_STARTING_ZONE;
	}
}
