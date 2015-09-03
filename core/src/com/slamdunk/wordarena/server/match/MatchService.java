package com.slamdunk.wordarena.server.match;

import java.io.StringWriter;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.slamdunk.wordarena.server.ServerCallback;
import com.slamdunk.wordarena.server.ServerConnection;

public class MatchService {
	public void search(String player, ServerCallback callback) {
		// Crée un JSON avec les paramètre de la requête.
		StringWriter writer = new StringWriter();
		Json json = new Json(OutputType.json); // On veut du vrai JSON et pas du minimal
		json.setWriter(writer);
		
		json.writeObjectStart();
		json.writeValue("player", player);
		json.writeObjectEnd();

		// Demande la validation au serveur
		ServerConnection.sendCommand("MATCH_SEARCH", writer.toString(), callback);
	}
}
