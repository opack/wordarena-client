package com.slamdunk.wordarena.server.lexis;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.slamdunk.wordarena.server.ServerCallback;
import com.slamdunk.wordarena.server.ServerConnection;

import java.io.StringWriter;

public class LexisService {
	public void validateWord(String word, ServerCallback callback) {
		// Crée un JSON avec les paramètre de la requête.
		StringWriter writer = new StringWriter();
		Json json = new Json(OutputType.json); // On veut du vrai JSON et pas du minimal
		json.setWriter(writer);
		
		json.writeObjectStart();
		json.writeValue("lang", "FR");
		json.writeValue("word", word);
		json.writeObjectEnd();

		// Demande la validation au serveur
		ServerConnection.sendCommand("LEXIS_VALIDATE", writer.toString(), callback);
	}
}
