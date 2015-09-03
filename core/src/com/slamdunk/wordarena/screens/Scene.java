package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.screen.SlamScreen;

/**
 * Gère la création et l'arrangement des composants dans une scène.
 * Cette classe ne contient pas d'intelligence : elle crée des composants (qui peuvent être
 * intelligents) et les place sur l'écran.
 */
public abstract class Scene extends Group {

    public Scene() {
        setTouchable(Touchable.childrenOnly);
    }

    /**
     * Crée les Actors qui composent cette scène.
     * Cette méthode devrait appeler une méthode createXXX() où
     * XXX correspond à 1 Actor. Cette createXXX() appellera à son
     * tour d'autres méthodes createXXX().
     */
    public abstract void create(SlamScreen screen, Skin skin);

    /**
     * Réarrange les composants si nécessaire
     */
    public void doLayout() {
        // Par défaut, rien à faire
    };
}
