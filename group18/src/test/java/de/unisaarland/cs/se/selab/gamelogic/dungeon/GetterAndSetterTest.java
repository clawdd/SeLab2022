package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.builder.AdventurerBuilder;
import de.unisaarland.cs.se.selab.builder.MonsterBuilder;
import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import de.unisaarland.cs.se.selab.builder.TrapBuilder;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Adventurer;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GetterAndSetterTest {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();

    static final ServerConnection<ActionCommand> SC = new ServerConnection<>(9018, -1,
            ACTION_FACTORY);
    final List<DungeonLord> list = new ArrayList<>();

    //<<--- Getter and Setter --->>

    //Empty
    @Test
    void testPlayerOwnedRooms() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final int result = dl.getDungeon().getPlayersOwnedRooms().size();

        Assertions.assertEquals(0, result);
    }

    //With elements
    @Test
    void testPlayerOwnedRooms2() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Room room = new RoomBuilder().withRestriction(Restriction.INNER_RING).build();

        dl.getDungeon().getPlayersOwnedRooms().add(room);
        dl.getDungeon().getPlayersOwnedRooms().add(room);
        final int result = dl.getDungeon().getPlayersOwnedRooms().size();

        Assertions.assertEquals(2, result);
    }

    @Test
    void testPlacedTrap() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final var result = dl.getDungeon().getPlacedTrap();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testPlacedMonsters() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final var result = dl.getDungeon().getPlacedMonsters().size();
        Assertions.assertEquals(0, result);
    }

    @Test
    void testGetPrison() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final var result = dl.getDungeon().getPrison().size();
        Assertions.assertEquals(0, result);
    }

    @Test
    void testGetAdventurer() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final var result = dl.getDungeon().getAdventurers().size();
        Assertions.assertEquals(0, result);
    }

    @Test
    void testGetBattleGroundPos() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final var result = dl.getDungeon().getBattleGroundPosition();
        Assertions.assertEquals(new Point(0, 0), result);
    }

    @Test
    void testAddAdventurer() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Adventurer tobias = new AdventurerBuilder().build();
        dl.getDungeon().getAdventurers().add(tobias);
        final int result = dl.getDungeon().getAdventurers().size();

        Assertions.assertEquals(1, result);
    }

    @Test
    void testClearAdventurer() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Adventurer tobias = new AdventurerBuilder().build();
        dl.getDungeon().getAdventurers().add(tobias);
        final int result = dl.getDungeon().getAdventurers().size();

        Assertions.assertEquals(1, result);

        dl.getDungeon().clearAdventurers();
        final int result2 = dl.getDungeon().getAdventurers().size();

        Assertions.assertEquals(0, result2);
    }

    @Test
    void checkTrap() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Trap trap = new TrapBuilder().build();

        dl.getDungeon().setTrap(trap);

        final var result = dl.getDungeon().getPlacedTrap();
        Assertions.assertEquals(trap, result);
    }

    @Test
    void clearDefenses() {

        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Trap trap = new TrapBuilder().build();
        final Monster monster = new MonsterBuilder().build();

        dl.getDungeon().setTrap(trap);
        dl.getMonster().add(monster);

        dl.getDungeon().clearPlacedDefenses();

        final var result1 = dl.getDungeon().getPlacedTrap();
        final var result2 = dl.getDungeon().getPlacedMonsters().size();

        Assertions.assertEquals(null, result1);
        Assertions.assertEquals(0, result2);

    }
}