package com.slamdunk.wordarena.screens.arena.celleffects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.enums.CellEffects;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Joue l'animation de prise de possession liée au marqueur du Player ayant
 * conquis cette cellule, puis change effectivement la possession de la cellule.
 */
public class TakeOwnershipEffect extends CellEffect {
	private AnimationDrawer drawer;
	
	public TakeOwnershipEffect(CellEffectsManager manager, ArenaCell cell) {
		super(manager, cell);
	}
	
	protected boolean init() {
		// Si le joueur n'a pas changé, inutile de jouer l'animation
		// On ne joue donc l'animation que si la cellule n'est pas actuellement
		// possédée, ou qu'elle l'est par un autre joueur
		if (getCell().getData().state != CellStates.OWNED
		|| !getManager().getPlayer().equals(getCell().getData().owner)) {
			
			// Lance l'animation de prise de possession
			final MarkerPack gainerPack = Assets.markerPacks.get(getManager().getPlayer().markerPack);
			
			drawer = new AnimationDrawer();
			drawer.setAnimation(gainerPack.conquestAnim, true, false);
			
			return true;
		}
		return false;
	}

	@Override
	public CellEffects getEffect() {
		return CellEffects.TAKE_OWNERSHIP;
	}
	
	@Override
	public boolean perform(float delta) {
		// Déroule l'animation
		drawer.updateTime(delta);
		
		// Vérifie si l'animation est terminée
		if (drawer.isAnimationFinished()) {
			
			// Arrête l'animation
			drawer.setActive(false);
			
			// Met à jour le statut de la cellule à la fin de l'animation
			getCell().getData().owner = getManager().getPlayer();
			getCell().getData().state = CellStates.OWNED;
			getCell().updateDisplay();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void render(Batch batch, float parentAlpha) {
		drawer.draw(getCell(), batch);
	}
}
