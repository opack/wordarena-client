package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.BordersAndCorners;

public class ActorHelper {
	/**
	 * L'alignement doit être fait de façon à ce que l'Actor soit
	 * placé à cheval sur la bordure.
	 */
	public static void alignOnBorder(Borders border, Vector2 cornerPosition, Actor actor) {
		switch (border) {
		case LEFT:
		case RIGHT:
			actor.setPosition(cornerPosition.x - actor.getWidth() / 2, cornerPosition.y);
			break;
		case BOTTOM:
		case TOP:
			actor.setPosition(cornerPosition.x, cornerPosition.y - actor.getHeight() / 2);
			break;
		}
	}
	
	/**
	 * L'alignement doit être fait de façon à ce que l'Actor soit
	 * placé le long de la bordure, à l'intérieur du rectangle virtuel
	 * dont le coin d'ancrage est spécifié
	 */
	public static void alignInside(BordersAndCorners border, Vector2 cornerPosition, float offset, Actor actor) {
		switch (border) {
		// Alignements par rapport à un bord : on modifie 1 seule coordonnée pour s'aligner sur ce bord
		case BOTTOM:
			actor.setPosition(cornerPosition.x, cornerPosition.y + offset);
			break;
		case LEFT:
			actor.setPosition(cornerPosition.x + offset, cornerPosition.y);
			break;
		case RIGHT:
			actor.setPosition(cornerPosition.x - actor.getWidth() - offset, cornerPosition.y);
			break;
		case TOP:
			actor.setPosition(cornerPosition.x, cornerPosition.y - actor.getHeight() - offset);
			break;
		// Alignement par rapport à un coin : on modifie les 2 coordonnées pour s'aligner sur ce coin
		case BOTTOM_LEFT:
			actor.setPosition(cornerPosition.x + offset, cornerPosition.y + offset);
			break;
		case BOTTOM_RIGHT:
			actor.setPosition(cornerPosition.x - actor.getWidth() - offset, cornerPosition.y + offset);
			break;
		case TOP_LEFT:
			actor.setPosition(cornerPosition.x + offset, cornerPosition.y - actor.getHeight() - offset);
			break;
		case TOP_RIGHT:
			actor.setPosition(cornerPosition.x - actor.getWidth() - offset, cornerPosition.y - actor.getHeight() - offset);
			break;
		default:
			break;
		}
	}
	
	/**
	 * L'alignement doit être fait de façon à ce que l'Actor soit
	 * placé le long de la bordure, à l'extérieur  du rectangle virtuel
	 * dont le coin d'ancrage est spécifié
	 */
	public static void alignOutside(Borders border, Vector2 cornerPosition, float offset, Actor actor) {
		switch (border) {
		case BOTTOM:
			actor.setPosition(cornerPosition.x, cornerPosition.y - actor.getHeight() - offset);
			break;
		case LEFT:
			actor.setPosition(cornerPosition.x - actor.getWidth() - offset, cornerPosition.y);
			break;
		case RIGHT:
			actor.setPosition(cornerPosition.x + offset, cornerPosition.y);
			break;
		case TOP:
			actor.setPosition(cornerPosition.x, cornerPosition.y + offset);
			break;
		}
	}
}
