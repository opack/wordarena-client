package com.slamdunk.wordarena.screens.arena.celleffects;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.data.game.Player;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;

public interface CellEffect {

	/**
	 * Initialise l'effet pour travailler avec les données indiquées.
	 * Cette méthode n'est appelée qu'une fois au début de l'application
	 * des effets, mais sera appelée de nouveau si les effets doivent
	 * de nouveau être appliqués sur un jeu de données différents
	 * (typiquement cette méthode est appelée 1 fois par validation
	 * de mot).
	 * @param cells
	 * @param player
	 * @param arena
	 * @return true si l'effet est initialisé et peut être appliqué, false
	 * sinon
	 */
	boolean init(List<CellActor> cells, Player player, ArenaOverlay arena);

	/**
	 * Effectue un traitement. Correspond à la méthode Actor.act().
	 * @param delta
	 * @return true si l'effet a terminé son traitement et qu'il n'est
	 * plus nécessaire d'appeler act() ou draw() de nouveau pour cet
	 * effet (donc qu'on peut passer au suivant).
	 */
	boolean act(float delta);

	/**
	 * Dessine des effets visuels. Correspond à la méthode Actor.draw().
	 * @param batch
	 * @param parentAlpha
	 */
	void draw(Batch batch, float parentAlpha);

}
