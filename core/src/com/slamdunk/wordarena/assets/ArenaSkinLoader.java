package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slamdunk.wordarena.data.arena.ArenaSkin;
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
	private com.slamdunk.toolkit.graphics.TextureAtlasEx atlas;
	
	public ArenaSkinLoader() {
		skin = new ArenaSkin();
	}
	
	public ArenaSkin load(String name) {
		// Si on a déjà chargé cette skin, on la retourne
		if (name.equals(skin.name)) {
			return skin;
		}
		
		// Ouvre l'atlas
		if (atlas != null) {
			atlas.dispose();
		}
		atlas = new com.slamdunk.toolkit.graphics.TextureAtlasEx("textures/arena_skin_" + name + ".atlas");
		
		// Charge les éléments de la skin
		skin.name = name;
		
		// Charge l'image de fond
		loadBackground();
		
		// Charge les images des murs
		loadWalls();
		
		// Charge les images des bords de zone
		loadZoneBorders();
		
		// Charge les images des types
		loadCellTypes();
		
		return skin;
	}
	
	private void loadBackground() {
		skin.background = atlas.findRegionDrawable("background", false);
	}

	private void loadWalls() {
		skin.wall_h = atlas.findRegionDrawable("wall_h", false);
		skin.wall_v = atlas.findRegionDrawable("wall_v", false);
	}

	private void loadZoneBorders() {
		skin.edge_h = atlas.findRegionDrawable("zone_edge_h", false);
		skin.edge_v = atlas.findRegionDrawable("zone_edge_v", false);
		
		skin.edge_h_highlighted = atlas.findRegionDrawable("zone_edge_h_highlighted", false);
		skin.edge_v_highlighted = atlas.findRegionDrawable("zone_edge_v_highlighted", false);
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
