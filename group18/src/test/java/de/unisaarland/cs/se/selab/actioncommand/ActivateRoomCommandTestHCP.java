package de.unisaarland.cs.se.selab.actioncommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.unisaarland.cs.se.selab.builder.MonsterBuilder;
import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import de.unisaarland.cs.se.selab.gamelogic.creatures.AttackStrategy;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import de.unisaarland.cs.se.selab.statemachine.BiddingEvalState;
import de.unisaarland.cs.se.selab.statemachine.BiddingPlaceState;
import de.unisaarland.cs.se.selab.statemachine.DefendState;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ActivateRoomCommandTestHCP {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(3333, -1, ACTION_FACTORY);

    @Test
    void checkCommId() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Martin", 42, 38, 1,
                10, 5, 20, 15, SC, playerList);
        playerList.add(dl);
        assertEquals(38, dl.getCommId());
    }

    @Test
    void checkStateMachine() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Martin", 42, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        playerList.add(dl);
        final Room room = new RoomBuilder().withId(21).build();
        final ActivateRoomCommand activateRoomCommand =
                new ActivateRoomCommand(stateMachine, SC, dl.getCommId(), room.getId());
        final BiddingPlaceState biddingPlaceState =
                new BiddingPlaceState(stateMachine);
        stateMachine.updateState(biddingPlaceState);
        assertSame(stateMachine, activateRoomCommand.getStateMachine());
    }

    @Test
    void checkServerConnection() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 5, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Katniss", 18, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        playerList.add(dl);
        final Room room = new RoomBuilder().withId(21).build();
        final ActivateRoomCommand activateRoomCommand =
                new ActivateRoomCommand(stateMachine, SC, dl.getCommId(), room.getId());
        final BiddingEvalState biddingEvalState =
                new BiddingEvalState(stateMachine);
        stateMachine.updateState(biddingEvalState);
        assertSame(SC, activateRoomCommand.getServerConnection());
    }

    @Test
    void monsterAction1() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 5, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Katniss", 18, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        playerList.add(dl);
        final Monster monster =
                new MonsterBuilder().withID(12).withAttackStrategy(AttackStrategy.BASIC).build();
        final DefendState defendState =
                new DefendState(stateMachine);
        stateMachine.updateState(defendState);
        assertSame(AttackStrategy.BASIC, monster.getAttackStrategy());
    }


    @Test
    void monsterAction3() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 5, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Katniss", 18, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        playerList.add(dl);
        final Monster monster =
                new MonsterBuilder().withID(12)
                        .withAttackStrategy(AttackStrategy.BASIC)
                        .withDamage(8).build();
        final DefendState defendState =
                new DefendState(stateMachine);
        final MonsterTargetedCommand monsterTargetCommand =
                new MonsterTargetedCommand(
                        stateMachine, SC, dl.getCommId(), monster.getId(), 2);
        stateMachine.updateState(defendState);
        assertEquals(ActionCommandEnum.ACTIONFAILED,
                monsterTargetCommand.monsterAction(false, monster.getId(), 2));
    }

    @Test
    void monsterAction2() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 5, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Katniss", 18, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        playerList.add(dl);
        final Monster monster =
                new MonsterBuilder().withID(12)
                        .withAttackStrategy(AttackStrategy.BASIC)
                        .withDamage(8).build();
        final DefendState defendState = new DefendState(stateMachine);
        final MonsterCommand monsterCommand =
                new MonsterCommand(stateMachine, SC, dl.getCommId(), monster.getId());
        stateMachine.updateState(defendState);
        assertEquals(ActionCommandEnum.ACTIONFAILED,
                monsterCommand.monsterAction(false, monster.getId(), -1));
    }

}