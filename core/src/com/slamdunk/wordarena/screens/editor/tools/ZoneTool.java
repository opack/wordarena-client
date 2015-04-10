package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.actors.ZoneActor;

public class ZoneTool extends EditorTool<ZoneActor> {
	@Override
	public void apply(CellActor cell) {
		// Retire la cellule de l'ancienne zone
		ZoneActor oldZone = cell.getZone();
		if (oldZone != null) {
			oldZone.removeCell(cell);
			oldZone.update();
		}
		
		// Ajoute la cellule à la nouvelle zone
		ZoneActor zone = getValue();
		zone.addCell(cell);
		zone.update();
	}

	@Override
	public void apply(Collection<CellActor> cells) {
		for (CellActor cell : cells) {
			apply(cell);
		}
	}
}
