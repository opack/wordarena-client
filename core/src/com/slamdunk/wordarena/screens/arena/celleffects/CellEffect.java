package com.slamdunk.wordarena.screens.arena.celleffects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellEffects;

/**
 * Correspond à l'effet qui se produit lorsqu'une lettre sélectionnée est
 * validée.
 * Lors du premier appel à act(), l'effet devient actif et la méthode
 * {@link #init()} est exécutée.
 * Tant que l'effet est inactif, la méthode {@link #render(Batch, float)}
 * n'est pas exécutée. Cela permet de séquencer les effets et de s'assurer
 * qu'ils seront bien dessinés les uns à la suite des autres uniquement
 * lorsqu'ils sont exécutés.
 * 
 * La méthode {@link #perform(float)} contient le traitement de l'effet,
 * et la méthode {@link #render(Batch, float)} contient le dessin de l'effet.
 * Ces méthodes ne sont exécutées que si l'action est active, donc pas
 * avant le premier appel à act(), et si {@link #isActive()} == TRUE.
 * ATTENTION ! Activer manuellement l'effet avec {@link #setActive(Boolean)}
 * peut conduire à des effets indésirables. En effet, la méthode {@link #render(Batch, float)}
 * peut être appelée dès que l'effet est actif, même si {@link #perform(float)}
 * n'a pas été exécutée !
 * 
 * Pour désactiver une action, on peut utiliser {@link #setActive(Boolean)}
 * ou retourner true à la fin de {@link #perform(float)}, pour indiquer
 * que le traitement est terminé.
 */
public abstract class CellEffect extends Action {
	private CellEffectsManager manager;
	private ArenaCell cell;
	private Boolean active;
	
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
	
	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ArenaCell getCell() {
		return cell;
	}

	public void setCell(ArenaCell cell) {
		this.cell = cell;
	}

	public abstract CellEffects getEffect();

	/**
	 * Appelée lors du premier appel à act().
	 * @return true si l'effet peut être appliqué, false sinon.
	 */
	protected boolean init() {
		return true;
	}
	
	@Override
	public final boolean act(float delta) {
		// Act a été appelée : l'action est donc active
		// si elle ne l'était pas encore
		if (active == null) {
			active = init();
		}
		
		// Si l'action est toujours active, on continue le traitement
		if (active == Boolean.TRUE
		&& perform(delta)) {
			// L'action est terminée
			active = Boolean.FALSE;
		}
		
		// Si active == FALSE, c'est que l'action a été
		// volontairement désactivée. On la termine.
		return active == Boolean.FALSE;
	}
	
	/**
	 * Méthode appelée à chaque act() sur l'action sur elle est active. Cette
	 * méthode contient le traitement de l'effet.
	 * @param delta
	 * @return Même contrat que act() : true si l'action est terminée, false sinon
	 */
	protected abstract boolean perform(float delta);

	public final void draw(Batch batch, float parentAlpha) {
		// Le dessin n'est fait que si l'action est active
		if (!active) {
			return;
		}
		render(batch, parentAlpha);
	}
	
	/**
	 * Méthode appelée à chaque draw() sur l'effet s'il est actif afin
	 * de dessiner l'effet.
	 * @param batch
	 * @param parentAlpha
	 * @return
	 */
	protected abstract void render(Batch batch, float parentAlpha);
}