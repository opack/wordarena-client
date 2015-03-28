package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.EdgeData;

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
	public void update(boolean highlighted) {
		if (data.border.isVertical()) {
			if (highlighted) {
				setDrawable(Assets.arenaSkin.edge_v_highlighted);
			} else {
				setDrawable(Assets.arenaSkin.edge_v);
			}
		} else {
			if (highlighted) {
				setDrawable(Assets.arenaSkin.edge_h_highlighted);
			} else {
				setDrawable(Assets.arenaSkin.edge_h);
			}
		}
	}
}
