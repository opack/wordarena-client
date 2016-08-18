package com.slamdunk.wordarena.screens.preeditor;

import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class PreEditorScreen extends SlamScreen {
	public static final String NAME = "PRE_EDITOR";

	public PreEditorScreen(WordArenaGame game) {
		super(game);

		PreEditorUI2 ui = new PreEditorUI2();
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

	public void create(String name, int width, int height) {
		EditorScreen editor = (EditorScreen)getGame().getScreen(EditorScreen.NAME);
		editor.createNewArena(name, width, height);
		getGame().setScreen(editor);
	}
	
	public void modify(String name) {
		EditorScreen editor = (EditorScreen)getGame().getScreen(EditorScreen.NAME);
		editor.editExistingArena(name);
		getGame().setScreen(editor);
	}
}
