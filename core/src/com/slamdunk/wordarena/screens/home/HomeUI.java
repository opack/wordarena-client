package com.slamdunk.wordarena.screens.home;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.lang.DoubleEntryArrayList;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.Overlap2DUtils;
import com.slamdunk.wordarena.UserManager;
import com.slamdunk.wordarena.Utils;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.GameStatus;
import com.slamdunk.wordarena.enums.GameTypes;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;
import com.uwsoft.editor.renderer.actor.TextBoxItem;

public class HomeUI extends UIOverlay {
	private static final String GAME_LABEL_STYLE_STATUS_HEADER = "games-status-header";
	private static final String GAME_LABEL_STYLE_OPPONENT_TURN = "games-opponent-turn";
	private static final String GAME_LABEL_STYLE_USER_TURN = "games-user-turn";
	private static final String GAME_LABEL_STYLE_GAME_OVER = "games-game-over";
	
	private HomeScreen screen;
	private Table gamesTable;
	
	/**
	 * Table associant une liste de parties à un statut et un type de partie
	 */
	private DoubleEntryArrayList<GameStatus, GameTypes, GameData> games;
	private String username;
	
	public HomeUI(HomeScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
		
		// Crée la table des parties en cours
		createCurrentGamesTable();
		
		// Charge les parties en cours
		games = new DoubleEntryArrayList<GameStatus, GameTypes, GameData>();
		loadCurrentGames(GameTypes.CAREER);
	}

	private void loadScene() {
		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Home");
		getStage().addActor(sceneLoader.sceneActor);
		
		// Bouton de démarrage de partie
		@SuppressWarnings("unchecked")
		final SelectBoxItem<String> selArena = (SelectBoxItem<String>) sceneLoader.sceneActor.getItemById("selArena");
		selArena.setWidth(150);
		selArena.setItems(Utils.loadArenaNames());
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnPlay", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.startNewGame(selArena.getSelected());
			}
		});
		
		final TextBoxItem txtGameId = (TextBoxItem) sceneLoader.sceneActor.getItemById("txtGameId");
		txtGameId.setWidth(50);
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnContinue", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				String text = txtGameId.getText();
				if (text.isEmpty()) {
					return;
				}
				screen.continueGame(Integer.parseInt(text));
			}
		});
		
		// Bouton Editor
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnEditor", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.launchEditor();
			}
		});
		
		// Bouton Options
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnOptions", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("DBG Options");
			}
		});
		
		// Bouton Quit
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnQuit", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.promptExit();
			}
		});
		
		// Boutons d'affichage des parties en cours
		loadCurrentGames();
	}
	
	private void createCurrentGamesTable() {
		// Créer une table
		gamesTable = new Table();
		
		// Placer la table dans un ScrollPane pour permettre le scroll
		ScrollPane scrollPane = new ScrollPane(gamesTable, Assets.uiSkin);
		scrollPane.setupOverscroll(15, 30, 200);
		scrollPane.setPosition(5, 5);
		scrollPane.setSize(470, 595);
		
		// Ajouter le ScrollPane au Stage
		getStage().addActor(scrollPane);
	}
	
	/**
	 * Affiche les parties en cours en interrogeant le serveur
	 * et en lisant le cache
	 */
	public void loadCurrentGames() {
		// Vider la table et afficher la bonne ligne d'entête
		gamesTable.clear();

		username = UserManager.getInstance().getUserData().name;
		
		// Charge les parties en cours pour chaque type
		fetchGames();
		
		// Ajouter ces parties à la table
		List<GameData> gameForStatus;
		for (GameStatus status : GameStatus.values()) {
			gameForStatus = games.get(status, gameType);
			if (gameForStatus != null) {
				// Ajoute une ligne d'entête
				createHeaderRow(status);
				
				// Ajoute les parties en cours
				for (GameData gameData : gameForStatus) {
					createGameRow(gameData, status);
				}
			}
		}
		
		// Remplit le reste avec du vide
		gamesTable.add().expand();
	}

	private void fetchGames() {
		// DBG Triche en attendant le chargement de vraies parties
		List<GameData> fetched = new ArrayList<GameData>();
		PlayerData p1 = new PlayerData();
		p1.name = "Alan";
		p1.markerPack = "blue";
		
		PlayerData p2 = new PlayerData();
		p2.name = "Bob";
		p2.markerPack = "orange";
		
		PlayerData p3 = new PlayerData();
		p3.name = "Charles";
		p3.markerPack = "green";
		
		PlayerData p4 = new PlayerData();
		p4.name = "Dave";
		p4.markerPack = "purple";
		
		GameData game1 = GameData.create();
		game1.header.gameType = GameTypes.DUEL;
		game1.players = new ArrayList<PlayerData>();
		game1.players.add(p1);
		game1.players.add(p2);
		game1.cinematic.curPlayer = 1;
		fetched.add(game1);
		
		GameData game2 = GameData.create();
		game2.header.gameType = GameTypes.DUEL;
		game2.players = new ArrayList<PlayerData>();
		game2.players.add(p1);
		game2.players.add(p3);
		game2.cinematic.curPlayer = 0;
		fetched.add(game2);
		
		GameData game3 = GameData.create();
		game3.header.gameType = GameTypes.DUEL;
		game3.players = new ArrayList<PlayerData>();
		game3.players.add(p1);
		game3.players.add(p4);
		game3.cinematic.curPlayer = 0;
		fetched.add(game3);
		
		GameData game4 = GameData.create();
		game4.header.gameType = GameTypes.TOURNAMENT;
		game4.players = new ArrayList<PlayerData>();
		game4.players.add(p1);
		game4.players.add(p2);
		game4.players.add(p3);
		game4.cinematic.curPlayer = 0;
		fetched.add(game4);
		
		GameData game5 = GameData.create();
		game5.header.gameType = GameTypes.DUEL;
		game5.players = new ArrayList<PlayerData>();
		game5.players.add(p1);
		game5.players.add(p2);
		game5.cinematic.curPlayer = 0;
		game5.cinematic.gameOver = true;
		fetched.add(game5);
		
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		
		
		
		// Répartit les jeux entre ceux où c'est au tour de l'utilisateur de jouer
		// et ceux où c'est à un adversaire de jouer
		games.clear();
		
		PlayerData currentPlayer;
		GameStatus status;
		for (GameData gameData : fetched) {
			// Détermine le statut de la partie
			if (gameData.cinematic.gameOver) {
				status = GameStatus.GAME_OVER;
			} else {
				currentPlayer = gameData.players.get(gameData.cinematic.curPlayer);
				if (username.equals(currentPlayer.name)) {
					status = GameStatus.USER_TURN;
				} else {
					status = GameStatus.OPPONENT_TURN;
				}
			}
			
			// Ajoute la partie à la liste
			games.add(status, gameData.header.gameType, gameData);
		}
	}
	
	/**
	 * Crée une ligne d'entête précisant l'état des parties
	 * qui seront affichées à la suite
	 * @param status
	 */
	private void createHeaderRow(GameStatus status) {
		final String header = Assets.i18nBundle.get("ui.home.games.header." + status.name());
		gamesTable.add(new Label(header, Assets.uiSkin, GAME_LABEL_STYLE_STATUS_HEADER));
		
		// Fin de la ligne
		gamesTable.row();
	}

	/**
	 * Crée une ligne dans la table des parties en cours
	 * @param gameData
	 * @param userTurn true si c'est à l'utilisateur de jouer
	 * @return
	 */
	private void createGameRow(GameData gameData, GameStatus status) {
		// Choix du style du label en fonction du joueur courant
		String labelStyle;
		switch (status) {
		case USER_TURN:
			labelStyle = GAME_LABEL_STYLE_USER_TURN;
			break;
		case OPPONENT_TURN:
			labelStyle = GAME_LABEL_STYLE_OPPONENT_TURN;
			break;
		case GAME_OVER:
		default:
			labelStyle = GAME_LABEL_STYLE_GAME_OVER;
			break;
		}
		
		// Ajout d'une image représentant le type de partie
		gamesTable.add(new Label(gameData.header.gameType.toString(), Assets.uiSkin, labelStyle));
		
		// Ajout d'un label avec la liste des adversaires
		StringBuilder opponents = new StringBuilder();
		for (PlayerData opponent : gameData.players) {
			if (opponents.length() != 0) {
				opponents.append(", ");
			}
			if (!username.equals(opponent.name)) {
				opponents.append(opponent.name);
			}
		}
		gamesTable.add(new Label(opponents.toString(), Assets.uiSkin, labelStyle));
		
		// Fin de la ligne
		gamesTable.row();
	}
}
