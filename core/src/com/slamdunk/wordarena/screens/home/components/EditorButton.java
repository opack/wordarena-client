package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.SlamScene;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class EditorButton extends TextButton {
    public static final String NAME = EditorButton.class.getName();

    public EditorButton(Skin skin, final SlamScene scene) {
        super("Editeur", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                ((HomeScreen)scene.getScreen()).launchEditor();
            }
        });
    }
}