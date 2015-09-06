package com.slamdunk.wordarena.screens.preeditor;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.WordArenaGame;

public class PreEditorUI extends UIOverlay {
	private PreEditorScreen screen;
	
	public PreEditorUI(PreEditorScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
	}
	
	private void loadScene() {
//DBG		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
//		sceneLoader.loadScene("PreEditor");
//		getStage().addActor(sceneLoader.sceneActor);
//
//		// Bouton Create
//		final TextBoxItem txtName = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtName");
//		final TextBoxItem txtWidth = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtWidth");
//		final TextBoxItem txtHeight = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtHeight");
//		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnCreate", new ClickListener() {
//			public void clicked(InputEvent event, float x, float y) {
//				int width = Integer.parseInt(txtWidth.getText());
//				int height = Integer.parseInt(txtHeight.getText());
//				screen.create(txtName.getText(), width, height);
//			}
//		});
//
//		// Choix de l'arène à modifier
//		@SuppressWarnings("unchecked")
//		final SelectBoxItem<String> selArena = (SelectBoxItem<String>) sceneLoader.sceneActor.getItemById("selArena");
//		selArena.setWidth(150);
//		selArena.setItems(Utils.loadArenaNames());
//		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnModify", new ClickListener() {
//			public void clicked(InputEvent event, float x, float y) {
//				screen.modify(selArena.getSelected());
//			}
//		});
	}
}
