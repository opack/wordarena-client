package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.wordarena.data.ArenaSkin;
import com.slamdunk.wordarena.enums.BordersAndCorners;
import com.slamdunk.wordarena.enums.CellTypes;

/**
 * Charge une skin d'arène. Seule 1 skin d'arène est chargée à la fois. Si un
 * appel est fait à load alors qu'une skin avait déjà été chargée, l'atlas
 * correspondant est dispose() et la skin courante est écrasée.
 * @author Didier
 *
 */
public class ArenaSkinLoader {
	private ArenaSkin skin;
	private TextureAtlasEx atlas;
	
	public ArenaSkinLoader() {
		skin = new ArenaSkin();
	}
	
	public ArenaSkin load(String name) {
		// Ouvre l'atlas
		if (atlas != null) {
			atlas.dispose();
		}
		atlas = new TextureAtlasEx("skins/arena_" + name + "/arena_" + name +  ".txt");		
		
		// Charge les éléments de la skin
		skin.name = name;
		
		// Charge l'image de fond
		loadBackground();
		
		// Charge les images des bords d'arène
		loadArenaBorders();
		
		// Charge les images des murs
		loadWalls();
		
		// Charge les images des bords de zone
		loadZoneBorders();
		
		// Charge les images des types
		loadCellTypes();
		
		return skin;
	}
	
	private void loadBackground() {
		skin.background = new TextureRegionDrawable(atlas.findRegion("background"));
	}

	private void loadArenaBorders() {
		skin.arenaBorders.clear();
		
		for (BordersAndCorners border : BordersAndCorners.values()) {
			
			String regionName = "arena_border_" + border.name().toLowerCase();
			TextureRegion region = atlas.findRegion(regionName);
			
			if (region != null) {
				skin.arenaBorders.put(border, new TextureRegionDrawable(region));
			}
		}
	}

	private void loadWalls() {
		skin.wall_h = new TextureRegionDrawable(atlas.findRegion("wall_h"));
		skin.wall_v = new TextureRegionDrawable(atlas.findRegion("wall_v"));
	}

	private void loadZoneBorders() {
		skin.edge_h = new TextureRegionDrawable(atlas.findRegion("zone_edge_h"));
		skin.edge_v = new TextureRegionDrawable(atlas.findRegion("zone_edge_v"));
		
		skin.edge_h_highlighted = new TextureRegionDrawable(atlas.findRegion("zone_edge_h_highlighted"));
		skin.edge_v_highlighted = new TextureRegionDrawable(atlas.findRegion("zone_edge_v_highlighted"));
	}

	private void loadCellTypes() {
		skin.cellTypes.clear();
		for (CellTypes type : CellTypes.values()) {
			putCellTypeImage(type, Boolean.FALSE);
			putCellTypeImage(type, Boolean.TRUE);
		}
	}
	
	private void putCellTypeImage(final CellTypes type, Boolean selected) {
		final String regionName = "cell_type_" + type.name() + "_" + (selected ? "selected" : "normal");
		
		final TextureRegion region = atlas.findRegion(regionName, true);
		if (region == null) {
			throw new IllegalStateException("Missing image " + regionName + " in atlas !");
		}
		
		skin.cellTypes.put(type, selected, region);
	}

	public ArenaSkin getSkin() {
		return skin;
	}

	public void dispose() {
		if (atlas != null) {
			atlas.dispose();
		}
	}
}
