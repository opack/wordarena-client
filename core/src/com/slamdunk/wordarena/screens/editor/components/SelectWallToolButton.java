package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.screens.editor.EditorScene;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.editor.tools.WallTool;

public class SelectWallToolButton extends TextButton {
    public static final String NAME = SelectWallToolButton.class.getName();

    public SelectWallToolButton(Skin skin, final EditorScene scene) {
        super(Assets.i18nBundle.get("ui.editor.tool.wall"), skin, "toggle");
        setName(NAME);

        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                EditorScreen screen = (EditorScreen)scene.getScreen();
                screen.setCurrentTool(WallTool.class);
                scene.showToolGroup(WallTool.NAME);
            }
        });
    }
}
