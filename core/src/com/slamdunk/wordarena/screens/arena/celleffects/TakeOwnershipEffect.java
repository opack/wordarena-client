package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;

public class TakeOwnershipEffect extends DefaultCellEffect {
	private AnimationDrawer drawer;
	
	public TakeOwnershipEffect() {
		drawer = new AnimationDrawer();
	}

	@Override
	protected boolean isCellTargetable(ArenaCell cell) {
		// Seules les cellules qui peuvent être possédées sont targetables
		return cell.getData().type.canBeOwned()
		// Seules les cellules qui sont actuellement libres, ou possédées
		// par un autre joueur sont targetables
			&& (cell.getData().state != CellStates.OWNED || !getPlayer().equals(cell.getData().owner));
	}
	
	@Override
	public boolean init(List<ArenaCell> cells, Player player, ArenaData arena) {
		super.init(cells, player, arena);
		
		// S'il n'y a pas de cellules à conquérir, il n'y a rien à faire
		if (getTargetCells().isEmpty()) {
			return false;
		}
		
		// Lance l'animation de prise de possession
		final MarkerPack gainerPack = Assets.markerPacks.get(player.markerPack);
		drawer.setAnimation(gainerPack.conquestAnim, true, false);
		
		return true;
	}
	
	@Override
	public boolean act(float delta) {
		// Déroule l'animation
		drawer.updateTime(delta);
		
		// Vérifie si l'animation est terminée
		if (drawer.isAnimationFinished()) {
			
			// Arrête l'animation
			drawer.setActive(false);
			
			// Met à jour le statut des cellules à la fin de l'animation
			updateArena();
			
			return true;
		}
		
		return false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (ArenaCell cell : getTargetCells()) {
			drawer.draw(cell, batch);
		}
	}

	/**
	 * Met à jour l'arène à la fin de la prise de contrôle
	 */
	private void updateArena() {
		for (ArenaCell cell : getTargetCells()) {
			cell.getData().owner = getPlayer();
			cell.getData().state = CellStates.OWNED;
			
			cell.updateDisplay();
		}
	}
}
