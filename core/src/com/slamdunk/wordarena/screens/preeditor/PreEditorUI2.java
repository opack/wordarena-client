package com.slamdunk.wordarena.screens.preeditor;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.WordArenaGame;

/**
 * Created by Didier on 30/08/2015.
 */
public class PreEditorUI2 extends UIOverlay {
    public PreEditorUI2() {
        // Par défaut, on travaillera dans un Stage qui prend tout l'écran
        Viewport viewport = new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
		createStage(viewport);
    }

    @Override
    protected void loadScenes() {
        loadScene(new PreEditorScene());
    }
}
