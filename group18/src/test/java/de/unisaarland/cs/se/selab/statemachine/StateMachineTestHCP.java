package de.unisaarland.cs.se.selab.statemachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class StateMachineTestHCP {

    static final ActionFactory<ActionCommand> ACTION_FACTORY =
            new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(5069, -1, ACTION_FACTORY);

    /*@Test
    void runMachine01() throws TimeoutException {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final EndState endState = new EndState(stateMachine);
        stateMachine.updateState(endState);
        stateMachine.runMachine();
        endState.run();
    }*/

    @Test
    void runMachine02() {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final EndState endState = new EndState(stateMachine);
        stateMachine.updateState(endState);
        assertSame(StateEnum.END, stateMachine.getCurrentState().getCurrentStateEnum());
    }

    @Test
    void runMachine03() {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        assertSame(SC, stateMachine.getSc());
    }

    @Test
    void runMachine04() {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        assertTrue(stateMachine.getCombatPlayerStack().isEmpty());
    }

    @Test
    void runMachine05() {
        final List<DungeonLord> playerlist = new ArrayList<>();
        final DungeonLord nyt = new DungeonLord("Dummy", 18, 18, 1,
                10, 5, 20, 15, SC, playerlist);
        playerlist.add(nyt);
    }

    /*@Test
    void runMachine06() {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<DungeonLord> playerlist = new ArrayList<>();
        final Stack<DungeonLord> playerStack = new Stack<>();
        final DungeonLord nyt = new DungeonLord("Dummy", 18, 18, 1,
                10, 5, 20, 15, SC, playerlist);
        playerlist.add(nyt);
        playerStack.add(nyt);
        stateMachine.handleCombatLeave();
    }*/

    @Test
    void runMachine07() throws TimeoutException {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final BiddingPlaceState biddingPlaceState = new BiddingPlaceState(stateMachine);
        biddingPlaceState.run();
    }

    @Test
    void runMachine08() {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final RegistrationState registrationState =
                new RegistrationState(stateMachine, "bla");
        assertEquals("bla", registrationState.getConfigString());
    }

    @Test
    void runMachine09() throws TimeoutException {
        final GameBoard gameBoard =
                new GameBoard(SC, 2, 4,
                        3, 3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final SetBattleGroundState setBattleGroundState =
                new SetBattleGroundState(stateMachine);
        setBattleGroundState.run();
    }
}