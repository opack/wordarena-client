package com.slamdunk.wordarena.screens.home;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.screens.Scene;

/**
 * Created by Didier on 30/08/2015.
 */
public class HomeUI2 extends UIOverlay {
    private HomeScreen screen;
    private Scene scene;

    public HomeUI2(final HomeScreen screen) {
        this.screen = screen;

        // Par défaut, on travaillera dans un Stage qui prend tout l'écran
        Viewport viewport = new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
		createStage(viewport);

        // Charge les éléments de la scène Overlap2D
        scene = new HomeScene();
        scene.create(screen, Assets.uiSkin);
        getStage().addActor(scene);
    }
}
