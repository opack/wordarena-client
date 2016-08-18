package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.actors.ZoomGestureHandler;
import com.slamdunk.wordarena.actors.ZoomInputProcessor;
import com.slamdunk.wordarena.data.arena.ArenaBuilder;
import com.slamdunk.wordarena.data.game.GameCache;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.GameTypes;
import com.slamdunk.wordarena.enums.Objectives;

import java.util.List;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	
	private MatchManager matchManager;
	private ArenaOverlay arena;
	private ArenaUI2 ui;
	
	public ArenaScreen(SlamGame game) {
		super(game);

		matchManager = new MatchManager();
		
		arena = new ArenaOverlay(matchManager);
		addOverlay(arena);
		
		ui = new ArenaUI2(matchManager);
		addOverlay(ui);
		ui.loadScenes();

		// Gestionnaires permettant de zoomer avec la souris ou un pinch.
		// Ces gestionnaires sont insérés après ArenaUI.
		OrthographicCamera camera = (OrthographicCamera)arena.getStage().getCamera();
		getInputMultiplexer().addProcessor(1, new GestureDetector(new ZoomGestureHandler(camera)));
		getInputMultiplexer().addProcessor(1, new ZoomInputProcessor(camera));
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public void pause () {
		matchManager.pause();
	}
	
	public ArenaOverlay getArena() {
		return arena;
	}

	public ArenaUI2 getUI() {
		return ui;
	}
	
	public void startNewGame(String arenaPlanFile, List<PlayerData> players) {
		// TODO Demande une nouvelle partie au serveur
		GameData game = GameData.create(); // TODO Récupérer la partie vierge retournée par le serveur
		// DBG En attendant la récupération des données de la nouvelle partie du serveur, on initialise ici les champs
		game._id = String.valueOf(MathUtils.random(65535));
		game.header.gameType = GameTypes.TRAINING;
		game.header.objective = Objectives.CONQUEST;
		game.players = players;
		System.out.println("DBG ArenaScreen.startNewGame() Création de la partie #" + game._id);

		// DBG Choisit le premier joueur à jouer : c'est le premier qui n'est pas NEUTRAL
		for (PlayerData player : game.players) {
			if (!player.isNeutral()) {
				game.cinematic.firstPlayer = player.place;
				game.cinematic.curPlayer = player.place;
				break;
			}
		}

		// Charge l'arène depuis le plan
		JsonValue json = new JsonReader().parse(Gdx.files.internal(arenaPlanFile));
		ArenaBuilder builder = new ArenaBuilder();
		builder.load(json);
		game.arena = builder.build();
		
		// Crée un nouveau cache pour cette partie
		GameCache cache = new GameCache();
		cache.create(game);
		cache.save();

		// TODO Met à jour la partie sur le serveur
		
		// Démarre la partie
		matchManager.init(arena, ui, cache);
	}
	
	public void continueGame(int gameId) {
		// Charge la partie depuis le cache
		GameCache cache = new GameCache();
		cache.load(gameId);
		
		// TODO S'assure auprès du serveur qu'il n'y a pas eut de màj plus récente
		//if (cache.getData().lastUpdateTime != ...) -> màj le cache
		
		// Démarre la partie
		matchManager.init(arena, ui, cache);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// Au cours d'une partie, appuyer sur back met le jeu en pause
		if (keycode == Keys.BACK) {
			matchManager.requestBack();
		}
	    return false;
	 }
}
