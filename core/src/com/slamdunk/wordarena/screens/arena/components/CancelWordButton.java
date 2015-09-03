package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.arena.MatchManager;

public class CancelWordButton extends TextButton {
    public static final String NAME = CancelWordButton.class.getName();

    public CancelWordButton(Skin skin, final MatchManager matchManager) {
        super("Annuler", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                matchManager.cancelWord();
            }
        });
    }
}