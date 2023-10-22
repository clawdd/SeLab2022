package de.unisaarland.cs.se.selab.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.builder.MonsterBuilder;
import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import de.unisaarland.cs.se.selab.builder.TrapBuilder;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.creatures.ImpTask;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Restriction;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DungeonLordTestHCP {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC = new ServerConnection<>(5005, -1,

            ACTION_FACTORY);
    static final List<DungeonLord> LORDS = new ArrayList<>(1);

    @Test
    void changeGoldMinus2() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeGold(-2);
        assertEquals(1, dungeonLord.getGold());
    }

    @Test
    void changeGoldPlus2() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeGold(2);
        assertEquals(5, dungeonLord.getGold());
    }


    @Test
    void checkGold00() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertTrue(dungeonLord.checkGold(2));
    }

    @Test
    void changeFood() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeFood(-3);
        assertEquals(0, dungeonLord.getFood());
    }

    @Test
    void checkFood00() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertFalse(dungeonLord.checkFood(4));
    }

    @Test
    void evalImpsOneGold() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.getImps().get(0).setCurrentTask(ImpTask.GOLD);
        dungeonLord.evalImps();
        assertEquals(4, dungeonLord.getGold());
    }

    @Test
    void evalImpsOneTunnel() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.getImps().get(0).setCurrentTask(ImpTask.SUPERVISE);
        dungeonLord.evalImps();
        assertEquals(3, dungeonLord.getGold());
    }

    @Test
    void employImps() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.employImps(3);
        assertEquals(6, dungeonLord.getImps().size());
    }

    @Test
    void checkUnemployedNumberOfImps() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertEquals(3, dungeonLord.getUnemployedNumberOfImps());
    }

    @Test
    void changeImps01() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeImps(2, ImpTask.GOLD);
        assertSame(dungeonLord.getImps().get(0).getCurrentTask(), ImpTask.GOLD);
    }

    @Test
    void changeImps02() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeImps(2, ImpTask.GOLD);
        assertSame(dungeonLord.getImps().get(1).getCurrentTask(), ImpTask.GOLD);
    }

    @Test
    void changeImps03() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeImps(2, ImpTask.GOLD);
        assertSame(dungeonLord.getImps().get(2).getCurrentTask(), ImpTask.REST);
    }

    @Test
    void checkNumberOfImpsDoingThisTask() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeImps(2, ImpTask.GOLD);
        assertEquals(2, dungeonLord.getNumberOfImpsDoingThisTask(ImpTask.GOLD));
    }

    @Test
    void checkImps() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertTrue(dungeonLord.checkImps(2));
    }

    @Test
    void changeEvilness() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeEvilness(5);
        assertEquals(8, dungeonLord.getEvilness());
    }

    @Test
    void changeEvilnessUpperBound() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeEvilness(5);
        assertFalse(dungeonLord.checkEvilness(10));
        assertEquals(8, dungeonLord.getEvilness());
    }

    @Test
    void changeEvilnessLowerBound() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        dungeonLord.changeEvilness(-3);
        assertFalse(dungeonLord.checkEvilness(-2));
        assertEquals(0, dungeonLord.getEvilness());
    }

    @Test
    void checkEvilness00() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertFalse(dungeonLord.checkEvilness(-4));
        assertFalse(dungeonLord.checkEvilness(13));
        assertTrue(dungeonLord.checkEvilness(12));
    }

    @Test
    void addMonster() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        final MonsterBuilder monsterBuilder = new MonsterBuilder();
        final Monster monster = monsterBuilder.build();
        dungeonLord.addMonster(monster);
        assertEquals(1, dungeonLord.getMonster().size());
    }

    @Test
    void placeTrap() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        final TrapBuilder trapBuilder = new TrapBuilder();
        final Trap trap = trapBuilder.build();
        dungeonLord.placeTrap(trap);
        assertEquals(0, dungeonLord.getTraps().size());
    }

    @Test
    void placeRoom() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        final RoomBuilder roomBuilder = new RoomBuilder();
        roomBuilder.withId(2003);
        roomBuilder.withRestriction(Restriction.OUTER_RING);
        final Room room = roomBuilder.build();
        dungeonLord.placeRoom(room, new Point(0, 0));
        assertTrue(dungeonLord.activateRoom(room.getId()));
    }

    @Test
    void activateRoom() {
        // TODO: 06.10.22 Tunnel an neuer Position platzieren.
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        final RoomBuilder roomBuilder = new RoomBuilder();
        roomBuilder.withId(2003);
        roomBuilder.withRestriction(Restriction.OUTER_RING);
        final Room room = roomBuilder.build();
        dungeonLord.digTunnel(new Point(1, 0));
        dungeonLord.placeRoom(room, new Point(1, 0));
        assertTrue(dungeonLord.activateRoom(room.getId()));
    }

    @Test
    void checkName() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertEquals(dungeonLord.getName(), "Svenja");
    }

    @Test
    void checkId() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertEquals(5, dungeonLord.getId());
    }

    @Test
    void checkCommId() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertEquals(10, dungeonLord.getCommId());
    }

    @Test
    void checkImps00() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertEquals(3, dungeonLord.getImps().size());
    }

    @Test
    void checkFood() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        8, 3, 3, SC, LORDS);
        assertEquals(8, dungeonLord.getFood());
    }

    @Test
    void checkGold() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 10,
                        3, 3, 3, SC, LORDS);
        assertEquals(10, dungeonLord.getGold());
    }

    @Test
    void checkMonster() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        final MonsterBuilder monsterBuilder = new MonsterBuilder();
        monsterBuilder.withID(601);
        final Monster monster = monsterBuilder.build();
        dungeonLord.addMonster(monster);
        assertEquals(601, dungeonLord.getMonster().get(0).getId());
    }

    @Test
    void checkTraps() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 3, SC, LORDS);
        assertTrue(dungeonLord.getTraps().isEmpty());
    }

    @Test
    void checkEvilness() {
        final DungeonLord dungeonLord =
                new DungeonLord("Svenja", 5, 10, 5, 3,
                        3, 3, 9, SC, LORDS);
        assertEquals(9, dungeonLord.getEvilness());
    }

    @Test
    void checkDungeon() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        assertEquals(0, dungeonLord.getDungeon().getPrison().size());
    }

    @Test
    void checkFirstPlacedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstPlacedBidType(BidType.FOOD);
        assertSame(dungeonLord.getFirstPlacedBidType(), BidType.FOOD);
    }

    @Test
    void checkSecondPlacedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstPlacedBidType(BidType.FOOD);
        dungeonLord.setSecondPlacedBidType(BidType.NICENESS);
        assertSame(dungeonLord.getSecondPlacedBidType(), BidType.NICENESS);
    }

    @Test
    void checkThirdPlacedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstPlacedBidType(BidType.FOOD);
        dungeonLord.setSecondPlacedBidType(BidType.NICENESS);
        dungeonLord.setThirdPlacedBidType(BidType.GOLD);
        assertSame(dungeonLord.getThirdPlacedBidType(), BidType.GOLD);
    }

    @Test
    void setFirstPlacedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstPlacedBidType(BidType.MONSTER);
        assertSame(dungeonLord.getFirstPlacedBidType(), BidType.MONSTER);
    }

    @Test
    void setSecondPlacedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstPlacedBidType(BidType.FOOD);
        dungeonLord.setSecondPlacedBidType(BidType.TRAP);
        assertSame(dungeonLord.getSecondPlacedBidType(), BidType.TRAP);
    }

    @Test
    void setThirdPlacedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstPlacedBidType(BidType.FOOD);
        dungeonLord.setSecondPlacedBidType(BidType.NICENESS);
        dungeonLord.setThirdPlacedBidType(BidType.TUNNEL);
        assertSame(dungeonLord.getThirdPlacedBidType(), BidType.TUNNEL);
    }

    @Test
    void checkFirstLockedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstLockedBidType(BidType.MONSTER);
        assertSame(dungeonLord.getFirstLockedBidType(), BidType.MONSTER);
    }

    @Test
    void setFirstLockedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstLockedBidType(BidType.MONSTER);
        assertSame(dungeonLord.getFirstLockedBidType(), BidType.MONSTER);
    }

    @Test
    void checkSecondLockedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstLockedBidType(BidType.MONSTER);
        dungeonLord.setSecondLockedBidType(BidType.ROOM);
        assertSame(dungeonLord.getSecondLockedBidType(), BidType.ROOM);
    }

    @Test
    void setSecondLockedBidType() {
        final DungeonLord dungeonLord =
                new DungeonLord("Sprudel", 19, 10, 20, 8,
                        6, 5, 14, SC, LORDS);
        dungeonLord.setFirstLockedBidType(BidType.MONSTER);
        dungeonLord.setSecondLockedBidType(BidType.ROOM);
        assertSame(dungeonLord.getSecondLockedBidType(), BidType.ROOM);
    }

}