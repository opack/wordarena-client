package com.slamdunk.wordarena.data.arena;

import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Construit une arène à partir d'un plan
 */
public class ArenaBuilder {
	public static final String ZONE_NONE = ZoneActor.NONE.getData().id;
	
	/**
	 * Caractère qui sépare l'info de 2 cellules dans le Json
	 */
	private static final String CELL_SEPARATOR = " ";
	
	/**
	 * Caractère qui sépare l'x et l'y d'un point dans le Json
	 */
	private static final String POSITION_SEPARATOR = ";";
	
	/**
	 * Indique si on est en train de construire une arène pour l'éditeur
	 * ou pour jouer.
	 */
	private EditorScreen editorScreen;
	
	private String arenaSkinName;
	private String[][] types;
	private String[][] letters;
	private int[][] powers;
	private int[][] owners;
	private String[][] zones;
	private List<Point> walls;
	
	private ArenaData arena;
	
	private Set<String> zonesNames;
	private Set<Point> cellsWithWalls;
	
	public ArenaBuilder() {
		arena = new ArenaData();
	}
	
	public EditorScreen getEditorScreen() {
		return editorScreen;
	}
	
	public void setArenaSkinName(String arenaSkinName) {
		this.arenaSkinName = arenaSkinName;
	}

	public void setEditorScreen(EditorScreen editorScreen) {
		this.editorScreen = editorScreen;
	}

	public void setTypes(String[][] types) {
		this.types = types;
	}

	public void setLetters(String[][] letters) {
		this.letters = letters;
	}

	public void setPowers(int[][] powers) {
		this.powers = powers;
	}

	public void setOwners(int[][] owners) {
		this.owners = owners;
	}

	public void setZones(String[][] zones) {
		this.zones = zones;
	}
	
	public void setWalls(List<Point> walls) {
		this.walls = walls;
	}

	/**
	 * Crée des cellules pour constituer une arène ayant
	 * la structure correspondant au plan fourni.
	 * @param plan
	 * @return true si le plan a pu être chargé, false sinon
	 */
	public boolean load(JsonValue plan) {
		setSize(plan.getInt("width", 1), plan.getInt("height", 1));
		if (arena.width == 0 || arena.height == 0) {
			return false;
		}
		
		// Charge le nom de l'arène
		setName(plan.getString("name"));
		
		// Charge le nom de la skin à appliquer
		setArenaSkinName(plan.getString("skin"));
		
		// Charge les types de cellule
		setTypes(extractStringTable(plan.get("types")));
		
		// Charge les lettres initiales.
		setLetters(extractStringTable(plan.get("letters")));
		
		// Charge les puissances
		setPowers(extractIntTable(plan.get("powers")));
		
		// Charge les possesseurs
		setOwners(extractIntTable(plan.get("owners")));
		
		// Charge les zones
		setZones(extractStringTable(plan.get("zones")));
		
		// Charge les murs
		setWalls(extractPointList(plan.get("walls")));
		
		return true;
	}
	
	private List<Point> extractPointList(JsonValue jsonValue) {
		List<Point> walls = new ArrayList<Point>();
		if (jsonValue != null) {
			String[] cellsPos;
			String[] cellPos;
			int x;
			int y;
			
			for (String wallCells : jsonValue.asStringArray()) {
				cellsPos = wallCells.split(CELL_SEPARATOR);
				
				cellPos = cellsPos[0].split(POSITION_SEPARATOR);
				x = Integer.parseInt(cellPos[0]);
				y = Integer.parseInt(cellPos[1]);			
				walls.add(new Point(x, y));
				
				cellPos = cellsPos[1].split(POSITION_SEPARATOR);
				x = Integer.parseInt(cellPos[0]);
				y = Integer.parseInt(cellPos[1]);			
				walls.add(new Point(x, y));
			}
		}
		return walls;
	}

	public void setSize(int width, int height) {
		arena.width = width;
		arena.height = height;
	}

	public void setName(String name) {
		arena.name = name;
	}

	private String[][] extractStringTable(JsonValue jsonValue) {
		String[] values = jsonValue.asStringArray();
		String[] cols;
		String[][] table = new String[arena.width][arena.height];
		final int maxRow = arena.height - 1;
		for (int row = 0; row < arena.height; row++) {
			cols = values[row].split(CELL_SEPARATOR);
			for (int col = 0; col < arena.width; col++) {
				table[col][maxRow - row] = cols[col];
			}
		}
		return table;
	}
	
	private int[][] extractIntTable(JsonValue jsonValue) {
		String[] values = jsonValue.asStringArray();
		String[] cols;
		int[][] table = new int[arena.width][arena.height];
		final int maxRow = arena.height - 1;
		for (int row = 0; row < arena.height; row++) {
			cols = values[row].split(CELL_SEPARATOR);
			for (int col = 0; col < arena.width; col++) {
				table[col][maxRow - row] = Integer.parseInt(cols[col]);
			}
		}
		return table;
	}

	public ArenaData build() {
		if (editorScreen == null) {
			// Génère des lettres en tenant compte de leur représentation
			arena.letterDeck = new Deck<Letters>(Letters.values(), 1);
		}
		
		// Chargement de la skin
		loadArenaSkin();
		
		// Construction des cellules
		buildCells();
		
		// Construction des murs
		buildWalls();
		
		// Construction des zones
		buildZones();
		
		return arena;
	}

	private void loadArenaSkin() {
		arena.skin = arenaSkinName;
		Assets.loadArenaSkin(arenaSkinName);
	}

	private void buildWalls() {
		arena.walls.clear();
		cellsWithWalls = new HashSet<Point>();
		
		// Crée les murs
		Point pos1;
		Point pos2;
		for (int curPos = 0; curPos < walls.size(); curPos += 2) {
			pos1 = walls.get(curPos);
			pos2 = walls.get(curPos + 1);
			
			// Ajoute le mur
			addWall(pos1, pos2);
			
			// Enregistre les cellules comme possédant des murs
			cellsWithWalls.add(pos1);
			cellsWithWalls.add(pos2);
		}
		
		// Recherche et crée les murs en coin
		createCornerWalls();
	}
	
	/**
	 * Recherche les murs formant des coins ou des murs s'étalant
	 * sur 2 longueurs
	 */
	private void createCornerWalls() {
		Point pos2 = new Point(0, 0);
		Point pos3 = new Point(0, 0);
		for (Point pos1 : cellsWithWalls) {
		// Teste les coins
			
			// Mur avec la cellule du haut ?
			pos2.setXY(pos1.getX(), pos1.getY() + 1);
			boolean hasWallUp = hasWall(pos1, pos2);
			
			// Mur avec la cellule du bas ?
			pos2.setXY(pos1.getX(), pos1.getY() - 1);
			boolean hasWallDown = hasWall(pos1, pos2);
			
			// Mur avec la cellule de gauche ?
			pos2.setXY(pos1.getX() - 1, pos1.getY());
			boolean hasWallLeft = hasWall(pos1, pos2);
			
			// Mur avec la cellule de droite ?
			pos2.setXY(pos1.getX() + 1, pos1.getY());
			boolean hasWallRight = hasWall(pos1, pos2);
			
			// Crée les murs en coin
			if (hasWallUp) {
				if (hasWallLeft) {
					pos2.setXY(pos1.getX() - 1, pos1.getY() + 1);
					addWall(pos1, pos2);
				}
				if (hasWallRight) {
					pos2.setXY(pos1.getX() + 1, pos1.getY() + 1);
					addWall(pos1, pos2);
				}
			} else if (hasWallDown) {
				if (hasWallLeft) {
					pos2.setXY(pos1.getX() - 1, pos1.getY() - 1);
					addWall(pos1, pos2);
				}
				if (hasWallRight) {
					pos2.setXY(pos1.getX() + 1, pos1.getY() - 1);
					addWall(pos1, pos2);
				}
			}
			
		// Teste les murs de 2 longueurs
			
			// Murs du haut sur 2 longueurs avec le voisin de gauche ?
			pos2.setXY(pos1.getX() - 1, pos1.getY());
			pos3.setXY(pos1.getX() - 1, pos1.getY() + 1);
			if (hasWallUp && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
			
			// Murs du haut sur 2 longueurs avec le voisin de droite ?
			pos2.setXY(pos1.getX() + 1, pos1.getY());
			pos3.setXY(pos1.getX() + 1, pos1.getY() + 1);
			if (hasWallUp && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
			
			// Murs du bas sur 2 longueurs avec le voisin de gauche ?
			pos2.setXY(pos1.getX() - 1, pos1.getY());
			pos3.setXY(pos1.getX() - 1, pos1.getY() - 1);
			if (hasWallDown && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
			
			// Murs du bas sur 2 longueurs avec le voisin de droite ?
			pos2.setXY(pos1.getX() + 1, pos1.getY());
			pos3.setXY(pos1.getX() + 1, pos1.getY() - 1);
			if (hasWallDown && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
			
			// Murs de gauche sur 2 longueurs avec le voisin du haut ?
			pos2.setXY(pos1.getX(), pos1.getY() + 1);
			pos3.setXY(pos1.getX() - 1, pos1.getY() + 1);
			if (hasWallLeft && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
			
			// Murs de gauche sur 2 longueurs avec le voisin du bas ?
			pos2.setXY(pos1.getX(), pos1.getY() - 1);
			pos3.setXY(pos1.getX() - 1, pos1.getY() - 1);
			if (hasWallLeft && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
			
			// Murs de droite sur 2 longueurs avec le voisin du haut ?
			pos2.setXY(pos1.getX(), pos1.getY() + 1);
			pos3.setXY(pos1.getX() + 1, pos1.getY() + 1);
			if (hasWallRight && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
			
			// Murs de droite sur 2 longueurs avec le voisin du bas ?
			pos2.setXY(pos1.getX(), pos1.getY() - 1);
			pos3.setXY(pos1.getX() + 1, pos1.getY() - 1);
			if (hasWallRight && hasWall(pos2, pos3)) {
				addWall(pos1, pos3);
			}
		}
	}

	private void addWall(Point pos1, Point pos2) {
		arena.addWall(arena.cells[pos1.getX()][pos1.getY()], arena.cells[pos2.getX()][pos2.getY()]);
	}

	private boolean hasWall(Point pos1, Point pos2) {
		return arena.isValidPos(pos2)
			&& arena.hasWall(arena.cells[pos1.getX()][pos1.getY()], arena.cells[pos2.getX()][pos2.getY()]);
	}

	private void buildZones() {
		arena.zones.clear();
		for (String zoneName : zonesNames) {
			arena.zones.add(new ZoneData(zoneName));
		}
	}

	private void buildCells() {
		zonesNames = new HashSet<String>();
		arena.cells = new CellData[arena.width][arena.height];
		
		CellData data;
		for (int y = arena.height - 1; y >= 0; y--) {
			for (int x = 0; x < arena.width; x++) {
				data = new CellData();
				arena.cells[x][y] = data;
				
				// Définition des données du modèle
				data.position.setXY(x, y);
				data.state = CellStates.OWNED;
				
				data.type = CellTypes.valueOf(types[x][y]);
				data.planLetter = letters[x][y];
				data.letter = chooseLetter(data.type, letters[x][y], arena.letterDeck);
				data.power = choosePower(data.type, powers[x][y]);
				data.ownerPlace = chooseOwner(data.type, owners[x][y]);
				data.zone = zones[x][y];
				zonesNames.add(data.zone);
			}
		}
	}

	/**
	 * Si le plan indique un owner = 0 on doit comprendre qu'il s'agit
	 * du joueur. Or la liste des joueurs ne contient pas le joueur Neutre,
	 * et l'owner 0 est donc le premier joueur réel. Il faut donc
	 * "convertir" l'indice du plan pour qu'il corresponde à la liste
	 * de joueurs.
	 * Ainsi l'owner réel = plan - 1.
	 * De plus, cette méthode s'assure que si la cellule ne peut pas être
	 * possédée, elle sera attribuée au joueur Neutre.
	 * @param cellType
	 * @param ownerIndex
	 * @return
	 */
	private int chooseOwner(CellTypes cellType, int ownerIndex) {
		if (!cellType.canBeOwned()) {
			return PlayerData.NEUTRAL.place;
		}
		/*DBG
		// Dans le plan JSON, le premier joueur réel à l'indice 0 et le joueur
		// NEUTRAL n'apparaîtra pas dans la liste des joueurs du GameData.
		// On doit donc considérer que le joueur 0 du plan correspond au
		// joueur -1 (NEUTRAL), que le joueur 1 du plan correspond au
		// premier joueur (indice 0) etc.
		if (editorScreen == null) {
			return ownerIndex - 1;
		}
		// En mode édition, le joueur NEUTRAL apparaît dans la liste. L'indice
		// mentionné peut donc être utilisé tel quel.
		else {*/
			return ownerIndex;
		//DBG}
	}

	private int choosePower(CellTypes cellType, int power) {
		if (!cellType.hasPower()) {
			return 0;
		}
		return power;
	}

	/**
	 * Retourne la lettre indiquée, ou une lettre tirée dans le tas
	 * si letter = LETTER_FROM_DECK.
	 * @param planLetter
	 * @return
	 */
	public static Letters chooseLetter(CellTypes cellType, String planLetter, Deck<Letters> letterDeck) {
		if (!cellType.hasLetter()) {
			return Letters.EMPTY;
		}

		// Récupère la lettre correspondant au symbole du plan
		Letters letter = Letters.getFromLabel(planLetter);
		
		// Si le symbole indique qu'on doit déduire la lettre du type,
		// on détermine la lettre en fonction du type
		if (Letters.FROM_TYPE == letter) {
			switch (cellType) {
			case B:
			case G:
			case L:
			case S:
				if (letterDeck != null) {
					return letterDeck.draw();
				}
				break;
			case J:
				return Letters.JOKER;
			case V:
				return Letters.EMPTY;
			}
		}
		
		// Si le symbole indique une lettre de l'alphabet : on la retourne
		return letter;
	}
}
