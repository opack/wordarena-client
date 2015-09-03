package com.slamdunk.wordarena.screens.arena;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.toolkit.ui.Overlap2DUtils;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.cell.MarkerPack;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.data.game.WordPlayed;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.stats.StatsTable;
import com.slamdunk.wordarena.screens.home.HomeScreen;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class ArenaUI extends UIOverlay {
	private SceneLoader sceneLoader;
	
	private MatchManager matchManager;
	
	private CompositeItem validateWord;
	private CompositeItem cancelWord;
	private Label currentPlayer;
	private Label currentWord;
	private Label info;
	private StatsTable statsTable;
	
	private GroupEx zoneMarkers;
	
	/**
	 * Map de travail permettant de stocker le nombre de zones par joueur
	 * lors du rafraîchissement des marqueurs de zone
	 */
	private Map<String, Integer> tmpZonesByPlayer;
	
	public ArenaUI(MatchManager matchManager) {
		this.matchManager = matchManager;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		loadScene();
		
		zoneMarkers = new GroupEx();
		getStage().addActor(zoneMarkers);
		tmpZonesByPlayer = new HashMap<String, Integer>();
	}
	
	/**
	 * Appelée à lorsque la partie affichée change. Cela permet
	 * d'éviter de recréer toute l'UI à chaque switch de partie.
	 * @param gameData
	 */
	public void init(GameData gameData, int nbRoundsToWin) {
		// Crée et remplit les marqueurs de possession de zone
		initZoneMarkers(gameData.arena.zones);
		updateZoneMarkers(gameData.arena.zones);
		
		// Initialise la table de statistiques
		statsTable.init(gameData.players, nbRoundsToWin);
		statsTable.layout();
		statsTable.pack();
		
		// Initialise les informations affichées
		setArenaName(gameData.arena.name);
		setInfo("");
		
		// Initialise les mots joués
		for (WordPlayed wordPlayed : gameData.wordsPlayed) {
			addPlayedWord(gameData.players.get(wordPlayed.player), wordPlayed.wordId);
		}
	}
	

	/**
	 * Crée les carrés indiquant la possession des joueurs en terme
	 * de zones
	 * @param zones 
	 */
	private void initZoneMarkers(List<ZoneData> zones) {
		// Prépare le marker par défaut
		MarkerPack neutralPack = Assets.markerPacks.get(Assets.MARKER_PACK_NEUTRAL);
		TextureRegionDrawable neutralPossessionMarker = neutralPack.possessionMarker;
		
		zoneMarkers.clear();
		GroupEx pauseZoneMarkers = statsTable.getZoneMarkers();
		pauseZoneMarkers.clear();
		
		int totalWidth = 0;
		for (ZoneData zoneData : zones) {
			// La zone NONE n'est pas représentée car elle ne peut pas être possédée
			if (zoneData.id.equals(ZoneActor.NONE.getData().id)) {
				continue;
			}
			
			// Crée l'image
			Image marker = new Image(neutralPossessionMarker);
			marker.setPosition(totalWidth, 0);
			zoneMarkers.addActor(marker);
			
			marker = new Image(neutralPossessionMarker);
			marker.setPosition(totalWidth, 0);
			pauseZoneMarkers.addActor(marker);
			
			// On a ajouté un marqueur donc la taille totale a changé
			totalWidth += neutralPossessionMarker.getMinWidth();
		}
		
		// Centre le groupe à l'écran
		zoneMarkers.setPosition((WordArenaGame.SCREEN_WIDTH - zoneMarkers.getWidth()) / 2, 701);
	}


	/**
	 * Charge les composants définis dans Overlap2D
	 */
	private void loadScene() {
//DBG		sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
//		sceneLoader.loadScene("Arena");
//		getStage().addActor(sceneLoader.sceneActor);
//
//		sceneLoader.sceneActor.setTouchable(Touchable.childrenOnly);
//
//		initReadyLayer();
//		initRunningLayer();
//		initPausedLayer();
//		initRoundOverLayer();
//		initGameOverLayer();
	}

	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "READY"
	 */
	private void initReadyLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnStart", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				matchManager.changeState(GameStates.RUNNING);
			}
		});
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "RUNNING"
	 */
	private void initRunningLayer() {
		validateWord = Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnValidateWord", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				matchManager.validateWord();
			}
		}).getItem();
		
		cancelWord = Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnCancelWord", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				matchManager.cancelWord();
			}
		}).getItem();
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnRefreshZone", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				matchManager.refreshStartingZone();
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnPause", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				matchManager.changeState(GameStates.PAUSED);
			}
		});
		
		currentPlayer = sceneLoader.sceneActor.getLabelById("lblCurrentPlayer");
		currentWord = sceneLoader.sceneActor.getLabelById("lblCurrentWord");
		info = sceneLoader.sceneActor.getLabelById("lblInfo");
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "PAUSED"
	 */
	private void initPausedLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnResume", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				matchManager.changeState(GameStates.RUNNING);
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnBackToHome2", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				getScreen().getGame().setScreen(HomeScreen.NAME);
			}
		});
		
		statsTable = new StatsTable();
		statsTable.setVisible(false);
		getStage().addActor(statsTable);
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "ROUND_OVER"
	 */
	private void initRoundOverLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnNextRound", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				matchManager.nextRound();
			}
		});
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "GAME_OVER"
	 */
	private void initGameOverLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnRetry", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnBackToHome1", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				getScreen().getGame().setScreen(HomeScreen.NAME);
			}
		});
	}

	/**
	 * Charge les composants à afficher lorsque le jeu est à l'état indiqué
	 */
	public void present(GameStates state) {
		for (GameStates cur : GameStates.values()) {
			sceneLoader.sceneActor.setLayerVisibilty(cur.name(), cur == state);
			// Il semble y avoir un bug dans Overlap2D : si l'export est fait alors qu'une couche
			// est masquée, cette couche arrivera verrouillée et même si on l'affiche elle ne sera
			// pas réceptive aux touch. On s'assure donc ici que la couche affichée est également
			// déverrouillée.
			sceneLoader.sceneActor.setLayerLock(cur.name(), cur != state);
		}
		
		// Gère la visibilité des composants ajoutés hors Overlap2D. Dommage qu'ils ne puissent pas
		// être rajoutée proprement dans la couche adéquate des l'UI chargée par Overlap2D...
		zoneMarkers.setVisible(state == GameStates.RUNNING);
		statsTable.setVisible(state == GameStates.PAUSED);
	}
	
	public void setCurrentPlayer(PlayerData player, int turn, int maxTurns, int round) {
		currentPlayer.setText(Assets.i18nBundle.format("ui.arena.currentTurn", player.name, round, turn, maxTurns));
		currentPlayer.setStyle(Assets.markerPacks.get(player.markerPack).labelStyle);
	}
	
	public void setCurrentWord(String word) {
		currentWord.setText(word);
	}
	
	public void setInfo(String message) {
		info.setText(message);
	}

	public void setArenaName(String code) {
		String name = Assets.i18nBundle.get("arena.title." + code);
		sceneLoader.sceneActor.getLabelById("lblArenaName").setText(name);
	}
	
	public void setRoundWinner(PlayerData winner) {
		if (winner == null) {
			// Egalité
			sceneLoader.sceneActor.getLabelById("lblRoundWinner").setText(Assets.i18nBundle.format("ui.arena.roundWinnerTie"));
		} else {
			// Victoire d'un joueur
			sceneLoader.sceneActor.getLabelById("lblRoundWinner").setText(Assets.i18nBundle.format("ui.arena.roundWinner", winner.name));
		}
	}
	
	public void setGameWinner(PlayerData winner) {
		if (winner == null) {
			// Egalité
			sceneLoader.sceneActor.getLabelById("lblGameWinner").setText(Assets.i18nBundle.format("ui.arena.gameWinnerTie"));
		} else {
			// Victoire d'un joueur
			sceneLoader.sceneActor.getLabelById("lblGameWinner").setText(Assets.i18nBundle.format("ui.arena.gameWinner", winner.name));
		}
	}
	
	public void showRefreshStartingZoneButton(boolean show) {
		sceneLoader.sceneActor.getCompositeById("btnRefreshZone").setVisible(show);
	}
	
	public void showWordValidationButtons(boolean show) {
		validateWord.setVisible(show);
		cancelWord.setVisible(show);
	}

	public void updateStats() {
		statsTable.update(matchManager.getCinematic().getPlayers());
		statsTable.setPosition(WordArenaGame.SCREEN_WIDTH / 2, WordArenaGame.SCREEN_HEIGHT - 10, Align.top);
	}
	
	/**
	 * Met à jour les carrés de possession de zone en fonction des
	 * zones possédées par chaque joueur.
	 * @param zones
	 */
	public void updateZoneMarkers(List<ZoneData> zones) {
		// Compte le nombre de zones possédées par chaque joueur
		tmpZonesByPlayer.clear();
		String playerPack;
		for (ZoneData zoneData : zones) {
			// On ne traite pas les zones NONE (cellules hors zone)
			if (zoneData.id.equals(ZoneActor.NONE.getData().id)) {
				continue;
			}
			
			// Récupère le pack du possesseur de la zone
			playerPack = matchManager.getPlayer(zoneData.ownerPlace).markerPack;
			
			// Ajoute 1 zone à dessiner avec ce pack
			Integer nbZones = tmpZonesByPlayer.get(playerPack);
			if (nbZones == null) {
				tmpZonesByPlayer.put(playerPack, 1);
			} else {
				tmpZonesByPlayer.put(playerPack, nbZones + 1);
			}
		}
		
		// Pour chaque joueur, remplit autant de carrés que de zones possédées
		// On prend d'abord le premier joueur, puis le neutre, puis le second joueur,
		// de façon à avoir une jolie barre
		int lastMarkedSquare = fillZoneMarkers(0, matchManager.getPlayer(0).markerPack);
		
		lastMarkedSquare = fillZoneMarkers(lastMarkedSquare, Assets.MARKER_PACK_NEUTRAL);
		
		lastMarkedSquare = fillZoneMarkers(lastMarkedSquare, matchManager.getPlayer(1).markerPack);
	}

	/**
	 * Remplit les marqueurs de zone à partir du offset-ième
	 * avec autant de positions que tmpZonesByPlayer l'indique
	 * pour le markerPack spécifié.
	 * @param offset 1er marqueur à remplir
	 * @param markerPack
	 * @return L'indice de la dernière zone remplie, pour fournir
	 * cette valeur à offset lors du prochain appel
	 */
	private int fillZoneMarkers(int offset, String markerPack) {
		// Récupère le nombre de zones à marquer
		Integer nbZonesOwned = tmpZonesByPlayer.get(markerPack);
		if (nbZonesOwned == null) {
			return offset;
		}
		
		// Récupère l'image du marqueur de possession à dessiner
		MarkerPack pack = Assets.markerPacks.get(markerPack);
		TextureRegionDrawable possessionMarker = pack.possessionMarker;
		
		// Met à jour les images et aussi celles de la StatsTable
		SnapshotArray<Actor> mainScreenMarkers = zoneMarkers.getChildren();
		SnapshotArray<Actor> pauseScreenMarkers = statsTable.getZoneMarkers().getChildren();
		Image marker;
		int markerIndex;
		for (int curZone = 0; curZone < nbZonesOwned; curZone++) {
			markerIndex = offset + curZone;
			
			// Met à jour l'image des marqueurs affichés sur l'arène
			marker = (Image)mainScreenMarkers.get(markerIndex);
			marker.setDrawable(possessionMarker);
			
			// Met à jour l' image de la stats table
			marker = (Image)pauseScreenMarkers.get(markerIndex);
			marker.setDrawable(possessionMarker);
		}
		return offset + nbZonesOwned;
	}
	
	public void addPlayedWord(PlayerData player, String word) {
		statsTable.addPlayedWord(player, word);
	}
}
