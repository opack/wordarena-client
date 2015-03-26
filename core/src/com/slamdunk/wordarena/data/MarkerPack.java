package com.slamdunk.wordarena.data;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Représente les différentes images des marqueurs pour cellule ainsi
 * qu'un style pour les écritures du joueur
 */
public class MarkerPack {
	/**
	 * Nom du pack
	 */
	public String name;
	
	/**
	 * Images des cellules en fonction d'un état de cellule et de sélection
	 */
	public DoubleEntryArray<CellStates, Boolean/*selected ?*/, TextureRegion> cell;
	
	/**
	 * Style du libellé à appliquer pour les écritures liées au joueur qui
	 * utilise ce pack
	 */
	public LabelStyle labelStyle;
	
	/**
	 * Animation à jouer lorsque la cellule est possédée. Cette animation est mise en
	 * pause sur la 1ère frame et déclenchée à intervalles aléatoires pour donner vie
	 * à la cellule.
	 */
	public Animation ownedAnim;
	
	/**
	 * Animation à jouer lorsque la cellule est contrôlée. Cette animation est mise en
	 * pause sur la 1ère frame et déclenchée à intervalles aléatoires pour donner vie
	 * à la cellule.
	 */
	public Animation controledAnim;
	
	/**
	 * Animation à jouer lorsque la cellule est sélectionnée. Cette animation est jouée
	 * en looping.
	 */
	public Animation selectedAnim;
	
	/**
	 * Animation à jouer lorsque le joueur gagne une cellule
	 */
	public Animation conquestAnim;
	
	/**
	 * Animation à jour de temps en temps pour donner vie à la cellule
	 */
	public Animation momentaryAnim;
	
	public MarkerPack() {
		cell = new DoubleEntryArray<CellStates, Boolean, TextureRegion>();
	}
}
