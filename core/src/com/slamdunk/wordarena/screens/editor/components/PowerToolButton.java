package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.editor.tools.PowerTool;

/**
 * Bouton gérant le choix d'une puissance de cellule
 */
public class PowerToolButton extends TextButton {
    public static final String NAME = PowerToolButton.class.getName();

    public PowerToolButton(Skin skin, final int power, final SlamScene scene) {
        super(String.valueOf(power), skin, "toggle");
        setName(NAME);

        //
        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                final EditorScreen screen = (EditorScreen)scene.getScreen();
                PowerTool tool = screen.getTool(PowerTool.class);
                tool.setValue(power);
            }
        });
    }
}
