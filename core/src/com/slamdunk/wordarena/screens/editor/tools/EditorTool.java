package com.slamdunk.wordarena.screens.editor.tools;

import com.slamdunk.wordarena.actors.CellActor;

import java.util.Collection;

public abstract class EditorTool<ValueType> {
	
	private ValueType value;
	
	/**
	 * Définit la valeur à appliquer
	 */
	public void setValue(ValueType value) {
		this.value = value;
	}
	
	/**
	 * Récupère la valeur à appliquer
	 */
	public ValueType getValue() {
		return value;
	}
	
	/**
	 * Applique l'outil sur la cellule indiquée
	 * @param cell
	 */
	public abstract void apply(CellActor cell);
	
	/**
	 * Applique l'outil sur les cellules indiquées
	 * @param cell
	 */
	public abstract void apply(Collection<CellActor> cells);
}
