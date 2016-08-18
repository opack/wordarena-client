package com.slamdunk.wordarena.screens.editor.tools;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

import java.util.Collection;

public class CellTypeTool extends EditorTool<CellTypes> {
	public static final String NAME = CellTypeTool.class.getName();
	
	public CellTypeTool() {
		setValue(CellTypes.L);
	}

	@Override
	public void apply(CellActor cell) {
		CellData cellData = cell.getData();
		CellTypes value = getValue();
		if (cellData.type == value) {
			return;
		}
		// Affecte le type de la cellule
		cellData.type = value;
		
		// Choix de la lettre
		cellData.planLetter = Letters.FROM_TYPE.label;
		cellData.letter = value.hasLetter() ? Letters.FROM_TYPE : Letters.EMPTY;
		
		
		// Choix d'une puissance
		cellData.power = value.hasPower() ? 1 : 0;
		
		if (!value.canBeOwned()) {
			cellData.ownerPlace = PlayerData.NEUTRAL.place;
		}
	}

	@Override
	public void apply(Collection<CellActor> cells) {
		for (CellActor cell : cells) {
			apply(cell);
		}
	}
}
