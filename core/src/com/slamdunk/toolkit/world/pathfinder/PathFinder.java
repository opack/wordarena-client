package com.slamdunk.toolkit.world.pathfinder;

import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.toolkit.world.point.PointManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class is a pathfinder which uses AStar algorithm.
 * 
 * The A* algorithm is an algorithm of search for way in a graph between an initial
 * node and a final node.  It uses a heuristic evaluation on each node to consider
 * the best way passing there, and visits then the nodes by order of this heuristic
 * evaluation. 
 * </br>AStar is an algorithm which certifies to find a way between a point
 * A and a point B if there is one.  It is more intelligent than Dijsktra because it
 * is not interested only in the points enabling him to approach the point B. At each
 * turn it uses the theorem of Pythagore to calculate an approximation of the distance
 * between the point running and the point B. At each point to analyze AStar updates
 * the parent of the point, the distance since the source and the distance (approximate)
 * to the arrival. When the visited point is the arrival point , AStar updates the list
 * that it will be returned.  For that it course from parent to parent from the point B
 * (arrival) to the point A (departure). Point A is not put in the list because we consider
 * that it is the position of the object asking for a path. In the case of the way does
 * not exist AStar will return null.
 * 
 * @author S. Cleret & D. Demange
 */

public class PathFinder {
	
    private class CostComparator implements Comparator<Point>, Serializable {

        private static final long serialVersionUID = 8286298148231746736L;

        public int compare(Point p1, Point p2) {
            return (totalCost[p1.getIndex()] - totalCost[p2.getIndex()]);
        }
    }

    private boolean canMoveDiagonally;

    private final HashSet<Point> closed;

    private int[] costSinceSrc;

    private final int[] initCost;

    private final Point[] initParent;

    private final ArrayList<Point> listPoint;

    private final PriorityQueue<Point> opened;
    
    private Point[] parent;
    
    private PointManager pointManager;
    
    /**
     * Valeur avec laquelle est initilisé le tableau de walkable en cas de reset()
     */
    private boolean resetValue;
    
    int[] totalCost;
    
    /**
     * Can indicate whether a position is walkable or not
     */
    private boolean[][] walkables;
    
    public PathFinder(int width, int height) {
    	this(width, height, true);
    }
    
    /**
     * Creates an AStar object.
     * @param resetValue default value for each position in the map
     */
    public PathFinder(int width, int height, boolean resetValue) {
    	if (width < 1 || height < 1) {
    		throw new IllegalArgumentException("Minimum size is 1x1 !");
    	}
        initCost = new int[width * height];
        initParent = new Point[width * height];

        closed = new HashSet<Point>();

        opened = new PriorityQueue<Point>(20, new CostComparator ());

        listPoint = new ArrayList<Point>(8);
        
        pointManager = new PointManager(width, height);
        
        walkables = new boolean[width][height];
        
        this.resetValue = resetValue;
        reset();
    }

	private void addIfWalkable(ArrayList<Point> listPoint2, int x, int y, int ignoredWalkabilityIndex) {
    	Point pos = pointManager.getPoint(x, y);
        if (pos != null
        // La position est ajoutée si on doit ignorer sa walkability ou si elle est walkable
        && (pos.getIndex() == ignoredWalkabilityIndex || walkables[pos.getX()][pos.getY()])) {
            listPoint.add(pos);
        }		
	}

	public List<Point> findPath(int fromX, int fromY, int toX, int toY) {
		return findPath(fromX, fromY, toX, toY, false);
	}
	
	public List<Point> findPath(int fromX, int fromY, int toX, int toY, boolean ignoreArrivalWalkable) {
		return findPath(pointManager.getPoint(fromX, fromY), pointManager.getPoint(toX, toY), ignoreArrivalWalkable);
	}
	
	/**
     * Finds a shortest path from the specified departure spot to the secified
     * arrival position. This method uses the A* algorithm has described in the
     * header. If a parameter is null or if the arrival position is occupied or
     * equal to the departure, this method returns null. If no path can be
     * found, this method returns null. The returned stack contains all the
     * successive positions to go through to reach the arrival.
     * 
     * @param departure
     *            The departure spot
     * @param arrival
     *            Where to go
     * @param ignoreArrivalWalkable
     * 			  If true, arrival position walkability is ignored.
     * @return null if no path could be found or a reference to a stack
     *         containing the positions to use
     */
    public List<Point> findPath(Point departure, Point arrival, boolean ignoreArrivalWalkable) {

        // If the destination is a blocked position, null is returned
        if ((departure == null) || (arrival == null)
        || (!ignoreArrivalWalkable && !walkables[arrival.getX()][arrival.getY()])
        || departure.equals(arrival)) {
            return null;
        }
        LinkedList<Point> path = new LinkedList<Point>();

        boolean isSonOpened = false;
        boolean isSonClosed = false;
        boolean pathFound = false;

        costSinceSrc = initCost.clone();
        totalCost = initCost.clone();
        parent = initParent.clone();

        closed.clear();
        opened.clear();

        opened.offer(departure);

        Point s = null;
        int ignoredWalkabilityIndex = -1;
        if (ignoreArrivalWalkable) {
        	ignoredWalkabilityIndex = arrival.getIndex();
        }

        while (!opened.isEmpty()) {
            s = opened.poll();

            if (s.equals(arrival)) {
                pathFound = true;
                break;
            }

            int sIndex = s.getIndex();

            for (Point t : getNeighborhood(s, ignoredWalkabilityIndex)) {
                int tIndex = t.getIndex();
                int tempCostSinceSrc = costSinceSrc[sIndex] + 1;
                int tempTotalCost = tempCostSinceSrc + getDistanceToArrival(t, arrival);

                isSonOpened = opened.contains(t);
                isSonClosed = closed.contains(t);

                if ((!isSonOpened && !isSonClosed) || (totalCost[tIndex] > tempTotalCost)) {

                    parent[tIndex] = s;
                    totalCost[tIndex] = tempTotalCost;
                    costSinceSrc[tIndex] = tempCostSinceSrc;

                    if (isSonOpened) {
                        opened.remove(t);
                    }

                    if (isSonClosed) {
                        closed.remove(t);
                    }

                    opened.offer(t);
                }
            }

            closed.add(s);
        }

        if (pathFound) {
            path.addFirst(arrival);
            s = parent[arrival.getIndex()];
            while (!s.equals(departure)) {
                path.addFirst(s);
                s = parent[s.getIndex()];
            }
            return path;
        }
        else {
            return null;
        }
    }

	private int getDistanceToArrival(Point position, Point destination) {
        return (destination.getX() - position.getX()) * (destination.getX() - position.getX()) + (destination.getY() - position.getY()) * (destination.getY() - position.getY());
    }

    private ArrayList<Point> getNeighborhood(Point center, int ignoredWalkabilityIndex) {
        listPoint.clear();
        
        // North
        addIfWalkable(listPoint, center.getX(), center.getY() - 1, ignoredWalkabilityIndex);

        // East
        addIfWalkable(listPoint, center.getX() + 1, center.getY(), ignoredWalkabilityIndex);

        // South
        addIfWalkable(listPoint, center.getX(), center.getY() + 1, ignoredWalkabilityIndex);

        // West
        addIfWalkable(listPoint, center.getX() - 1, center.getY(), ignoredWalkabilityIndex);

        if (canMoveDiagonally) {
	        // North East
        	addIfWalkable(listPoint, center.getX() + 1, center.getY() - 1, ignoredWalkabilityIndex);
	        
	        // South East
	        addIfWalkable(listPoint, center.getX() + 1, center.getY() + 1, ignoredWalkabilityIndex);
	        
	        // South West
	        addIfWalkable(listPoint, center.getX() - 1, center.getY() + 1, ignoredWalkabilityIndex);
	        
	        // North West
	        addIfWalkable(listPoint, center.getX() - 1, center.getY() - 1, ignoredWalkabilityIndex);
        }
        return listPoint;
    }
    
    public PointManager getPointManager() {
		return pointManager;
	}

	public boolean[][] getWalkables() {
		return walkables;		
	}
    
    public boolean isCanMoveDiagonally() {
		return canMoveDiagonally;
	}

	public boolean isWalkable(int x, int y) {
		if (x < 0 || x >= walkables.length
		|| y < 0 || y >= walkables[0].length) {
			return false;
		}
		return walkables[x][y];
	}

	public void reset() {
    	// Par défaut toute la map est walkable
        for (int curCol = 0; curCol < walkables.length; curCol++) {
        	for (int curRow = 0; curRow < walkables[0].length; curRow++) {
        		walkables[curCol][curRow] = resetValue;
        	}
        }
	}

	public void setCanMoveDiagonally(boolean canMoveDiagonally) {
		this.canMoveDiagonally = canMoveDiagonally;
	}
	
	public void setWalkable(int x, int y, boolean walkable) {
		walkables[x][y] = walkable;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// Print column number
		sb.append("  |");
		for (int col = 0; col < walkables.length - 1; col ++) {
			sb.append(col / 10).append("|");
		}
		sb.append("\n  |");
		for (int col = 0; col < walkables.length - 1; col ++) {
			sb.append(col % 10).append("|");
		}
		sb.append("\n");
		
		// Print row data
		for (int row = walkables[0].length - 1; row >= 0; row--) {
			// Print row number
			if (row < 10) {
				sb.append(" ");
			}
			sb.append(row).append("|");
			// Print row data
			for (int col = 0; col < walkables.length - 1; col ++) {
				if (walkables[col][row]) {
					sb.append(" |");
				} else {
					sb.append("X|");
				}
			}
			// Print row number at end of line
			sb.append(row);
			sb.append("\n");
		}
		
		// Print column number
		sb.append("  |");
		for (int col = 0; col < walkables.length - 1; col ++) {
			sb.append(col / 10).append("|");
		}
		sb.append("\n  |");
		for (int col = 0; col < walkables.length - 1; col ++) {
			sb.append(col % 10).append("|");
		}
		sb.append("\n");
		return sb.toString();
	}
}
