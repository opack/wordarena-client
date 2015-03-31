package com.slamdunk.wordarena.data;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.wordarena.enums.BordersAndCorners;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CornerTypes;

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
	 * Image affichée dans les carrés indiquant la possession de chaque joueur
	 */
	public TextureRegionDrawable possessionMarker;
	
	public DoubleEntryArray<BordersAndCorners, CornerTypes, TextureRegionDrawable> zoneEdges;
	
	public MarkerPack() {
		zoneEdges = new DoubleEntryArray<BordersAndCorners, CornerTypes, TextureRegionDrawable>();
	}
	
	/**
	 * Retourne l'animation de propriétaire pour le pack et l'état de la cellule indiqués.
	 * @param data
	 * @return
	 */
	public Animation getCellAnim(CellData data) {
		if (data.selected) {
			return selectedAnim;
		} else if (data.state == CellStates.OWNED) {
			return ownedAnim;
		} else if (data.state == CellStates.CONTROLED) {
			return controledAnim;
		}
		// Ce cas ne devrait pas arriver
		return null;
	}
}
