package com.slamdunk.wordarena.actors.celleffects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Joue en séquence l'animation de perdre de possession liée au marqueur
 * de l'ancien Player, puis celle de prise de possession liée au marqueur
 * du Player ayant conquis cette cellule
 */
public class TakeOwnershipEffect implements CellEffect {

	@Override
	public void applyEffect(Player player, final ArenaCell cell, ArenaData arena) {
		// Si le joueur n'a pas changé, inutile de jouer les animations
		if (cell.getData().state == CellStates.OWNED
		&& player.equals(cell.getData().owner)) {
			return;
		}
		
		final AnimationDrawer animDrawer = cell.getOwnerActorDBG().getAnimationDrawer();
		final MarkerPack looserPack = Assets.markerPacks.get(cell.getData().owner.markerPack);
		final MarkerPack gainerPack = Assets.markerPacks.get(player.markerPack);
		
		// Crée l'action jouant l'animation de perte de possession
		Action loseOwnershipAnim = new Action() {
			@Override
			public boolean act(float delta) {
				animDrawer.setAnimationMomentary(looserPack.cellLostAnim, true, null, false, false);
				return true;
			}
		};
		
		
		// Crée l'action jouant l'animation de prise de possession
		Action gainOwnershipAnim = new Action() {
			@Override
			public boolean act(float delta) {
				if (animDrawer.getAnimationMomentary().isAnimationFinished(animDrawer.getStateTime())) {
					animDrawer.setAnimationMomentary(gainerPack.cellGainedAnim, true, null, false, false);
				}
				return true;
			}
		};
		
		// Crée l'action qui lancera un updateDisplay() à la fin pour choisir la bonne image
		Action updateDisplay = new Action() {
			@Override
			public boolean act(float delta) {
				if (animDrawer.getAnimationMomentary().isAnimationFinished(animDrawer.getStateTime())) {
					cell.updateDisplay();
				}
				return true;
			}
		};
		
		// Lancement des 3 actions l'une après l'autre
		cell.addAction(Actions.sequence(loseOwnershipAnim, gainOwnershipAnim, updateDisplay));
	}

}
