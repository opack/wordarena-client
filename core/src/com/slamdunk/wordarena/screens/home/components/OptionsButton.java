package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.home.HomeScreen;

/**
 * Created by Didier on 31/08/2015.
 */
public class OptionsButton extends TextButton {
    public OptionsButton(Skin skin, final HomeScreen screen) {
        super("Options", skin);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                System.out.println("DBG Options");
            }
        });
    }
}
