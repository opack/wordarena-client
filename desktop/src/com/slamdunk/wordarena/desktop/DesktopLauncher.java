package com.slamdunk.wordarena.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slamdunk.wordarena.WordArenaGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WordArenaGame.SCREEN_WIDTH;
		config.height = WordArenaGame.SCREEN_HEIGHT;
		new LwjglApplication(new WordArenaGame(), config);
	}
}
