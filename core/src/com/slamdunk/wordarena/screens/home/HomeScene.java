package com.slamdunk.wordarena.screens.home;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.screens.Scene;
import com.slamdunk.wordarena.screens.home.components.ArenaSelector;
import com.slamdunk.wordarena.screens.home.components.EditorButton;
import com.slamdunk.wordarena.screens.home.components.HomeBackground;
import com.slamdunk.wordarena.screens.home.components.OptionsButton;
import com.slamdunk.wordarena.screens.home.components.QuitButton;

/**
 * Crée les composants et les positionne sur la scène
 */
public class HomeScene extends Scene {
    public static final String NAME = HomeScene.class.getName();

    public HomeScene() {
        setName(NAME);
    }

    @Override
    public void create(SlamScreen screen, Skin skin) {
        HomeScreen homeScreen = (HomeScreen)screen;

        createBackground();
        createArenaSelector(skin, homeScreen);
        createQuitButton(skin, homeScreen);
        createOptionsButton(skin, homeScreen);
        createEditorButton(skin, homeScreen);
    }

    private void createArenaSelector(Skin skin, HomeScreen screen) {
        SelectBox<String> selArena = new ArenaSelector(skin, screen);
        selArena.setSize(150, 50);
        selArena.setPosition(240 - selArena.getWidth() / 2, 450 - selArena.getHeight() / 2);
        addActor(selArena);
    }

    private void createEditorButton(Skin skin, HomeScreen screen) {
        Button btnEditor = new EditorButton(skin, screen);
        btnEditor.setSize(150, 50);
        btnEditor.setPosition(240 - btnEditor.getWidth() / 2, 350 - btnEditor.getHeight() / 2);
        addActor(btnEditor);
    }

    private void createOptionsButton(Skin skin, HomeScreen screen) {
        Button btnOptions = new OptionsButton(skin, screen);
        btnOptions.setSize(150, 50);
        btnOptions.setPosition(240 - btnOptions.getWidth() / 2, 250 - btnOptions.getHeight() / 2);
        addActor(btnOptions);
    }

    private void createQuitButton(Skin skin, HomeScreen screen) {
        Button btnQuit = new QuitButton(skin, screen);
        btnQuit.setSize(150, 50);
        btnQuit.setPosition(240 - btnQuit.getWidth() / 2, 150 - btnQuit.getHeight() / 2);
        addActor(btnQuit);
    }

    /**
     * Image de fond
     */
    private void createBackground() {
        Image imgBackground = new HomeBackground();
        imgBackground.setPosition(0, 0);
        addActor(imgBackground);
    }
}
