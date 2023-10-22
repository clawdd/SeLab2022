package de.unisaarland.cs.se.selab.builder;

import de.unisaarland.cs.se.selab.gamelogic.dungeon.Restriction;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing the methods in Room
 */
class RoomBuilderTest {

    final RoomBuilder roooomBuilder = new RoomBuilder();

    @Test
    void checkDefaultID() {
        Assertions.assertEquals(0, roooomBuilder.build().getId());
    }

    @Test
    void checkID42() {
        roooomBuilder.withId(42);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(42, room.getId());
    }

    @Test
    void checkActivation4() {
        roooomBuilder.withActivation(4);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(4, room.getActivationCost());
    }

    @Test
    void checkFood3() {
        roooomBuilder.withFood(3);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(3, room.getFood());
    }

    @Test
    void checkNiceness22() {
        roooomBuilder.withNiceness(22);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(22, room.getNiceness());
    }

    @Test
    void checkGold8() {
        roooomBuilder.withGold(8);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(8, room.getGold());
    }

    @Test
    void checkImps9() {
        roooomBuilder.withImps(9);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(9, room.getImps());
    }

    @Test
    void checkRestrictionInnerRing() {
        roooomBuilder.withRestriction(Restriction.INNER_RING);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(Restriction.INNER_RING, room.getRestriction());
    }

    @Test
    void checkRestrictionOuterRing() {
        roooomBuilder.withRestriction(Restriction.OUTER_RING);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(Restriction.OUTER_RING, room.getRestriction());
    }

    @Test
    void checkRestrictionLowerHalf() {
        roooomBuilder.withRestriction(Restriction.LOWER_HALF);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(Restriction.LOWER_HALF, room.getRestriction());
    }

    @Test
    void checkRestrictionUpperHAlf() {
        roooomBuilder.withRestriction(Restriction.UPPER_HALF);
        final Room room = roooomBuilder.build();
        Assertions.assertEquals(Restriction.UPPER_HALF, room.getRestriction());
    }

    @Test
    void checkIsActiveTrue() {
        roooomBuilder.withRestriction(Restriction.UPPER_HALF);
        final Room room = roooomBuilder.build();
        Assertions.assertFalse(room.isActive());
    }

    @Test
    void checkIsTunnel() {
        roooomBuilder.withRestriction(Restriction.UPPER_HALF);
        final Room room = roooomBuilder.build();
        Assertions.assertFalse(room.isTunnel());
    }

}