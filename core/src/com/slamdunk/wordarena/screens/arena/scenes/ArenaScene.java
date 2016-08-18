package com.slamdunk.wordarena.screens.arena.scenes;

import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.enums.GameStates;

public abstract class ArenaScene extends SlamScene {
    /**
     * Retourne le GameState correspondant à cette scène
     * @return
     */
    public abstract GameStates getGameState();
}
