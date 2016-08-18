package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.wordarena.assets.Assets;

/**
 * Created by Didier on 31/08/2015.
 */
public class TopMaskImage extends Image {

    public TopMaskImage() {
        super(Assets.atlas.findRegion("ui/image_topmask"));
        setTouchable(Touchable.disabled);
    }
}
