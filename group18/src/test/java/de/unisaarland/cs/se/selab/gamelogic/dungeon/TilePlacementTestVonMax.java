package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TilePlacementTestVonMax {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();

    static final ServerConnection<ActionCommand> SC = new ServerConnection<>(5324, -1,
            ACTION_FACTORY);

    //<<---Testing 0,0--->>

    @Test
    void placeTiles1() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);
        final Point point = new Point(0, 0);
        final DungeonTile tile = new Tunnel();

        final boolean result = dl.getDungeon().placeTunnel(tile, point);
        Assertions.assertFalse(result);
    }

    @Test
    void placeTiles2() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);
        final Point point = new Point(0, 0);
        final DungeonTile tile = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();

        final boolean result = dl.getDungeon().placeRoom(tile, point);
        Assertions.assertTrue(result);
    }

    //<---Placing out of bounds--->

    @Test
    void ofbTunnel() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 6)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(6, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(6, 6)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(-1, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, -1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(-1, -1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(-1, 6)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(6, -1)));

    }

    @Test
    void ofbRoom() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room3 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room4 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room5 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room6 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room7 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room8 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();


        Assertions.assertFalse(dl.getDungeon().placeTunnel(room1, new Point(0, 6)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(room2, new Point(6, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(room3, new Point(6, 6)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(room4, new Point(-1, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(room5, new Point(0, -1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(room6, new Point(-1, -1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(room7, new Point(-1, 6)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(room8, new Point(6, -1)));

    }

    //<<---Placing room in Bounds but on null tile--->>

    @Test
    void placingRoomNull() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room3 = new RoomBuilder().withRestriction(Restriction.UPPER_HALF).build();
        final Room room4 = new RoomBuilder().withRestriction(Restriction.LOWER_HALF).build();

        Assertions.assertFalse(dl.getDungeon().placeRoom(room1, new Point(0, 2)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(2, 2)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room3, new Point(0, 1)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room4, new Point(0, 4)));
    }

    //<<---Placing Tunnel on Tunnel--->>

    @Test
    void tunnelOnTunnel() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 0)));

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
    }

    //<<---Placing room on room--->>

    @Test
    void roomOnRoom() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();

        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(0, 0)));
    }

    //<<---Placing room with Restriction in Bounds on tunnel--->>

    @Test
    void outerRingRoom() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room3 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room4 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();


        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 4)));

        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(0, 1)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room3, new Point(0, 3)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room4, new Point(0, 4)));
    }

    @Test
    void innerRing() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room3 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room4 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();


        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 1)));

        Assertions.assertFalse(dl.getDungeon().placeRoom(room1, new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room2, new Point(1, 1)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room3, new Point(2, 1)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room4, new Point(3, 1)));
    }

    @Test
    void upperhalf() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.UPPER_HALF).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.UPPER_HALF).build();

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 0)));

        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(1, 0)));
    }

    @Test
    void lowerHalf() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.LOWER_HALF).build();

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 0)));

        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(2, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room1, new Point(3, 0)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(4, 0)));
    }

    //<<---Outer ring circle--->>>

    @Test
    void circle() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
    }

    //<<---Edge cases--->>

    @Test
    void corners() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));

        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 3)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 3)));
    }

    //<<---Donut--->>

    @Test
    void donut() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
    }

    //<<---Crafting block--->>

    @Test
    void threeByThree() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));

        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 1)));
    }

    //<<--- One by One --->>

    @Test
    void oneByOne1() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 1, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room3 = new RoomBuilder().withRestriction(Restriction.UPPER_HALF).build();
        final Room room4 = new RoomBuilder().withRestriction(Restriction.LOWER_HALF).build();

        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room3, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room4, new Point(0, 0)));
    }

    @Test
    void oneByOne2() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 1, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room2 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room3 = new RoomBuilder().withRestriction(Restriction.UPPER_HALF).build();
        final Room room4 = new RoomBuilder().withRestriction(Restriction.LOWER_HALF).build();

        Assertions.assertTrue(dl.getDungeon().placeRoom(room2, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room3, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room4, new Point(0, 0)));
    }

    @Test
    void oneByOne3() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 1, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room3 = new RoomBuilder().withRestriction(Restriction.UPPER_HALF).build();
        final Room room4 = new RoomBuilder().withRestriction(Restriction.LOWER_HALF).build();

        Assertions.assertTrue(dl.getDungeon().placeRoom(room3, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room4, new Point(0, 0)));
    }

    @Test
    void oneByOne4() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 1, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room4 = new RoomBuilder().withRestriction(Restriction.LOWER_HALF).build();

        Assertions.assertTrue(dl.getDungeon().placeRoom(room4, new Point(0, 0)));
    }

    //<<--- Room Neighbour testing--->>>

    @Test
    void roomNeighbour1() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(0, 1)));

    }

    @Test
    void roomNeighbour2() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 01)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(0, 1)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(0, 0)));

    }

    @Test
    void roomNeighbour3() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(0, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(1, 0)));

    }

    @Test
    void roomNeighbour4() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();

        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeRoom(room1, new Point(1, 0)));
        Assertions.assertFalse(dl.getDungeon().placeRoom(room2, new Point(0, 0)));

    }

    //<<--- Count the tiles--->>

    @Test
    void countTiles() {


        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final int result = dl.getDungeon().countTunnelTiles();

        Assertions.assertEquals(1, result);

    }

    //<<---Max amount of tiles in the dungeon--->

    @Test
    void countTiles2() {


        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1));
        dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2));
        dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 2));
        dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 2));
        dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 1));
        dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0));
        dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0));

        final int result = dl.getDungeon().countTunnelTiles();

        Assertions.assertEquals(8, result);

    }

    @Test
    void checkRoomDiagonal() {

        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room1 = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        final Room room2 = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();

        dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1));
        dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 1));

        final boolean result1 = dl.getDungeon().placeRoom(room2, new Point(0, 0));
        final boolean result2 = dl.getDungeon().placeRoom(room1, new Point(1, 1));

        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);

    }

}