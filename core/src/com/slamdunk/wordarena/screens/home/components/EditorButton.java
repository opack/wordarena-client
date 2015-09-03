package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class EditorButton extends TextButton {

        public EditorButton(Skin skin, final HomeScreen screen) {
            super("Editeur", skin);

            addListener(new ButtonClickListener() {
                @Override
                public void clicked(Button button) {
                    screen.launchEditor();
                }
            });
        }
    }