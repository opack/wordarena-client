package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.List;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.game.PlayerData;

public interface CellEffectsApplicationFinishedListener {
	void onEffectApplicationFinished(PlayerData player, List<CellActor> processedCells);
}
