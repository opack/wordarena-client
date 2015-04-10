package com.slamdunk.wordarena.screens.editor;

import com.slamdunk.wordarena.data.game.Player;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.screens.arena.MatchManager;
import com.slamdunk.wordarena.screens.arena.WordSelectionHandler;

public class EditorMatchManager extends MatchManager {
	@Override
	public void changeState(GameStates newState) {
	}
	
	@Override
	public WordSelectionHandler getWordSelectionHandler() {
		return null;
	}
	
	@Override
	public void nextRound() {
	}
	
	@Override
	public void validateWord() {
	}
	
	@Override
	public void cancelWord() {
	}
	
	@Override
	public void refreshStartingZone() {
	}
	
	@Override
	public void requestBack() {
	}
	
	@Override
	public void pause() {
	}
	
	@Override
	public void zoneChangedOwner(Player oldOwner, Player newOwner) {
	}
}
