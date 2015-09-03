package com.slamdunk.wordarena.screens.arena;

import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.Scene;

public abstract class ArenaScene extends Scene {
    /**
     * Retourne le GameState correspondant à cette scène
     * @return
     */
    public abstract GameStates getGameState();
}
