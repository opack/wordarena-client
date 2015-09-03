package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.MatchManager;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class HomeButton extends TextButton {
    public static final String NAME = HomeButton.class.getName();

    public HomeButton(Skin skin, final SlamScreen screen) {
        super("Accueil", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                screen.getGame().setScreen(HomeScreen.NAME);
            }
        });
    }
}