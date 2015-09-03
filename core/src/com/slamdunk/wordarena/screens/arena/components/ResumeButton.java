package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.MatchManager;

public class ResumeButton extends StartButton {
    public static final String NAME = ResumeButton.class.getName();

    public ResumeButton(Skin skin, final MatchManager matchManager) {
        super(skin, matchManager);
        setName(NAME);
        setText("Continuer");
    }
}