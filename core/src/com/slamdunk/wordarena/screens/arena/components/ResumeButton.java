package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.wordarena.screens.arena.MatchManager;

public class ResumeButton extends StartButton {
    public static final String NAME = ResumeButton.class.getName();

    public ResumeButton(Skin skin, final MatchManager matchManager) {
        super(skin, matchManager);
        setName(NAME);
        setText("Continuer");
    }
}