package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class PlayButton extends TextButton {
    public static final String NAME = ArenaSelector.class.getName();

    public PlayButton(Skin skin, final SlamScene scene) {
        super("Jouer", skin, "simple-button");
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                // Récupération de l'arène sélectionnée
                ArenaSelector arenaSelector = scene.findActor(ArenaSelector.NAME);
                String arena = arenaSelector.getSelected();

                // Lancement du jeu
                if (arena != null
                && !arena.isEmpty()) {
                    HomeScreen screen = (HomeScreen)scene.getScreen();
                    screen.startNewGame(arena);
                }
            }
        });
    }
}