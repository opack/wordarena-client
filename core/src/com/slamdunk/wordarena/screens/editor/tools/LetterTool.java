package com.slamdunk.wordarena.screens.editor.tools;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

import java.util.Collection;

public class LetterTool extends EditorTool<Letters> {
	public LetterTool() {
		setValue(Letters.FROM_TYPE);
	}

	@Override
	public void apply(CellActor cell) {
		CellTypes type = cell.getData().type;
		
		// On ne fait rien pour les cellules d'un type
		// n'affichant pas de lettre ou pour le joker
		// (dont la lettre sera toujours "?")
		if (!type.hasLetter()
		|| type == CellTypes.J) {
			return;
		}
		cell.getData().letter = getValue();
		cell.getData().planLetter = getValue().label;
	}

	@Override
	public void apply(Collection<CellActor> cells) {
		for (CellActor cell : cells) {
			apply(cell);
		}
	}
}
