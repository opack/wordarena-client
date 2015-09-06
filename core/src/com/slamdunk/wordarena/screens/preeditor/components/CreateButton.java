package com.slamdunk.wordarena.screens.preeditor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.screens.preeditor.PreEditorScreen;

public class CreateButton extends TextButton {
    public static final String NAME = CreateButton.class.getName();

    public CreateButton(Skin skin, final SlamScene scene) {
        super("Créer", skin);
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                TextField txtWidth = (TextField)scene.findActor(WidthTextField.NAME);
                int width = Integer.parseInt(txtWidth.getText());

                TextField txtHeight = (TextField)scene.findActor(HeightTextField.NAME);
                int height = Integer.parseInt(txtHeight.getText());

                TextField txtName = (TextField)scene.findActor(NameTextField.NAME);
                String name = txtName.getText();

//				Assets.playSound(Assets.clickSound);
                PreEditorScreen screen = (PreEditorScreen)scene.getScreen();
                screen.create(txtName.getText(), width, height);
            }
        });
    }
}