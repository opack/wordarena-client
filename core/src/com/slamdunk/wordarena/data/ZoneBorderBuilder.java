package com.slamdunk.wordarena.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ZoneEdge;
import com.slamdunk.wordarena.assets.Assets;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.BordersAndCorners;
import com.slamdunk.wordarena.enums.CornerTypes;

/**
 * Fait 3 passes pour trouver les bords de la zone :
 * 	1. Pour chaque cellule, détermine si un côté est un bord de zone.
 * 	2. Pour chaque cellule, détermine le type de coin en bord de zone.
 *  3. Pour chaque cellule, crée les bords de zone.  
 * @author Didier
 *
 */
public class ZoneBorderBuilder {
	private class CellBorders {
		CornerTypes cornerOnTopLeft;
		CornerTypes cornerOnTopRight;
		CornerTypes cornerOnBottomRight;
		CornerTypes cornerOnBottomLeft;
		
		boolean borderOnTop;
		boolean borderOnRight;
		boolean borderOnBottom;
		boolean borderOnLeft;
		
		CellBorders cellOnTop;
		CellBorders cellOnRight;
		CellBorders cellOnBottom;
		CellBorders cellOnLeft;
		
		ArenaCell cell;
	}
	
	private Map<Point, ArenaCell> cells;
	private List<ZoneEdge> edgesToFill;
	
	private Map<Point, CellBorders> borders;
	
	public ZoneBorderBuilder(Map<Point, ArenaCell> cells, List<ZoneEdge> edgesToFill) {
		this.cells = cells;
		this.edgesToFill = edgesToFill;
		borders = new HashMap<Point, ZoneBorderBuilder.CellBorders>();
	}
	
	public void build(Player owner, boolean highlighted) {
		performBordersPass();
		performCornersPass();
		createEdges(owner, highlighted);
	}

	/**
	 * Parcours les CellBorders et crée des ZoneEdge pour chaque bord de zone
	 */
	private void createEdges(Player owner, boolean highlighted) {
		MarkerPack pack = Assets.markerPacks.get(owner.markerPack);
				
		edgesToFill.clear();
		for (CellBorders cellBorders : borders.values()) {
			// On ne traite que les cellBorders qui ont une ArenaCell attachée : cela
			// indique une cellule qui fait partie de la zone.
			if (cellBorders.cell == null) {
				continue;
			}
			
			// Crée les bords
			if (cellBorders.borderOnTop) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.TOP, CornerTypes.NONE, pack, highlighted));
			}
			if (cellBorders.borderOnRight) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.RIGHT, CornerTypes.NONE, pack, highlighted));
			}
			if (cellBorders.borderOnBottom) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.BOTTOM, CornerTypes.NONE, pack, highlighted));
			}
			if (cellBorders.borderOnLeft) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.LEFT, CornerTypes.NONE, pack, highlighted));
			}
			
			// Crée les coins
			if (cellBorders.cornerOnTopLeft != CornerTypes.NONE) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.TOP_LEFT, cellBorders.cornerOnTopLeft, pack, highlighted));
			}
			if (cellBorders.cornerOnTopRight != CornerTypes.NONE) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.TOP_RIGHT, cellBorders.cornerOnTopRight, pack, highlighted));
			}
			if (cellBorders.cornerOnBottomLeft != CornerTypes.NONE) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.BOTTOM_LEFT, cellBorders.cornerOnBottomLeft, pack, highlighted));
			}
			if (cellBorders.cornerOnBottomRight != CornerTypes.NONE) {
				edgesToFill.add(createEdge(cellBorders.cell, BordersAndCorners.BOTTOM_RIGHT, cellBorders.cornerOnBottomRight, pack, highlighted));
			}
		}
	}

	/**
	 * Crée un ZoneEdge ayant l'image adaptée à la bordure ou au coin
	 * spécifié
	 * @param borderOrCorner
	 * @return
	 */
	private ZoneEdge createEdge(ArenaCell cell, BordersAndCorners borderOrCorner, CornerTypes cornerType, MarkerPack pack, boolean highlighted) {
		ZoneEdge edge = new ZoneEdge();
		
		// Définit les propriétés du ZoneEdge
		edge.getData().cell = cell;
		edge.getData().borderOrCorner = borderOrCorner;
		edge.getData().cornerType = cornerType;
		edge.getData().anchorPos = getCornerPos(cell, borderOrCorner);
		
		// Choisit l'image adaptée
		edge.setScaling(Scaling.stretch);
		edge.setAlign(Align.center);
		edge.updateDisplay(pack, highlighted);
		
		return edge;
	}

	/**
	 * Retourne le point de base pour positionner les images correspondant au coin de la cellule indiquée.
	 * Par convention, le point d'ancrage est défini dans le sens des aiguilles
	 * d'une montre, sachant que l'image se dessine vers la droite et vers le haut.
	 * Ainsi, selon la valeur de borderOrCorner, anchorPos désignera :
	 * 
	 *       borderOrCorner     |    anchroPos
	 *  ------------------------|-----------------
	 *	LEFT,BOTTOM,BOTTOM_LEFT	| coin bas-gauche
	 *  TOP,TOP_LEFT			| coin haut-gauche
	 *  TOP_RIGHT				| coin haut-droit
	 *  RIGHT,BOTTOM_RIGHT		| coin bas-droit 
	 *  
	 * @param cell
	 * @param borderOrCorner
	 * @return
	 */
	private Vector2 getCornerPos(ArenaCell cell, BordersAndCorners borderOrCorner) {
		switch (borderOrCorner) {
		case TOP:
		case TOP_LEFT:
			return new Vector2(cell.getX(), cell.getTop());
			
		case TOP_RIGHT:
			return new Vector2(cell.getRight(), cell.getTop());
		
		case RIGHT:
		case BOTTOM_RIGHT:
			return new Vector2(cell.getRight(), cell.getY());
			
		case LEFT:
		case BOTTOM:
		case BOTTOM_LEFT:
			return new Vector2(cell.getX(), cell.getY());
			
		default:
			// Ne doit pas pouvoir arriver
			return null;
		}
	}

	/**
	 * Pour chaque cellule, détermine si il y a une bordure sur les 4 coins,
	 * vers l'intérieur ou l'extérieur
	 */
	private void performCornersPass() {
		for (CellBorders cellBorders : borders.values()) {
			// On ne traite que les cellBorders qui ont une ArenaCell attachée : cela
			// indique une cellule qui fait partie de la zone.
			if (cellBorders.cell == null) {
				continue;
			}
			
		// Teste le coin haut-gauche
			
			// Teste s'il y a un coin plein vers l'intérieur, ce qui est le cas
			// s'il y a un bord à gauche et en haut
			if (cellBorders.borderOnLeft && cellBorders.borderOnTop) {
				cellBorders.cornerOnTopLeft = CornerTypes.INNER_CORNER;
			}
			
			// Teste s'il y a un coin plein vers l'extérieur, ce qui est le cas
			// s'il y a un bord à gauche sur le voisin du dessus et en haut sur le voisin de gauche
			else if (cellBorders.cellOnTop.borderOnLeft && cellBorders.cellOnLeft.borderOnTop) {
				cellBorders.cornerOnTopLeft = CornerTypes.OUTER_CORNER;
			}
			
			// Teste s'il y a une jointure entre 2 bords horizontaux, ce qui est le cas
			// s'il y a un bord en haut et un autre bord en haut sur le voisin de gauche
			else if (cellBorders.borderOnTop && cellBorders.cellOnLeft.borderOnTop) {
				cellBorders.cornerOnTopLeft = CornerTypes.HORIZONTAL_JOINT;
			}
			
			// Teste s'il y a une jointure entre 2 bords verticaux, ce qui est le cas
			// s'il y a un bord à gauche et un autre bord à gauche sur le voisin du haut
			else if (cellBorders.borderOnLeft && cellBorders.cellOnTop.borderOnLeft) {
				cellBorders.cornerOnTopLeft = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin fait partie d'une longue ligne de la zone, il faut donc mettre un joint
			else if (cellBorders.borderOnTop) {
				cellBorders.cornerOnTopLeft = CornerTypes.HORIZONTAL_JOINT;
			} else if (cellBorders.borderOnLeft) {
				cellBorders.cornerOnTopLeft = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin est vide
			else {
				cellBorders.cornerOnTopLeft = CornerTypes.NONE;
			}
			
		// Teste le coin haut-droit
		
			// Teste s'il y a un coin plein vers l'intérieur, ce qui est le cas
			// s'il y a un bord à droite et en haut
			if (cellBorders.borderOnRight && cellBorders.borderOnTop) {
				cellBorders.cornerOnTopRight = CornerTypes.INNER_CORNER;
			}
			
			// Teste s'il y a un coin plein vers l'extérieur, ce qui est le cas
			// s'il y a un bord à droite sur le voisin du dessus et en haut sur le voisin de gauche
			else if (cellBorders.cellOnTop.borderOnRight && cellBorders.cellOnRight.borderOnTop) {
				cellBorders.cornerOnTopRight = CornerTypes.OUTER_CORNER;
			}
			
			// Teste s'il y a une jointure entre 2 bords horizontaux, ce qui est le cas
			// s'il y a un bord en haut et un autre bord en haut sur le voisin de droite
			else if (cellBorders.borderOnTop && cellBorders.cellOnRight.borderOnTop) {
				cellBorders.cornerOnTopRight = CornerTypes.HORIZONTAL_JOINT;
			}
			
			// Teste s'il y a une jointure entre 2 bords verticaux, ce qui est le cas
			// s'il y a un bord à droite et un autre bord à droite sur le voisin du haut
			else if (cellBorders.borderOnRight && cellBorders.cellOnTop.borderOnRight) {
				cellBorders.cornerOnTopRight = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin fait partie d'une longue ligne de la zone, il faut donc mettre un joint
			else if (cellBorders.borderOnTop) {
				cellBorders.cornerOnTopRight = CornerTypes.HORIZONTAL_JOINT;
			} else if (cellBorders.borderOnRight) {
				cellBorders.cornerOnTopRight = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin est vide
			else {
				cellBorders.cornerOnTopRight = CornerTypes.NONE;
			}
			
		// Teste le coin bas-droit
		
			// Teste s'il y a un coin plein vers l'intérieur, ce qui est le cas
			// s'il y a un bord à droite et en bas
			if (cellBorders.borderOnRight && cellBorders.borderOnBottom) {
				cellBorders.cornerOnBottomRight = CornerTypes.INNER_CORNER;
			}
			
			// Teste s'il y a un coin plein vers l'extérieur, ce qui est le cas
			// s'il y a un bord à droite sur le voisin du dessous et en bas sur le voisin de gauche
			else if (cellBorders.cellOnBottom.borderOnRight && cellBorders.cellOnRight.borderOnBottom) {
				cellBorders.cornerOnBottomRight = CornerTypes.OUTER_CORNER;
			}
			
			// Teste s'il y a une jointure entre 2 bords horizontaux, ce qui est le cas
			// s'il y a un bord en bas et un autre bord en bas sur le voisin de droite
			else if (cellBorders.borderOnBottom && cellBorders.cellOnRight.borderOnBottom) {
				cellBorders.cornerOnBottomRight = CornerTypes.HORIZONTAL_JOINT;
			}
			
			// Teste s'il y a une jointure entre 2 bords verticaux, ce qui est le cas
			// s'il y a un bord à droite et un autre bord à droite sur le voisin du bas
			else if (cellBorders.borderOnRight && cellBorders.cellOnBottom.borderOnRight) {
				cellBorders.cornerOnBottomRight = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin fait partie d'une longue ligne de la zone, il faut donc mettre un joint
			else if (cellBorders.borderOnBottom) {
				cellBorders.cornerOnBottomRight = CornerTypes.HORIZONTAL_JOINT;
			} else if (cellBorders.borderOnRight) {
				cellBorders.cornerOnBottomRight = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin est vide
			else {
				cellBorders.cornerOnBottomRight = CornerTypes.NONE;
			}
			
		// Teste le coin bas-gauche
		
			// Teste s'il y a un coin plein vers l'intérieur, ce qui est le cas
			// s'il y a un bord à gauche et en bas
			if (cellBorders.borderOnLeft && cellBorders.borderOnBottom) {
				cellBorders.cornerOnBottomLeft = CornerTypes.INNER_CORNER;
			}
			
			// Teste s'il y a un coin plein vers l'extérieur, ce qui est le cas
			// s'il y a un bord à gauche sur le voisin du dessous et en bas sur le voisin de gauche
			else if (cellBorders.cellOnBottom.borderOnLeft && cellBorders.cellOnLeft.borderOnBottom) {
				cellBorders.cornerOnBottomLeft = CornerTypes.OUTER_CORNER;
			}
			
			// Teste s'il y a une jointure entre 2 bords horizontaux, ce qui est le cas
			// s'il y a un bord en bas et un autre bord en bas sur le voisin de gauche
			else if (cellBorders.borderOnBottom && cellBorders.cellOnLeft.borderOnBottom) {
				cellBorders.cornerOnBottomLeft = CornerTypes.HORIZONTAL_JOINT;
			}
			
			// Teste s'il y a une jointure entre 2 bords verticaux, ce qui est le cas
			// s'il y a un bord à gauche et un autre bord à gauche sur le voisin du bas
			else if (cellBorders.borderOnLeft && cellBorders.cellOnBottom.borderOnLeft) {
				cellBorders.cornerOnBottomLeft = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin fait partie d'une longue ligne de la zone, il faut donc mettre un joint
			else if (cellBorders.borderOnBottom) {
				cellBorders.cornerOnBottomLeft = CornerTypes.HORIZONTAL_JOINT;
			} else if (cellBorders.borderOnLeft) {
				cellBorders.cornerOnBottomLeft = CornerTypes.VERTICAL_JOINT;
			}
			
			// Le coin est vide
			else {
				cellBorders.cornerOnBottomLeft = CornerTypes.NONE;
			}
		}
	}

	/**
	 * Pour chaque cellule, détermine si il y a une bordure sur les 4 côtés
	 */
	private void performBordersPass() {
		Point cellPos;
		Point neighborPos = new Point(0, 0);
		CellBorders cellBorders;
		
		for (ArenaCell cell : cells.values()) {
			cellPos = cell.getData().position;
			cellBorders = getOrCreateCellBorders(cellPos);
			
			// Teste s'il y a un bord de zone en haut. C'est le cas s'il n'y a pas
			// d'autre cellule dans la zone au-dessus.
			Point.add(cellPos, Borders.TOP.getOffset(), neighborPos);
			cellBorders.cellOnTop = getOrCreateCellBorders(neighborPos);
			cellBorders.borderOnTop = cellBorders.cellOnTop.cell == null;
			
			// Teste s'il y a un bord de zone à droite. C'est le cas s'il n'y a pas
			// d'autre cellule dans la zone à droite.
			Point.add(cellPos, Borders.RIGHT.getOffset(), neighborPos);
			cellBorders.cellOnRight = getOrCreateCellBorders(neighborPos);
			cellBorders.borderOnRight = cellBorders.cellOnRight.cell == null;
			
			// Teste s'il y a un bord de zone en bas. C'est le cas s'il n'y a pas
			// d'autre cellule dans la zone en-dessous.
			Point.add(cellPos, Borders.BOTTOM.getOffset(), neighborPos);
			cellBorders.cellOnBottom = getOrCreateCellBorders(neighborPos);
			cellBorders.borderOnBottom = cellBorders.cellOnBottom.cell == null;
			
			// Teste s'il y a un bord de zone à gauche. C'est le cas s'il n'y a pas
			// d'autre cellule dans la zone à gauche.
			Point.add(cellPos, Borders.LEFT.getOffset(), neighborPos);
			cellBorders.cellOnLeft = getOrCreateCellBorders(neighborPos);
			cellBorders.borderOnLeft = cellBorders.cellOnLeft.cell == null;
			
		}
	}

	/**
	 * Retourne l'objet CellBorders associé à la cellule dont la position est spécifiée.
	 * Si un tel objet n'existe pas encore, il est créé.
	 * @param cellPos
	 * @return
	 */
	private CellBorders getOrCreateCellBorders(Point cellPos) {
		CellBorders cellBorders = borders.get(cellPos);
		if (cellBorders == null) {
			cellBorders = new CellBorders();
			cellBorders.cell = cells.get(cellPos);
			
			// ATTENTION ! Il faut créer une nouvelle instance de Point car l'instance
			// de point reçue désigne un objet qui va être modifié !
			borders.put(new Point(cellPos), cellBorders);
		}
		return cellBorders;
	}
}
