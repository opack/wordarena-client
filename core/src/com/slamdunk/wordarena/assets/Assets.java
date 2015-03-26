package com.slamdunk.wordarena.assets;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.MySkin;

public class Assets {
	private static final String CELL_TYPE_PREFIX = "type_";
	
	public static final String MARKER_PACK_NEUTRAL = "neutral";
	
	public static TypedProperties appProperties;
	public static I18NBundle i18nBundle;
	public static ResourceManager overlap2dResourceManager;
	public static Skin skin;
	
	/**
	 * Overlap2D ne charge pas les skins. On doit donc surcharger la méthode getSkin() du ResourceManager
	 * pour retourner une skin qu'on aura chargé nous même afin d'avoir la possibilité d'utiliser les
	 * widgets de l'UI standard de LibGDX.
	 * On crée donc un MySkin et on y met notre skin.
	 */
	private static MySkin specialSkinForOverlap;
	public static TextureAtlasEx atlas;
	public static Map<String, MarkerPack> markerPacks;
	public static DoubleEntryArray<CellTypes, Boolean/*selected?*/, TextureRegion> cellTypes;

	public static TextureRegionDrawable edge_h;
	public static TextureRegionDrawable edge_v;
	public static TextureRegionDrawable edge_h_highlighted;
	public static TextureRegionDrawable edge_v_highlighted;
	
	public static TextureRegionDrawable wall_h;
	public static TextureRegionDrawable wall_v;
	
	public static Animation explosionAnim;
	public static Animation breakGlassAnim;
	
	public static void load () {
		loadAppProperties();
		loadI18N();
		loadOverlapResources();
		loadSkin();
		loadAtlas();
	}
	
	public static void dispose () {
		disposeAtlas();
		disposeSkin();
		disposeOverlapResources();
	}
	
	private static void loadAppProperties() {
		appProperties = new TypedProperties("wordarena.properties");
	}
	
	public static void loadAtlas() {
		final float frameDuration = appProperties.getFloatProperty("anim.frameDuration", 0.125f);
		
		atlas = new TextureAtlasEx("textures/wordarena.txt");
		
		// Charge les marker-packs
		loadMarkerPacks(frameDuration);
		
		// Charge les images des types
		loadCellTypes();
		
		// Charge les images des bords de zone
		edge_v = new TextureRegionDrawable(atlas.findRegion("zone_edge2_v"));
		edge_h = new TextureRegionDrawable(atlas.findRegion("zone_edge2_h"));
		edge_v_highlighted = new TextureRegionDrawable(atlas.findRegion("zone_edge2_v_highlighted"));
		edge_h_highlighted = new TextureRegionDrawable(atlas.findRegion("zone_edge2_h_highlighted"));
		
		// Charge les images des murs
		wall_v = new TextureRegionDrawable(atlas.findRegion("wall_v"));
		wall_h = new TextureRegionDrawable(atlas.findRegion("wall_h"));
		
		// Charge les animations diverses
		explosionAnim = atlas.findAnimation("explosion", frameDuration, true);
		breakGlassAnim = explosionAnim; // TODO Faire une explosion de verre
	}
	
	public static void loadI18N() {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/WordArena");
		Locale locale = new Locale(SlamSettings.LANGUAGE.get());
		i18nBundle = I18NBundle.createBundle(baseFileHandle, locale);
		// On demande à ce qu'une clé manquante ne provoque pas la levée d'une exception.
		// Le cas échéant, le libellé sera donc la clé avec "???" autour.
		I18NBundle.setExceptionOnMissingKey(false);
	}
	
	private static void loadOverlapResources() {
		// On est obligés de charger une seconde fois la skin car si on utilise ce MySkin pour
		// l'UI de Scene2D, on subit un texture bleeding sur les lettres. Je ne sais pas pourquoi,
		// donc on se retrouve à charger 2 fois la même skin :(
		specialSkinForOverlap = new MySkin(Gdx.files.internal("skins/wordarena/uiskin.json"));
		overlap2dResourceManager = new ResourceManager() {
			@Override
			public MySkin getSkin() {
				return specialSkinForOverlap;
			}
		};
		overlap2dResourceManager.initAllResources();
	}
	
	private static void disposeOverlapResources() {
		overlap2dResourceManager.dispose();
	}

	private static void loadSkin() {
		skin = new Skin(Gdx.files.internal("skins/wordarena/uiskin.json"));
	}
	
	private static void disposeSkin() {
		skin.dispose();
	}

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void playSound (Sound sound) {
		if (SlamSettings.SFX_ACTIVATED.get()) {
			sound.play(SlamSettings.SFX_VOLUME.get());
		}
	}
	
	private static void loadMarkerPacks(float frameDuration) {
		// Crée l'objet chargé de parcourir l'atlas et la skin pour créer les packs
		PackLoader packLoader = new PackLoader(skin, frameDuration);
		
		// Charge la liste des marker-packs
		final String[] packList = appProperties.getStringArrayProperty("markerpacks", ",");

		// Charge et enregistre le pack de la liste
		markerPacks = new HashMap<String, MarkerPack>();
		for (String packName : packList) {
			markerPacks.put(packName, packLoader.load(packName, atlas));
		}
	}
	
	private static void loadCellTypes() {
		cellTypes = new DoubleEntryArray<CellTypes, Boolean, TextureRegion>();
		
		for (CellTypes type : CellTypes.values()) {
			putCellTypeImage(type, Boolean.FALSE);
			putCellTypeImage(type, Boolean.TRUE);
		}
	}
	
	private static void putCellTypeImage(final CellTypes type, Boolean selected) {
		final String regionName = CELL_TYPE_PREFIX + type.name() + "_" + (selected ? "selected" : "normal");
		
		final TextureRegion region = atlas.findRegion(regionName, true);
		if (region == null) {
			throw new IllegalStateException("Missing image " + regionName + " in atlas !");
		}
		
		cellTypes.put(type, selected, region);
	}
	
	private static void disposeAtlas() {
		atlas.dispose();
	}
	
	/**
	 * Retourne l'animation de propriétaire pour le pack et l'état de la cellule indiqués.
	 * @param data
	 * @return
	 */
	public static Animation getCellAnim(CellData data) {
		MarkerPack pack = markerPacks.get(data.owner.markerPack);
		if (data.selected) {
			return pack.selectedAnim;
		} else if (data.state == CellStates.OWNED) {
			return pack.ownedAnim;
		} else if (data.state == CellStates.CONTROLED) {
			return pack.controledAnim;
		}
		// Ce cas ne devrait pas arriver
		return null;
	}
	
	/**
	 * Retourne l'image de type de cellule pour l'état de la cellule indiqué.
	 * @param data
	 * @return
	 */
	public static TextureRegion getCellTypeRegion(CellData data) {
		return cellTypes.get(data.type, data.selected);
	}
	
	/**
	 * Retourne le LabelStyle pour le pack indiqué.
	 * @param pack
	 * @return
	 */
	public static LabelStyle getLabelStyle(String pack) {
		final String markerPack = pack != null ? pack : MARKER_PACK_NEUTRAL;
		return markerPacks.get(markerPack).labelStyle;
	}
}
