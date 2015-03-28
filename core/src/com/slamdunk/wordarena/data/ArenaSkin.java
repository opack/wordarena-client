package com.slamdunk.wordarena.data;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.wordarena.enums.BordersAndCorners;
import com.slamdunk.wordarena.enums.CellTypes;

/**
 * Représente la skin à utiliser pour dessiner l'arène
 */
public class ArenaSkin {
	public String name;
	
	public TextureRegionDrawable edge_h;
	public TextureRegionDrawable edge_v;
	
	public TextureRegionDrawable edge_h_highlighted;
	public TextureRegionDrawable edge_v_highlighted;
	
	public TextureRegionDrawable wall_h;
	public TextureRegionDrawable wall_v;
	
	public DoubleEntryArray<CellTypes, Boolean/*selected?*/, TextureRegion> cellTypes;
	
	/**
	 * Images à placer à côté des cellules de bord d'arène
	 */
	public Map<BordersAndCorners, TextureRegionDrawable> arenaBorders;
	
	public TextureRegionDrawable background;
	
	public ArenaSkin() {
		cellTypes = new DoubleEntryArray<CellTypes, Boolean, TextureRegion>();
		arenaBorders = new HashMap<BordersAndCorners, TextureRegionDrawable>();
	}
	
	/**
	 * Retourne l'image de type de cellule pour l'état de la cellule indiqué.
	 * @param data
	 * @return
	 */
	public TextureRegion getCellTypeRegion(CellData data) {
		return cellTypes.get(data.type, data.selected);
	}
}
