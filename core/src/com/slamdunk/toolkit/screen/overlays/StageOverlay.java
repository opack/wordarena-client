package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.toolkit.screen.SlamScene;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Une couche d'affichage qui contient un Stage dans lequel on peut mettre tout et n'importe quoi.
 * Cela permet d'avoir plusieurs stages et donc plusieurs caméras différentes, utiles pour superposer
 * par exemple le monde, une minimap, une couche d'UI...
 */
public abstract class StageOverlay implements SlamOverlay {
	private Stage stage;
	private SlamScreen screen;
	private Map<String, SlamScene> scenes;
	
	public Stage getStage() {
		return stage;
	}
	
	public SlamScreen getScreen() {
		return screen;
	}

	public void setScreen(SlamScreen screen) {
		this.screen = screen;
	}

	public void createStage(Viewport viewport) {
		stage = new Stage(viewport);

		scenes = new HashMap<String, SlamScene>();
		loadScenes();
	}

	/**
	 * Charge les scène
	 */
	protected void loadScenes() {
		// Par défaut, aucune scène
	}

	/**
	 * Crée la scène (appel de create()) et l'ajoute au Stage
	 * et à la liste de scènes
	 * @param scene
	 */
	protected SlamScene loadScene(SlamScene scene) {
		scene.create(Assets.uiSkinDefault);

		getStage().addActor(scene);
		scenes.put(scene.getName(), scene);
		scene.setOverlay(this);

		return scene;
	}

	public SlamScene getScene(String name) {
		return scenes.get(name);
	}

	public Collection<SlamScene> getScenes() {
		return scenes.values();
	}
	
	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}
	
	@Override
	public void act(float delta) {
		if (stage != null) {
			stage.act(delta);
		}
	}
	
	@Override
	public void draw() {
		if (stage != null) {
			stage.draw();
		}
	}

	@Override
	public void dispose() {
		if (stage != null) {
			stage.dispose();
		}
	}
}
