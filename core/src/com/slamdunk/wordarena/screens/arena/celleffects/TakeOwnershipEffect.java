package com.slamdunk.wordarena.screens.arena.celleffects;

import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellEffects;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Joue l'animation de prise de possession liée au marqueur du Player ayant
 * conquis cette cellule, puis change effectivement la possession de la cellule.
 */
public class TakeOwnershipEffect extends CellEffect {
	private boolean playAnim;
	
	public TakeOwnershipEffect(CellEffectsManager manager, ArenaCell cell) {
		super(manager, cell);
		
		// Si le joueur n'a pas changé, inutile de jouer l'animation
		// On ne joue donc l'animation que si la cellule n'est pas actuellement
		// possédée, ou qu'elle l'est par un autre joueur
		playAnim = cell.getData().state != CellStates.OWNED
				|| !manager.getPlayer().equals(cell.getData().owner);
	}

	private boolean launched = false;
	private AnimationDrawer drawer;

	@Override
	public CellEffects getEffect() {
		return CellEffects.TAKE_OWNERSHIP;
	}
	
	@Override
	public boolean act(float delta) {
		if (!playAnim) {
			return true;
		}
		
		
		// Lance l'animation
		if (!launched) {
			launchAnim(getManager().getPlayer(), getCell());
		}
		
		// Vérifie si l'animation est terminée
		if (checkEffectEnd()) {

			// Met à jour le statut de la cellule à la fin de l'animation
			getCell().getData().owner = getManager().getPlayer();
			getCell().getData().state = CellStates.OWNED;
			getCell().updateDisplay();
			
			return true;
		}
		
		return false;
	}
	
	private boolean checkEffectEnd() {
		if (drawer.getAnimationMomentary().isAnimationFinished(drawer.getStateTime())) {
			drawer.setActive(false);
			return true;
		}
		return false;
	}

	private void launchAnim(Player player, ArenaCell cell) {
		drawer = cell.getOwnerActor().getAnimationDrawer();
		
		// Lance l'animation de prise de possession
		final MarkerPack gainerPack = Assets.markerPacks.get(player.markerPack);
		drawer.setAnimationMomentary(gainerPack.cellGainedAnim, true, null, false, false);
		
		launched = true;
	}

}
