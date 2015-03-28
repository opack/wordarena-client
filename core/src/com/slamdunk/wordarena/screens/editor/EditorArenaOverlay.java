package com.slamdunk.wordarena.screens.editor;

import com.slamdunk.wordarena.actors.ApplyToolListener;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;

public class EditorArenaOverlay extends ArenaOverlay {
	
	public void setArenaName(String name) {
		getData().name = name;
	}
	
	/**
	 * Change la skin et la charge
	 * @param skin
	 */
	public void setArenaSkin(String skin) {
		getData().skin = skin;
		Assets.loadArenaSkin(skin);
	}
	
	public void setArenaSize(int width, int height) {
		getData().width = width;
		getData().height = height;
	}
	
	public void createEmptyArena() {
		recreateCells();
		getData().walls.clear();
		getData().zones.clear();
		
		super.resetArena();
	}

	public void recreateCells() {
		ArenaData arena = getData();
		
		arena.cells = new ArenaCell[arena.width][arena.height];
		
		ArenaCell cell;
		CellData data;
		for (int y = arena.height - 1; y >= 0; y--) {
			for (int x = 0; x < arena.width; x++) {
				cell = new ArenaCell(Assets.skin);
				cell.addListener(new ApplyToolListener((EditorScreen)getScreen(), cell));
				arena.cells[x][y] = cell;
				
				// Définition des données du modèle
				data = cell.getData();
				data.position.setXY(x, y);
				data.state = CellStates.OWNED;
				
				data.type = CellTypes.L;
				data.planLetter = Letters.FROM_TYPE.label;
				data.letter = Letters.FROM_TYPE;
				data.power = 1;
				data.owner = Player.NEUTRAL;
				
				// Placement de la cellule dans le monde et mise à jour du display
				cell.setPosition(x * cell.getWidth(), y * cell.getHeight());
				cell.updateDisplay();
			}
		}
	}

	public ArenaZone createZone(String id) {
		ArenaZone zone = new ArenaZone(null, id);
		getData().zones.add(zone);
		getArenaGroup().addActor(zone);
		return zone;
	}
}
