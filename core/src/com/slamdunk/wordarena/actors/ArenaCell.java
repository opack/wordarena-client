package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.toolkit.world.SlamActor;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Une cellule de l'arène. Une cellule contient bien sûr une lettre
 * mais aussi 4 bords qui indique à quelle zone appartient la cellule.
 */
public class ArenaCell extends GroupEx {
	private final static int WIDTH = 48;
	private final static int HEIGHT = 48;
	
	private final float MOMENTARY_ANIM_INTERVAL_MIN = 15.0f;
	private final float MOMENTARY_ANIM_INTERVAL_MAX = 45.0f;
	
	/**
	 * Le modèle de cette cellule
	 */
	private final CellData data;
	
	private SlamActor ownerActor;
	
	private Label letter;
	
	private Image cellTypeImage;
	
	private boolean momentaryTimerActive;
	private float momentaryTimer;
	
	public ArenaCell(final Skin skin) {
		// Crée les composants de la cellule
		data = new CellData();
		
		// Crée les acteurs dans l'ordre de superposition
		ownerActor = new SlamActor(WIDTH, HEIGHT, false);
		ownerActor.createDrawers(true, true, false);
		ownerActor.setTouchable(Touchable.disabled);
		ownerActor.getTextureDrawer().setActive(true);
		ownerActor.getAnimationDrawer().setActive(false);
		addActor(ownerActor);
		
		cellTypeImage = new Image();
		cellTypeImage.setTouchable(Touchable.disabled);
		cellTypeImage.setBounds(0, 0, ownerActor.getWidth(), ownerActor.getHeight());
		addActor(cellTypeImage);
		
		letter = new Label("", skin);
		letter.setAlignment(Align.center, Align.center);
		letter.setTouchable(Touchable.disabled);
		letter.setWidth(ownerActor.getWidth());
		letter.setPosition(ownerActor.getWidth() / 2, ownerActor.getHeight() / 2, Align.center);
		addActor(letter);
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
		
		if (!momentaryTimerActive) {
			return;
		}

		// Mise à jour du timer
		momentaryTimer -= delta;
		
		// Si le temps est venu de jouer l'animation momentanée, on la joue
		AnimationDrawer animDrawer = ownerActor.getAnimationDrawer();
		if (momentaryTimer <= 0) {
			if (!animDrawer.isActive()) {
				animDrawer.setStateTime(0);
				animDrawer.setActive(true);
			} else if (ownerActor.getAnimationDrawer().isAnimationFinished()) {
				animDrawer.setActive(false);
				startMomentaryAnimTimer();
			}
		}
	}
	
	public SlamActor getOwnerActor() {
		return ownerActor;
	}
	
	public CellData getData() {
		return data;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ArenaCell) {
			// 2 cellules sont considérées identiques si elles sont
			// au même endroit
			ArenaCell cell2 = (ArenaCell)other;
			return cell2.data.position.equals(data.position);
		}
		return false;
	}
	
	/**
	 * Affiche ou masque la lettre de la cellule
	 * @param show
	 */
	public void showLetter(boolean show) {
		letter.setVisible(show);
	}
	
	@Override
	public int hashCode() {
		return data.position.hashCode();
	}
	
	public void updateDisplay() {
		ownerActor.getTextureDrawer().setTextureRegion(Assets.getCellOwnerImage(data));
		
		ownerActor.getAnimationDrawer().setAnimation(Assets.getCellOwnerMomentaryAnim(data), false, false);
		momentaryTimerActive = data.state == CellStates.OWNED && data.selected == false;
		if (momentaryTimerActive && momentaryTimer <= 0) {
			// Lance le timer pour jouer l'animation de cellule possédée dans un certain temps
			startMomentaryAnimTimer();
		}
		
		letter.setText(data.letter.label);
		letter.setStyle(Assets.skin.get("power-" + data.power, LabelStyle.class));
		
		cellTypeImage.setDrawable(Assets.getCellTypeImage(data));
	}

	/**
	 * Sélectionne la cellule, ce qui a pour effet de changer son état
	 * (qui passe à SELECTED) et l'image de la lettre
	 */
	public void select() {
		data.selected = true;
		updateDisplay();
	}
	
	/**
	 * Désélectionne la cellule, ce qui a pour effet de changer son état
	 * (qui passe à NORMAL) et l'image de la lettre
	 */
	public void unselect() {
		data.selected = false;
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
	
	@Override
	public String toString() {
		return data.position.toString();
	}
}
