package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Didier on 02/09/2015.
 */
public class ArenaNameLabel extends Label {
    public static final String NAME = ArenaNameLabel.class.getName();

    public ArenaNameLabel(Skin skin) {
        super("Arena", skin);
        setName(NAME);

        setAlignment(Align.center);
    }
}
