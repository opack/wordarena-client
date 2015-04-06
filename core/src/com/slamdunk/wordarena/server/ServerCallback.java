package com.slamdunk.wordarena.server;

import com.badlogic.gdx.utils.JsonValue;

public interface ServerCallback {

	/**
	 * Appelée une fois que la réponse du serveur est reçue
	 * @param jsonResponse
	 */
	void onResponse(JsonValue jsonResponse);

	/**
	 * Appelée si une exception a été levée lors de l'appel
	 * au serveur et que la commande n'a donc pas pû être
	 * effectuée
	 * @param serverException
	 */
	void onCallException(CallServerException serverException);

}
