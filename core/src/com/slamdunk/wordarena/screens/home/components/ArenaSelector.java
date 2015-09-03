package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.slamdunk.wordarena.Utils;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class ArenaSelector extends SelectBox<String> {

    public ArenaSelector(Skin skin, final HomeScreen screen) {
        super(skin);
        setItems(Utils.loadArenaNames());

        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.startNewGame(getSelected());
            }
        });
    }
}
