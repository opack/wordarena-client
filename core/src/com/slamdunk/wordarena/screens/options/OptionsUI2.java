package com.slamdunk.wordarena.screens.options;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.assets.Assets;

public class OptionsUI2 extends UIOverlay {
    public OptionsUI2() {
        // Par défaut, on travaillera dans un Stage qui prend tout l'écran
        Viewport viewport = new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
		createStage(viewport);
    }

    @Override
    protected void loadScenes() {
        loadScene(new OptionsScene(), Assets.uiSkinDefault);
    }
}
