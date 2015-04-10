package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.actors.ZoomGestureHandler;
import com.slamdunk.wordarena.actors.ZoomInputProcessor;
import com.slamdunk.wordarena.data.game.Player;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	
	private MatchManager matchManager;
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	public ArenaScreen(SlamGame game) {
		super(game);

		matchManager = new MatchManager();
		
		arena = new ArenaOverlay();
		addOverlay(arena);
		
		ui = new ArenaUI(matchManager);
		addOverlay(ui);

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

	public ArenaUI getUI() {
		return ui;
	}

	public void prepareGame(String arenaPlanFile, List<Player> players) {
		matchManager.init(this, arenaPlanFile, players);
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
