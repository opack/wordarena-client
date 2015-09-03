package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.MatchManager;

public class ValidateWordButton extends TextButton {
    public static final String NAME = ValidateWordButton.class.getName();

    public ValidateWordButton(Skin skin, final MatchManager matchManager) {
        super("Valider", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                matchManager.validateWord();
            }
        });
    }
}