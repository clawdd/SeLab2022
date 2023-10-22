package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import java.awt.Point;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    DungeonTile tnl(final int kind) {
        switch (kind) {
            case 1 -> {
                return new Tunnel();
            }
            case 2 -> {
                final var tunnel = new Tunnel();
                tunnel.setConquered(true);
                return tunnel;
            }
            default -> {
                return null;
            }
        }
    }

    @Test
    void findClosestUnconqueredTiles1() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(2), tnl(2), tnl(1)},
                {tnl(0), tnl(0), tnl(1), tnl(0)}
        };

        final var expected = Set.of(
                new Point(3, 2), new Point(2, 3));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles2() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(0), tnl(0), tnl(1), tnl(0)}
        };

        final var expected = Set.of(
                new Point(3, 2));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles3() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(0), tnl(0), tnl(0), tnl(1)}
        };

        final var expected = Set.of(
                new Point(3, 3), new Point(1, 3));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles4() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(1)},
                {tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(2), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(1), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)}
        };

        final var expected = Set.of(
                new Point(0, 6));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles5() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(1), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)}
        };

        final var expected = Set.of(
                new Point(5, 0), new Point(1, 6));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles6() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(2)},
                {tnl(2), tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(2), tnl(2), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(0), tnl(2), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(2), tnl(1), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0)}
        };

        final var expected = Set.of(
                new Point(5, 3));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles7() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(2), tnl(0), tnl(0), tnl(2), tnl(0), tnl(0), tnl(2)},
                {tnl(2), tnl(2), tnl(0), tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(0), tnl(2), tnl(0), tnl(2), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(0), tnl(2), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(0), tnl(1), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(2), tnl(2), tnl(2), tnl(0), tnl(0), tnl(0)}
        };

        final var expected = Set.of(
                new Point(5, 3), new Point(2, 6));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles8() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(2), tnl(0), tnl(0), tnl(0), tnl(0), tnl(0), tnl(2)},
                {tnl(2), tnl(2), tnl(0), tnl(1), tnl(0), tnl(0), tnl(2)},
                {tnl(0), tnl(2), tnl(0), tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(0), tnl(2), tnl(0), tnl(2), tnl(0), tnl(0), tnl(2)},
                {tnl(0), tnl(2), tnl(0), tnl(0), tnl(1), tnl(0), tnl(1)},
                {tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2)}
        };

        final var expected = Set.of(
                new Point(5, 6), new Point(5, 4));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles9() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(2), tnl(0), tnl(0), tnl(0), tnl(2), tnl(0), tnl(0), tnl(2)},
                {tnl(2), tnl(2), tnl(2), tnl(0), tnl(2), tnl(0), tnl(0), tnl(2)},
                {tnl(0), tnl(0), tnl(2), tnl(0), tnl(2), tnl(0), tnl(0), tnl(2)},
                {tnl(0), tnl(0), tnl(2), tnl(0), tnl(2), tnl(0), tnl(2), tnl(1)},
                {tnl(2), tnl(2), tnl(2), tnl(0), tnl(2), tnl(0), tnl(2), tnl(0)},
                {tnl(2), tnl(0), tnl(0), tnl(0), tnl(2), tnl(0), tnl(2), tnl(0)},
                {tnl(1), tnl(2), tnl(2), tnl(2), tnl(1), tnl(2), tnl(2), tnl(0)}
        };

        final var expected = Set.of(
                new Point(7, 0), new Point(7, 4), new Point(4, 7));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles10() {
        final DungeonTile[][] square = {
                {tnl(1), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(2), tnl(2), tnl(2)},
                {tnl(0), tnl(0), tnl(1), tnl(0)}
        };

        final var expected = Set.of(
                new Point(0, 0));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles11() {
        final DungeonTile[][] square = {
                {tnl(1)}
        };
        final var expected = Set.of(
                new Point(0, 0));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles12() {
        final DungeonTile[][] square = {
                {tnl(2)}
        };
        final var expected = Set.of();
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findClosestUnconqueredTiles13() {
        final DungeonTile[][] square = {
                {tnl(2), tnl(1), tnl(0), tnl(0)},
                {tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(0), tnl(0), tnl(0)},
                {tnl(0), tnl(0), tnl(0), tnl(0)}
        };

        final var expected = Set.of(
                new Point(0, 1));
        final var result = Set.copyOf(PathFinder.findClosestUnconqueredTiles(
                square, new Point(0, 0)));

        Assertions.assertEquals(expected, result);
    }
}