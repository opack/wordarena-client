package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.enums.CellStates;

public class PackLoader {
	private static final String MARKER_PACK_PREFIX = "marker_";
	
	private Skin skin;
	private float frameDuration;
	
	public PackLoader(Skin skin, float frameDuration) {
		this.skin = skin;
		this.frameDuration = frameDuration;
	}

	public MarkerPack load(String name, TextureAtlasEx atlas) {
		// Crée le pack
		MarkerPack pack = new MarkerPack();
		pack.name = name;
		
		// Charge les images des cellules
		pack.cell.put(CellStates.OWNED, Boolean.FALSE, atlas.findRegion(MARKER_PACK_PREFIX + name + "_owned_normal", true));
		pack.cell.put(CellStates.OWNED, Boolean.TRUE, atlas.findRegion(MARKER_PACK_PREFIX + name + "_owned_selected", true));
		pack.cell.put(CellStates.CONTROLED, Boolean.FALSE, atlas.findRegion(MARKER_PACK_PREFIX + name + "_controled_normal", true));
		pack.cell.put(CellStates.CONTROLED, Boolean.TRUE, atlas.findRegion(MARKER_PACK_PREFIX + name + "_controled_selected", true));
		
		// Charge le style de label
		pack.labelStyle = skin.get(MARKER_PACK_PREFIX + name, LabelStyle.class);
		
		// Charge les animations
		pack.cellGainedAnim = atlas.findAnimation(MARKER_PACK_PREFIX + name + "_gain", frameDuration, true);
		pack.cellMomentaryAnim = atlas.findAnimation(MARKER_PACK_PREFIX + name + "_owned_normal", frameDuration, true);
		
		return pack;
	}

}
