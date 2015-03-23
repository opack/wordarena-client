package com.slamdunk.wordarena;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
	private static final String MARKER_PACK_PREFIX = "marker";
	private static final String CELL_TYPE_PREFIX = "type";
	
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
	public static TextureAtlas atlas;
	public static Map<String, MarkerPack> markerPacks;
	public static DoubleEntryArray<CellTypes, Boolean/*selected?*/, TextureRegionDrawable> cellTypes;

	public static TextureRegionDrawable edge_h;
	public static TextureRegionDrawable edge_v;
	public static TextureRegionDrawable edge_h_highlighted;
	public static TextureRegionDrawable edge_v_highlighted;
	
	public static TextureRegionDrawable wall_h;
	public static TextureRegionDrawable wall_v;
	
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
		atlas = new TextureAtlas("textures/wordarena.txt");
		
		// Charge les marker-packs
		loadMarkerPacks();
		
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
	
	private static void loadMarkerPacks() {
		// Charge la liste des marker-packs
		final String[] packList = appProperties.getStringArrayProperty("markerpacks", ",");
		
		// Charge les cellules et les bords
		markerPacks = new HashMap<String, MarkerPack>();
		
		MarkerPack pack;
		for (String packName : packList) {
			// Crée le pack
			pack = new MarkerPack();
			pack.name = packName;
			
			// Charge les images des cellules
			putMarkerPackImage(pack, CellStates.OWNED, Boolean.FALSE);
			putMarkerPackImage(pack, CellStates.OWNED, Boolean.TRUE);
			putMarkerPackImage(pack, CellStates.CONTROLED, Boolean.FALSE);
			putMarkerPackImage(pack, CellStates.CONTROLED, Boolean.TRUE);
			
			// Charge le style de label
			pack.labelStyle = skin.get(MARKER_PACK_PREFIX + "_" + packName, LabelStyle.class);
			
			// Enregistre le pack
			markerPacks.put(packName, pack);
		}
	}
	
	private static void putMarkerPackImage(final MarkerPack pack, final CellStates state, Boolean selected) {
		final String regionName = formatMarkerPackCellRegionName(pack.name, state, selected);
		final TextureRegion region = atlas.findRegion(regionName);
		fixBleeding(region);
		pack.cell.put(state, selected, region);
	}
	
	private static void loadCellTypes() {
		cellTypes = new DoubleEntryArray<CellTypes, Boolean, TextureRegionDrawable>();
		
		for (CellTypes type : CellTypes.values()) {
			putCellTypeImage(type, Boolean.FALSE);
			putCellTypeImage(type, Boolean.TRUE);
		}
	}
	
	private static void putCellTypeImage(final CellTypes type, Boolean selected) {
		final String regionName = formatCellTypeRegionName(type.name(), selected);
		final TextureRegion region = atlas.findRegion(regionName);
		if (region == null) {
			throw new IllegalStateException("Missing image " + regionName + " in atlas !");
		}
		fixBleeding(region);
		cellTypes.put(type, selected, new TextureRegionDrawable(region));
	}
	
	/**
	 * Permet de corriger le texture bleeding en décalant les coordonnées de la région d'un demi-pixel.
	 * Cette méthode vient de http://www.wendytech.de/2012/08/fixing-bleeding-in-libgdxs-textureatlas/.
	 * @param region
	 */
	public static void fixBleeding(TextureRegion region) {
		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);       
	}

	/**
	 * Retourne le nom d'une région d'une cellule en fonction du nom du pack et de l'état de la
	 * cellule pour lequel on souhaite récupérer l'image du pack dans l'atlas
	 * @param pack
	 * @param state
	 * @param selected
	 * @return
	 */
	private static String formatMarkerPackCellRegionName(String pack, CellStates state, boolean selected) {
		return MARKER_PACK_PREFIX + "_"
			+ pack + "_"
			+ state.name().toLowerCase() + "_"
			+ (selected ? "selected" : "normal");
	}
	
	/**
	 * Retourne le nom d'une région d'une cellule en fonction du type et de l'état de sélection
	 * @param type
	 * @param selected
	 * @return
	 */
	private static String formatCellTypeRegionName(String type, boolean selected) {
		return CELL_TYPE_PREFIX + "_"
			+ type + "_"
			+ (selected ? "selected" : "normal");
	}
	
	private static void disposeAtlas() {
		atlas.dispose();
	}

	/**
	 * Retourne l'image de propriétaire pour le pack et l'état de la cellule indiqués.
	 * @param data
	 * @return
	 */
	public static TextureRegion getCellOwnerImage(CellData data) {
		return markerPacks.get(data.owner.markerPack).cell.get(data.state, data.selected);
	}
	
	/**
	 * Retourne l'image de type de cellule pour l'état de la cellule indiqué.
	 * @param data
	 * @return
	 */
	public static TextureRegionDrawable getCellTypeImage(CellData data) {
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
