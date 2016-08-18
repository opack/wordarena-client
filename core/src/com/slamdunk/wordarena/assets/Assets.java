package com.slamdunk.wordarena.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.data.arena.ArenaSkin;
import com.slamdunk.wordarena.data.arena.cell.MarkerPack;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.MySkin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Assets {
	public static final String MARKER_PACK_NEUTRAL = "neutral";
	public static final String MARKER_PACK_EDITOR = "editor";
	
	private static final String SKIN_FILE_DEFAULT = "skins/ui_default/ui_default.json";
	private static final String SKIN_FILE_SPECIFIC = "skins/ui_specific/ui_specific.json";
	
	public static TypedProperties appProperties;
	public static I18NBundle i18nBundle;
	public static ResourceManager overlap2dResourceManager;
	public static Skin uiSkinDefault;
	public static Skin uiSkinSpecific;

	/**
	 * Overlap2D ne charge pas les skins. On doit donc surcharger la méthode getSkin() du ResourceManager
	 * pour retourner une skin qu'on aura chargé nous même afin d'avoir la possibilité d'utiliser les
	 * widgets de l'UI standard de LibGDX.
	 * On crée donc un MySkin et on y met notre skin.
	 */
	private static MySkin specialSkinForOverlap;
	public static com.slamdunk.toolkit.graphics.TextureAtlasEx atlas;
	public static Map<String, MarkerPack> markerPacks;
	public static ArenaSkin arenaSkin;	
	
	public static Animation explosionAnim;
	public static Animation breakGlassAnim;
	
	private static ArenaSkinLoader arenaSkinLoader;
	
	public static void load () {
		loadAppProperties();
		loadI18N();
		//DBGloadOverlapResources();
		loadAtlas();
		loadSkin();
		loadMarkerPacks();
	}
	
	public static void dispose () {
		disposeArenaSkinLoader();
		disposeAtlas();
		disposeSkin();
		disposeOverlapResources();
	}
	
	private static void loadAppProperties() {
		appProperties = new TypedProperties("wordarena.properties");
	}
	
	private static void loadAtlas() {
		atlas = new com.slamdunk.toolkit.graphics.TextureAtlasEx("textures/general.atlas");

		// Charge les animations diverses
		final float frameDuration = appProperties.getFloatProperty("anim.frameDuration", 0.125f);
		explosionAnim = atlas.findAnimation("explosion", frameDuration, true);
		breakGlassAnim = explosionAnim; // TODO Faire une explosion de verre
	}

	private static void loadI18N() {
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
		specialSkinForOverlap = new MySkin(Gdx.files.internal(SKIN_FILE_DEFAULT));
		overlap2dResourceManager = new ResourceManager() {
			@Override
			public MySkin getSkin() {
				return specialSkinForOverlap;
			}
		};
		overlap2dResourceManager.initAllResources();
	}
	
	private static void disposeOverlapResources() {
		if (overlap2dResourceManager != null) {
			overlap2dResourceManager.dispose();
		}
	}

	private static void loadSkin() {
		uiSkinDefault = new Skin(Gdx.files.internal(SKIN_FILE_DEFAULT));
		uiSkinSpecific = new Skin(Gdx.files.internal(SKIN_FILE_SPECIFIC), atlas);
		NinePatch dbg = atlas.createPatch("ui/button_lambda_normal");
	}
	
	private static void disposeSkin() {
		uiSkinDefault.dispose();
	}

	private static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void playSound (Sound sound) {
		if (SlamSettings.SFX_ACTIVATED.get()) {
			sound.play(SlamSettings.SFX_VOLUME.get());
		}
	}
	
	private static void loadMarkerPacks() {
		final float frameDuration = appProperties.getFloatProperty("anim.frameDuration", 0.125f);

		// Crée l'objet chargé de parcourir l'atlas et la skin pour créer les packs
		MarkerPackLoader packLoader = new MarkerPackLoader(uiSkinDefault, frameDuration);
		
		// Charge la liste des marker-packs
		final String[] packList = appProperties.getStringArrayProperty("markerpacks", ",");

		// Charge et enregistre le pack de la liste
		markerPacks = new HashMap<String, MarkerPack>();
		for (String packName : packList) {
			markerPacks.put(packName, packLoader.load(packName, atlas));
		}
	}
	
	private static void disposeAtlas() {
		atlas.dispose();
	}
	
	public static void loadArenaSkin(String name) {
		if (arenaSkinLoader == null) {
			arenaSkinLoader = new ArenaSkinLoader();
		}
		arenaSkin = arenaSkinLoader.load(name);
	}
	
	private static void disposeArenaSkinLoader() {
		if (arenaSkinLoader != null) {
			arenaSkinLoader.dispose();
		}
	}
}
