package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.wordarena.Utils;

public class ArenaSelector extends SelectBox<String> {
    public static final String NAME = ArenaSelector.class.getName();

    public ArenaSelector(Skin skin) {
        super(skin);
        setName(NAME);

        setItems(Utils.loadArenaNames());
    }
}
