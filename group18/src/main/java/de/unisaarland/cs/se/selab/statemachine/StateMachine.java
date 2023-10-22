package de.unisaarland.cs.se.selab.statemachine;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionCommandEnum;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import java.util.List;
import java.util.Stack;


/**
 * This is the state machine which handles all states
 */
public class StateMachine {

    private State currentState;
    private final GameBoard gameBoard;
    private final ServerConnection<ActionCommand> sc;
    private final Stack<DungeonLord> combatPlayerStack;

    /**
     * Constructor for StateMachine; created in Main, handed over by states
     *
     * @param gameBoard to access elements of game-board
     * @param sc        required to access users' action and send out events
     */
    public StateMachine(final GameBoard gameBoard, final ServerConnection<ActionCommand> sc) {
        this.gameBoard = gameBoard;
        this.sc = sc;
        this.combatPlayerStack = new Stack<>();
    }

    /**
     * updateState ensures that we invoke the correct run-method of the respective state
     *
     * @param state current state of StateMachine; required for run-method
     */
    public void updateState(final State state) {
        currentState = state;
    }


    /**
     * Starts with first state RegisterState and goes through all states
     *
     * @throws TimeoutException exception when TimeOuting
     */
    public void runMachine() throws TimeoutException {
        do {
            this.currentState.run();
        } while (this.currentState.getCurrentStateEnum() != StateEnum.REALEND);
    }

    /**
     * @return trivial getter for current state
     */
    public State getCurrentState() {
        return currentState;
    }


    /**
     * @return to access elements of game-board
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * @return required to access users' action and send out events
     */
    public ServerConnection<ActionCommand> getSc() {
        return sc;
    }

    /**
     * returns the stack of all players which need to go through the combat phase
     *
     * @return stack of players
     */
    public Stack<DungeonLord> getCombatPlayerStack() {
        return combatPlayerStack;
    }

    /**
     * Queue of all players who need to run through the combat phase
     */
    public void updateCombatPlayerStack() {
        for (int i = gameBoard.getPlayers().size() - 1; i >= 0; i--) {
            this.combatPlayerStack.add(gameBoard.getPlayers().get(i));
        }
    }

    /**
     * handle the timeout of a player during combat
     */
    public void handleCombatTimeout() {
        getGameBoard().removePlayer(
                getCombatPlayerStack().pop().getCommId());
        getGameBoard().setCurrentSeason(0);
        updateState(new SetBattleGroundState(this));
    }

    /**
     * handle the leave of a player during combat
     */
    public void handleCombatLeave() {
        getCombatPlayerStack().pop();
        getGameBoard().setCurrentSeason(0);
        updateState(new SetBattleGroundState(this));
    }

    /**
     * Execute next action until the breakAction is executed or the player left
     *
     * @param breakAction the action after which the loop ends
     * @return true if the player left / had a timeout, else false
     */
    public boolean doCombatAndCheckIfLeft(final ActionCommandEnum breakAction) {
        ActionCommand ac;
        ActionCommandEnum acReturn = null;
        do {
            if (currentState.checkAllPlayersLeftAndUpdateState()) {
                return true;
            }
            if (acReturn != ActionCommandEnum.ACTIONWRONGPLAYER
                    && acReturn != ActionCommandEnum.LEAVE) {
                getSc().sendActNow(getCombatPlayerStack().peek().getCommId());
            }
            try {
                ac = getSc().nextAction();
            } catch (TimeoutException e) {
                // remove player from game board and from stack, skip combat for that player
                handleCombatTimeout();
                return true;
            }
            final var listWithCurrentPlayer = List
                    .of(getCombatPlayerStack().peek().getCommId());
            acReturn = ac.execute(listWithCurrentPlayer);
            if (acReturn == ActionCommandEnum.LEAVE) {
                if (gameBoard.getPlayers().stream().noneMatch(p ->
                        p.getCommId() == getCombatPlayerStack()
                                .peek().getCommId())) {
                    // remove player from stack, skip combat for that player
                    handleCombatLeave();
                    return true;
                }
                final ActionCommand finalAc = ac;
                getCombatPlayerStack().removeIf(p -> p.getCommId() == finalAc.getCommId());
            }
        } while (acReturn != breakAction);
        return false;
    }
}
