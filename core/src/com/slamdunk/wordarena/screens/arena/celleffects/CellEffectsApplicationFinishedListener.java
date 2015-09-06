package com.slamdunk.wordarena.screens.arena.celleffects;

import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.game.PlayerData;

import java.util.List;

public interface CellEffectsApplicationFinishedListener {
	void onEffectApplicationFinished(PlayerData player, List<CellActor> processedCells);
}
