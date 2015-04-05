package com.slamdunk.wordarena.screens.arena;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.wordarena.server.ServerCallback;
import com.slamdunk.wordarena.server.ServerException;
import com.slamdunk.wordarena.server.lexis.LexisService;

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
	
	private final Set<String> alreadyPlayed;
	private WordValidationListener listener;
	
	public WordValidator(WordValidationListener listener) {
		this.listener = listener;
		alreadyPlayed = new HashSet<String>();
	}
	
	/**
	 * Réinitialise la liste des mots joués à ce round
	 */
	public void clearAlreadyPlayedList() {
		alreadyPlayed.clear();
	}
	
	/**
	 * Tente de valider le mot. 
	 * @param selectedLetters
	 * @return true si le mot est valide
	 */
	public void validate(final String word) {
		// Vérifie si le mot a déjà été joué
		if (alreadyPlayed.contains(word)) {
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
						alreadyPlayed.add(word);
						
						listener.onWordValidated(word);
					}
				}
	
				@Override
				public void onException(ServerException serverException) {
					listener.onWordValidationFailed(word);
				}
			});
		}
	}
}
