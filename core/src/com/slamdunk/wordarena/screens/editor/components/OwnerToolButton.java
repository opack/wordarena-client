package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.editor.tools.OwnerTool;

/**
 * Bouton gérant le choix d'un type de cellue
 */
public class OwnerToolButton extends TextButton {
    public static final String NAME = OwnerToolButton.class.getName();

    public OwnerToolButton(Skin skin, final PlayerData player, final SlamScene scene) {
        super(player.name, skin, "toggle");
        setName(NAME);

        //
        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                final EditorScreen screen = (EditorScreen)scene.getScreen();
                OwnerTool tool = screen.getTool(OwnerTool.class);
                tool.setValue(player);
                System.out.println("DBG " + player);
            }
        });
    }
}
