package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.Overlap2DUtils;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.home.HomeScreen;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class ArenaUI extends UIOverlay {
	private SceneLoader sceneLoader;
	
	private MatchManager gameManager;
	
	private CompositeItem validateWord;
	private CompositeItem cancelWord;
	private Label currentPlayer;
	private Label currentWord;
	private Label info;
	private Label stats;
	
	public ArenaUI(MatchManager gameManager) {
		this.gameManager = gameManager;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		loadScene();
		
		createZoneSquares();
	}
	

	/**
	 * Crée les carrés indiquant la possession des joueurs en terme
	 * de zones
	 */
	private void createZoneSquares() {
		// TODO Auto-generated method stub
	}


	/**
	 * Charge les composants définis dans Overlap2D
	 */
	private void loadScene() {
		sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Arena");
		getStage().addActor(sceneLoader.sceneActor);
		
		sceneLoader.sceneActor.setTouchable(Touchable.childrenOnly);

		initReadyLayer();
		initRunningLayer();
		initPausedLayer();
		initRoundOverLayer();
		initGameOverLayer();
	}

	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "READY"
	 */
	private void initReadyLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnStart", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.RUNNING);
			}
		});
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "RUNNING"
	 */
	private void initRunningLayer() {
		validateWord = Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnValidateWord", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.validateWord();
			}
		}).getItem();
		
		cancelWord = Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnCancelWord", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.cancelWord();
			}
		}).getItem();
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnRefreshZone", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.refreshStartingZone();
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnPause", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.PAUSED);
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
				gameManager.changeState(GameStates.RUNNING);
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnBackToHome2", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				getScreen().getGame().setScreen(HomeScreen.NAME);
			}
		});
		
		stats = sceneLoader.sceneActor.getLabelById("lblStats");
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "ROUND_OVER"
	 */
	private void initRoundOverLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnNextRound", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.nextRound();
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
	}
	
	public void setCurrentPlayer(Player player, int turn, int maxTurns, int round) {
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
	
	public void setRoundWinner(String winner) {
		sceneLoader.sceneActor.getLabelById("lblRoundWinner").setText(Assets.i18nBundle.format("ui.arena.roundWinner", winner));
	}
	
	public void setGameWinner(String winner) {
		sceneLoader.sceneActor.getLabelById("lblGameWinner").setText(Assets.i18nBundle.format("ui.arena.gameWinner", winner));
	}
	
	public void showRefreshStartingZoneButton(boolean show) {
		sceneLoader.sceneActor.getCompositeById("btnRefreshZone").setVisible(show);
	}
	
	public void showWordValidationButtons(boolean show) {
		validateWord.setVisible(show);
		cancelWord.setVisible(show);
	}

	public void updateStats() {
		StringBuilder sb = new StringBuilder();
		for (Player player : gameManager.getCinematic().getPlayers()) {
			sb.append("== ").append(player.name).append(" ==");
			sb.append("\n\tScore : ").append(player.score);
			sb.append("\n\tZones : ").append(player.nbZonesOwned).append("/").append(gameManager.getNbZones());
			sb.append("\n\tRounds : ").append(player.nbRoundsWon).append("/").append(gameManager.getCinematic().getNbWinningRoundsPerGame());
			sb.append("\n");
		}
		
		stats.setText(sb.toString());
	}
	
	/**
	 * Met à jour les carrés de possession de zone en fonction des
	 * zones possédées par chaque joueur.
	 */
	public void updateZoneSquares() {
		// TODO DBG Afficher l'image couleur des joueurs dans autant de carrés que de zones possédées
	}
}
