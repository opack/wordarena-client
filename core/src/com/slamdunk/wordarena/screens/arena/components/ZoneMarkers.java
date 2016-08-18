package com.slamdunk.wordarena.screens.arena.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.actors.ZoneActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.cell.MarkerPack;
import com.slamdunk.wordarena.data.arena.zone.ZoneData;
import com.slamdunk.wordarena.screens.arena.MatchManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Didier on 02/09/2015.
 */
public class ZoneMarkers extends GroupEx {
    public static final String NAME = ZoneMarkers.class.getName();

    /**
     * Map de travail permettant de stocker le nombre de zones par joueur
     * lors du rafraîchissement des marqueurs de zone
     */
    private Map<String, Integer> tmpZonesByPlayer;

    private MatchManager matchManager;

    public ZoneMarkers(MatchManager matchManager) {
        setTouchable(Touchable.disabled);
        setName(NAME);

        this.matchManager = matchManager;
        tmpZonesByPlayer = new HashMap<String, Integer>();
    }
    /**
     * Crée les carrés indiquant la possession des joueurs en terme
     * de zones
     * @param zones
     */
    public void init(List<ZoneData> zones, GroupEx pauseZoneMarkers) {
        // Prépare le marker par défaut
        MarkerPack neutralPack = Assets.markerPacks.get(Assets.MARKER_PACK_NEUTRAL);
        TextureRegionDrawable neutralPossessionMarker = neutralPack.possessionMarker;

        clear();
        pauseZoneMarkers.clear();

        int totalWidth = 0;
        for (ZoneData zoneData : zones) {
            // La zone NONE n'est pas représentée car elle ne peut pas être possédée
            if (zoneData.id.equals(ZoneActor.NONE.getData().id)) {
                continue;
            }

            // Crée l'image
            Image marker = new Image(neutralPossessionMarker);
            marker.setPosition(totalWidth, 0);
            addActor(marker);

            marker = new Image(neutralPossessionMarker);
            marker.setPosition(totalWidth, 0);
            pauseZoneMarkers.addActor(marker);

            // On a ajouté un marqueur donc la taille totale a changé
            totalWidth += neutralPossessionMarker.getMinWidth();
        }
    }

    /**
     * Met à jour les carrés de possession de zone en fonction des
     * zones possédées par chaque joueur.
     * @param zones
     */
    public void update(List<ZoneData> zones, GroupEx pauseZoneMarkers) {
        // Compte le nombre de zones possédées par chaque joueur
        tmpZonesByPlayer.clear();
        String playerPack;
        for (ZoneData zoneData : zones) {
            // On ne traite pas les zones NONE (cellules hors zone)
            if (zoneData.id.equals(ZoneActor.NONE.getData().id)) {
                continue;
            }

            // Récupère le pack du possesseur de la zone
            playerPack = matchManager.getPlayer(zoneData.ownerPlace).markerPack;

            // Ajoute 1 zone à dessiner avec ce pack
            Integer nbZones = tmpZonesByPlayer.get(playerPack);
            if (nbZones == null) {
                tmpZonesByPlayer.put(playerPack, 1);
            } else {
                tmpZonesByPlayer.put(playerPack, nbZones + 1);
            }
        }

        // Pour chaque joueur, remplit autant de carrés que de zones possédées
        // On prend d'abord le premier joueur, puis le neutre, puis le second joueur,
        // de façon à avoir une jolie barre
        // TODO Pas très dynamique : ne fonctionne pas avec plus de 2 joueurs ou si le joueur 1 n'est pas en indice 1 par ex
        int lastMarkedSquare = fillZoneMarkers(0, matchManager.getPlayer(1).markerPack, pauseZoneMarkers);

        lastMarkedSquare = fillZoneMarkers(lastMarkedSquare, Assets.MARKER_PACK_NEUTRAL, pauseZoneMarkers);

        fillZoneMarkers(lastMarkedSquare, matchManager.getPlayer(2).markerPack, pauseZoneMarkers);
    }

    /**
     * Remplit les marqueurs de zone à partir du offset-ième
     * avec autant de positions que tmpZonesByPlayer l'indique
     * pour le markerPack spécifié.
     * @param offset 1er marqueur à remplir
     * @param markerPack
     * @return L'indice de la dernière zone remplie, pour fournir
     * cette valeur à offset lors du prochain appel
     */
    private int fillZoneMarkers(int offset, String markerPack, GroupEx pauseZoneMarkers) {
        // Récupère le nombre de zones à marquer
        Integer nbZonesOwned = tmpZonesByPlayer.get(markerPack);
        if (nbZonesOwned == null) {
            return offset;
        }

        // Récupère l'image du marqueur de possession à dessiner
        MarkerPack pack = Assets.markerPacks.get(markerPack);
        TextureRegionDrawable possessionMarker = pack.possessionMarker;

        // Met à jour les images et aussi celles de la StatsTable
        SnapshotArray<Actor> mainScreenMarkers = getChildren();
        SnapshotArray<Actor> pauseScreenMarkers = pauseZoneMarkers.getChildren();
        Image marker;
        int markerIndex;
        for (int curZone = 0; curZone < nbZonesOwned; curZone++) {
            markerIndex = offset + curZone;

            // Met à jour l'image des marqueurs affichés sur l'arène
            marker = (Image)mainScreenMarkers.get(markerIndex);
            marker.setDrawable(possessionMarker);

            // Met à jour l' image de la stats table
            marker = (Image)pauseScreenMarkers.get(markerIndex);
            marker.setDrawable(possessionMarker);
        }
        return offset + nbZonesOwned;
    }
}
