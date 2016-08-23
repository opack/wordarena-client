package com.slamdunk.wordarena.screens.options.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.screens.home.components.ArenaSelector;
import com.slamdunk.wordarena.screens.preeditor.PreEditorScreen;

public class SaveButton extends TextButton {
    public static final String NAME = SaveButton.class.getName();

    public SaveButton(Skin skin, final SlamScene scene) {
        super("Sauver", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                TextField txtServerIP = scene.findActor(ServerIPTextField.NAME);
                String serverIP = txtServerIP.getText();

                if (serverIP.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}")) {
                    Assets.appProperties.setStringProperty("server.address", serverIP);
                } else {
                    System.err.println("ERROR : Server IP (" + serverIP + ") is invalid and will not be saved.");
                }
            }
        });
    }
}