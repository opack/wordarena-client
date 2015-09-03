package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.arena.MatchManager;

public class RefreshZoneButton extends TextButton {
    public static final String NAME = RefreshZoneButton.class.getName();

    public RefreshZoneButton(Skin skin, final MatchManager matchManager) {
        super("Nouvelles lettres", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                matchManager.refreshStartingZone();
            }
        });
    }
}