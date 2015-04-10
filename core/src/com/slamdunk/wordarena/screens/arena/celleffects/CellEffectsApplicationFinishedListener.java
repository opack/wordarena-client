package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.List;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.game.Player;

public interface CellEffectsApplicationFinishedListener {
	void onEffectApplicationFinished(Player player, List<CellActor> processedCells);
}
