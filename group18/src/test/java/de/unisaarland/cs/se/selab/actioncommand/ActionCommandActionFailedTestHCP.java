package de.unisaarland.cs.se.selab.actioncommand;

import static org.junit.jupiter.api.Assertions.assertSame;

import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import de.unisaarland.cs.se.selab.statemachine.RegistrationState;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ActionCommandActionFailedTestHCP {


    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(3234, -1, ACTION_FACTORY);

    @Test
    void executeBuildRoom() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Han Solo", 42, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final DungeonLord nyt = new DungeonLord("Wrong.IO", 18, 18, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<Integer> allowedCommIds = new ArrayList<>();
        allowedCommIds.add(30);
        playerList.add(dl);
        playerList.add(nyt);
        final BuildRoomCommand buildRoomCommand =
                new BuildRoomCommand(stateMachine, SC, 30, 2, 1, 10);
        assertSame(ActionCommandEnum.ACTIONFAILED, buildRoomCommand.execute(allowedCommIds));
    }

    @Test
    void executeHireMonster() {
        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Han Solo", 42, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final DungeonLord nyt = new DungeonLord("Wrong.IO", 18, 18, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<Integer> allowedCommIds = new ArrayList<>();
        allowedCommIds.add(30);
        playerList.add(dl);
        playerList.add(nyt);
        final HireMonsterCommand hireMonsterCommand =
                new HireMonsterCommand(stateMachine, SC, 30, 10);
        assertSame(ActionCommandEnum.ACTIONFAILED, hireMonsterCommand.execute(allowedCommIds));
    }

    @Test
    void executeBattleGround() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<Integer> allowedCommIds = new ArrayList<>();
        allowedCommIds.add(30);
        final BattleGroundCommand battleGroundCommand =
                new BattleGroundCommand(stateMachine, SC, 30, 5, 5);
        assertSame(ActionCommandEnum.ACTIONFAILED, battleGroundCommand.execute(allowedCommIds));
    }

    @Test
    void executeEndTurn() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<Integer> allowedCommIds = new ArrayList<>();
        allowedCommIds.add(30);
        final EndTurnCommand endTurnCommand =
                new EndTurnCommand(stateMachine, SC, 30);
        assertSame(ActionCommandEnum.ACTIONFAILED, endTurnCommand.execute(allowedCommIds));
    }

    @Test
    void executeActivateRoom() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<Integer> allowedCommIds = new ArrayList<>();
        allowedCommIds.add(30);
        final ActivateRoomCommand activateRoomCommand =
                new ActivateRoomCommand(stateMachine, SC, 30, 20);
        assertSame(ActionCommandEnum.ACTIONFAILED, activateRoomCommand.execute(allowedCommIds));
    }

    @Test
    void registerCommand() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        stateMachine.updateState(new RegistrationState(stateMachine, "blabla"));
        assertSame(StateEnum.REGISTRATION, stateMachine.getCurrentState().getCurrentStateEnum());
    }

    @Test
    void executeStartGame() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<Integer> allowedCommIds = new ArrayList<>();
        allowedCommIds.add(30);
        final StartGameCommand startGameCommand =
                new StartGameCommand(stateMachine, SC, 30);
        assertSame(ActionCommandEnum.ACTIONFAILED, startGameCommand.execute(allowedCommIds));
    }



}