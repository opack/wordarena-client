package com.slamdunk.wordarena.screens.arena.scenes;

import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.toolkit.screen.SlamScene;

public abstract class ArenaScene extends SlamScene {
    /**
     * Retourne le GameState correspondant à cette scène
     * @return
     */
    public abstract GameStates getGameState();
}
