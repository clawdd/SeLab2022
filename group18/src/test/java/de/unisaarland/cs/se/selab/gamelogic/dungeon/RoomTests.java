package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RoomTests {

    @Test
    void testRestrictions1() {

        final Room room = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();
        Assertions.assertEquals(Restriction.INNER_RING, room.getRestriction());

    }

    @Test
    void testRestrictions2() {

        final Room room = new RoomBuilder().withRestriction(Restriction.OUTER_RING).build();
        Assertions.assertEquals(Restriction.OUTER_RING, room.getRestriction());

    }

    @Test
    void testRestrictions3() {

        final Room room = new RoomBuilder().withRestriction(Restriction.UPPER_HALF).build();
        Assertions.assertEquals(Restriction.UPPER_HALF, room.getRestriction());

    }

    @Test
    void testRestrictions4() {

        final Room room = new RoomBuilder().withRestriction(Restriction.LOWER_HALF).build();
        Assertions.assertEquals(Restriction.LOWER_HALF, room.getRestriction());

    }

    @Test
    void testConquered1() {
        final Room room = new RoomBuilder().build();
        Assertions.assertFalse(room.getConquered());
    }

    @Test
    void testConquered2() {
        final Room room = new RoomBuilder().build();
        room.setConquered(true);
        Assertions.assertTrue(room.getConquered());
    }

}
