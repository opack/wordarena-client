package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.Scene;
import com.slamdunk.wordarena.screens.arena.components.ArenaNameLabel;
import com.slamdunk.wordarena.screens.arena.components.HomeButton;
import com.slamdunk.wordarena.screens.arena.components.NextRoundButton;
import com.slamdunk.wordarena.screens.arena.components.RoundWinnerLabel;

public class ArenaRoundOverScene extends ArenaScene {
    public static final String NAME = ArenaRoundOverScene.class.getName();

    private MatchManager matchManager;

    public ArenaRoundOverScene(MatchManager matchManager) {
        setName(NAME);
        this.matchManager = matchManager;
    }

    @Override
    public GameStates getGameState() {
        return GameStates.ROUND_OVER;
    }

    @Override
    public void create(SlamScreen screen, Skin skin) {
        ArenaScreen arenaScreen = (ArenaScreen)screen;

        createRoundWinnerLabel(skin);
        createNextRoundButton(skin);
        createHomeButton(skin, arenaScreen);
    }

    private void createRoundWinnerLabel(Skin skin) {
        Label lblRoundWinner = new RoundWinnerLabel(skin);
        lblRoundWinner.setSize(480, 50);
        lblRoundWinner.setPosition(0, 650);
        addActor(lblRoundWinner);
    }

    private void createNextRoundButton(Skin skin) {
        Button btnNextRound = new NextRoundButton(skin, matchManager);
        btnNextRound.setSize(150, 50);
        btnNextRound.setPosition(240 - btnNextRound.getWidth() / 2, 250 - btnNextRound.getHeight() / 2);
        addActor(btnNextRound);
    }

    private void createHomeButton(Skin skin, ArenaScreen screen) {
        Button btnHome = new HomeButton(skin, screen);
        btnHome.setSize(150, 50);
        btnHome.setPosition(240 - btnHome.getWidth() / 2, 150 - btnHome.getHeight() / 2);
        addActor(btnHome);
    }
}
