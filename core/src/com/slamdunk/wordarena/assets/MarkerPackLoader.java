package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.BordersAndCorners;
import com.slamdunk.wordarena.enums.CornerTypes;

public class MarkerPackLoader {
	private static final String MARKER_PACK_PREFIX = "marker_";
	
	private Skin skin;
	private float frameDuration;
	
	public MarkerPackLoader(Skin skin, float frameDuration) {
		this.skin = skin;
		this.frameDuration = frameDuration;
	}

	public MarkerPack load(String name, TextureAtlasEx atlas) {
		// Crée le pack
		MarkerPack pack = new MarkerPack();
		pack.name = name;
		final String atlasPackName = MARKER_PACK_PREFIX + name;
		
		// Charge le style de label
		pack.labelStyle = skin.get(atlasPackName, LabelStyle.class);
		
		// Charge les images des régions
		TextureRegion region = atlas.findRegion(atlasPackName + "_zone_h");
		pack.zone_h = region != null ? new TextureRegionDrawable(region) : null;
		region = atlas.findRegion(atlasPackName + "_zone_v");
		pack.zone_v = region != null ? new TextureRegionDrawable(region) : null;
		
		for (Borders border : Borders.values()) {
			region = atlas.findRegion(atlasPackName + "_zone_" + border.name().toLowerCase());
			pack.zones.put(border, region != null ? new TextureRegionDrawable(region) : null);
		}
		
		String borderRegionName;
		for (BordersAndCorners border : BordersAndCorners.values()) {
			for (CornerTypes cornerType : CornerTypes.values()) {
				// Construit le nom de la région à récupérer à partir du bord et du coin courants
				borderRegionName = atlasPackName
						+ "_zone_" + border.name().toLowerCase()
						+ ((cornerType != CornerTypes.NONE) ? cornerType.name().toLowerCase() : "");
				
				// Récupère la région
				region = atlas.findRegion(borderRegionName);
				
				// Stocke la région
				if (region != null) {
					pack.zoneEdges.put(border, cornerType, new TextureRegionDrawable(region));
				}
			}
		}
		
		// Charge les animations
		pack.ownedAnim = atlas.findAnimation(atlasPackName + "_owned", frameDuration, true);
		pack.controledAnim = atlas.findAnimation(atlasPackName + "_controled", frameDuration, true);
		pack.selectedAnim = atlas.findAnimation(atlasPackName + "_selected", frameDuration, true);
		pack.conquestAnim = atlas.findAnimation(atlasPackName + "_conquest", frameDuration, true);
		
		return pack;
	}

}
