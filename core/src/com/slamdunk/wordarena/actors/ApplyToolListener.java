package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.editor.tools.EditorTool;

public class ApplyToolListener extends InputListener {
	private CellActor cell;
	private EditorScreen screen;
	
	public ApplyToolListener(EditorScreen screen, CellActor cell) {
		this.screen = screen;
		this.cell = cell;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		return true;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		@SuppressWarnings("rawtypes")
		EditorTool tool = screen.getCurrentTool();
		if (tool == null) {
			return;
		}
		tool.apply(cell);
		cell.updateDisplay();
	}
}

