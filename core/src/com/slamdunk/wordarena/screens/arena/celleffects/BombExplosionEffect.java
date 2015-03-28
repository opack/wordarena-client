package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;

public class BombExplosionEffect extends DefaultCellEffect {
	
	/**
	 * Liste de cellules sur lesquelles dessiner l'animation d'explosion
	 */
	private Set<ArenaCell> explodedNeighbors;
	
	/**
	 * Liste des bombes qui explosent suite à une réaction en chaine
	 */
	private Set<ArenaCell> chainReaction;
	
	private AnimationDrawer drawer;
	
	public BombExplosionEffect() {
		explodedNeighbors = new HashSet<ArenaCell>();
		chainReaction = new HashSet<ArenaCell>();
		drawer = new AnimationDrawer();
	}
	
	@Override
	protected boolean isCellTargetable(ArenaCell cell) {
		return cell.getData().type == CellTypes.B;
	}
	
	@Override
	public boolean init(List<ArenaCell> cells, Player player, ArenaData arena) {
		super.init(cells, player, arena);
		
		// S'il n'y a pas de bombe, il n'y a rien à faire
		if (getTargetCells().isEmpty()) {
			return false;
		}
		
		explodedNeighbors.clear();
		chainReaction.clear();
		// Fait exploser les voisins directs et recherche d'éventuelles explosion en chaine
		for (ArenaCell bomb : getTargetCells()) {
			explode(bomb, player);
		}
		
		// Démarre l'animation de l'explosion
		drawer.setAnimation(Assets.explosionAnim, true, false);
		
		// TODO Joue un son d'explosion
		
		return true;
	}

	/**
	 * Ajoute les voisins de la cellule indiquée qui peuvent exploser, càd ceux
	 * possédés par un autre joueur
	 * @param bomb
	 * @param player
	 * @param trackChainReaction Si true, les bombes voisines sont stockées dans une
	 * liste pour pouvoir les faire exploser par la suite.
	 */
	private void explode(ArenaCell bomb, Player player) {
		// Récupère les voisins
		List<ArenaCell> tmpNeighbors = new ArrayList<ArenaCell>(4);
		getArena().getNeighbors4(bomb, tmpNeighbors);
		
		CellData neighborData;
		boolean isBomb;
		boolean isEnnemy;
		for (ArenaCell neighbor : tmpNeighbors) {
			// Si cette cellule est déjà prévue pour exploser, on n'a rien à faire de plus
			// (évite la récurrence infinie pour des bombes en cercle)
			if (explodedNeighbors.contains(neighbor)) {
				continue;
			}
			
			// Teste si cette cellule est une bombe
			isBomb = isCellTargetable(neighbor);
			
			// Le voisin explose s'il appartient à un joueur adverse
			// ou qu'il a explosé parce que c'était une bombe
			neighborData = neighbor.getData();
			isEnnemy = !neighborData.owner.equals(player)
					&& !neighborData.owner.equals(Player.NEUTRAL)
					&& neighborData.state == CellStates.OWNED;
			
			// Ce voisin explose s'il est une bombe ou une cellule ennemie
			if (isBomb || isEnnemy) {
				explodedNeighbors.add(neighbor);
			}
			
			// Si c'est une bombe, on la fait exploser
			if (isBomb) {
				// Ajoute la bombe à la réaction en chaine
				chainReaction.add(neighbor);
				
				// Déclenche l'explosion des cellules voisines
				explode(neighbor, player);
			}
		}
	}

	@Override
	public boolean act(float delta) {
		// Déroule l'animation
		drawer.updateTime(delta);
				
		// Vérifie si l'animation est terminée et met à jour l'arène
		if (drawer.isAnimationFinished()) {
			updateArena();
			return true;
		}
		
		// L'animation (et donc l'effet) n'est pas terminé
		return false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (ArenaCell cell : getTargetCells()) {
			drawer.draw(cell, batch);
		}
		for (ArenaCell cell : explodedNeighbors) {
			drawer.draw(cell, batch);
		}
	}

	/**
	 * Met à jour l'arène après l'explosion
	 */
	private void updateArena() {
		Set<ArenaZone> impactedZones = new HashSet<ArenaZone>();
		
		// Supprime la possession adverse des cellules explosées
		CellData neighborData;
		for (ArenaCell neighbor : explodedNeighbors) {
			neighborData = neighbor.getData();
			
			// La cellule perd son owner
			neighborData.owner = Player.NEUTRAL;
			
			// Si la cellule n'est pas dans une zone, elle devient possédée par le neutre
			if (ArenaZone.NONE.equals(neighborData.zone)) {
				neighborData.state = CellStates.OWNED;
			}
			// Sinon, elle est contrôlée par le neutre
			else {
				neighborData.state = CellStates.CONTROLED;
			}
			
			// Mise à jour de l'apparence de la cellule
			neighbor.updateDisplay();
			
			// Note la zone impactée pour màj
			impactedZones.add(neighborData.zone);
		}
		
		// Les cellules bombe (sélectionnées + réaction en chaine) deviennent des cellules normales
		getTargetCells().addAll(chainReaction);
		for (ArenaCell cell : getTargetCells()) {
			// La cellule n'est plus une bombe car elle a explosé
			cell.getData().type = CellTypes.L;
			
			// Mise à jour de l'apparence de la cellule
			cell.updateDisplay();
		}
		
		// Met à jour les zones touchées
		for (ArenaZone impactedZone : impactedZones) {
			impactedZone.updateOwner();
		}
	}
}
