package com.slamdunk.wordarena.data.arena.zone;

import com.badlogic.gdx.math.Vector2;
import com.slamdunk.wordarena.enums.BordersAndCorners;
import com.slamdunk.wordarena.enums.CornerTypes;

/**
 * Représente un côté d'une zone s'étendant entre 2 cellules
 */
public class EdgeData {
	public BordersAndCorners borderOrCorner;
	public CornerTypes cornerType;
	
	/**
	 * Point de départ par rapport auquel sera dessinée la bordure.
	 * Par convention, ce point d'ancrage est défini dans le sens des aiguilles
	 * d'une montre, sachant que l'image se dessine vers la droite et vers le haut.
	 * Ainsi, selon la valeur de borderOrCorner, anchorPos désignera :
	 * 
	 *       borderOrCorner     |    anchroPos
	 *  ------------------------|-----------------
	 *	LEFT,BOTTOM,BOTTOM_LEFT	| coin bas-gauche
	 *  TOP,TOP_LEFT			| coin haut-gauche
	 *  TOP_RIGHT				| coin haut-droit
	 *  RIGHT,BOTTOM_RIGHT		| coin bas-droit 
	 */
	public Vector2 anchorPos;
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof EdgeData) {
			// Identique si c'est le même côté de la même cellule
			EdgeData edge2 = (EdgeData)other;
			return edge2.borderOrCorner == borderOrCorner
				&& edge2.cornerType == cornerType;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return borderOrCorner.hashCode() *31 ^ cornerType.hashCode() * 31;
	}
}
