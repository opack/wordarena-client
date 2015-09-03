package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.cell.MarkerPack;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.Scene;
import com.slamdunk.wordarena.screens.arena.components.CancelWordButton;
import com.slamdunk.wordarena.screens.arena.components.CurrentPlayerLabel;
import com.slamdunk.wordarena.screens.arena.components.CurrentWordLabel;
import com.slamdunk.wordarena.screens.arena.components.HomeButton;
import com.slamdunk.wordarena.screens.arena.components.InfoLabel;
import com.slamdunk.wordarena.screens.arena.components.PauseButton;
import com.slamdunk.wordarena.screens.arena.components.RefreshZoneButton;
import com.slamdunk.wordarena.screens.arena.components.ResumeButton;
import com.slamdunk.wordarena.screens.arena.components.StartButton;
import com.slamdunk.wordarena.screens.arena.components.ValidateWordButton;
import com.slamdunk.wordarena.screens.arena.components.ZoneMarkers;

import java.util.List;

public class ArenaRunningScene extends ArenaScene {
    public static final String NAME = ArenaRunningScene.class.getName();

    private MatchManager matchManager;

    private ZoneMarkers zoneMarkers;

    public ArenaRunningScene(MatchManager matchManager) {
        setName(NAME);
        this.matchManager = matchManager;
    }

    @Override
    public GameStates getGameState() {
        return GameStates.RUNNING;
    }

    @Override
    public void create(SlamScreen screen, Skin skin) {
        ArenaScreen arenaScreen = (ArenaScreen)screen;

        createCurrentPlayerLabel(skin);
        createCurrentWordLabel(skin);
        createInfoLabel(skin);
        createZoneMarkers();
        createValidateWordButton(skin);
        createCancelWordButton(skin);
        createRefreshZoneButton(skin);
    }

    private void createCurrentPlayerLabel(Skin skin) {
        Label lblCurrentPlayer = new CurrentPlayerLabel(skin);
        lblCurrentPlayer.setSize(480, 50);
        lblCurrentPlayer.setPosition(0, 775 - lblCurrentPlayer.getHeight() / 2);
        addActor(lblCurrentPlayer);
    }

    private void createCurrentWordLabel(Skin skin) {
        Label lblCurrentWord = new CurrentWordLabel(skin);
        lblCurrentWord.setSize(480, 50);
        lblCurrentWord.setPosition(0, 750 - lblCurrentWord.getHeight() / 2);
        addActor(lblCurrentWord);
    }

    private void createInfoLabel(Skin skin) {
        Label lblInfo = new InfoLabel(skin);
        lblInfo.setSize(480, 50);
        lblInfo.setPosition(0, 725 - lblInfo.getHeight() / 2);
        addActor(lblInfo);
    }

    private void createZoneMarkers() {
        zoneMarkers = new ZoneMarkers(matchManager);
        zoneMarkers.setPosition(0, 700);
        addActor(zoneMarkers);
    }

    private void createValidateWordButton(Skin skin) {
        Button btnValidateWord = new ValidateWordButton(skin, matchManager);
        btnValidateWord.setSize(150, 50);
        btnValidateWord.setPosition(380 - btnValidateWord.getWidth() / 2, 700 - btnValidateWord.getHeight() / 2);
        addActor(btnValidateWord);
    }

    private void createCancelWordButton(Skin skin) {
        Button btnCancelWord = new CancelWordButton(skin, matchManager);
        btnCancelWord.setSize(150, 50);
        btnCancelWord.setPosition(100 - btnCancelWord.getWidth() / 2, 700 - btnCancelWord.getHeight() / 2);
        addActor(btnCancelWord);
    }

    private void createRefreshZoneButton(Skin skin) {
        Button btnResfreshZone = new RefreshZoneButton(skin, matchManager);
        btnResfreshZone.setSize(150, 50);
        btnResfreshZone.setPosition(240 - btnResfreshZone.getWidth() / 2, 700 - btnResfreshZone.getHeight() / 2);
        addActor(btnResfreshZone);
    }

    private void createPauseButton(Skin skin) {
        Button btnPause = new PauseButton(skin, matchManager);
        btnPause.setSize(150, 50);
        btnPause.setPosition(480 - btnPause.getWidth() - 10, 800 - btnPause.getHeight() - 10);
        addActor(btnPause);
    }

    @Override
    public void doLayout() {
        if (zoneMarkers != null) {
            zoneMarkers.setPosition((WordArenaGame.SCREEN_WIDTH - zoneMarkers.getWidth()) / 2, 700);
        }
    }
}
