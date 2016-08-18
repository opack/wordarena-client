package com.slamdunk.wordarena.data.arena;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

public class ArenaSerializer implements Json.Serializer<ArenaData>{
	private EditorScreen editorScreen;
	
	public void setEditorScreen(EditorScreen editorScreen) {
		this.editorScreen = editorScreen;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void write(Json json, ArenaData arena, Class knownType) {
		StringBuilder sb = new StringBuilder();
		json.writeObjectStart();
		json.writeValue("name", arena.name);
		json.writeValue("skin", arena.skin);
		json.writeValue("width", arena.width);
		json.writeValue("height", arena.height);
		
		// Types de cellules
		json.writeArrayStart("types");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].type).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Lettres initiales
		json.writeArrayStart("letters");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				if (editorScreen != null) {
					// Si on est dans l'éditeur, alors on note la lettre
					// du plan.
					sb.append(arena.cells[x][y].planLetter);
				} else {
					// Si on joue une vraie partie et qu'on souhaite
					// enregistrer l'état actuel, alors on va noter la
					// lettre effectivement affichée dans la cellule.
					sb.append(arena.cells[x][y].letter.label);
				}
				sb.append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Puissances
		json.writeArrayStart("powers");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].power).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Possesseurs
		json.writeArrayStart("owners");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				// Si la cellule n'est pas possédée (donc si elle est juste contrôlée)
				// alors on l'attribue au joueur neutre de façon à ce que dans le plan
				// ce contrôle (qui est déterminé dynamiquement) ne soit pas vu comme
				// une possession.
				if (arena.cells[x][y].state == CellStates.OWNED) {
					sb.append(arena.cells[x][y].ownerPlace/*DBG + 1*/).append(" ");
				} else {
					sb.append(PlayerData.NEUTRAL.place/*DBG + 1*/).append(" ");
				}
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Zones
		json.writeArrayStart("zones");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].zone).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Murs
		json.writeArrayStart("walls");
		Point pos1;
		Point pos2;
		DoubleEntryArray<CellData, CellData, Boolean> addedWalls = new DoubleEntryArray<CellData, CellData, Boolean>();
		for (CellData cell1 : arena.walls.getEntries1()) {
			for (CellData cell2 : arena.walls.getEntries2(cell1)) {
				// Si le mur a déjà été ajouté au json, on ne le remet pas
				if (addedWalls.get(cell1, cell2) != null
				|| addedWalls.get(cell2, cell1) != null) {
					continue;
				}
				
				pos1 = cell1.position;
				pos2 = cell2.position;
				
				// Si c'est un mur en coin, on ne le sérialise pas
				if (pos1.getX() != pos2.getX()
				&& pos1.getY() != pos2.getY()) {
					continue;
				}
				
				sb.setLength(0);
				sb.append(pos1.getX()).append(";").append(pos1.getY());
				sb.append(" ");
				sb.append(pos2.getX()).append(";").append(pos2.getY());
				
				json.writeValue(sb.toString());
				
				// On note que le mur a déjà été ajouté pour pas qu'il apparaisse en double dans le json
				addedWalls.put(cell1, cell2, Boolean.TRUE);
			}
		}
		json.writeArrayEnd();
		
		json.writeObjectEnd();
	}

	@Override
	public ArenaData read(Json json, JsonValue plan, @SuppressWarnings("rawtypes") Class type) {
		ArenaBuilder builder = new ArenaBuilder();
		builder.setEditorScreen(editorScreen);
		builder.load(plan);
		return builder.build();
	}
}
