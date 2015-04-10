package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.game.Player;
import com.slamdunk.wordarena.enums.CellStates;

public class OwnerTool extends EditorTool<Player> {
	
	public OwnerTool() {
		setValue(Player.NEUTRAL);
	}

	@Override
	public void apply(CellActor cell) {
		CellData data = cell.getData();
		if (!data.type.canBeOwned()) {
			return;
		}
		data.owner = getValue();
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
