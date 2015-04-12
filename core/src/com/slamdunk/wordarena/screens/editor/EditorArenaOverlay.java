package com.slamdunk.wordarena.screens.editor;

import com.slamdunk.wordarena.actors.ApplyToolListener;
import com.slamdunk.wordarena.actors.CellActor;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.ArenaData;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;
import com.slamdunk.wordarena.screens.arena.MatchManager;

public class EditorArenaOverlay extends ArenaOverlay {
	
	public EditorArenaOverlay(MatchManager matchManager) {
		super(matchManager);
	}

	public void createEmptyArena(int width, int height, String skin) {
		Assets.loadArenaSkin(skin);
		
		ArenaData arenaData = new ArenaData();
		arenaData.width = width;
		arenaData.height = height;
		arenaData.skin = skin;
		
		createEmptyCells(arenaData);
		arenaData.zones.add(ZoneActor.NONE.getData());
		
		setData(arenaData);
		
		super.resetArena();
	}

	private void createEmptyCells(ArenaData arenaData) {
		arenaData.cells = new CellData[arenaData.width][arenaData.height];
		
		CellData data;
		for (int y = arenaData.height - 1; y >= 0; y--) {
			for (int x = 0; x < arenaData.width; x++) {
				data = new CellData();
				arenaData.cells[x][y] = data;
				
				// Définition des données du modèle
				data.position.setXY(x, y);
				data.state = CellStates.OWNED;
				
				data.type = CellTypes.L;
				data.planLetter = Letters.FROM_TYPE.label;
				data.letter = Letters.FROM_TYPE;
				data.power = 1;
				data.ownerPlace = PlayerData.NEUTRAL.place;
				data.zone = ZoneActor.NONE.getData().id;
			}
		}
	}
	
	@Override
	public void resetCells() {
		// Crée les cellules
		super.resetCells();
		
		// Change le listener associé aux cellules
		EditorScreen screen = (EditorScreen)getScreen();
		final int width = getData().width;
		final int height = getData().height;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				CellActor cell = getCell(x, y);
				cell.getListeners().clear(); // TODO Voir si on ne pourrait pas juste ajouter le listener du tool sans faire un clear. Cela laisse la possibilité plus tard de tester l'arène en cours d'édition
				cell.addListener(new ApplyToolListener(screen, cell));
			}
		}
	}

	public ZoneActor createZone(String id) {
		ZoneActor zone = new ZoneActor(null, id);
		getData().zones.add(zone.getData());
		getArenaGroup().addActor(zone);
		return zone;
	}
}
