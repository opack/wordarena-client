package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.slamdunk.toolkit.graphics.drawers.AnimationDrawer;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;

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
	
	private AnimationDrawer animationDrawer;
	
	private Label letter;
	
	private Image cellTypeImage;
	
	private boolean momentaryTimerActive;
	private float momentaryTimer;
	
	public ArenaCell(final Skin skin) {
		// Définit la taille de la cellule
		setSize(WIDTH, HEIGHT);
		
		// Crée les composants de la cellule
		data = new CellData();
		
		// Crée les acteurs dans l'ordre de superposition
		animationDrawer = new AnimationDrawer();
		animationDrawer.setPaused(true);
		animationDrawer.setActive(true);
		
		cellTypeImage = new Image();
		cellTypeImage.setTouchable(Touchable.disabled);
		cellTypeImage.setBounds(0, 0, WIDTH, HEIGHT);
		addActor(cellTypeImage);
		
		letter = new Label("", skin);
		letter.setAlignment(Align.center, Align.center);
		letter.setTouchable(Touchable.disabled);
		letter.setWidth(WIDTH);
		letter.setPosition(WIDTH / 2, HEIGHT / 2, Align.center);
		addActor(letter);
	}
	
	public AnimationDrawer getAnimationDrawer() {
		return animationDrawer;
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
		animationDrawer.updateTime(delta);
		
		if (!momentaryTimerActive) {
			return;
		}

		// Mise à jour du timer
		momentaryTimer -= delta;
		
		// Si le temps est venu de jouer l'animation, on la joue
		if (momentaryTimer <= 0) {
			if (!animationDrawer.isActive()) {
				animationDrawer.setStateTime(0);
				animationDrawer.setPaused(false);
			} else if (animationDrawer.isAnimationFinished()) {
				animationDrawer.setPaused(true);
				startMomentaryAnimTimer();
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Dessine l'animation
		animationDrawer.draw(this, batch);
		
		// Le super est placé après l'animation pour que la lettre
		// et le dessin de type de cellule soient dessinés au-dessus
		super.draw(batch, parentAlpha);
	}
	
	public void updateDisplay() {
		// Met à jour l'animation à jour en fonction de l'état de la cellule
		animationDrawer.setAnimation(Assets.getCellAnim(data), true, false);
		animationDrawer.setStateTime(0);
		
		// Si la cellule n'est pas sélectionnée, on démarre le timer qui va déclencher
		// l'animation momentanée de la cellule
		momentaryTimerActive = data.selected == false;
		if (momentaryTimerActive && momentaryTimer <= 0) {
			// Lance le timer pour jouer l'animation de cellule possédée dans un certain temps
			startMomentaryAnimTimer();
		}
		
		// Met à jour la lettre
		letter.setText(data.letter.label);
		letter.setStyle(Assets.skin.get("power-" + data.power, LabelStyle.class));
		
		// Met à jour l'image représentant le type de cellule
		cellTypeImage.setDrawable(Assets.getCellTypeImage(data));
	}

	/**
	 * Affiche ou masque la lettre de la cellule
	 * @param show
	 */
	public void showLetter(boolean show) {
		letter.setVisible(show);
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
