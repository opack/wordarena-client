package com.slamdunk.wordarena.screens.options;

import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class OptionsScreen extends SlamScreen {
	public static final String NAME = "OPTIONS";

	public OptionsScreen(WordArenaGame game) {
		super(game);

		OptionsUI2 ui = new OptionsUI2();
		addOverlay(ui);
		ui.loadScenes();
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			getGame().setScreen(HomeScreen.NAME);
		}
	    return false;
	}
}
