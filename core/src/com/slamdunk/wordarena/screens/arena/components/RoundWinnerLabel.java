package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Didier on 02/09/2015.
 */
public class RoundWinnerLabel extends Label {
    public static final String NAME = RoundWinnerLabel.class.getName();

    public RoundWinnerLabel(Skin skin) {
        super("Round winner", skin);
        setName(NAME);

        setAlignment(Align.center);
    }
}
