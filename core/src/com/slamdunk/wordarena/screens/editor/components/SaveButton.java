package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

public class SaveButton extends TextButton {
    public static final String NAME = SaveButton.class.getName();

    public SaveButton(Skin skin, final SlamScene scene) {
        super("Enregistrer", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                EditorScreen screen = (EditorScreen)scene.getScreen();
                screen.save();
            }
        });
    }
}
