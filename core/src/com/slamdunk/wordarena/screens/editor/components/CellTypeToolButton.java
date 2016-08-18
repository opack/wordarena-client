package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;

/**
 * Bouton gérant le choix d'un type de cellue
 */
public class CellTypeToolButton extends TextButton {
    public static final String NAME = CellTypeToolButton.class.getName();

    public CellTypeToolButton(Skin skin, String text, final CellTypes cellType, final SlamScene scene) {
        super(text, skin, "toggle");
        setName(NAME);

        //
        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                final EditorScreen screen = (EditorScreen)scene.getScreen();
                CellTypeTool tool = screen.getTool(CellTypeTool.class);
                tool.setValue(cellType);
            }
        });
    }
}
