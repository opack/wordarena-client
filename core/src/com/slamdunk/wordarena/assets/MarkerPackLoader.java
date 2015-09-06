package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.wordarena.data.arena.cell.MarkerPack;
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

	public MarkerPack load(String name, com.slamdunk.toolkit.graphics.TextureAtlasEx atlas) {
		// Crée le pack
		MarkerPack pack = new MarkerPack();
		pack.name = name;
		final String atlasPackName = MARKER_PACK_PREFIX + name;
		
		// Charge le style de label
		if (skin.has(atlasPackName, LabelStyle.class)) {
			pack.labelStyle = skin.get(atlasPackName, LabelStyle.class);
		}
		
		// Charge le marqueur de possession
		pack.possessionMarker = atlas.findRegionDrawable(atlasPackName + "_possession", false);
		
		// Charge les animations de cellule
		pack.ownedAnim = atlas.findAnimation(atlasPackName + "_owned", frameDuration, true);
		pack.controledAnim = atlas.findAnimation(atlasPackName + "_controled", frameDuration, true);
		pack.selectedAnim = atlas.findAnimation(atlasPackName + "_selected", frameDuration, true);
		pack.conquestAnim = atlas.findAnimation(atlasPackName + "_conquest", frameDuration, true);
		
		// Charge les images de zone
		loadZoneBorders(pack, atlas, atlasPackName);
		loadZoneJoints(pack, atlas, atlasPackName);
		loadZoneInnerCorners(pack, atlas, atlasPackName);
		loadZoneOuterCorners(pack, atlas, atlasPackName);
		
		return pack;
	}
	
	private void loadZoneJoints(MarkerPack pack, com.slamdunk.toolkit.graphics.TextureAtlasEx atlas, String atlasPackName) {
	// Charge l'image de référence (TOP_LEFT_VERTICAL_JOINT)
		TextureRegion topLeftVerticalJoint = loadBorderRegion(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.VERTICAL_JOINT);
		TextureRegionDrawable topLeftVerticalJointDrawable = new TextureRegionDrawable(topLeftVerticalJoint);
		pack.zoneEdges.put(BordersAndCorners.TOP_LEFT, CornerTypes.VERTICAL_JOINT, topLeftVerticalJointDrawable);
		
		// Procède aux transformations pour créer les autres images
		TextureRegion topRightVerticalJoint = new TextureRegion(topLeftVerticalJoint);
		topRightVerticalJoint.flip(true, false);
		TextureRegionDrawable topRightVerticalJointDrawable = new TextureRegionDrawable(topRightVerticalJoint);
		pack.zoneEdges.put(BordersAndCorners.TOP_RIGHT, CornerTypes.VERTICAL_JOINT, topRightVerticalJointDrawable);
		
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_RIGHT, CornerTypes.VERTICAL_JOINT, topRightVerticalJointDrawable);
		
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_LEFT, CornerTypes.VERTICAL_JOINT, topLeftVerticalJointDrawable);
		
	// Charge l'image de référence (TOP_LEFT_HORIZONTAL_JOINT)
		TextureRegion topLeftHorizontalJoint = loadBorderRegion(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.HORIZONTAL_JOINT);
		TextureRegionDrawable topLeftHorizontalJointDrawable = new TextureRegionDrawable(topLeftHorizontalJoint);
		pack.zoneEdges.put(BordersAndCorners.TOP_LEFT, CornerTypes.HORIZONTAL_JOINT, topLeftHorizontalJointDrawable);
		
		// Procède aux transformations pour créer les autres images
		pack.zoneEdges.put(BordersAndCorners.TOP_RIGHT, CornerTypes.HORIZONTAL_JOINT, topLeftHorizontalJointDrawable);
		
		TextureRegion bottomRightHorizontalJoint = new TextureRegion(topLeftHorizontalJoint);
		bottomRightHorizontalJoint.flip(false, true);
		TextureRegionDrawable bottomRightHorizontalJointDrawable = new TextureRegionDrawable(bottomRightHorizontalJoint);
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_RIGHT, CornerTypes.HORIZONTAL_JOINT, bottomRightHorizontalJointDrawable);
		
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_LEFT, CornerTypes.HORIZONTAL_JOINT, bottomRightHorizontalJointDrawable);
	}
	
	private void loadZoneInnerCorners(MarkerPack pack, com.slamdunk.toolkit.graphics.TextureAtlasEx atlas, String atlasPackName) {
		// Charge l'image de référence (TOP_LEFT_INNER_CORNER)
		TextureRegion topLeftInner = loadBorderRegion(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.INNER_CORNER);
		TextureRegionDrawable topLeftInnerDrawable = new TextureRegionDrawable(topLeftInner);
		pack.zoneEdges.put(BordersAndCorners.TOP_LEFT, CornerTypes.INNER_CORNER, topLeftInnerDrawable);
		
		// Flip pour créer les autres images des coins intérieurs
		TextureRegion topRightInner = new TextureRegion(topLeftInner);
		topRightInner.flip(true, false);
		TextureRegionDrawable topRightInnerDrawable = new TextureRegionDrawable(topRightInner);
		pack.zoneEdges.put(BordersAndCorners.TOP_RIGHT, CornerTypes.INNER_CORNER, topRightInnerDrawable);
		
		TextureRegion bottomRightInner = new TextureRegion(topLeftInner);
		bottomRightInner.flip(true, true);
		TextureRegionDrawable bottomRightInnerDrawable = new TextureRegionDrawable(bottomRightInner);
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_RIGHT, CornerTypes.INNER_CORNER, bottomRightInnerDrawable);
		
		TextureRegion bottomLeftInner = new TextureRegion(topLeftInner);
		bottomLeftInner.flip(false, true);
		TextureRegionDrawable bottomLeftInnerDrawable = new TextureRegionDrawable(bottomLeftInner);
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_LEFT, CornerTypes.INNER_CORNER, bottomLeftInnerDrawable);
	}
	
	private void loadZoneOuterCorners(MarkerPack pack, com.slamdunk.toolkit.graphics.TextureAtlasEx atlas, String atlasPackName) {
		// Charge l'image de référence (TOP_LEFT_OUTER_CORNER)
		TextureRegion topLeftOuter = loadBorderRegion(pack, atlas, atlasPackName, BordersAndCorners.TOP_LEFT, CornerTypes.OUTER_CORNER);
		TextureRegionDrawable topLeftOuterDrawable = new TextureRegionDrawable(topLeftOuter);
		pack.zoneEdges.put(BordersAndCorners.TOP_LEFT, CornerTypes.OUTER_CORNER, topLeftOuterDrawable);
		
		// Flip pour créer les autres images des coins intérieurs
		TextureRegion topRightOuter = new TextureRegion(topLeftOuter);
		topRightOuter.flip(true, false);
		TextureRegionDrawable topRightOuterDrawable = new TextureRegionDrawable(topRightOuter);
		pack.zoneEdges.put(BordersAndCorners.TOP_RIGHT, CornerTypes.OUTER_CORNER, topRightOuterDrawable);
		
		TextureRegion bottomRightOuter = new TextureRegion(topLeftOuter);
		bottomRightOuter.flip(true, true);
		TextureRegionDrawable bottomRightOuterDrawable = new TextureRegionDrawable(bottomRightOuter);
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_RIGHT, CornerTypes.OUTER_CORNER, bottomRightOuterDrawable);
		
		TextureRegion bottomLeftOuter = new TextureRegion(topLeftOuter);
		bottomLeftOuter.flip(false, true);
		TextureRegionDrawable bottomLeftOuterDrawable = new TextureRegionDrawable(bottomLeftOuter);
		pack.zoneEdges.put(BordersAndCorners.BOTTOM_LEFT, CornerTypes.OUTER_CORNER, bottomLeftOuterDrawable);
	}

	private void loadZoneBorders(MarkerPack pack, com.slamdunk.toolkit.graphics.TextureAtlasEx atlas, String atlasPackName) {
		// Charge l'image de référence (LEFT)
		TextureRegion leftBorder = loadBorderRegion(pack, atlas, atlasPackName, BordersAndCorners.LEFT, CornerTypes.NONE);
		pack.zoneEdges.put(BordersAndCorners.LEFT, CornerTypes.NONE, new TextureRegionDrawable(leftBorder));
		
		// Procède au flip pour créer l'image de droite
		TextureRegion rightBorder = new TextureRegion(leftBorder);
		rightBorder.flip(true, false);		
		pack.zoneEdges.put(BordersAndCorners.RIGHT, CornerTypes.NONE, new TextureRegionDrawable(rightBorder));
		
		// Charge l'image de référence (TOP)
		TextureRegion topBorder = loadBorderRegion(pack, atlas, atlasPackName, BordersAndCorners.TOP, CornerTypes.NONE);
		pack.zoneEdges.put(BordersAndCorners.TOP, CornerTypes.NONE, new TextureRegionDrawable(topBorder));
		
		// Procède au flip pour créer l'image du bas
		TextureRegion bottomBorder = new TextureRegion(topBorder);
		bottomBorder.flip(false, true);		
		pack.zoneEdges.put(BordersAndCorners.BOTTOM, CornerTypes.NONE, new TextureRegionDrawable(bottomBorder));
	}
	
	private AtlasRegion loadBorderRegion(MarkerPack pack, com.slamdunk.toolkit.graphics.TextureAtlasEx atlas, String atlasPackPrefix, BordersAndCorners border, CornerTypes cornerType) {
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
		return atlas.findRegion(borderRegionName);
	}

}
