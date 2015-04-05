package com.slamdunk.wordarena.server;

import com.badlogic.gdx.utils.JsonValue;

public interface ServerCallback {

	/**
	 * Appelée une fois que la réponse du serveur est reçue
	 * @param jsonResponse
	 */
	void onResponse(JsonValue jsonResponse);

	/**
	 * Appelée si une exception a été levée par le serveur
	 * ou avant d'appeler le serveur
	 * @param serverException
	 */
	void onException(ServerException serverException);

}
