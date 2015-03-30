package com.slamdunk.wordarena.enums;

import com.slamdunk.toolkit.world.point.Point;

/**
 * Détaille les côtés d'une cellule
 */
public enum Borders {
	TOP(new Point(0, 1)),
	RIGHT(new Point(+1, 0)),
	BOTTOM(new Point(0, -1)),
	LEFT(new Point(-1, 0));
	
	public boolean isVertical() {
		return this == LEFT || this == RIGHT;
	}
	
	public boolean isHorizontal() {
		return this == TOP || this == BOTTOM;
	}
	
	Point offset;
	
	private Borders(Point offset) {
		this.offset = offset;
	}
	
	public Point getOffset() {
		return offset;
	}
}
