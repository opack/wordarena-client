package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Didier on 02/09/2015.
 */
public class CurrentWordLabel extends Label {
    public static final String NAME = CurrentWordLabel.class.getName();

    public CurrentWordLabel(Skin skin) {
        super("Current word", skin);
        setName(NAME);

        setAlignment(Align.center);
    }
}
