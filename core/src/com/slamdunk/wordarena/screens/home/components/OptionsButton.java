package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class OptionsButton extends TextButton {
    public static final String NAME = OptionsButton.class.getName();

    public OptionsButton(Skin skin, final SlamScene scene) {
        super("Options", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                ((HomeScreen)scene.getScreen()).launchOptions();
            }
        });
    }
}
