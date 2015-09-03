package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.components.ArenaNameLabel;
import com.slamdunk.wordarena.screens.arena.components.HomeButton;
import com.slamdunk.wordarena.screens.arena.components.StartButton;

public class ArenaReadyScene extends ArenaScene {
    public static final String NAME = ArenaReadyScene.class.getName();

    private MatchManager matchManager;

    public ArenaReadyScene(MatchManager matchManager) {
        setName(NAME);
        this.matchManager = matchManager;
    }

    @Override
    public GameStates getGameState() {
        return GameStates.READY;
    }

    @Override
    public void create(SlamScreen screen, Skin skin) {
        ArenaScreen arenaScreen = (ArenaScreen)screen;

        createArenaLabel(skin);
        createStartButton(skin);
        createHomeButton(skin, arenaScreen);
    }

    private void createArenaLabel(Skin skin) {
        Label lblArena = new ArenaNameLabel(skin);
        lblArena.setSize(480, 50);
        lblArena.setPosition(0, 650);
        addActor(lblArena);
    }

    private void createStartButton(Skin skin) {
        Button btnStart = new StartButton(skin, matchManager);
        btnStart.setSize(150, 50);
        btnStart.setPosition(240 - btnStart.getWidth() / 2, 250 - btnStart.getHeight() / 2);
        addActor(btnStart);
    }

    private void createHomeButton(Skin skin, ArenaScreen screen) {
        Button btnHome = new HomeButton(skin, screen);
        btnHome.setSize(150, 50);
        btnHome.setPosition(240 - btnHome.getWidth() / 2, 150 - btnHome.getHeight() / 2);
        addActor(btnHome);
    }
}
