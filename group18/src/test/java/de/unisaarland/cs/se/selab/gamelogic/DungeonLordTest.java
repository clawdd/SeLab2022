package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.builder.MonsterBuilder;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DungeonLordTest {

    static final ActionFactory<ActionCommand> AF = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(1235, -1, AF);

    @Test
    void testAddMonster() {
        final MonsterBuilder mBuilder = new MonsterBuilder();
        final Monster monster = new Monster(mBuilder);
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Martin", 42, 0, 1,
                10, 5, 20, 15, SC, players);
        dl.addMonster(monster);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getMonster(), dl.getMonster());
    }

    @Test
    void testGetName() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Martin", 42, 0, 1,
                10, 5, 20, 15, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getName(), dl.getName());
    }

    @Test
    void testGetId() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Jonathan", 12, 8, 2,
                1000, 5, 20, 15, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getId(), dl.getId());
    }

    @Test
    void testGetCommId() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Julian", 543, 34, 5,
                0, 0, 0, 15, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getCommId(), dl.getCommId());
    }

    @Test
    void testGetImps() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Max", 5, 123, 43,
                134, 6, 8, 3, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getImps(), dl.getImps());
    }

    @Test
    void testGetFood() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Joshua", 86, 26, 4,
                53, 7, 1, 6, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getFood(), dl.getFood());
    }

    @Test
    void testGetGold() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Colin", 0, 0, 1,
                1234, 1234, 1234, 1, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getGold(), dl.getGold());
    }

    @Test
    void testGetMonster() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Person", 0, 0, 1,
                0, 0, 0, 0, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getMonster(), dl.getMonster());
    }

    @Test
    void testGetTraps() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Max", 5, 3, 2,
                4, 7, 6, 2, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getTraps(), dl.getTraps());
    }

    @Test
    void testGetEvilness() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Martin", 3, 4, 1,
                97, 56, 3, 15, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getEvilness(), dl.getEvilness());
    }

    @Test
    void testGetDungeon() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Martin", 3, 4, 1,
                97, 56, 3, 15, SC, players);
        players.add(dl);
        Assertions.assertEquals(players.get(0).getDungeon(), dl.getDungeon());
    }
}