package com.slamdunk.wordarena.screens.arena.stats;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.cell.MarkerPack;
import com.slamdunk.wordarena.data.game.PlayerData;

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
		
		// Crée le zoneMarkers avec 1 image afin de réserver l'espace suffisant
		zoneMarkers = new GroupEx();
		
		Label wordsTitle = new Label(Assets.i18nBundle.format("ui.arena.wordsTitle"), Assets.uiSkin);
		playedWords = new Table();
		ScrollPane wordsScroll = new ScrollPane(playedWords, Assets.uiSkin);
		wordsScroll.setupOverscroll(15, 30, 200);
		
		// Place composants dans le tableau
		// Noms des joueurs
		add(p1Stats.lblName);
		add();
		add(p2Stats.lblName);
		row();
		
		// Zones possédées
		add(zonesTitle).colspan(3);
		row();
		add(zoneMarkers).colspan(3).align(Align.center);
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
		add(wordsScroll).height(200).fillX().colspan(3).align(Align.center);
	}
	
	public void init(List<PlayerData> players, int nbRoundsToWin) {
		// Prépare les libellés
		p1Stats.init(players.get(0), nbRoundsToWin);
		p2Stats.init(players.get(1), nbRoundsToWin);
		
		// Vide la liste des mots
		playedWords.clear();
		
		// Met à jour les autres infos
		update(players);
	}

	public void update(List<PlayerData> players) {
		p1Stats.update(players.get(0));
		p2Stats.update(players.get(1));
	}

	public void addPlayedWord(PlayerData player, String word) {
		MarkerPack pack = Assets.markerPacks.get(player.markerPack);
		
		Label lblWord = new Label(word, pack.labelStyle);
		playedWords.add(lblWord).center().row();
	}
}
