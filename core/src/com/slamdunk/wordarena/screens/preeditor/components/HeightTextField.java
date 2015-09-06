package com.slamdunk.wordarena.screens.preeditor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by Didier on 06/09/2015.
 */
public class HeightTextField extends TextField {
    public static final String NAME = HeightTextField.class.getName();

    public HeightTextField(Skin skin) {
        super("", skin);
        setName(NAME);
    }
}
