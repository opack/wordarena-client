package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.slamdunk.toolkit.lang.KeyListMap;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellEffects;
import com.slamdunk.wordarena.enums.CellTypes;

/**
 * Gère les effets à appliquer sur les cellules lors de la validation
 * d'un mot
 */
public class CellEffectsManager extends Actor {
	private static KeyListMap<CellTypes, CellEffects> effects;
	static {
		effects = new KeyListMap<CellTypes, CellEffects>();
		
		// ATTENTION ! Les appels à registerCellEffect doivent être réalisés dans l'ordre
		// dans lequel on souhaite voir les effets appliqués !
		
		// Casse le verre des cellules voisines lorsqu'un cellule Lettre, Bombe ou Joker est validée
		registerCellEffect(CellEffects.BREAK_NEIGHBOR_GLASS, CellTypes.L, CellTypes.B, CellTypes.J);
		
		// Défait la possession des cellules voisines ennemies lorsqu'une cellule Bombe est validée
		registerCellEffect(CellEffects.BOMB_EXPLOSION, CellTypes.B);
		
		// Prend possession des cellules validées
		registerCellEffect(CellEffects.TAKE_OWNERSHIP, CellTypes.L, CellTypes.B, CellTypes.J, CellTypes.S);
	}
	
	private static void registerCellEffect(CellEffects effect, CellTypes... types) {
		for (CellTypes type : types) {
			effects.putValue(type, effect);
		}
	}
	
	/**
	 * Contient les cellules sur lesquelles les effets ont été appliqués
	 */
	private List<ArenaCell> processedCells;
	
	/**
	 * Indique si des effets sont en cours d'application
	 */
	private boolean processingEffects;
	
	/**
	 * Liste des effets appliqués
	 */
	private List<CellEffect> applyedEffects;
	
	/**
	 * Listener à notifier lorsque toutes les cellules ont appliqué leurs effets
	 */
	private CellEffectsApplicationFinishedListener listener;
	
	private Player player;
	private ArenaData arena;
	
	public CellEffectsManager() {
		processedCells = new ArrayList<ArenaCell>();
		applyedEffects = new ArrayList<CellEffect>();
	}
	
	public void setListener(CellEffectsApplicationFinishedListener listener) {
		this.listener = listener;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArenaData getArena() {
		return arena;
	}

	public void setArena(ArenaData arena) {
		this.arena = arena;
	}

	/**
	 * Déclenche les effets sur les cellules sélectionnées
	 * @param player 
	 * @param selectedCells
	 */
	public void triggerCellEffects(Player player, List<ArenaCell> selectedCells) {
		this.player = player;
		
		processedCells.clear();
		processedCells.addAll(selectedCells);
		
		applyedEffects.clear();
		
		List<CellEffects> effectList;
		ParallelAction managerActions = new ParallelAction();
		SequenceAction cellActions;
		CellEffect effectAction;
		
		for (ArenaCell cell : selectedCells) {
			// Récupère la liste des effets à appliquer à ce type de cellule
			effectList = effects.get(cell.getData().type);
			
			if (effectList != null) {
				// Crée une nouvelle action qui contiendra l'ensemble ordonné
				// des actions à exécuter
				cellActions = new SequenceAction();
				
				// Ajoute chaque effet à la liste des actions
				for (CellEffects effect : effectList) {
					
					// Crée l'effet
					effectAction = EffectFactory.create(effect, this, cell);
					
					// L'ajoute à ceux qui doivent être exécutés
					if (effectAction != null) {
						cellActions.addAction(effectAction);
						applyedEffects.add(effectAction);
					}
					
				}
				
				// Ajoute la liste des actions à la cellule
				managerActions.addAction(cellActions);
			}
		}
		
		// Ajoute l'ensemble des actions à jouer sur les cellules au manager
		addAction(managerActions);
		processingEffects = true;
	}
	
	@Override
	public void act(float delta) {
		if (!processingEffects) {
			return;
		}
		
		// Joue les actions
		super.act(delta);
		
		// S'il n'y a plus d'actions en cours, on notifie le listener
		if (getActions().size == 0) {
			processingEffects = false;
			if (listener != null) {
				listener.onEffectApplicationFinished(player, processedCells);
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (!processingEffects) {
			return;
		}
		
		super.draw(batch, parentAlpha);
		
		// Dessine les effets visuels actifs
		for (CellEffect effect : applyedEffects) {
			if (effect.isActive()) {
				effect.draw(batch, parentAlpha);
			}
		}
	}

	/**
	 * Notifie le manager que cette cellule a fini d'appliquer tous les effets liés
	 * à sa validation dans un mot. Une fois que toutes les cellules ont fait de même,
	 * le manager notifie son listener.
	 * @param cell
	 */
	public void notifyEndEffectApplication(ArenaCell cell) {
//		// Retire la cellule des cellules en cours d'application d'effets
//		if (currentCells.remove(cell)) {
//			processedCells.add(cell);
//			
//			// S'il n'y a plus de cellules en cours, on notifie le listener
//			if (currentCells.isEmpty()
//			&& listener != null) {
//				listener.onEffectApplicationFinished(player, processedCells);
//			}
//		}
	}
}
