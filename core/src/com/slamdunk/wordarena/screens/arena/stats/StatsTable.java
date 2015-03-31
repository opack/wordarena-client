package com.slamdunk.wordarena.screens.arena.stats;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.MarkerPack;
import com.slamdunk.wordarena.data.Player;

public class StatsTable extends Table {
	
	private PlayerStatsUI p1Stats;
	private PlayerStatsUI p2Stats;
	
	private GroupEx zoneMarkers;
	
	private Table playedWords;
	
	public StatsTable() {
		createTable();
	}
	
	public GroupEx getZoneMarkers() {
		return zoneMarkers;
	}

	private void createTable() {
		// Crée les composants
		p1Stats = new PlayerStatsUI();
		p2Stats = new PlayerStatsUI();
		
		Label scoreTitle = new Label(Assets.i18nBundle.format("ui.arena.scoreTitle"), Assets.uiSkin);		
		Label roundsTitle = new Label(Assets.i18nBundle.format("ui.arena.roundsTitle"), Assets.uiSkin);
		Label zonesTitle = new Label(Assets.i18nBundle.format("ui.arena.zonesTitle"), Assets.uiSkin);
		
		zoneMarkers = new GroupEx();
		
		Label wordsTitle = new Label(Assets.i18nBundle.format("ui.arena.wordsTitle"), Assets.uiSkin);
		playedWords = new Table();
		
		// Place composants dans le tableau
		// Noms des joueurs
		add(p1Stats.lblName);
		add();
		add(p2Stats.lblName);
		row();
		
		// Zones possédées
		add(zonesTitle).colspan(3);
		row();
		add(zoneMarkers).colspan(3);
		row();
		
		// Nombre de rounds gagnés
		add(roundsTitle).colspan(3);
		row();
		add(p1Stats.gpRoundsMarkers);
		add();
		add(p2Stats.gpRoundsMarkers);
		row();
		
		// Score
		add(scoreTitle).colspan(3);
		row();
		add(p1Stats.lblScore).center();
		add();
		add(p2Stats.lblScore).center();
		row();
		
		// Mots déjà joués
		add(wordsTitle).colspan(3);
		row();
		add(playedWords).colspan(3);
	}
	
	public void init(Array<Player> players, int nbRoundsToWin) {
		// Prépare les libellés
		p1Stats.init(players.get(0), nbRoundsToWin);
		p2Stats.init(players.get(1), nbRoundsToWin);
		
		// Vide la liste des mots
		playedWords.clear();
		
		// Met à jour les autres infos
		update(players);
	}

	public void update(Array<Player> players) {
		p1Stats.update(players.get(0));
		p2Stats.update(players.get(1));
		pack();
	}

	public void addPlayedWord(Player player, String word) {
		MarkerPack pack = Assets.markerPacks.get(player.markerPack);
		
		Label lblWord = new Label(word, pack.labelStyle);
		playedWords.add(lblWord).center().row();
	}
}
