package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class QuitButton extends TextButton {

        public QuitButton(Skin skin, final HomeScreen screen) {
            super("Quitter", skin);

            addListener(new ButtonClickListener() {
                @Override
                public void clicked(Button button) {
                    screen.promptExit();
                }
            });
        }
    }