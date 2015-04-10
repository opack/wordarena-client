package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.CellActor;

public class PowerTool extends EditorTool<Integer> {
	public PowerTool() {
		setValue(1);
	}

	@Override
	public void apply(CellActor cell) {
		if (!cell.getData().type.hasPower()) {
			return;
		}
		cell.getData().power = getValue();
	}

	@Override
	public void apply(Collection<CellActor> cells) {
		for (CellActor cell : cells) {
			apply(cell);
		}
	}
}
