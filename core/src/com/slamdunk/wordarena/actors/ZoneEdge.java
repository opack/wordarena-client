package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.wordarena.data.EdgeData;
import com.slamdunk.wordarena.data.MarkerPack;

public class ZoneEdge extends Image {
	private EdgeData data;

	public ZoneEdge() {
		this.data = new EdgeData();
	}
	
	public EdgeData getData() {
		return data;
	}
	
	/**
	 * Choisit l'image appropriée pour ce bord de zone
	 * en fonction des données
	 * @param highlighted Indique si la bordure doit être
	 * mise en surbrillance
	 */
	public void updateDisplay(MarkerPack markerPack, boolean highlighted) {
		// Choisit l'image
		TextureRegionDrawable drawable = markerPack.zoneEdges.get(data.borderOrCorner, data.cornerType);
		if (drawable == null) {
			return;
		}
		setDrawable(drawable);
		
		// Si la taille de l'image a changé, on modifie la taille de la bordure
		// et on la réaligne
		if (getWidth() != drawable.getMinWidth()
		|| getHeight() != drawable.getMinHeight()) {
			setSize(drawable.getMinWidth(), drawable.getMinHeight());
			
			// Aligne la bordure par rapport à la cellule
			ActorHelper.alignInside(data.borderOrCorner, data.anchorPos, ArenaZone.BORDER_POS_OFFSET, this);
		}
	}
}
