package com.slamdunk.wordarena.data.game;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.slamdunk.wordarena.data.arena.ArenaData;
import com.slamdunk.wordarena.data.arena.ArenaSerializer;

/**
 * Gère le cache d'une partie
 */
public class GameCache {
	private FileHandle file;
	private GameData data;
	
	public GameData getData() {
		return data;
	}

	public void setData(GameData data) {
		this.data = data;
	}

	public void create(int gameId) {
		data = new GameData();
		data.id = gameId;
		save();
	}
	
	public void save() {
		Json json = new Json();
		json.setSerializer(ArenaData.class, new ArenaSerializer());
		final String serialized = json.prettyPrint(data);
		
		file = Gdx.files.local("cache/" + data.id + ".json");
		file.writeString(serialized, false, "UTF-8");
	}
	
	/**
	 * Charge le fichier JSON du cache correspondant à cette partie
	 * ou en crée un nouveau s'il n'y en a pas
	 * @param gameId
	 * @return true si le fichier existe et a été chargé,
	 * false s'il n'existait pas et a été créé
	 */
	public boolean loadOrCreate(int gameId) {
		file = Gdx.files.local("cache/" + gameId + ".json");
		if (file.exists()) {
			Json json = new Json();
			data = json.fromJson(GameData.class, file);
			return true;
		} else {
			create(gameId);
			return false;
		}
	}
	
	/**
	 * Ajoute un message dans le cache
	 * @param time
	 * @param playerId
	 * @param message
	 */
	public void addChat(Date time, String playerId, String message) {
		
	}
	
}
