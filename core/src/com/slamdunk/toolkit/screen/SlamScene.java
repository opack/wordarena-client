package com.slamdunk.toolkit.screen;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.screen.overlays.SlamOverlay;

/**
 * Gère la création et l'arrangement des composants dans une scène.
 * Cette classe ne contient pas d'intelligence : elle crée des composants (qui peuvent être
 * intelligents) et les place sur l'écran.
 */
public abstract class SlamScene extends Group {

    /**
     * Overlay auquel appartient cette scène
     */
    private SlamOverlay overlay;

    public SlamScene() {
        setTouchable(Touchable.childrenOnly);
    }

    /**
     * Crée les Actors qui composent cette scène.
     * Cette méthode devrait appeler une méthode createXXX() où
     * XXX correspond à 1 Actor. Cette createXXX() appellera à son
     * tour d'autres méthodes createXXX().
     */
    public abstract void create(Skin skin);

    /**
     * Réarrange les composants si nécessaire
     */
    public void doLayout() {
        // Par défaut, rien à faire
    }

    public SlamOverlay getOverlay() {
        return overlay;
    }

    public void setOverlay(SlamOverlay overlay) {
        this.overlay = overlay;
    }

    public SlamScreen getScreen() {
        return overlay.getScreen();
    }
}
