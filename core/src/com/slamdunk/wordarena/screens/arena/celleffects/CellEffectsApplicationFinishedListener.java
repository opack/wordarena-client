package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.List;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.Player;

public interface CellEffectsApplicationFinishedListener {
	void onEffectApplicationFinished(Player player, List<ArenaCell> processedCells);
}
