package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.WordPlayed;
import com.slamdunk.wordarena.server.CallServerException;
import com.slamdunk.wordarena.server.ServerCallback;
import com.slamdunk.wordarena.server.lexis.LexisService;

import java.util.Date;
import java.util.List;

/**
 * Valide un mot à partir d'un lexique et en maintenant une liste de mots précédemment
 * validés
 */
public class WordValidator {
	public interface WordValidationListener {
		/**
		 * Appelée une fois la validation d'un mot effectuée
		 * avec succès, indiquant que le mot est valide
		 * @param word
		 */
		void onWordValidated(String word);
		
		/**
		 * Appelée une fois la validation d'un mot effectuée
		 * avec succès, mais que le mot a déjà été joué et
		 * est donc refusé
		 * @param word
		 */
		void onWordAlreadyPlayed(String word);
		
		/**
		 * Appelée une fois la validation d'un mot effectuée
		 * avec succès, mais que le mot n'est pas dans le
		 * lexique et est donc refusé
		 * @param word
		 */
		void onWordUnknown(String word);
		
		/**
		 * Appelée si la validation d'un mot a échoué, indiquant
		 * qu'il faut réessayer
		 * @param word
		 */
		void onWordValidationFailed(String word);
	}

	private List<WordPlayed> wordsPlayed;
	private WordValidationListener listener;
	
	public WordValidator(WordValidationListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Réinitialise la liste des mots joués à ce round
	 */
	public void clearAlreadyPlayedList() {
		wordsPlayed.clear();
	}
	
	/**
	 * Tente de valider le mot.
	 * @return true si le mot est valide
	 */
	public void validate(final String word, final int playerPlace) {
		// DBG
		listener.onWordValidated(word);
		// DBG
/*
		// Vérifie si le mot a déjà été joué
		if (isAlreadyPlayed(word)) {
			listener.onWordAlreadyPlayed(word);
		}
		// Si le mot n'a pas encore été joué, on vérifie
		// s'il existe bien dans le lexique
		else {
			LexisService lexisService = new LexisService();
			lexisService.validateWord(word, new ServerCallback() {
				public void onResponse(JsonValue response) {
					// Extrait la réponse
					if (!response.getBoolean("result")) {
						listener.onWordUnknown(word);
					}
					
					// Le mot est valide. Ajout à la liste des mots joués.
					else {
						WordPlayed wordPlayed = new WordPlayed();
						wordPlayed.time = new Date(); // TODO Trouver une technique pour qu'on utilise l'heure serveur. Le serveur doit-il créer cette instance ?
						wordPlayed.player = playerPlace;
						wordPlayed.wordId = word;
						wordsPlayed.add(wordPlayed);
						
						listener.onWordValidated(word);
					}
				}
	
				@Override
				public void onCallException(CallServerException serverException) {
					listener.onWordValidationFailed(word);
				}
			});
		}*/
	}

	/**
	 * Indique si le mot a déjà été joué
	 * @param word
	 * @return
	 */
	private boolean isAlreadyPlayed(String word) {
		for (WordPlayed wordPlayed : wordsPlayed) {
			if (wordPlayed.wordId.equals(word)) {
				return true;
			}
		}
		return false;
	}

	public void init(GameData game) {
		wordsPlayed = game.wordsPlayed;
	}
}
