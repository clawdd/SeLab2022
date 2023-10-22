package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * for checking battleground stuff
 */
public class PathFinder {

    /**
     * @param square     square
     * @param entryPoint entry point
     * @return the closest unconquered tiles to the entry point
     */
    public static List<Point> findClosestUnconqueredTiles(final DungeonTile[][] square,
                                                          final Point entryPoint) {
        int pointsAmount = 0;
        // calculate amount of tiles that are not null
        for (final DungeonTile[] tiles : square) {
            for (final DungeonTile tile : tiles) {
                if (tile != null) {
                    pointsAmount++;
                }
            }
        }

        // find distance of all points
        final Map<Point, Integer> points = findPointsAndDistance(square, entryPoint, 0,
                new HashMap<>(), pointsAmount);

        final Map<Point, Integer> pointsNotConquered = new HashMap<>();

        points.forEach((p, d) -> {
            if (!square[p.x][p.y].getConquered()) {
                pointsNotConquered.put(p, d);
            }
        });

        int minimumDistance = 225;
        for (final Map.Entry<Point, Integer> entry : pointsNotConquered.entrySet()) {
            final Integer d = entry.getValue();
            if (d < minimumDistance) {
                minimumDistance = d;
            }
        }

        final List<Point> resultList = new LinkedList<>();

        final int finalMinimumDistance = minimumDistance;
        pointsNotConquered.forEach((p, d) -> {
            if (d == finalMinimumDistance) {
                resultList.add(p);
            }
        });

        return resultList;
    }

    /**
     * finds all points that are connected to point together with their distance
     *
     * @param square     square
     * @param point      starting point
     * @param distance   initial distance
     * @param neighbours initial neighbours
     * @param totalTiles total amount of tiles to find
     * @return points with their distance
     */
    public static Map<Point, Integer> findPointsAndDistance(final DungeonTile[][] square,
                                                            final Point point,
                                                            final int distance,
                                                            final Map<Point, Integer> neighbours,
                                                            final int totalTiles) {
        neighbours.put(point, distance);
        final Point[] neighboursToCheck = {
                new Point(point.x - 1, point.y),
                new Point(point.x + 1, point.y),
                new Point(point.x, point.y + 1),
                new Point(point.x, point.y - 1)
        };
        for (final Point neighbour : neighboursToCheck) {
            if (neighbour.x >= 0 && neighbour.x < square.length
                    && neighbour.y >= 0 && neighbour.y < square[0].length
                    && square[neighbour.x][neighbour.y] != null
                    && (neighbours.get(neighbour) == null
                    || distance + 1 < neighbours.get(neighbour))
                    && neighbours.size() <= totalTiles) {
                neighbours.put(neighbour, distance + 1);
                neighbours.putAll(findPointsAndDistance(
                        square, neighbour, distance + 1, neighbours,
                        totalTiles));
            }
        }
        return Map.copyOf(neighbours);
    }

    /**
     * find the closest null-tiles to the point (0, 0)
     *
     * @param square square
     * @return the list of points
     */
    public static List<Point> findClosestNullPointsTo00(final DungeonTile[][] square) {
        int minimumDistance = 225;
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square.length; j++) {
                if (square[i][j] == null && i + j < minimumDistance) {
                    minimumDistance = i + j;
                }
            }
        }
        final List<Point> result = new LinkedList<>();
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square.length; j++) {
                if (square[i][j] == null && i + j == minimumDistance) {
                    result.add(new Point(i, j));
                }
            }
        }
        return result;
    }
}
