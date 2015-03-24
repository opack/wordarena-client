package com.slamdunk.wordarena;

import java.util.List;

import com.slamdunk.toolkit.lang.KeyListMap;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.celleffects.BombExplosionEffect;
import com.slamdunk.wordarena.actors.celleffects.BreakNeighborGlassEffect;
import com.slamdunk.wordarena.actors.celleffects.CellEffect;
import com.slamdunk.wordarena.actors.celleffects.TakeOwnershipEffect;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellTypes;

/**
 * Gère les effets à appliquer sur les cellules lors de la validation
 * d'un mot
 */
public class CellEffectsManager {
	private static CellEffectsManager instance;
	
	private KeyListMap<CellTypes, CellEffect> effects;
	
	private CellEffectsManager() {
		effects = new KeyListMap<CellTypes, CellEffect>();
		
		// On casse le verre des cellules voisines lorsqu'un cellule Lettre, Bombe ou Joker est validée
		registerCellEffect(new BreakNeighborGlassEffect(), CellTypes.L, CellTypes.B, CellTypes.J);
		
		// On défait la possession des cellules voisines ennemies lorsqu'une cellule Bombe est validée
		registerCellEffect(new BombExplosionEffect(), CellTypes.B);
		
		// On possède les cellules validées
		registerCellEffect(new TakeOwnershipEffect(), CellTypes.L, CellTypes.B, CellTypes.J, CellTypes.S);
	}
	
	private void registerCellEffect(CellEffect effect, CellTypes... types) {
		for (CellTypes type : types) {
			effects.putValue(type, effect);
		}
	}

	public static CellEffectsManager getInstance() {
		if (instance == null) {
			instance = new CellEffectsManager();
		}
		return instance;
	}
	
	/**
	 * Déclenche les effets sur les cellules sélectionnées
	 * @param player 
	 * @param selectedCells
	 */
	public void triggerCellEffects(Player player, List<ArenaCell> selectedCells, ArenaData arena) {
		List<CellEffect> effectList;
		for (ArenaCell cell : selectedCells) {
			effectList = effects.get(cell.getData().type);
			if (effectList != null) {
				for (CellEffect effect : effectList) {
					// Applique l'effet lié au type de la cellule
					effect.applyEffect(player, cell, arena);
//DBG					
//					// Petite temporisation pour que tous les effets n'arrivent pas en même temps
//					try {
//						Thread.sleep(125);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
		}
	}
}
