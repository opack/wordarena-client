package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.lang.KeyListMap;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.actors.CellSelectionListener;
import com.slamdunk.wordarena.actors.WallBuilder;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.ArenaBuilder;
import com.slamdunk.wordarena.data.arena.ArenaData;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.screens.arena.celleffects.CellEffectsManager;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArenaOverlay extends WorldOverlay {
	private ArenaData data;
	
	private Image background;
	private GroupEx arenaGroup;
	private GroupEx cellsGroup;
	private Group wallsGroup;
	private Group zonesGroup;
	
	private CellEffectsManager cellEffectsManager;
	private MatchManager matchManager;
	
	private CellActor[][] cells;
	private Map<String, ZoneActor> zones;
	
	public ArenaOverlay(MatchManager matchManager) {
		this.matchManager = matchManager;
		
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));

		background = new Image();
		getWorld().addActor(background);
		
		arenaGroup = new GroupEx();
		getWorld().addActor(arenaGroup);
		
		cellsGroup = new GroupEx();
		cellsGroup.setTouchable(Touchable.childrenOnly);
		arenaGroup.addActor(cellsGroup);
		
		wallsGroup = new Group();
		wallsGroup.setTouchable(Touchable.disabled);
		arenaGroup.addActor(wallsGroup);
		
		zonesGroup = new Group();
		zonesGroup.setTouchable(Touchable.disabled);
		arenaGroup.addActor(zonesGroup);
		
		cellEffectsManager = new CellEffectsManager();
		arenaGroup.addActor(cellEffectsManager);
		
		zones = new HashMap<String, ZoneActor>();
	}
	
	public ArenaData getData() {
		return data;
	}
	
	protected void setData(ArenaData data) {
		this.data = data;
	}
	
	public CellEffectsManager getCellEffectsManager() {
		return cellEffectsManager;
	}
	
	public void setMatchManager(MatchManager matchManager) {
		this.matchManager = matchManager;
	}

	/**
	 * Crée l'arène de jeu
	 */
	public void buildArena(String plan) {
		// Charge le plan
		JsonValue json = new JsonReader().parse(Gdx.files.internal(plan));
		
		// Crée les données de l'arène à partir du plan
		ArenaBuilder builder = new ArenaBuilder();
		if (getScreen() instanceof EditorScreen) {
			builder.setEditorScreen((EditorScreen)getScreen());
		}
		builder.load(json);
		data = builder.build();
			
		// Construit l'arène
		resetArena();
	}
	
	/**
	 * Charge les données d'arène spécifiées
	 * @param arenaData
	 */
	public void loadArena(ArenaData arenaData) {
		this.data = arenaData;
		resetArena();
	}
	
	protected GroupEx getArenaGroup() {
		return arenaGroup;
	}
		
	/**
	 * Reconstruit l'arène à partir des données de l'ArenaData
	 */
	public void resetArena() {
		// Modifie le fond de l'arène
		resetBackground();
		
		// Ajoute les cellules
		resetCells();
		
		// Ajoute les murs
		resetWalls();
		
		// Ajoute les zones
		resetZones();
		
		// Centre l'arène dans la zone d'affichage
		centerArena();
	}
	
	private void resetBackground() {
		background.setDrawable(Assets.arenaSkin.background);
		background.setBounds(0, 0, WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
	}

	/**
	 * Reconstruit les cellules à partir des données de l'ArenaData
	 */
	public void resetCells() {
		final WordSelectionHandler wordSelectionHandler = matchManager.getWordSelectionHandler();
		
		cellsGroup.clear();
		cells = new CellActor[data.width][data.height];
		
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				// Construction de la cellule
				CellActor cell = new CellActor(Assets.uiSkinDefault, data.cells[x][y], matchManager);
				cell.addListener(new CellSelectionListener(cell, wordSelectionHandler));
			
				// Placement de la cellule dans le monde
				cell.setPosition(x * cell.getWidth(), y * cell.getHeight());
				cell.updateDisplay();
				
				// Ajout de la cellule à l'arène
				cells[x][y] = cell;
				cellsGroup.addActor(cell);
			}
		}
		
		// Dimensionne les autres couches pour qu'elles soient superposées
		// afin que toutes les coordonnées des éléments de l'arène soient
		// relatives à la cellule (0;0)
		final float width = cellsGroup.getWidth();
		final float height = cellsGroup.getWidth();
		wallsGroup.setBounds(0, 0, width, height);
		zonesGroup.setBounds(0, 0, width, height);
		arenaGroup.setBounds(0, 0, width, height);
	}
	
	/**
	 * Reconstruit les murs à partir des données de l'ArenaData
	 */
	public void resetWalls() {
		wallsGroup.clear();
		Actor wallActor;
		for (CellData cellData1 : data.walls.getEntries1()) {
			for (CellData cellData2 : data.walls.getEntries2(cellData1)) {
				CellActor cell1 = cells[cellData1.position.getX()][cellData1.position.getY()];
				CellActor cell2 = cells[cellData2.position.getX()][cellData2.position.getY()];
				
				wallActor = WallBuilder.buildWall(cell1, cell2);
				if (wallActor != null) {
					wallsGroup.addActor(wallActor);
				}
			}
		}
	}
	
	/**
	 * Ajoute dynamiquement un mur à l'arène
	 * @param cell1
	 * @param cell2
	 */
	public void addWall(CellActor cell1, CellActor cell2) {
		// Mise à jour du modèle
		data.addWall(cell1.getData(), cell2.getData());
		
		// Mise à jour de la vue
		Actor wallActor = WallBuilder.buildWall(cell1, cell2);
		if (wallActor != null) {
			wallsGroup.addActor(wallActor);
		}
	}
	
	/**
	 * Indique s'il y a un mur entre les 2 cellules spécifiées
	 * @param cell1
	 * @param cell2
	 */
	public boolean hasWall(CellActor cell1, CellActor cell2) {
		// Mise à jour du modèle
		return data.hasWall(cell1.getData(), cell2.getData());
	}
	
	/**
	 * Retire dynamiquement un mur à l'arène
	 * @param cell1
	 * @param cell2
	 */
	public void removeWall(CellActor cell1, CellActor cell2) {
		// Mise à jour du modèle
		data.removeWall(cell1.getData(), cell2.getData());
		
		// Mise à jour de la vue
		resetWalls();
	}
	
	/**
	 * Reconstruit les zones à partir des données de l'ArenaData
	 */
	public void resetZones() {
		
		// Organise les cellules par zones
		KeyListMap<String, CellActor> cellsByZone = new KeyListMap<String, CellActor>();
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				// Regroupe les cellules par zone
				// DBG Faut-il laisser les cellules sans zone dans la zone_none ou pas ?
//				if (!ZONE_NONE.equals(zones[x][y])) {
					cellsByZone.putValue(data.cells[x][y].zone, cells[x][y]);
//				}
			}
		}
		
		zonesGroup.clear();
		zones.clear();
		
		// Construit les zones
		ZoneActor zone;
		List<CellActor> cells;
		for (ZoneData zoneData : data.zones) {
			// Crée la zone. Si c'est la zone none, alors on l'a déjà
			if (zoneData.id.equals(ZoneActor.NONE.getData().id)) {
				ZoneActor.NONE.setData(zoneData);
				zone = ZoneActor.NONE;
			} else {
				zone = new ZoneActor(matchManager, zoneData);
			}
			
			// Affecte les cellules à la zone
			cells = cellsByZone.get(zoneData.id);
			if (cells != null) {
				for (CellActor cell : cells) {
					zone.addCell(cell);
				}
			}
			
			// Met à jour la zone et son owner
			zone.update();
			
			// Ajoute la zone à l'overlay
			zones.put(zoneData.id, zone);
			zonesGroup.addActor(zone);
		}
	}

	protected void centerArena() {
		arenaGroup.updateBounds();
		arenaGroup.setX(Math.max(0, (int)((WordArenaGame.SCREEN_WIDTH - arenaGroup.getWidth()) / 2)));
		arenaGroup.setY(Math.max(0, (int)((672 - arenaGroup.getHeight()) / 2)));
	}

	/**
	 * Change le propriétaire des cellules indiquées et celui des zones
	 * les contenant le cas échéant
	 * @param cells
	 * @param owner
	 */
	public void setCellsOwner(List<CellActor> cells, PlayerData owner) {
		// Change le propriétaire des cellules et note les zones impactées
		Set<String> impactedZones = new HashSet<String>();
		ZoneActor zone;
		CellData cellData;
		for (CellActor cell : cells) {
			cellData = cell.getData();
			cellData.ownerPlace = owner.place;
			cellData.state = CellStates.OWNED;
			// On ne fait pas d'updateDisplay() car le rafraîchissement de la zone le fera
			
			zone = zones.get(cellData.zone);
			if (zone != null) {
				impactedZones.add(cellData.zone);
			}
		}
		
		// Change le propriétaire des zones
		for (String impactedZone : impactedZones) {
			zone = zones.get(impactedZone);
			if (zone != null) {
				zone.updateOwner();
			}
		}
	}

	/**
	 * Affiche ou masque l'arène
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		getWorld().setVisible(visible);
	}
	
	/**
	 * Affiche ou masque le lettres
	 * @param visible
	 */
	public void showLetters(boolean show) {
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				cells[x][y].showLetter(show);
			}
		}
	}
	
	/**
	 * Si le joueur n'a pas encore joué, il peut demander de nouvelles
	 * lettres dans sa zone de départ
	 */
	public void refreshStartingZone(PlayerData owner) {
		// Recherche la zone de cet owner
		for (ZoneActor zone : zones.values()) {
			if (owner.place == zone.getData().ownerPlace) {
				// Tire de nouvelles lettres pour les cellules de cette zone
				CellData cellData;
				for (CellActor cell : zone.getCells()) {
					cellData = cell.getData();
					cellData.letter = ArenaBuilder.chooseLetter(cellData.type, cellData.planLetter, data.letterDeck);
					cell.updateDisplay();
				}
				// Les lettres de la zone ont été changées. On suppose qu'il n'y a
				// qu'une seule zone de départ. Inutile donc de continuer à traiter
				// les autres zones.
				break;
			}
		}
	}

	/**
	 * Active ou désactive la sélection de cellules
	 * @param enabled
	 */
	public void enableCellSelection(boolean enabled) {
		if (enabled) {
			arenaGroup.setTouchable(Touchable.childrenOnly);
		} else {
			arenaGroup.setTouchable(Touchable.disabled);
		}
	}

	public CellActor getCell(CellData data) {
		if (data == null || data.position == null) {
			return null;
		}
		return getCell(data.position.getX(), data.position.getY());
	}
	
	public CellActor getCell(int x, int y) {
		if (!data.isValidPos(x, y)) {
			return null;
		}
		return cells[x][y];
	}

	public ZoneActor getZone(String id) {
		return zones.get(id);
	}
	
	/**
	 * Retourne une liste des 8 voisins de la cellule indiquée
	 * @param cell
	 * @param listToFill Liste qui contiendra les résultats
	 * @return
	 */
	public void getNeighbors8(CellActor cell, List<CellActor> listToFill) {
		final int x = cell.getData().position.getX();
		final int y = cell.getData().position.getY();
		
		addCellIfValidPos(x - 1, y - 1, listToFill);
		addCellIfValidPos(x + 0, y - 1, listToFill);
		addCellIfValidPos(x + 1, y - 1, listToFill);
		
		addCellIfValidPos(x - 1, y + 0, listToFill);
		addCellIfValidPos(x + 1, y + 0, listToFill);
		
		addCellIfValidPos(x - 1, y + 1, listToFill);
		addCellIfValidPos(x + 0, y + 1, listToFill);
		addCellIfValidPos(x + 1, y + 1, listToFill);
	}
	
	/**
	 * Retourne une liste des 4 voisins (haut, bas, gauche, droite) 
	 * de la cellule indiquée
	 * @param cell
	 * @param listToFill Liste qui contiendra les résultats
	 * @return
	 */
	public void getNeighbors4(CellActor cell, List<CellActor> listToFill) {
		final int x = cell.getData().position.getX();
		final int y = cell.getData().position.getY();
		
		addCellIfValidPos(x + 0, y - 1, listToFill);
		
		addCellIfValidPos(x - 1, y + 0, listToFill);
		addCellIfValidPos(x + 1, y + 0, listToFill);
		
		addCellIfValidPos(x + 0, y + 1, listToFill);
	}
	
	private void addCellIfValidPos(int x, int y, List<CellActor> listToFill) {
		if (data.isValidPos(x, y)) {
			listToFill.add(cells[x][y]);
		}
	}
}
