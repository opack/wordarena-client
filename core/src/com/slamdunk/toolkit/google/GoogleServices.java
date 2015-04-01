package com.slamdunk.toolkit.google;

public interface GoogleServices {
	public void signIn();
	public void signOut();
	public void rateGame();
	public void submitScore(long score);
	public void showScores();
	public boolean isSignedIn();
}
