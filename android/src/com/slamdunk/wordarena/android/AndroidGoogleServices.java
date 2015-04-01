package com.slamdunk.wordarena.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.slamdunk.toolkit.google.GoogleServices;
import com.slamdunk.wordarena.R;

public class AndroidGoogleServices implements GoogleServices {
	private final static int REQUEST_CODE_UNUSED = 9002;

	private Activity activity;
	private GameHelper gameHelper;
	
	/**
	 * Méthode appelée lors du onCreate() sur l'Activity afin
	 * d'initialiser le GameHelper
	 */
	public void create(Activity activity) {
		this.activity = activity;
		
		gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);
		
		// Cette ligne permet d'éviter les tentatives d'auto-signin au démarrage
		// de l'application si on met 0. Voir la javadoc de la méthode pour une
		// explication détaillée
		//gameHelper.setMaxAutoSignInAttempts(0);
		
		gameHelper.setup(new GameHelper.GameHelperListener() {
			@Override
			public void onSignInSucceeded() {
			}

			@Override
			public void onSignInFailed() {
			}
		});
	}
	
	/**
	 * Appelée par le onStart() de l'Activity
	 */
	public void onStart() {
		gameHelper.onStart(activity);
	}

	/**
	 * Appelée par le onStop() de l'Activity
	 */
	public void onStop() {
		gameHelper.onStop();
	}

	/**
	 * Appelée par le onActivityResult() de l'Activity
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void signIn() {
		try {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("AndroidGoogleServices", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		try {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("AndroidGoogleServices", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame() {
		// Replace the end of the URL with the package of your game
		String str ="https://play.google.com/store/apps/details?id=com.slamdunk.wordarena";
		activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void submitScore(long score) {
		if (isSignedIn()) {
			Games.Leaderboards.submitScore(
				gameHelper.getApiClient(),
				activity.getString(R.string.leaderboard_test),
				score);
			
			showScores();
		} else {
			// Maybe sign in here then redirect to submitting score?
			Gdx.app.log("AndroidGoogleServices", "Score submission failed: user not signed in.");
		}
	}

	@Override
	public void showScores() {
		if (isSignedIn()) {
			Intent leaderBoardIntent = Games.Leaderboards.getLeaderboardIntent(
				gameHelper.getApiClient(),
				activity.getString(R.string.leaderboard_test));
			
			activity.startActivityForResult(leaderBoardIntent, REQUEST_CODE_UNUSED);
		} else {
			// Maybe sign in here then redirect to showing scores?
			Gdx.app.log("AndroidGoogleServices", "Score show failed: user not signed in.");
		}
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}
}
