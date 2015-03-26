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
	private ArenaData arena;
	
	public EditorArenaOverlay() {
		arena = new ArenaData();
		setData(arena);
	}
	
	public void setArenaName(String name) {
		arena.name = name;
	}
	
	public void setArenaSize(int width, int height) {
		arena.width = width;
		arena.height = height;
	}
	
	@Override
	public void resetArena() {
		recreateCells();
		arena.walls.clear();
		arena.zones.clear();
		
		super.resetArena();
		
		centerArena();
	}

	public void recreateCells() {
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
		arena.zones.add(zone);
		getArenaGroup().addActor(zone);
		return zone;
	}
}
