package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;

/**
 * Gère les effets à appliquer sur les cellules lors de la validation
 * d'un mot
 */
public class CellEffectsManager extends Actor {
	private final static List<CellEffect> EFFECTS_LIST;
	static {
		EFFECTS_LIST = new ArrayList<CellEffect>();
		EFFECTS_LIST.add(new BreakNeighborGlassEffect());
		EFFECTS_LIST.add(new BombExplosionEffect());
		EFFECTS_LIST.add(new TakeOwnershipEffect());
	}
	
	/**
	 * Liste ordonnée des effets à appliquer
	 */
	private List<CellEffect> effectsToApply;
	
	/**
	 * Indice de l'effet courant
	 */
	private int currentEffect;
	
	/**
	 * Indique si le manager est en train d'appliquer des effets
	 */
	private boolean processing;
	
	/**
	 * Indique si les effets sont mis en pause
	 */
	private boolean paused;
	
	/**
	 * Cellules sur lesquelles la méthode applyEffects() a été appelée
	 */
	private List<CellActor> cells;
	
	private PlayerData player;
	
	private CellEffectsApplicationFinishedListener listener;
	
	public CellEffectsManager() {
		effectsToApply = new ArrayList<CellEffect>();
		cells = new ArrayList<CellActor>();
	}
	
	public void setListener(CellEffectsApplicationFinishedListener listener) {
		this.listener = listener;
	}
	
	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * Applique les effets sur les cellules indiquées
	 * @return true si les effets ont pu être lancés
	 */
	public boolean applyEffects(List<CellActor> targetCells, PlayerData player, ArenaOverlay arena) {
		// Si on est déjà en train d'appliquer des effets, on refuse
		if (processing) {
			return false;
		}
		
		this.player = player;
		
		// Copie les cellules
		cells.clear();
		cells.addAll(targetCells);
		
		// Initialise les effets
		effectsToApply.clear();
		for (CellEffect effect : EFFECTS_LIST) {
			
			// Initialise l'effet et l'ajoute à la liste des effets
			// à appliquer s'il a été correctement initialisé
			if (effect.init(cells, player, arena)) {
				effectsToApply.add(effect);
			}
			
		}
		
		// Démarre l'application des effets au prochain appel à act()
		currentEffect = 0;
		processing = true;
		
		return true;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (processing && !paused) {
			
			// S'il n'y a plus d'effet à appliquer, on a terminé
			if (currentEffect >= effectsToApply.size()) {
				// Arrête le traitement
				processing = false;
				
				// Notifie le listener
				if (listener != null) {
					listener.onEffectApplicationFinished(player, cells);
				}
			}
			
			// Sinon, applique l'effet courant
			else if (effectsToApply.get(currentEffect).act(delta)) {
				
				// L'effet est terminé, on passe au suivant
				currentEffect++;
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (processing
		&& currentEffect < effectsToApply.size()) {
			effectsToApply.get(currentEffect).draw(batch, parentAlpha);
		}
	}
}
