package com.slamdunk.wordarena.screens.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.ArenaData;
import com.slamdunk.wordarena.data.arena.ArenaSerializer;
import com.slamdunk.wordarena.data.game.Player;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;
import com.slamdunk.wordarena.screens.editor.tools.EditorTool;
import com.slamdunk.wordarena.screens.editor.tools.LetterTool;
import com.slamdunk.wordarena.screens.editor.tools.OwnerTool;
import com.slamdunk.wordarena.screens.editor.tools.PowerTool;
import com.slamdunk.wordarena.screens.editor.tools.WallTool;
import com.slamdunk.wordarena.screens.editor.tools.ZoneTool;
import com.slamdunk.wordarena.screens.preeditor.PreEditorScreen;

public class EditorScreen extends SlamScreen {
public static final String NAME = "EDITOR";
	private EditorArenaOverlay arena;
	private EditorUI ui;
	private EditorMatchManager matchManager;
	
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends EditorTool>, EditorTool> tools;
	
	@SuppressWarnings("rawtypes")
	private EditorTool currentTool;
	
	public EditorScreen(WordArenaGame game) {
		super(game);
		
		createMatchManager();
		createTools();
	
		arena = new EditorArenaOverlay();
		addOverlay(arena);
		
		ui = new EditorUI(this);
		addOverlay(ui);
	}
	
	private void createMatchManager() {
		matchManager = new EditorMatchManager();
		List<Player> players = new ArrayList<Player>();
		players.add(Player.NEUTRAL);
		players.add(new Player(Assets.i18nBundle.get("ui.editor.player.1"), "blue", 0));
		players.add(new Player(Assets.i18nBundle.get("ui.editor.player.2"), "orange", 1));
		players.add(new Player(Assets.i18nBundle.get("ui.editor.player.3"), "green", 2));
		players.add(new Player(Assets.i18nBundle.get("ui.editor.player.4"), "purple", 3));
		matchManager.getCinematic().setPlayers(players);
	}

	@SuppressWarnings("rawtypes")
	private void createTools() {
		tools = new HashMap<Class<? extends EditorTool>, EditorTool>();
		tools.put(CellTypeTool.class, new CellTypeTool());
		tools.put(LetterTool.class, new LetterTool());
		tools.put(PowerTool.class, new PowerTool());
		tools.put(OwnerTool.class, new OwnerTool());
		tools.put(ZoneTool.class, new ZoneTool());
		tools.put(WallTool.class, new WallTool());
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			getGame().setScreen(PreEditorScreen.NAME);
		}
	    return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends EditorTool> T getTool(Class<T> toolClass) {
		return (T)tools.get(toolClass);
	}

	@SuppressWarnings("rawtypes")
	public void setCurrentTool(Class<? extends EditorTool> toolClass) {
		currentTool = getTool(toolClass);
	}

	@SuppressWarnings("rawtypes")
	public EditorTool getCurrentTool() {
		return currentTool;
	}

	public ZoneActor getOrCreateZone(String id) {
		ZoneActor zone = arena.getZone(id);
		if (zone == null) {
			return arena.createZone(id);
		} else {
			return zone;
		}
	}

	public void save() {
		Json json = new Json();
		json.setSerializer(ArenaData.class, new ArenaSerializer());
		final String serialized = json.prettyPrint(arena.getData());
		
		FileHandle file = Gdx.files.absolute("E:\\Projets\\Programmes\\WordArena\\wordarena-client\\android\\assets\\arenas\\" + arena.getData().name + ".json");
		file.writeString(serialized, false, "UTF-8");
	}
	
	public void createNewArena(String name, int width, int height) {
		arena.createEmptyArena(width, height, "default");
		prepareUI(name);
	}
	
	public void editExistingArena(String name) {
		arena.buildArena("arenas/" + name + ".json", matchManager);
		prepareUI(name);
	}
	
	private void prepareUI(String name) {
		arena.getData().name = name;
		arena.getData().skin = "default"; // TODO Ajouter une liste pour sélectionner la skin
		ui.loadData(arena);
		getTool(WallTool.class).setArena(arena);
	}

	public List<Player> getPlayers() {
		return matchManager.getCinematic().getPlayers();
	}
}
