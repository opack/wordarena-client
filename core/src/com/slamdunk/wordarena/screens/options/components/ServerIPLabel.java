package com.slamdunk.wordarena.screens.options.components;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class ServerIPLabel extends Label {
    public static final String NAME = ServerIPLabel.class.getName();

    public ServerIPLabel(Skin skin) {
        super("Server IP address :", skin);
        setTouchable(Touchable.disabled);
        setName(NAME);

        setAlignment(Align.right);
    }
}
