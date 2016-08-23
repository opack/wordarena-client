package com.slamdunk.wordarena.screens.options;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.screens.common.components.HomeButton;
import com.slamdunk.wordarena.screens.options.components.SaveButton;
import com.slamdunk.wordarena.screens.options.components.ServerIPLabel;
import com.slamdunk.wordarena.screens.options.components.ServerIPTextField;

public class OptionsScene extends SlamScene {

    @Override
    public void create(Skin skin) {
        createServerIPLabel(skin);
        createServerIPTextField(skin);

        createSaveButton(Assets.uiSkinSpecific);
        createHomeButton(skin);

    }

    private void createServerIPLabel(Skin skin) {
        Label lblServerIP = new ServerIPLabel(skin);
        lblServerIP.setPosition(240, 580, Align.right);
        addActor(lblServerIP);
    }

    private void createServerIPTextField(Skin skin) {
        TextField txtServerIP = new ServerIPTextField(skin);
        txtServerIP.setSize(150, 50);
        txtServerIP.setPosition(240, 580);
        addActor(txtServerIP);
    }

    private void createSaveButton(Skin skin) {
        Button btnSave = new SaveButton(skin, this);
        btnSave.setSize(150, 50);
        btnSave.setPosition(240 - btnSave.getWidth() / 2, 470);
        addActor(btnSave);
    }

    private void createHomeButton(Skin skin) {
        Button btnHome = new HomeButton(skin, this);
        btnHome.setSize(150, 50);
        btnHome.setPosition(10, 800 - btnHome.getHeight());
        addActor(btnHome);
    }
}
