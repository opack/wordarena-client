package com.slamdunk.wordarena.screens.editor;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.game.PlayerData;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.arena.components.ArenaNameLabel;
import com.slamdunk.wordarena.screens.arena.components.HomeButton;
import com.slamdunk.wordarena.screens.editor.components.CellTypeToolButton;
import com.slamdunk.wordarena.screens.editor.components.CreateZoneButton;
import com.slamdunk.wordarena.screens.editor.components.LetterSelectBox;
import com.slamdunk.wordarena.screens.editor.components.OwnerToolButton;
import com.slamdunk.wordarena.screens.editor.components.PowerToolButton;
import com.slamdunk.wordarena.screens.editor.components.SaveButton;
import com.slamdunk.wordarena.screens.editor.components.SelectCellTypeToolButton;
import com.slamdunk.wordarena.screens.editor.components.SelectLetterToolButton;
import com.slamdunk.wordarena.screens.editor.components.SelectOwnerToolButton;
import com.slamdunk.wordarena.screens.editor.components.SelectPowerToolButton;
import com.slamdunk.wordarena.screens.editor.components.SelectWallToolButton;
import com.slamdunk.wordarena.screens.editor.components.SelectZoneToolButton;
import com.slamdunk.wordarena.screens.editor.components.ZoneNameTextField;
import com.slamdunk.wordarena.screens.editor.components.ZoneSelectBox;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;
import com.slamdunk.wordarena.screens.editor.tools.LetterTool;
import com.slamdunk.wordarena.screens.editor.tools.OwnerTool;
import com.slamdunk.wordarena.screens.editor.tools.PowerTool;
import com.slamdunk.wordarena.screens.editor.tools.WallTool;
import com.slamdunk.wordarena.screens.editor.tools.ZoneTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Didier on 05/09/2015.
 */
public class EditorScene extends SlamScene {
    public static final String NAME = EditorScene.class.getName();

    private List<Group> toolGroups;

    public EditorScene() {
        setName(NAME);
    }

    @Override
    public void create(Skin skin) {
        createHomeButton(Assets.uiSkinSpecific);
        createArenaNameLabel(skin);
        createSaveButton(Assets.uiSkinSpecific);

        createToolBar(Assets.uiSkinSpecific);

        toolGroups = new ArrayList<Group>();
        createCellTypeToolGroup(Assets.uiSkinSpecific);
        createLetterToolGroup(skin);
        createPowerToolGroup(Assets.uiSkinSpecific);
        createOwnerToolGroup(Assets.uiSkinSpecific);
        createZoneToolGroup(skin);
        createWallToolGroup(skin);

        // Au début, on cache tous les outils
        showToolGroup("");
    }

    private void createHomeButton(Skin skin) {
        Button btn = new HomeButton(skin, this);
        btn.setSize(75, 50);
        btn.setPosition(5, 745);
        addActor(btn);
    }

    private void createArenaNameLabel(Skin skin) {
        Label lbl = new ArenaNameLabel(skin);
        lbl.setSize(480, 50);
        lbl.setPosition(0, 755);
        addActor(lbl);
    }

    private void createSaveButton(Skin skin) {
        Button btn = new SaveButton(skin, this);
        btn.setSize(75, 50);
        btn.setPosition(400, 745);
        addActor(btn);
    }

    private void createToolBar(Skin skin) {
        TextButton cellType = new SelectCellTypeToolButton(skin, this);
        cellType.setSize(75, 44);
        cellType.setPosition(15, 696);
        addActor(cellType);

        TextButton letter = new SelectLetterToolButton(skin, this);
        letter.setSize(75, 44);
        letter.setPosition(90, 696);
        addActor(letter);

        TextButton power = new SelectPowerToolButton(skin, this);
        power.setSize(75, 44);
        power.setPosition(165, 696);
        addActor(power);

        TextButton owner = new SelectOwnerToolButton(skin, this);
        owner.setSize(75, 44);
        owner.setPosition(240, 696);
        addActor(owner);

        TextButton zone = new SelectZoneToolButton(skin, this);
        zone.setSize(75, 44);
        zone.setPosition(315, 696);
        addActor(zone);

        TextButton wall = new SelectWallToolButton(skin, this);
        wall.setSize(75, 44);
        wall.setPosition(390, 696);
        addActor(wall);

        ButtonGroup<TextButton> toolBar = new ButtonGroup<TextButton>();
        toolBar.add(cellType, letter, power, owner, zone, wall);
        toolBar.uncheckAll();
    }

    private void createCellTypeToolGroup(Skin skin) {
        // Création du label d'invite
        Label prompt = new Label(Assets.i18nBundle.get("ui.editor.type.prompt"), skin);
        prompt.setPosition(12, 674);

        // Création des boutons pour les différents types de cellule
        TextButton voidType = new CellTypeToolButton(skin, Assets.i18nBundle.get("cellTypes.V"), CellTypes.V, this);
        voidType.setSize(50, 50);
        voidType.setPosition(19, 600);

        TextButton letterType = new CellTypeToolButton(skin, Assets.i18nBundle.get("cellTypes.L"), CellTypes.L, this);
        letterType.setSize(50, 50);
        letterType.setPosition(97, 600);

        TextButton jokerType = new CellTypeToolButton(skin, Assets.i18nBundle.get("cellTypes.J"), CellTypes.J, this);
        jokerType.setSize(50, 50);
        jokerType.setPosition(176, 600);

        TextButton specialType = new CellTypeToolButton(skin, Assets.i18nBundle.get("cellTypes.S"), CellTypes.S, this);
        specialType.setSize(50, 50);
        specialType.setPosition(254, 600);

        TextButton glassType = new CellTypeToolButton(skin, Assets.i18nBundle.get("cellTypes.G"), CellTypes.G, this);
        glassType.setSize(50, 50);
        glassType.setPosition(333, 600);

        TextButton bombType = new CellTypeToolButton(skin, Assets.i18nBundle.get("cellTypes.B"), CellTypes.B, this);
        bombType.setSize(50, 50);
        bombType.setPosition(411, 600);

        // Group chargé de faire en sorte qu'un seul type de cellule est sélectionné à la fois
        ButtonGroup<TextButton> cellTypesGroup = new ButtonGroup<TextButton>();
        cellTypesGroup.add(voidType, letterType, jokerType, specialType, glassType, bombType);
        cellTypesGroup.uncheckAll();

        // Group contenant les composants utilisés pour le choix de type de cellule
        Group toolGroup = new Group();
        toolGroup.setName(CellTypeTool.NAME);
        toolGroup.addActor(prompt);
        toolGroup.addActor(voidType);
        toolGroup.addActor(letterType);
        toolGroup.addActor(jokerType);
        toolGroup.addActor(specialType);
        toolGroup.addActor(glassType);
        toolGroup.addActor(bombType);

        addActor(toolGroup);
        toolGroups.add(toolGroup);
    }

    private void createLetterToolGroup(Skin skin) {
        // Création du label d'invite
        Label prompt = new Label(Assets.i18nBundle.get("ui.editor.letter.prompt"), skin);
        prompt.setPosition(12, 674);

        // Création de la liste de sélection
        SelectBox<Letters> letters = new LetterSelectBox(skin, this);
        letters.setWidth(150);
        letters.setPosition(12, 625);

        // Group contenant les composants utilisés pour le choix de type de cellule
        Group toolGroup = new Group();
        toolGroup.setName(LetterTool.NAME);
        toolGroup.addActor(prompt);
        toolGroup.addActor(letters);

        addActor(toolGroup);
        toolGroups.add(toolGroup);
    }

    private void createPowerToolGroup(Skin skin) {
        // Création du label d'invite
        Label prompt = new Label(Assets.i18nBundle.get("ui.editor.power.prompt"), skin);
        prompt.setPosition(12, 674);

        // Création des boutons pour les différents types de cellule
        TextButton power0 = new PowerToolButton(skin, 0, this);
        power0.setSize(50, 50);
        power0.setPosition(20, 611);
        TextButton power1 = new PowerToolButton(skin, 1, this);
        power1.setSize(50, 50);
        power1.setPosition(89, 611);
        TextButton power2 = new PowerToolButton(skin, 2, this);
        power2.setSize(50, 50);
        power2.setPosition(158, 611);

        // Group chargé de faire en sorte qu'un seul type de cellule est sélectionné à la fois
        ButtonGroup<TextButton> powersGroup = new ButtonGroup<TextButton>();
        powersGroup.add(power0, power1, power2);
        powersGroup.uncheckAll();

        // Group contenant les composants utilisés pour le choix de type de cellule
        Group toolGroup = new Group();
        toolGroup.setName(PowerTool.NAME);
        toolGroup.addActor(prompt);
        toolGroup.addActor(power0);
        toolGroup.addActor(power1);
        toolGroup.addActor(power2);

        addActor(toolGroup);
        toolGroups.add(toolGroup);
    }

    private void createOwnerToolGroup(Skin skin) {
        // Création du label d'invite
        Label prompt = new Label(Assets.i18nBundle.get("ui.editor.owner.prompt"), skin);
        prompt.setPosition(12, 674);

        // Création des boutons pour les différents joueurs
        HorizontalGroup layout = new HorizontalGroup(); // Group chargé d'afficher les boutons en ligne
        ButtonGroup<TextButton> toggleGroup = new ButtonGroup<TextButton>(); // Group chargé de faire en sorte qu'un seul type de cellule est sélectionné à la fois
        List<PlayerData> players = ((EditorScreen)getScreen()).getPlayers();
        for (PlayerData player : players) {
            TextButton button = new OwnerToolButton(skin, player, this);
            button.setSize(50, 50);

            layout.addActor(button);
            toggleGroup.add(button);
        }

        layout.setPosition(0, 611);
        layout.layout();
        toggleGroup.uncheckAll();

        // Group contenant les composants utilisés pour le choix du possesseur
        Group toolGroup = new Group();
        toolGroup.setName(OwnerTool.NAME);
        toolGroup.addActor(prompt);
        toolGroup.addActor(layout);

        addActor(toolGroup);
        toolGroups.add(toolGroup);
    }

    private void createZoneToolGroup(Skin skin) {
        // Création du label d'invite
        Label promptSelectExisting = new Label(Assets.i18nBundle.get("ui.editor.zone.prompt"), skin);
        promptSelectExisting.setPosition(12, 674);

        // Création de la liste de sélection
        SelectBox<ZoneActor> zones = new ZoneSelectBox(skin, this);
        zones.setWidth(150);
        zones.setPosition(12, 625);

        // Création du label d'invite à la création d'une nouvelle zone
        Label promptCreateNew = new Label(Assets.i18nBundle.get("ui.editor.zone.create.prompt"), skin);
        promptCreateNew.setPosition(12, 594);

        // Création du champ de texte permettant la saisie du nom
        TextField zoneName = new ZoneNameTextField(skin);
        zoneName.setSize(150, 50);
        zoneName.setPosition(12, 535);

        // Création du bouton qui crée effectivement la zone
        TextButton createButton = new CreateZoneButton(skin, this);
        createButton.setSize(75, 50);
        createButton.setPosition(176, 535);

        // Group contenant les composants utilisés pour le choix de type de cellule
        Group toolGroup = new Group();
        toolGroup.setName(ZoneTool.NAME);
        toolGroup.addActor(promptSelectExisting);
        toolGroup.addActor(zones);
        toolGroup.addActor(promptCreateNew);
        toolGroup.addActor(zoneName);
        toolGroup.addActor(createButton);

        addActor(toolGroup);
        toolGroups.add(toolGroup);
    }

    private void createWallToolGroup(Skin skin) {
        // Création du label d'invite
        Label prompt = new Label(Assets.i18nBundle.get("ui.editor.wall.prompt"), skin);
        prompt.setPosition(12, 674);
        // Définit la taille du label pour que le wrapping se fasse correctement. On sait que pour
        // ce label le texte peut être long, d'où l'utilisation du wrapping.
        prompt.setWidth(468);
        prompt.setWrap(true);

        // Group contenant les composants utilisés pour le choix de type de cellule
        Group toolGroup = new Group();
        toolGroup.setName(WallTool.NAME);
        toolGroup.addActor(prompt);

        addActor(toolGroup);
        toolGroups.add(toolGroup);
    }

    public void showToolGroup(String toolName) {
        if (toolName == null) {
            return;
        }
        for (Group toolGroup : toolGroups) {
            toolGroup.setVisible(toolName.equals(toolGroup.getName()));
        }
    }
}
