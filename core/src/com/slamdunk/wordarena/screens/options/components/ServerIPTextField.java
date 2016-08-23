package com.slamdunk.wordarena.screens.options.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.slamdunk.wordarena.assets.Assets;

public class ServerIPTextField extends TextField {
    public static final String NAME = ServerIPTextField.class.getName();

    public ServerIPTextField(Skin skin) {
        super(Assets.appProperties.getProperty("server.address", ""), skin);
        setName(NAME);
    }
}
