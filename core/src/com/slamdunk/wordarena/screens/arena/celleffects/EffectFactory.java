package com.slamdunk.wordarena.screens.arena.celleffects;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellEffects;

public class EffectFactory {

	public static CellEffect create(CellEffects effect, CellEffectsManager manager, ArenaCell cell) {
		switch (effect) {
		case BOMB_EXPLOSION:
			return new BombExplosionEffect(manager, cell);
		case BREAK_NEIGHBOR_GLASS:
			return new BreakNeighborGlassEffect(manager, cell);
		case TAKE_OWNERSHIP:
			return new TakeOwnershipEffect(manager, cell);
		default:
			System.err.println("Unkown effet " + effect + " : nothing will be created");
			return null;
		}
	}

}
