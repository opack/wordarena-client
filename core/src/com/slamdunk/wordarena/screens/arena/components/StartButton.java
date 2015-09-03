package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;
import com.slamdunk.wordarena.screens.arena.MatchManager;

public class StartButton extends TextButton {
    public static final String NAME = StartButton.class.getName();

    public StartButton(Skin skin, final MatchManager matchManager) {
        super("Démarrer", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                matchManager.changeState(GameStates.RUNNING);
            }
        });
    }
}