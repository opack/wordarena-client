package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.components.ArenaNameLabel;
import com.slamdunk.wordarena.screens.arena.components.HomeButton;
import com.slamdunk.wordarena.screens.arena.components.ResumeButton;
import com.slamdunk.wordarena.screens.arena.stats.StatsTable;

public class ArenaPausedScene extends ArenaScene {
    public static final String NAME = ArenaPausedScene.class.getName();

    private MatchManager matchManager;

    public ArenaPausedScene(MatchManager matchManager) {
        setName(NAME);

        this.matchManager = matchManager;
    }

    @Override
    public GameStates getGameState() {
        return GameStates.PAUSED;
    }

    @Override
    public void create(SlamScreen screen, Skin skin) {
        ArenaScreen arenaScreen = (ArenaScreen)screen;

        createArenaLabel(skin);
        createResumeButton(skin);
        createHomeButton(skin, arenaScreen);
        createStatsTable();
    }

    private void createArenaLabel(Skin skin) {
        Label lblArena = new ArenaNameLabel(skin);
        lblArena.setSize(480, 50);
        lblArena.setPosition(0, 650);
        addActor(lblArena);
    }

    private void createResumeButton(Skin skin) {
        Button btnResume = new ResumeButton(skin, matchManager);
        btnResume.setSize(150, 50);
        btnResume.setPosition(240 - btnResume.getWidth() / 2, 250 - btnResume.getHeight() / 2);
        addActor(btnResume);
    }

    private void createHomeButton(Skin skin, ArenaScreen screen) {
        Button btnHome = new HomeButton(skin, screen);
        btnHome.setSize(150, 50);
        btnHome.setPosition(240 - btnHome.getWidth() / 2, 150 - btnHome.getHeight() / 2);
        addActor(btnHome);
    }

    private void createStatsTable() {
        Table tblStats = new StatsTable();
        tblStats.setVisible(false);
        tblStats.setPosition(0, 100);
        addActor(tblStats);
    }
}
