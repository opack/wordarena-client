package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.wordarena.data.MarkerPack;

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
		
		// Charge le style de label
		pack.labelStyle = skin.get(MARKER_PACK_PREFIX + name, LabelStyle.class);
		
		// Charge les animations
		pack.ownedAnim = atlas.findAnimation(MARKER_PACK_PREFIX + name + "_owned", frameDuration, true);
		pack.controledAnim = atlas.findAnimation(MARKER_PACK_PREFIX + name + "_controled", frameDuration, true);
		pack.selectedAnim = atlas.findAnimation(MARKER_PACK_PREFIX + name + "_selected", frameDuration, true);
		pack.conquestAnim = atlas.findAnimation(MARKER_PACK_PREFIX + name + "_conquest", frameDuration, true);
		
		return pack;
	}

}
