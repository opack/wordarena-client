package com.slamdunk.wordarena.screens.editor.tools;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellStates;

import java.util.Collection;

public class OwnerTool extends EditorTool<PlayerData> {
	
	public OwnerTool() {
		setValue(PlayerData.NEUTRAL);
	}

	@Override
	public void apply(CellActor cell) {
		CellData data = cell.getData();
		if (!data.type.canBeOwned()) {
			return;
		}
		data.ownerPlace = getValue().place;
		data.state = CellStates.OWNED;
		
		// Met à jour l'owner de la zone
		cell.getZone().updateOwner();
	}

	@Override
	public void apply(Collection<CellActor> cells) {
		for (CellActor cell : cells) {
			apply(cell);
		}
	}
}
