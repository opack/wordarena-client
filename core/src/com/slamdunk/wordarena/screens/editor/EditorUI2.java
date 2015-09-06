package com.slamdunk.wordarena.screens.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.ui.Overlap2DUtils;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.SimpleButtonI18NScript;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;
import com.slamdunk.wordarena.screens.arena.ArenaUI2;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;
import com.slamdunk.wordarena.screens.editor.tools.EditorTool;
import com.slamdunk.wordarena.screens.editor.tools.LetterTool;
import com.slamdunk.wordarena.screens.editor.tools.OwnerTool;
import com.slamdunk.wordarena.screens.editor.tools.PowerTool;
import com.slamdunk.wordarena.screens.editor.tools.WallTool;
import com.slamdunk.wordarena.screens.editor.tools.ZoneTool;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.LabelItem;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;
import com.uwsoft.editor.renderer.actor.TextBoxItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EditorUI2 extends ArenaUI2 {
	private EditorScreen screen;
	private LabelItem lblName;

	private Array<ZoneActor> zones;
	private SelectBoxItem<ZoneActor> selZone;

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends EditorTool>, SimpleButtonI18NScript> toolsScripts;

	public EditorUI2(EditorScreen screen) {
		super(screen.getMatchManager());
		
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
//DBG		loadScene();
	}
	
	public void loadData(ArenaOverlay arena) {
		// Change le nom de l'arène
		lblName.setText(arena.getData().name);
		
		// Charge les zones existantes
		loadZonesFromArena(arena);
	}
	
	private void loadZonesFromArena(ArenaOverlay arena) {
		// Trie les zones existantes
		List<String> sortedZones = new ArrayList<String>();
		for (ZoneData zoneData : arena.getData().zones) {
			sortedZones.add(zoneData.id);
		}
		Collections.sort(sortedZones);
		
		// Ajoute les zones à la liste
		zones.clear();
		zones.add(ZoneActor.NONE);
		for (String zoneId : sortedZones) {
			zones.add(arena.getZone(zoneId));
		}
		selZone.setItems(zones);
	}
	
	@SuppressWarnings("rawtypes")
	public void loadScene() {
//DBG		toolsScripts = new HashMap<Class<? extends EditorTool>, SimpleButtonI18NScript>();
//
//		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
//		sceneLoader.loadScene("Editor");
//		getStage().addActor(sceneLoader.sceneActor);
//
//		lblName = sceneLoader.sceneActor.getLabelById("lblName");
//
//		// Bouton Save
//		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnHome", new ClickListener() {
//			public void clicked(InputEvent event, float x, float y) {
//				getScreen().getGame().setScreen(HomeScreen.NAME);
//			}
//		});
//
//		// Bouton Save
//		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnSave", new ClickListener() {
//			public void clicked(InputEvent event, float x, float y) {
//				screen.save();
//			}
//		});
//
//		// Choix du type de cellule
//		loadToolType(sceneLoader);
//
//		// Choix de la lettre
//		loadToolLetter(sceneLoader);
//
//		// Bouton Power
//		loadToolPower(sceneLoader);
//
//		// Bouton Owner
//		loadToolOwner(sceneLoader);
//
//		// Bouton Zone
//		loadToolZone(sceneLoader);
//
//		// Bouton Create wall
//		loadToolWall(sceneLoader);
	}
	
	@SuppressWarnings("rawtypes")
	private void setSelectToolScript(SceneLoader sceneLoader, String buttonId, final Class<? extends EditorTool> toolClass) {
		// Affecte le script au composant
		final SimpleButtonI18NScript script = Overlap2DUtils.createSimpleButtonScript(sceneLoader, buttonId, new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				selectTool(toolClass);
			}
		});
		
		// Enregistre le script pour pouvoir le désélectionner lors de la sélection d'un autre outil
		toolsScripts.put(toolClass, script);
	}
	
	private void loadToolWall(SceneLoader sceneLoader) {
		setSelectToolScript(sceneLoader, "btnToolCreateWall", WallTool.class);
	}

	@SuppressWarnings("unchecked")
	private void loadToolZone(SceneLoader sceneLoader) {
		// Initialise la liste des zones
		zones = new Array<ZoneActor>();
		zones.add(ZoneActor.NONE);
		selZone = (SelectBoxItem<ZoneActor>)sceneLoader.sceneActor.getItemById("selZone");
		selZone.setWidth(150);
		selZone.setItems(zones);
		selZone.setSelected(screen.getTool(ZoneTool.class).getValue());
		
		// Initialise la partie de création de zone
		final TextBoxItem txtZone = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtZone");
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnCreateZone", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// Vérifie que le champ de nom de zone n'est vide 
				final String zoneName = txtZone.getText();
				if (zoneName.isEmpty()) {
					return;
				}
				
				// Vérifie que le champ de nom de zone n'indique pas une zone existante
				boolean exists = false;
				for (ZoneActor zone : selZone.getItems()) {
					if (zoneName.equals(zone.getData().id)) {
						exists = true;
						break;
					}
				}
				
				// Crée le nouvelle zone et l'ajoute à la liste
				final ZoneActor newZone = screen.getOrCreateZone(zoneName);
				if (!exists) {
					zones.add(newZone);
					selZone.setItems(zones);
				}					
				txtZone.setText("");
				
				// Sélectionne la nouvelle zone
				selZone.setSelected(newZone);
			}
		});
		
		// Ajoute le listener pour màj la valeur applicable dans l'outil
		selZone.addListener(new ChangeListener() {
			private ZoneActor lastValue;
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ZoneTool tool = screen.getTool(ZoneTool.class);
				ZoneActor selected = selZone.getSelected();
				tool.setValue(selected);
				
				if (lastValue != null) {
					lastValue.highlight(false);
				}
				lastValue = selected;
				selected.highlight(true);
			}
		});
		
		// Initialie le bouton de sélection de l'outil
		setSelectToolScript(sceneLoader, "btnToolZone", ZoneTool.class);
	}

	private void loadToolOwner(SceneLoader sceneLoader) {
		// Initialise la liste des joueurs
		@SuppressWarnings("unchecked")
		final SelectBoxItem<PlayerData> selOwner = (SelectBoxItem<PlayerData>)sceneLoader.sceneActor.getItemById("selOwner");
		selOwner.setWidth(150);
		Array<PlayerData> items = new Array<PlayerData>();
		for (PlayerData player : screen.getPlayers()) {
			items.add(player);
		}
		selOwner.setItems(items);
		selOwner.setSelected(screen.getTool(OwnerTool.class).getValue());
		
		// Ajoute le listener pour màj la valeur applicable dans l'outil
		selOwner.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				OwnerTool tool = screen.getTool(OwnerTool.class);
				tool.setValue(selOwner.getSelected());
			}
		});
		
		// Initialie le bouton de sélection de l'outil
		setSelectToolScript(sceneLoader, "btnToolOwner", OwnerTool.class);
	}

	private void loadToolPower(SceneLoader sceneLoader) {
		// Initialise le champ de saisie de puissance
		final TextBoxItem txtPower = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtPower");
		txtPower.setText(screen.getTool(PowerTool.class).getValue().toString());
		
		// Ajoute le listener pour màj la valeur applicable dans l'outil
		txtPower.setTextFieldListener(new TextField.TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				final String text = txtPower.getText();
				if (text == null
				|| text.isEmpty()) {
					return;
				}
				int power = Integer.parseInt(text);
				PowerTool tool = screen.getTool(PowerTool.class);
				tool.setValue(power);
			}
		});
		
		// Initialie le bouton de sélection de l'outil
		setSelectToolScript(sceneLoader, "btnToolPower", PowerTool.class);
	}

	private void loadToolLetter(SceneLoader sceneLoader) {
		// Initialise la liste de lettres
		@SuppressWarnings("unchecked")
		final SelectBoxItem<Letters> selLetter = (SelectBoxItem<Letters>)sceneLoader.sceneActor.getItemById("selLetter");
		selLetter.setWidth(150);
		Array<Letters> letters = new Array<Letters>();
		for (Letters letter : Letters.values()) {
			// Seules les lettres avec une représentation de 0 sont affichées.
			// On n'affiche donc pas les lettres spéciales comme le JOKER, EMPTY ou FROM_TYPE.
			if (letter.representation > 0) {
				letters.add(letter);
			}
		}
		selLetter.setItems(letters);
		selLetter.setSelected(screen.getTool(LetterTool.class).getValue());
		
		// Ajoute le listener pour màj la valeur applicable dans l'outil
		selLetter.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LetterTool tool = screen.getTool(LetterTool.class);
				tool.setValue(selLetter.getSelected());
			}
		});
		
		// Initialie le bouton de sélection de l'outil
		setSelectToolScript(sceneLoader, "btnToolLetter", LetterTool.class);
	}

	private void loadToolType(SceneLoader sceneLoader) {
		// Initialise la liste de types
		@SuppressWarnings("unchecked")
		final SelectBoxItem<CellTypes> selType = (SelectBoxItem<CellTypes>) sceneLoader.sceneActor.getItemById("selType");
		selType.setWidth(150);
		selType.setItems(CellTypes.values());
		selType.setSelected(screen.getTool(CellTypeTool.class).getValue());
		
		// Ajoute le listener pour màj la valeur applicable dans l'outil
		selType.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CellTypeTool tool = screen.getTool(CellTypeTool.class);
				tool.setValue(selType.getSelected());
			}
		});

		// Initialie le bouton de sélection de l'outil
		setSelectToolScript(sceneLoader, "btnToolType", CellTypeTool.class);
	}

	@SuppressWarnings("rawtypes")
	public void selectTool(Class<? extends EditorTool> toolClass) {
		// Désélectionne les autre boutons
		for (Map.Entry<Class<? extends EditorTool>, SimpleButtonI18NScript> toolScript : toolsScripts.entrySet()) {
			if (toolScript.getKey() != toolClass) {
				toolScript.getValue().setToggle(false);
			}
		}
		
		// Sélectionne l'outil
		screen.setCurrentTool(toolClass);
	}
}
