package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class HomeButton extends TextButton {
    public static final String NAME = HomeButton.class.getName();

    public HomeButton(Skin skin, final SlamScene scene) {
        super("Accueil", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                scene.getScreen().getGame().setScreen(HomeScreen.NAME);
            }
        });
    }
}