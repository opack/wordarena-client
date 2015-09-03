package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.GameData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.data.game.WordPlayed;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.components.ArenaNameLabel;
import com.slamdunk.wordarena.screens.arena.components.CancelWordButton;
import com.slamdunk.wordarena.screens.arena.components.CurrentPlayerLabel;
import com.slamdunk.wordarena.screens.arena.components.CurrentWordLabel;
import com.slamdunk.wordarena.screens.arena.components.GameWinnerLabel;
import com.slamdunk.wordarena.screens.arena.components.InfoLabel;
import com.slamdunk.wordarena.screens.arena.components.RefreshZoneButton;
import com.slamdunk.wordarena.screens.arena.components.RoundWinnerLabel;
import com.slamdunk.wordarena.screens.arena.components.ValidateWordButton;
import com.slamdunk.wordarena.screens.arena.components.ZoneMarkers;
import com.slamdunk.wordarena.screens.arena.stats.StatsTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaUI2 extends UIOverlay {
	private Map<String, ArenaScene> scenes;

	private MatchManager matchManager;

    // TODO : Se passer de ces variables. Cela permettrait en plus d'itérer sur la map scenes pour faire les create() dans loadScenes(). Voir Si cela n'est pas trop coûteux en perf de devoir faire des findActor() à chaque fois du coup.
	private Button validateWord;
	private Button cancelWord;
    private Button refreshStarting;
	private Label currentPlayer;
	private Label currentWord;
	private Label info;
	private StatsTable statsTable;

	private ZoneMarkers zoneMarkers;

	public ArenaUI2(MatchManager matchManager) {
		this.matchManager = matchManager;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));

		scenes = new HashMap<String, ArenaScene>();
		loadScenes();
	}

    /**
	 * Charge les composants définis dans Overlap2D
	 */
	private void loadScenes() {
		scenes.clear();

		loadScene(new ArenaReadyScene(matchManager));

        ArenaScene running = loadScene(new ArenaRunningScene(matchManager));
        zoneMarkers = running.findActor(ZoneMarkers.NAME);
		validateWord = running.findActor(ValidateWordButton.NAME);
		cancelWord = running.findActor(CancelWordButton.NAME);
        refreshStarting = running.findActor(RefreshZoneButton.NAME);
		currentPlayer = running.findActor(CurrentPlayerLabel.NAME);
		currentWord = running.findActor(CurrentWordLabel.NAME);
		info = running.findActor(InfoLabel.NAME);

		ArenaScene paused = loadScene(new ArenaPausedScene(matchManager));
		statsTable = paused.findActor(StatsTable.NAME);

        loadScene(new ArenaRoundOverScene(matchManager));

        loadScene(new ArenaGameOverScene(matchManager));
	}

    /**
     * Crée la scène et l'ajoute au Stage
     * @param scene
     */
    private ArenaScene loadScene(ArenaScene scene) {
        scene.create(getScreen(), Assets.uiSkin);

        getStage().addActor(scene);
        scenes.put(scene.getName(), scene);

        return scene;
    }

    /**
     * Appelée à lorsque la partie affichée change. Cela permet
     * d'éviter de recréer toute l'UI à chaque switch de partie.
     * @param gameData
     */
    public void init(GameData gameData, int nbRoundsToWin) {
        // Crée et remplit les marqueurs de possession de zone
        zoneMarkers.init(gameData.arena.zones, statsTable.getZoneMarkers()); // TODO : On devrait mettre à jour la table de stats autrement, car on crée un lien trop fort entre ZoneMarkers et la table de stats
        zoneMarkers.update(gameData.arena.zones, statsTable.getZoneMarkers());
        scenes.get(ArenaRunningScene.NAME).doLayout();

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
	 * Charge les composants à afficher lorsque le jeu est à l'état indiqué
	 */
	public void present(GameStates state) {
		for (ArenaScene scene : scenes.values()) {
			scene.setVisible(scene.getGameState() == state);
		}
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
        for (ArenaScene scene : scenes.values()) {
            ArenaNameLabel lblArenaName = scene.findActor(ArenaNameLabel.NAME);
            if (lblArenaName != null) {
                lblArenaName.setText(name);
            }
        }
	}
	
	public void setRoundWinner(PlayerData winner) {
        String text;
		if (winner == null) {
			// Egalité
            text = Assets.i18nBundle.format("ui.arena.roundWinnerTie");
		} else {
			// Victoire d'un joueur
            text = Assets.i18nBundle.format("ui.arena.roundWinner", winner.name);
		}

        RoundWinnerLabel label = (RoundWinnerLabel)scenes.get(ArenaRoundOverScene.NAME).findActor(RoundWinnerLabel.NAME);
        if (label != null) {
            label.setText(text);
        }
	}
	
	public void setGameWinner(PlayerData winner) {
        String text;
		if (winner == null) {
			// Egalité
            text = Assets.i18nBundle.format("ui.arena.gameWinnerTie");
		} else {
			// Victoire d'un joueur
            text = Assets.i18nBundle.format("ui.arena.gameWinner", winner.name);
		}

        GameWinnerLabel label = (GameWinnerLabel)scenes.get(ArenaGameOverScene.NAME).findActor(GameWinnerLabel.NAME);
        if (label != null) {
            label.setText(text);
        }
	}
	
	public void showRefreshStartingZoneButton(boolean show) {
        refreshStarting.setVisible(show);
	}
	
	public void showWordValidationButtons(boolean show) {
		validateWord.setVisible(show);
		cancelWord.setVisible(show);
	}

	public void updateStats() {
		statsTable.update(matchManager.getCinematic().getPlayers());
		statsTable.setPosition(WordArenaGame.SCREEN_WIDTH / 2, WordArenaGame.SCREEN_HEIGHT - 10, Align.top);
	}
	
	public void addPlayedWord(PlayerData player, String word) {
		statsTable.addPlayedWord(player, word);
	}

    public void updateZoneMarkers(List<ZoneData> zones) {
        zoneMarkers.update(zones, statsTable.getZoneMarkers());
    }
}
