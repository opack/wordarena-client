package com.slamdunk.wordarena.screens.arena.scenes;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.MatchManager;
import com.slamdunk.wordarena.screens.arena.components.ArenaNameLabel;
import com.slamdunk.wordarena.screens.arena.components.HomeButton;
import com.slamdunk.wordarena.screens.arena.components.ResumeButton;
import com.slamdunk.wordarena.screens.arena.components.StatsTable;

public class ArenaPausedScene extends ArenaScene {
    public static final String NAME = ArenaPausedScene.class.getName();

    private MatchManager matchManager;
    private StatsTable tblStats;

    public ArenaPausedScene(MatchManager matchManager) {
        setName(NAME);

        this.matchManager = matchManager;
    }

    @Override
    public GameStates getGameState() {
        return GameStates.PAUSED;
    }

    @Override
    public void create(Skin skin) {
        createArenaLabel(skin);
        createResumeButton(skin);
        createHomeButton(skin);
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

    private void createHomeButton(Skin skin) {
        Button btnHome = new HomeButton(skin, this);
        btnHome.setSize(150, 50);
        btnHome.setPosition(240 - btnHome.getWidth() / 2, 150 - btnHome.getHeight() / 2);
        addActor(btnHome);
    }

    private void createStatsTable() {
        tblStats = new StatsTable();
        addActor(tblStats);
    }

    @Override
    public void doLayout() {
        if (tblStats != null) {
            tblStats.setPosition(240 - tblStats.getWidth() / 2, 300);
        }
    }
}
