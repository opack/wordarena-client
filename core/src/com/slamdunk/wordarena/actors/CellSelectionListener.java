package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.wordarena.screens.arena.WordSelectionHandler;

public class CellSelectionListener extends InputListener {
	private ArenaCell cell;
	private WordSelectionHandler wordSelectionHandler;
	private boolean enabled;
	
	public CellSelectionListener(ArenaCell cell, WordSelectionHandler wordSelectionHandler) {
		this.cell = cell;
		this.wordSelectionHandler = wordSelectionHandler;
		enabled = true;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		// Si le listener est désactivé, on ne récupère pas le touchUp
		return enabled;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (cell.getData().type.isSelectable()) {
			wordSelectionHandler.selectCell(cell);
		}
	}
}

