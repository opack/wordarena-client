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
	 * Animation à jouer lorsque le joueur perd une cellule
	 */
	public Animation cellLostAnim;
	
	/**
	 * Animation à jouer lorsque le joueur gagne une cellule
	 */
	public Animation cellGainedAnim;
	
	public MarkerPack() {
		cell = new DoubleEntryArray<CellStates, Boolean, TextureRegion>();
	}
}
