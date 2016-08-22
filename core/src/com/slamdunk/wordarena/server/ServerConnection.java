package com.slamdunk.wordarena.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.assets.Assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ServerConnection {
	
	/**
	 * Lance un thread qui se connecte au serveur et lui envoie une commande.
	 * @param command
	 * @param parameters
	 * @param callback
	 */
	public static void sendCommand(final String command, final String parameters, final ServerCallback callback) {
		new Thread() {
			@Override
			public void run() {
				try {
					
					// Envoie la commande au serveur
					JsonValue jsonResponse = callServer(command, parameters);
					
					// Donne la réponse au callback
					if (callback != null) {
						callback.onResponse(jsonResponse);
					}
					
				} catch (CallServerException e) {
					
					// Si une exception s'est produite lors de l'appel, on l'envoie au callback
					if (callback != null) {
						callback.onCallException(e);
					}
				}
			}
			
			/**
			 * Envoie la commande spécifiée au serveur et retourne, le cas échéant, sa réponse
			 * @param command
			 * @param parameters
			 * @return
			 * @throws CallServerException
			 */
			private JsonValue callServer(String command, String parameters) throws CallServerException {
				// Ouvre une connexion vers le serveur
				String serverAddress = Assets.appProperties.getProperty("server.address", "");
				int serverPort = Assets.appProperties.getIntegerProperty("server.port", 1601);
				Socket socket = Gdx.net.newClientSocket(Protocol.TCP, serverAddress, serverPort, null);
				
				try {
					// Envoie la requête au serveur
					PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
					writer.println("{\"command\":\"" + command + "\",\"parameters\":" + parameters + "}");
					writer.flush();
					
					// Récupère la réponse
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					String response = reader.readLine();
					
					// Transforme la réponse en objet Json
					return new JsonReader().parse(response);
					
				} catch (UnsupportedEncodingException e) {
					
					Gdx.app.log(WordArenaGame.LOG_TAG, "ERROR : Unsupported encoding UTF-8 : " + e.getMessage());
					throw new CallServerException(e);
					
				} catch (IOException e) {
					
					Gdx.app.log(WordArenaGame.LOG_TAG, "ERROR : Error while connecting to server : " + e.getMessage());
					throw new CallServerException(e);
					
				} finally {
					
					// Ferme la connexion
					socket.dispose();
					
				}
			}
			
		}.start();
	}
}
