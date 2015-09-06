package com.slamdunk.wordarena.screens.preeditor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by Didier on 06/09/2015.
 */
public class WidthTextField extends TextField {
    public static final String NAME = WidthTextField.class.getName();

    public WidthTextField(Skin skin) {
        super("", skin);
        setName(NAME);
    }
}
