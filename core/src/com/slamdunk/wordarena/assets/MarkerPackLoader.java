package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.wordarena.data.MarkerPack;
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
		
		// Charge les images des 4 bords de zone
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP, CornerTypes.NONE);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.LEFT, CornerTypes.NONE);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.RIGHT, CornerTypes.NONE);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM, CornerTypes.NONE);
		
		// Charge les images des 4 coins intérieurs de zone
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.INNER_CORNER);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_RIGHT, CornerTypes.INNER_CORNER);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_LEFT, CornerTypes.INNER_CORNER);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_RIGHT, CornerTypes.INNER_CORNER);
		
		// Charge les images des 4 coins extérieurs de zone
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.OUTER_CORNER);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_RIGHT, CornerTypes.OUTER_CORNER);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_LEFT, CornerTypes.OUTER_CORNER);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_RIGHT, CornerTypes.OUTER_CORNER);
		
		// Charge les images des 4 joints horizontaux de zone
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.HORIZONTAL_JOINT);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_RIGHT, CornerTypes.HORIZONTAL_JOINT);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_LEFT, CornerTypes.HORIZONTAL_JOINT);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_RIGHT, CornerTypes.HORIZONTAL_JOINT);
		
		// Charge les images des 4 joints verticaux de zone
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.VERTICAL_JOINT);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.TOP_RIGHT, CornerTypes.VERTICAL_JOINT);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_LEFT, CornerTypes.VERTICAL_JOINT);
		loadBorderImage(pack, atlas, atlasPackName, BordersAndCorners.BOTTOM_RIGHT, CornerTypes.VERTICAL_JOINT);
		
		// Charge les animations
		pack.ownedAnim = atlas.findAnimation(atlasPackName + "_owned", frameDuration, true);
		pack.controledAnim = atlas.findAnimation(atlasPackName + "_controled", frameDuration, true);
		pack.selectedAnim = atlas.findAnimation(atlasPackName + "_selected", frameDuration, true);
		pack.conquestAnim = atlas.findAnimation(atlasPackName + "_conquest", frameDuration, true);
		
		return pack;
	}

	private void loadBorderImage(MarkerPack pack, TextureAtlasEx atlas, String atlasPackPrefix, BordersAndCorners border, CornerTypes cornerType) {
		// Construit le nom de la région à récupérer à partir du bord et du coin courants
		String borderRegionName;
		if (cornerType != CornerTypes.NONE) {
			borderRegionName = atlasPackPrefix
					+ "_zone_" + border.name().toLowerCase()
					+ "_" + cornerType.name().toLowerCase();
		} else {
			borderRegionName = atlasPackPrefix
					+ "_zone_" + border.name().toLowerCase();
		}
		
		// Récupère la région
		TextureRegion region = atlas.findRegion(borderRegionName);
		
		// Stocke la région
		if (region != null) {
			pack.zoneEdges.put(border, cornerType, new TextureRegionDrawable(region));
		}
	}

}
