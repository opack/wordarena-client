package com.slamdunk.wordarena.screens.arena.scenes;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.MatchManager;
import com.slamdunk.wordarena.screens.arena.components.GameWinnerLabel;
import com.slamdunk.wordarena.screens.arena.components.HomeButton;
import com.slamdunk.wordarena.screens.arena.components.RetryButton;

public class ArenaGameOverScene extends ArenaScene {
    public static final String NAME = ArenaGameOverScene.class.getName();

    private MatchManager matchManager;

    public ArenaGameOverScene(MatchManager matchManager) {
        setName(NAME);
        this.matchManager = matchManager;
    }

    @Override
    public GameStates getGameState() {
        return GameStates.GAME_OVER;
    }

    @Override
    public void create(Skin skin) {
        createGameWinnerLabel(skin);
        createRetryButton(skin);
        createHomeButton(skin);
    }

    private void createGameWinnerLabel(Skin skin) {
        Label lblGameWinner = new GameWinnerLabel(skin);
        lblGameWinner.setSize(480, 50);
        lblGameWinner.setPosition(0, 650);
        addActor(lblGameWinner);
    }

    private void createRetryButton(Skin skin) {
        Button btnNextRound = new RetryButton(skin, matchManager);
        btnNextRound.setSize(150, 50);
        btnNextRound.setPosition(240 - btnNextRound.getWidth() / 2, 250 - btnNextRound.getHeight() / 2);
        addActor(btnNextRound);
    }

    private void createHomeButton(Skin skin) {
        Button btnHome = new HomeButton(skin, this);
        btnHome.setSize(150, 50);
        btnHome.setPosition(240 - btnHome.getWidth() / 2, 150 - btnHome.getHeight() / 2);
        addActor(btnHome);
    }
}
