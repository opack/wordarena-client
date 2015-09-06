package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.SlamScene;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class QuitButton extends TextButton {
    public static final String NAME = QuitButton.class.getName();

    public QuitButton(Skin skin, final SlamScene scene) {
        super("Quitter", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                ((HomeScreen)scene.getScreen()).promptExit();
            }
        });
    }
}