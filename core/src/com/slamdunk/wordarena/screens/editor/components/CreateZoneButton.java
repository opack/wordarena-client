package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

/**
 * Bouton gérant le choix d'un type de cellue
 */
public class CreateZoneButton extends TextButton {
    public static final String NAME = CreateZoneButton.class.getName();

    public CreateZoneButton(Skin skin, final SlamScene scene) {
        super(Assets.i18nBundle.get("ui.editor.zone.create.button"), skin, "toggle");
        setName(NAME);

        //
        addListener(new ButtonClickListener() {
            @Override
            public void clicked(Button button) {
                //				Assets.playSound(Assets.clickSound);
                EditorScreen screen = (EditorScreen)scene.getScreen();
                TextField txtZone = scene.findActor(ZoneNameTextField.NAME);
                ZoneSelectBox selZone = scene.findActor(ZoneSelectBox.NAME);

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
                    selZone.add(newZone);
                }
                txtZone.setText("");

                // Sélectionne la nouvelle zone
                selZone.setSelected(newZone);
            }
        });
    }
}
