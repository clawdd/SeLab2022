package de.unisaarland.cs.se.selab.statemachine;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.builder.AdventurerBuilder;
import de.unisaarland.cs.se.selab.builder.MonsterBuilder;
import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Adventurer;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.DungeonTile;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Restriction;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Tunnel;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EndStateTest {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(2346, -1, ACTION_FACTORY);

    @Test
    void testCalculatePoints2() throws TimeoutException {

        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<DungeonLord> playerList = new ArrayList<>();
        final DungeonLord jolian = new DungeonLord("Jolian", 0, 1234, 5,
                10, 123, 6, 9, SC, playerList);
        final DungeonLord caesar = new DungeonLord("Caesar", 1, 9876, 5,
                2345, 12_345, 10_000, 10, SC, playerList);
        playerList.add(jolian);
        playerList.add(caesar);
        gameBoard.getPlayers().addAll(playerList);

        final Monster monster0 = (new MonsterBuilder()).withID(0).build();
        final Monster monster1 = (new MonsterBuilder()).withID(1).build();
        final Monster monster2 = (new MonsterBuilder()).withID(2).build();
        final Monster monster3 = (new MonsterBuilder()).withID(3).build();
        final Monster monster4 = (new MonsterBuilder()).withID(4).build();
        final Monster monster5 = (new MonsterBuilder()).withID(5).build();
        final Adventurer adventurer0 = new AdventurerBuilder().withId(0).build();
        final Adventurer adventurer1 = new AdventurerBuilder().withId(1).build();
        final Adventurer adventurer2 = new AdventurerBuilder().withId(2).build();
        final Adventurer adventurer3 = new AdventurerBuilder().withId(3).build();
        final Adventurer adventurer4 = new AdventurerBuilder().withId(4).build();
        final Adventurer adventurer5 = new AdventurerBuilder().withId(5).build();
        final Room room0 = (new RoomBuilder()).withId(0)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room1 = (new RoomBuilder()).withId(1)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room2 = (new RoomBuilder()).withId(2)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room3 = (new RoomBuilder()).withId(3)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room4 = (new RoomBuilder()).withId(4)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room5 = (new RoomBuilder()).withId(5)
                .withRestriction(Restriction.UPPER_HALF).build();
        final DungeonTile[][] dungeonSquareCaesar = {
                {new Tunnel(), new Tunnel(), room0, new Tunnel(), room2},
                {new Tunnel(), null, null, null, null},
                {room1, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
        };
        dungeonSquareCaesar[0][0].setConquered(true);
        dungeonSquareCaesar[0][1].setConquered(true);
        dungeonSquareCaesar[0][2].setConquered(true);
        caesar.getDungeon().setDungeonSquare(dungeonSquareCaesar);
        caesar.addMonster(monster0);
        caesar.addMonster(monster1);
        caesar.addMonster(monster2);
        caesar.getDungeon().getPrison().add(adventurer0);
        caesar.getDungeon().getPrison().add(adventurer1);
        caesar.getDungeon().getPrison().add(adventurer2);
        final DungeonTile[][] dungeonSquareJolian = {
                {new Tunnel(), new Tunnel(), null, null, null},
                {null, room3, new Tunnel(), null, null},
                {null, room4, new Tunnel(), new Tunnel(), room5},
                {null, null, null, null, null},
                {null, null, null, null, null}
        };
        dungeonSquareJolian[0][0].setConquered(true);
        dungeonSquareJolian[0][1].setConquered(true);
        dungeonSquareJolian[1][1].setConquered(true);
        jolian.getDungeon().setDungeonSquare(dungeonSquareJolian);
        jolian.addMonster(monster3);
        jolian.addMonster(monster4);
        jolian.addMonster(monster5);
        jolian.getDungeon().getPrison().add(adventurer3);
        jolian.getDungeon().getPrison().add(adventurer4);
        jolian.getDungeon().getPrison().add(adventurer5);

        final EndState endState = new EndState(stateMachine);
        endState.calculateDungeonProperties1();
        endState.calculateDungeonProperties2();
        endState.calculateDungeonProperties3();
        final int[][] evaluationDungeon = endState.getEvaluationDungeon();
        // row 0: conquered tile, row 1: unconquered tile,
        // row 2: tunnel tile, row 3: rooms, row 4: // check whether tunnel tile is conquered
        Assertions.assertEquals(3, evaluationDungeon[0][0]);
        Assertions.assertEquals(3, evaluationDungeon[0][1]);
        Assertions.assertEquals(5, evaluationDungeon[1][0]);
        Assertions.assertEquals(4, evaluationDungeon[1][1]);
        Assertions.assertEquals(5, evaluationDungeon[2][0]);
        Assertions.assertEquals(4, evaluationDungeon[2][1]);
        Assertions.assertEquals(3, evaluationDungeon[3][0]);
        Assertions.assertEquals(3, evaluationDungeon[3][1]);
        Assertions.assertEquals(3, evaluationDungeon[4][0]);
        Assertions.assertEquals(2, evaluationDungeon[4][1]);
    }

    @Test
    void testCalculatePoints() throws TimeoutException {

        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<DungeonLord> playerList = new ArrayList<>();
        final DungeonLord jolian = new DungeonLord("Jolian", 0, 1234, 5,
                10, 123, 6, 9, SC, playerList);
        final DungeonLord caesar = new DungeonLord("Caesar", 1, 9876, 5,
                2345, 12_345, 10_000, 10, SC, playerList);
        playerList.add(jolian);
        playerList.add(caesar);
        gameBoard.getPlayers().addAll(playerList);

        final Monster monster0 = (new MonsterBuilder()).withID(0).build();
        final Monster monster1 = (new MonsterBuilder()).withID(1).build();
        final Monster monster2 = (new MonsterBuilder()).withID(2).build();
        final Monster monster3 = (new MonsterBuilder()).withID(3).build();
        final Monster monster4 = (new MonsterBuilder()).withID(4).build();
        final Monster monster5 = (new MonsterBuilder()).withID(5).build();
        final Adventurer adventurer0 = new AdventurerBuilder().withId(0).build();
        final Adventurer adventurer1 = new AdventurerBuilder().withId(1).build();
        final Adventurer adventurer2 = new AdventurerBuilder().withId(2).build();
        final Adventurer adventurer3 = new AdventurerBuilder().withId(3).build();
        final Adventurer adventurer4 = new AdventurerBuilder().withId(4).build();
        final Adventurer adventurer5 = new AdventurerBuilder().withId(5).build();
        final Room room0 = (new RoomBuilder()).withId(0)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room1 = (new RoomBuilder()).withId(1)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room2 = (new RoomBuilder()).withId(2)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room3 = (new RoomBuilder()).withId(3)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room4 = (new RoomBuilder()).withId(4)
                .withRestriction(Restriction.UPPER_HALF).build();
        final Room room5 = (new RoomBuilder()).withId(5)
                .withRestriction(Restriction.UPPER_HALF).build();
        final DungeonTile[][] dungeonSquareCaesar = {
                {new Tunnel(), new Tunnel(), room0, new Tunnel(), room2},
                {new Tunnel(), null, null, null, null},
                {room1, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
        };
        dungeonSquareCaesar[0][0].setConquered(true);
        dungeonSquareCaesar[0][1].setConquered(true);
        dungeonSquareCaesar[0][2].setConquered(true);
        caesar.getDungeon().setDungeonSquare(dungeonSquareCaesar);
        caesar.addMonster(monster0);
        caesar.addMonster(monster1);
        caesar.addMonster(monster2);
        caesar.getDungeon().getPrison().add(adventurer0);
        caesar.getDungeon().getPrison().add(adventurer1);
        caesar.getDungeon().getPrison().add(adventurer2);
        final DungeonTile[][] dungeonSquareJolian = {
                {new Tunnel(), new Tunnel(), null, null, null},
                {null, room3, new Tunnel(), null, null},
                {null, room4, new Tunnel(), new Tunnel(), room5},
                {null, null, null, null, null},
                {null, null, null, null, null}
        };
        dungeonSquareJolian[0][0].setConquered(true);
        dungeonSquareJolian[0][1].setConquered(true);
        dungeonSquareJolian[1][1].setConquered(true);
        jolian.getDungeon().setDungeonSquare(dungeonSquareJolian);
        jolian.addMonster(monster3);
        jolian.addMonster(monster4);
        jolian.addMonster(monster5);
        jolian.getDungeon().getPrison().add(adventurer3);
        jolian.getDungeon().getPrison().add(adventurer4);
        jolian.getDungeon().getPrison().add(adventurer5);

        final EndState endState = new EndState(stateMachine);
        endState.calculatePoints();
        // row 0: conquered tile, row 1: unconquered tile,
        // row 2: tunnel tile, row 3: rooms, row 4: // check whether tunnel tile is conquered
        Assertions.assertEquals(3, endState.getTableOfAllScores()[0][0]);
        Assertions.assertEquals(3, endState.getTableOfAllScores()[0][1]);

    }

}