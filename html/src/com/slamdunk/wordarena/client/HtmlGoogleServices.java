package com.slamdunk.wordarena.client;

import com.slamdunk.toolkit.google.GoogleServices;

public class HtmlGoogleServices implements GoogleServices {
	@Override
	public void signIn() {
		System.out.println("HtmlGoogleServies: signIn()");
	}

	@Override
	public void signOut() {
		System.out.println("HtmlGoogleServies: signOut()");
	}

	@Override
	public void rateGame() {
		System.out.println("HtmlGoogleServices: rateGame()");
	}

	@Override
	public void submitScore(long score) {
		System.out.println("HtmlGoogleServies: submitScore(" + score + ")");
	}

	@Override
	public void showScores() {
		System.out.println("HtmlGoogleServies: showScores()");
	}

	@Override
	public boolean isSignedIn() {
		System.out.println("HtmlGoogleServies: isSignedIn()");
		return false;
	}
}