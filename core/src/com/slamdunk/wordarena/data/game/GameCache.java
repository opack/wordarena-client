package com.slamdunk.wordarena.data.game;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
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

	public void create(String gameId) {
		data = GameData.create();
		data._id = gameId;
		save();
	}
	
	public void create(GameData game) {
		data = game;
		save();
	}
	
	public void save() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		json.setSerializer(ArenaData.class, new ArenaSerializer());
		final String serialized = json.prettyPrint(data);
		
		file = Gdx.files.local("cache/" + data._id + ".json");
		file.writeString(serialized, false, "UTF-8");
	}
	
	/**
	 * Charge le fichier JSON du cache correspondant à cette partie
	 * @param gameId
	 * @return true si le fichier existe et a été chargé,
	 * false s'il n'existait pas
	 */
	public boolean load(int gameId) {
		file = Gdx.files.local("cache/" + gameId + ".json");
		if (!file.exists()) {
			return false;
		}
		Json json = new Json();
		json.setSerializer(ArenaData.class, new ArenaSerializer());
		// TODO Charger d'abord tout ce qui n'est pas l'arène, puis l'arène (car elle a besoin de la liste de joueurs)
		data = json.fromJson(GameData.class, file);
		return true;
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
