package com.slamdunk.wordarena.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.RatioResolutionStrategy;
import com.slamdunk.wordarena.WordArenaGame;

public class AndroidLauncher extends AndroidApplication {
	private AndroidGoogleServices googleServices;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create the GameHelper
		googleServices = new AndroidGoogleServices();
		googleServices.create(this);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.resolutionStrategy = new RatioResolutionStrategy(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
		config.hideStatusBar = true;
		
		initialize(new WordArenaGame(googleServices), config);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		googleServices.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		googleServices.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		googleServices.onActivityResult(requestCode, resultCode, data);
	}
}
