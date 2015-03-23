package com.slamdunk.wordarena.actors.celleffects;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.Player;

/**
 * Joue en séquence l'animation de perdre de possession liée au marqueur
 * de l'ancien Player, puis celle de prise de possession liée au marqueur
 * du Player ayant conquis cette cellule
 */
public class TakeOwnershipEffect implements CellEffect {

	@Override
	public void applyEffect(Player player, ArenaCell cell, ArenaData arena) {
		// TODO Créer l'action jouant l'animation de perte de possession
		// ...
		
		// TODO Créer l'action jouant l'animation de prise de possession,
		// qui lancera un updateDisplay() à la fin pour choisir la bonne image.
		// ...
		
		// Lancement des 2 actions l'une après l'autre
		//cell.addAction(Actions.sequence(loseCellAnim, gainCellAnim));
	}

}
