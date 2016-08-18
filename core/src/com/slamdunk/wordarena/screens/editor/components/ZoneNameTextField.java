package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Bouton gérant le choix d'un type de cellue
 */
public class ZoneNameTextField extends TextField {
    public static final String NAME = ZoneNameTextField.class.getName();

    public ZoneNameTextField(Skin skin) {
        super("", skin);
        setName(NAME);
    }
}
