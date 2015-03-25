package com.slamdunk.wordarena.actors.celleffects;

import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Joue en séquence l'animation de perdre de possession liée au marqueur
 * de l'ancien Player, puis celle de prise de possession liée au marqueur
 * du Player ayant conquis cette cellule
 */
public class TakeOwnershipEffect extends CellEffect {
	
	public TakeOwnershipEffect(Player player, ArenaCell cell, ArenaData arena) {
		super(player, cell, arena);
	}

	private boolean launched = false;
	private AnimationDrawer drawer;

	@Override
	public boolean act(float delta) {
		
		// Si le joueur n'a pas changé, inutile de jouer les animations
		final CellData cellData = getCell().getData();
		if (cellData.state == CellStates.OWNED
		&& getPlayer().equals(cellData.owner)) {
			return true;
		}
		
		// Lance l'animation
		if (!launched) {
			launchAnim(getPlayer(), getCell());
		}
		
		// Vérifie si l'animation est terminée
		if (checkEffectEnd()) {

			// Met à jour le statut de la cellule à la fin de l'animation
			cellData.owner = getPlayer();
			cellData.state = CellStates.OWNED;
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
		drawer = cell.getOwnerActorDBG().getAnimationDrawer();
		
		// Lance l'animation de prise de possession
		final MarkerPack gainerPack = Assets.markerPacks.get(player.markerPack);
		drawer.setAnimationMomentary(gainerPack.cellGainedAnim, true, null, false, false);
		
		launched = true;
	}

}
