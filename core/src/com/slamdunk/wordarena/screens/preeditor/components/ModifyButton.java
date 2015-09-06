package com.slamdunk.wordarena.screens.preeditor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.SlamScene;
import com.slamdunk.wordarena.screens.home.components.ArenaSelector;
import com.slamdunk.wordarena.screens.preeditor.PreEditorScreen;

public class ModifyButton extends TextButton {
    public static final String NAME = ModifyButton.class.getName();

    public ModifyButton(Skin skin, final SlamScene scene) {
        super("Modifier", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                ArenaSelector selArena = (ArenaSelector)scene.findActor(ArenaSelector.NAME);
                String name = selArena.getSelected();

                if (name != null && !name.isEmpty()) {
//				    Assets.playSound(Assets.clickSound);
                    PreEditorScreen screen = (PreEditorScreen) scene.getScreen();
                    screen.modify(name);
                }
            }
        });
    }
}