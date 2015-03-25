package com.slamdunk.wordarena.screens.arena.celleffects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellEffects;

/**
 * Correspond à l'effet qui se produit lorsqu'une lettre sélectionnée est
 * validée.
 */
public abstract class CellEffect extends Action{
	private CellEffectsManager manager;
	private ArenaCell cell;
	
	public CellEffect(CellEffectsManager manager, ArenaCell cell) {
		this.manager = manager;
		this.cell = cell;
	}

	public CellEffectsManager getManager() {
		return manager;
	}

	public void setManager(CellEffectsManager manager) {
		this.manager = manager;
	}

	public ArenaCell getCell() {
		return cell;
	}

	public void setCell(ArenaCell cell) {
		this.cell = cell;
	}

	public abstract CellEffects getEffect();
}