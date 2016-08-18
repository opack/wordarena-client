package com.slamdunk.wordarena.screens.home;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.screens.home.components.ArenaSelector;
import com.slamdunk.wordarena.screens.home.components.EditorButton;
import com.slamdunk.wordarena.screens.home.components.HomeBackground;
import com.slamdunk.wordarena.screens.home.components.OptionsButton;
import com.slamdunk.wordarena.screens.home.components.PlayButton;
import com.slamdunk.wordarena.screens.home.components.QuitButton;

/**
 * Crée les composants et les positionne sur la scène
 */
public class HomeScene extends SlamScene {
    public static final String NAME = HomeScene.class.getName();

    public HomeScene() {
        setName(NAME);
    }

    @Override
    public void create(Skin skin) {
        createBackground();
        createArenaSelector(skin);
        createPlayButton(Assets.uiSkinSpecific);
        createQuitButton(Assets.uiSkinSpecific);
        createOptionsButton(Assets.uiSkinSpecific);
        createEditorButton(Assets.uiSkinSpecific);
    }

    private void createArenaSelector(Skin skin) {
        SelectBox<String> selArena = new ArenaSelector(skin);
        selArena.setSize(150, 50);
        selArena.setPosition(155 - selArena.getWidth() / 2, 450 - selArena.getHeight() / 2);
        addActor(selArena);
    }

    private void createPlayButton(Skin skin) {
        Button btnOptions = new PlayButton(skin, this);
        btnOptions.setSize(150, 50);
        btnOptions.setPosition(325 - btnOptions.getWidth() / 2, 450 - btnOptions.getHeight() / 2);
        addActor(btnOptions);
    }

    private void createEditorButton(Skin skin) {
        Button btnEditor = new EditorButton(skin, this);
        btnEditor.setSize(150, 50);
        btnEditor.setPosition(240 - btnEditor.getWidth() / 2, 350 - btnEditor.getHeight() / 2);
        addActor(btnEditor);
    }

    private void createOptionsButton(Skin skin) {
        Button btnOptions = new OptionsButton(skin, this);
        btnOptions.setSize(150, 50);
        btnOptions.setPosition(240 - btnOptions.getWidth() / 2, 250 - btnOptions.getHeight() / 2);
        addActor(btnOptions);
    }

    private void createQuitButton(Skin skin) {
        Button btnQuit = new QuitButton(skin, this);
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
