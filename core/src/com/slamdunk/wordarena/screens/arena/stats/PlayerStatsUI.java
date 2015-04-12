package com.slamdunk.wordarena.screens.arena.stats;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.cell.MarkerPack;
import com.slamdunk.wordarena.data.game.PlayerData;

public class PlayerStatsUI {
	public Label lblName;
	public Label lblScore;
	public GroupEx gpRoundsMarkers;
	
	public PlayerStatsUI() {
		lblName = new Label("", Assets.uiSkin);
		lblScore = new Label("", Assets.uiSkin);
		gpRoundsMarkers = new GroupEx();
	}

	public void init(PlayerData player, int nbRoundsToWin) {
		MarkerPack pack = Assets.markerPacks.get(player.markerPack);
		
		lblName.setStyle(pack.labelStyle);
		lblName.setText(player.name);
		
		lblScore.setStyle(pack.labelStyle);
		lblScore.setText(String.valueOf(player.score));
		
		initRoundsMarkers(pack, nbRoundsToWin);
	}
	
	private void initRoundsMarkers(MarkerPack pack, int nbRoundsToWin) {
		// Prépare le marker par défaut
		MarkerPack neutralPack = Assets.markerPacks.get(Assets.MARKER_PACK_NEUTRAL);
		TextureRegionDrawable neutralPossessionMarker = neutralPack.possessionMarker;
		
		gpRoundsMarkers.clear();
		
		int totalWidth = 0;
		for (int curRound = 0; curRound < nbRoundsToWin; curRound++) {
			// Crée l'image
			Image marker = new Image(neutralPossessionMarker);
			marker.setPosition(totalWidth, 0);
			gpRoundsMarkers.addActor(marker);
			
			// On a ajouté un marqueur donc la taille totale a changé
			totalWidth += neutralPossessionMarker.getMinWidth();
		}
	}

	public void update(PlayerData player) {
		// Met à jour le score
		lblScore.setText(String.valueOf(player.score));
		
		// Met à jour les rounds gagnés
		updateRoundMarkers(player);
	}

	private void updateRoundMarkers(PlayerData player) {
		// Prépare le marker par défaut
		MarkerPack neutralPack = Assets.markerPacks.get(Assets.MARKER_PACK_NEUTRAL);
		TextureRegionDrawable neutralPossessionMarker = neutralPack.possessionMarker;
		
		// Prépare le marker du joueur
		MarkerPack playerPack = Assets.markerPacks.get(player.markerPack);
		TextureRegionDrawable playerPossessionMarker = playerPack.possessionMarker;
		
		// Met à jour les marqueurs
		SnapshotArray<Actor> markers = gpRoundsMarkers.getChildren();
		Image marker;
		for (int curMarker = 0; curMarker < player.nbRoundsWon; curMarker++) {
			marker = (Image)markers.get(curMarker);
			marker.setDrawable(playerPossessionMarker);
		}
		for (int curMarker = player.nbRoundsWon; curMarker < markers.size; curMarker++) {
			marker = (Image)markers.get(curMarker);
			marker.setDrawable(neutralPossessionMarker);
		}
	}
}
