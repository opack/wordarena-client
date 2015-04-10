package com.slamdunk.wordarena.screens.home;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.data.game.Player;
import com.slamdunk.wordarena.enums.PlayerKind;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;
import com.slamdunk.wordarena.screens.preeditor.PreEditorScreen;

public class HomeScreen extends SlamScreen {
	public static final String NAME = "HOME";
	
	private HomeUI ui;
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public HomeScreen(WordArenaGame game) {
		super(game);
		
		ui = new HomeUI(this);
		addOverlay(ui);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			promptExit();
		}
	    return false;
	 }

	/**
	 * Propose à l'utilisateur de quitter le jeu
	 */
	public void promptExit() {
		// TODO Afficher une boîte de confirmation
		Gdx.app.exit();
	}

	public void launchGame(String arenaFile) {
		Player p1 = new Player();
		p1.id = "Alan";
		p1.place = 0;
		p1.score = 0;
		p1.markerPack = "blue";
		p1.kind = PlayerKind.HUMAN_LOCAL;
		
		Player p2 = new Player();
		p2.id = "Bob";
		p2.place = 1;
		p2.score = 0;
		p2.markerPack = "orange";
		p2.kind = PlayerKind.HUMAN_LOCAL;
		
		List<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		
		ArenaScreen arena = (ArenaScreen)getGame().getScreen(ArenaScreen.NAME);
		arena.prepareGame("arenas/" + arenaFile + ".json", players);
		getGame().setScreen(ArenaScreen.NAME);
	}
	
	public void launchEditor() {
		getGame().setScreen(PreEditorScreen.NAME);
	}
}
