package com.slamdunk.wordarena.screens.preeditor.components;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Didier on 06/09/2015.
 */
public class GroupLabel extends Label {
    public static final String NAME = GroupLabel.class.getName();

    public GroupLabel(String text, Skin skin) {
        super(text, skin);
        setTouchable(Touchable.disabled);
        setName(NAME);

        setAlignment(Align.center);
    }
}
