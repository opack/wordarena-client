package com.slamdunk.wordarena.screens.arena.celleffects;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellEffects;

/**
 * Notifie le manager que tous les effets ont été appliqués sur cette cellule.
 */
public class NotifyEndEffectApply extends CellEffect {

	public NotifyEndEffectApply(CellEffectsManager manager, ArenaCell cell) {
		super(manager, cell);
	}

	@Override
	public CellEffects getEffect() {
		return CellEffects.NOTIFY_END_EFFECT_APPLY;
	}

	@Override
	public boolean act(float delta) {
		getManager().notifyEndEffectApplication(getCell());
		return true;
	}

}
