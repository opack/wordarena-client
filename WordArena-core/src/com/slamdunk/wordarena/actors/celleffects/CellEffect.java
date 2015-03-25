package com.slamdunk.wordarena.actors.celleffects;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.Player;

/**
 * Correspond à l'effet qui se produit lorsqu'une lettre sélectionnée
 * est validée.
 */
public interface CellEffect {
	/**
	 * Applique l'effet sur la cellule indiquée, et éventuellement
	 * d'autres de l'arène
	 * @param player 
	 * @param cell
	 * @param arena
	 */
	void applyEffect(Player player, ArenaCell cell, ArenaData arena);
}
