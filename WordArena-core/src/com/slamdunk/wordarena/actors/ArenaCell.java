package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.toolkit.world.SlamActor;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;

/**
 * Une cellule de l'arène. Une cellule contient bien sûr une lettre
 * mais aussi 4 bords qui indique à quelle zone appartient la cellule.
 */
public class ArenaCell extends GroupEx {
	private final static int WIDTH = 48;
	private final static int HEIGHT = 48;
	
	/**
	 * Le modèle de cette cellule
	 */
	private final CellData data;
	
	private SlamActor ownerActor;
	
	private Label letter;
	
	private Image cellTypeImage;
	
	public ArenaCell(final Skin skin) {
		// Crée les composants de la cellule
		data = new CellData();
		
		// Crée les acteurs dans l'ordre de superposition
		ownerActor = new SlamActor(WIDTH, HEIGHT, false);
		ownerActor.createDrawers(true, true, true);
		ownerActor.setTouchable(Touchable.disabled);
		ownerActor.getTextureDrawer().setTextureRegion(Assets.getCellOwnerImage(data));
		ownerActor.getTextureDrawer().setActive(true);
		addActor(ownerActor);
		
		cellTypeImage = new Image(Assets.getCellTypeImage(data));
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
	
	public SlamActor getOwnerActorDBG() {
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
