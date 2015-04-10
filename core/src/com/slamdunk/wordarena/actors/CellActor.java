package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.graphics.BatchUtils;
import com.slamdunk.toolkit.graphics.BatchUtils.TextAlignment;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.toolkit.graphics.drawers.TextureDrawer;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.arena.cell.CellData;
import com.slamdunk.wordarena.data.game.Player;

/**
 * Une cellule de l'arène. Une cellule contient bien sûr une lettre
 * mais aussi 4 bords qui indique à quelle zone appartient la cellule.
 */
public class CellActor extends Actor {
	private final static int WIDTH = 48;
	private final static int HEIGHT = 48;
	
	private final float MOMENTARY_ANIM_INTERVAL_MIN = 15.0f;
	private final float MOMENTARY_ANIM_INTERVAL_MAX = 45.0f;
	
	/**
	 * Le modèle de cette cellule
	 */
	private final CellData data;
	
	/**
	 * La zone à laquelle appartient cette cellule
	 */
	private ZoneActor zone;
	
	private AnimationDrawer ownerDrawer;
	
	private LabelStyle letterStyle;
	private Rectangle bounds;
	
	private TextureDrawer cellTypeDrawer;
	
	private boolean momentaryTimerActive;
	private float momentaryTimer;
	
	public CellActor(final Skin skin) {
		this(skin, null);
	}
	
	public CellActor(final Skin skin, CellData data) {
		// Définit la taille de la cellule
		setSize(WIDTH, HEIGHT);
		
		// Crée les composants de la cellule
		this.data = data;
		
		// Drawer pour animer le fond en fonction de l'owner
		ownerDrawer = new AnimationDrawer();
		ownerDrawer.setPaused(true);
		ownerDrawer.setActive(true);

		// Drawer pour dessiner l'image en fonction du type de cellule
		cellTypeDrawer = new TextureDrawer();
		cellTypeDrawer.setActive(true);
		
		// Style et rectangle utilisé pour le dessin de la lettre
		if (data != null) {
			letterStyle = Assets.uiSkin.get("power-" + data.power, LabelStyle.class);
		} else {
			letterStyle = Assets.uiSkin.get("power-0", LabelStyle.class);
		}
		bounds = new Rectangle(getX(), getY(), WIDTH, HEIGHT);
	}
	
	public AnimationDrawer getAnimationDrawer() {
		return ownerDrawer;
	}
	
	public CellData getData() {
		return data;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof CellActor) {
			// 2 cellules sont considérées identiques si elles sont
			// au même endroit
			CellActor cell2 = (CellActor)other;
			return cell2.data.position.equals(data.position);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return data.position.hashCode();
	}

	/**
	 * Choisit un délai aléatoire avant la prochaine exécution de l'animation
	 */
	private void startMomentaryAnimTimer() {
		momentaryTimer = MathUtils.random(MOMENTARY_ANIM_INTERVAL_MIN, MOMENTARY_ANIM_INTERVAL_MAX);
		momentaryTimerActive = true;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		// Met à jour l'animation
		ownerDrawer.updateTime(delta);
		
		if (!momentaryTimerActive) {
			return;
		}

		// Mise à jour du timer
		momentaryTimer -= delta;
		
		// Si le temps est venu de jouer l'animation, on la joue
		if (momentaryTimer <= 0) {
			if (ownerDrawer.isPaused()) {
				ownerDrawer.setStateTime(0);
				ownerDrawer.setPaused(false);
			} else if (ownerDrawer.isAnimationFinished()) {
				ownerDrawer.setPaused(true);
				startMomentaryAnimTimer();
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Dessine l'animation de l'owner
		if (data.type.canBeOwned()) {
			ownerDrawer.draw(this, batch);
		}
		
		// Dessine le type de cellule
		cellTypeDrawer.draw(this, batch);
		
		// Dessine la lettre
		if (letterStyle != null) {
			letterStyle.font.setColor(letterStyle.fontColor);
			BatchUtils.drawString(batch, letterStyle.font, data.letter.label, TextAlignment.MIDDLE_CENTER, bounds);
		}
	}
	
	public void updateDisplay() {
		momentaryTimerActive = false;
		if (data.type.canBeOwned()) {
			// Met à jour l'animation à jour en fonction de l'état de la cellule
			ownerDrawer.setAnimation(Assets.markerPacks.get(data.owner.markerPack).getCellAnim(data), true, false);
			ownerDrawer.setStateTime(0);
			
			// Si la cellule n'est pas sélectionnée, on démarre le timer qui va déclencher
			// l'animation momentanée de la cellule
			momentaryTimerActive = data.selected == false;
			if (momentaryTimerActive && momentaryTimer <= 0) {
				// Lance le timer pour jouer l'animation de cellule possédée dans un certain temps
				startMomentaryAnimTimer();
			}
		}
		
		// Met à jour la lettre
		letterStyle = Assets.uiSkin.get("power-" + data.power, LabelStyle.class);
		bounds.set(getX(), getY(), getWidth(), getHeight());
		
		// Met à jour l'image représentant le type de cellule
		cellTypeDrawer.setTextureRegion(Assets.arenaSkin.getCellTypeRegion(data));
	}

	/**
	 * Affiche ou masque la lettre de la cellule
	 * @param show
	 */
	public void showLetter(boolean show) {
		if (show) {
			letterStyle = Assets.uiSkin.get("power-" + data.power, LabelStyle.class);
		} else {
			letterStyle = null;
		}
	}

	/**
	 * Sélectionne ou désélectionne la cellule, ce qui a pour effet de changer 
	 * son état, l'image de la lettre et la possession de la zone.
	 * En effet, la zone va simuler l'appartenance de cette cellule au joueur
	 * courant car elle est sélectionnée, afin de montrer au joueur ce qui
	 * arrivera s'il valide le mot courant.
	 */
	public void select(boolean selected) {
		data.selected = selected;
		
		zone.updateOwner();
		
		updateDisplay();
	}

	/**
	 * Change le propriétaire de la cellule et met à jour l'image
	 * @param owner
	 */
	public void setOwner(Player owner) {
		data.owner = owner;
		updateDisplay();		
	}
	
	public ZoneActor getZone() {
		return zone;
	}

	public void setZone(ZoneActor zone) {
		this.zone = zone;
		data.zone = zone.getData().id;
	}

	@Override
	public String toString() {
		return data.position.toString();
	}
}
