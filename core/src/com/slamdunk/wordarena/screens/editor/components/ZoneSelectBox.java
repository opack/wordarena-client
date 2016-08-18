package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.editor.tools.ZoneTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Liste déroulante proposant le choix d'une lettre
 */
public class ZoneSelectBox extends SelectBox<ZoneActor> {
    public static final String NAME = ZoneSelectBox.class.getName();

    private Array<ZoneActor> zones;

    public ZoneSelectBox(Skin skin, final SlamScene scene) {
        super(skin);
        setName(NAME);

        // Crée la liste des zones
        zones = new Array<ZoneActor>();
        zones.add(ZoneActor.NONE);
        setItems(zones);
        //DBG setSelected(screen.getTool(ZoneTool.class).getValue());
        setSelected(null);

        // Ajoute le listener pour màj la valeur applicable dans l'outil
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final EditorScreen screen = (EditorScreen)scene.getScreen();
                ZoneTool tool = screen.getTool(ZoneTool.class);
                tool.setValue(getSelected());
            }
        });
    }

    public void loadZonesFromArena(ArenaOverlay arena) {
        // Trie les zones existantes
        List<String> sortedZones = new ArrayList<String>();
        for (ZoneData zoneData : arena.getData().zones) {
            sortedZones.add(zoneData.id);
        }
        Collections.sort(sortedZones);

        // Ajoute les zones à la liste
        zones.clear();
        boolean hasZoneNone = false;
        for (String zoneId : sortedZones) {
            zones.add(arena.getZone(zoneId));

            if (zoneId.equals(ZoneActor.NONE.getData().id)) {
                hasZoneNone = true;
            }
        }
        if (!hasZoneNone) {
            zones.insert(0, ZoneActor.NONE);
        }
        setItems(zones);
        // Suppression barbare de la sélection avec le clear puis sélection du premier item :
        // cette technique vise à déclencher le changeListener pour que l'outil lié reçoive
        // bien la valeur actuellement visible comme étant sélectionnée aux yeux de l'utilisateur
        getSelection().clear();
        setSelectedIndex(0);
    }

    public void add(ZoneActor zone) {
        zones.add(zone);
        setItems(zones);
    }
}
