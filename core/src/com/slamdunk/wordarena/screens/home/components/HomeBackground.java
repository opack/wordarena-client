package com.slamdunk.wordarena.screens.home.components;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.wordarena.assets.Assets;

/**
 * Created by Didier on 31/08/2015.
 */
public class HomeBackground extends Image {

    public HomeBackground() {
        super(Assets.atlas.findRegion("bkgnd_home"));
    }
}
