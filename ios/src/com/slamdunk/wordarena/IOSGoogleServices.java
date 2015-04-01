package com.slamdunk.wordarena;

import com.slamdunk.toolkit.google.GoogleServices;

public class IOSGoogleServices implements GoogleServices {
	@Override
	public void signIn() {
		System.out.println("IOSGoogleServies: signIn()");
	}

	@Override
	public void signOut() {
		System.out.println("IOSGoogleServies: signOut()");
	}

	@Override
	public void rateGame() {
		System.out.println("IOSGoogleServices: rateGame()");
	}

	@Override
	public void submitScore(long score) {
		System.out.println("IOSGoogleServies: submitScore(" + score + ")");
	}

	@Override
	public void showScores() {
		System.out.println("IOSGoogleServies: showScores()");
	}

	@Override
	public boolean isSignedIn() {
		System.out.println("IOSGoogleServies: isSignedIn()");
		return false;
	}
}