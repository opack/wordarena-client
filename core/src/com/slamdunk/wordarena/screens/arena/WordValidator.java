package com.slamdunk.wordarena.screens.arena;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.slamdunk.wordarena.enums.ReturnCodes;

/**
 * Valide un mot à partir d'un lexique et en maintenant une liste de mots précédemment
 * validés
 */
public class WordValidator {
	private static final int MIN_WORD_LENGTH = 2;
	
	private final Set<String> lexis;
	private final Set<String> alreadyPlayed;
	private String lastValidatedWord;
	
	public WordValidator() {

		lexis = new HashSet<String>();
		alreadyPlayed = new HashSet<String>();
		loadWords();
	}
	
	/**
	 * Charge les mots du lexique
	 */
	private void loadWords() {
		lexis.clear();
		FileHandle file = Gdx.files.internal("words.txt");
		BufferedReader reader = new BufferedReader(file.reader("UTF-8"));
		String extracted = null;
		try {
			while ((extracted = reader.readLine()) != null) {
				if (extracted.length() >= MIN_WORD_LENGTH) {
					lexis.add(extracted);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public ReturnCodes validate(String word) {
		lastValidatedWord = word;
		
		// Vérifie si le mot est valide
		ReturnCodes result = ReturnCodes.OK;
		if (!lexis.contains(lastValidatedWord)) {
			result = ReturnCodes.UNKNOWN_WORD;
		}
		
		if (alreadyPlayed.contains(lastValidatedWord)) {
			result = ReturnCodes.WORD_ALREADY_PLAYED;
		}
		
		// Le mot est valide. Ajout à la liste des mots joués.
		if (result == ReturnCodes.OK) {
			alreadyPlayed.add(lastValidatedWord);
		}
		return result;
	}
	
	/**
	 * Retourne le dernier mot dont on a tenté la validation
	 * au moyen de la méthode {@link #validate()}.
	 * @return
	 */
	public String getLastValidatedWord() {
		return lastValidatedWord;
	}
}
