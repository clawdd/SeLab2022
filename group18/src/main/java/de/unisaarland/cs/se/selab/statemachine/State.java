package de.unisaarland.cs.se.selab.statemachine;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class represents a state
 */
public abstract class State {

    /**
     * only to be accessed by subclasses
     */
    protected StateMachine stateMachine;
    private StateEnum currentStateEnum;

    /**
     * @param stateMachine used as super-constructor // @param currentStateEnum to be hardcoded in
     *                     concrete states
     * @param stateEnum    stateEnum
     */
    public State(final StateMachine stateMachine, final StateEnum stateEnum) {
        this.stateMachine = stateMachine;
        this.currentStateEnum = stateEnum;
    }

    /**
     * dependent on the state, run simulates the respective state
     *
     * @throws TimeoutException exception
     */
    public abstract void run() throws TimeoutException;

    /**
     * @return returns current StateMachine, e.g. to access GameBoard
     */
    public StateMachine getStateMachine() {
        return stateMachine;
    }

    /**
     * @param stateMachine default Setter
     */
    public void setStateMachine(final StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    /**
     * @return used to access CurrentState
     */
    public StateEnum getCurrentStateEnum() {
        return currentStateEnum;
    }

    /**
     * @param stateEnum new StateEnum value
     */
    public void setCurrentStateEnum(final StateEnum stateEnum) {
        this.currentStateEnum = stateEnum;
    }

    /**
     * checks whether current bid is last bid of a player
     *
     * @param commId of player
     * @return true if last bid, false else
     */
    public boolean isLastBidOfPlayer(final int commId) {
        final var bs = getStateMachine().getGameBoard().getBiddingSquare();
        return 3 == bs.getBiddingInboxQueue().stream().filter(b ->
                b.getPlayer().getCommId() == commId).count();
    }

    /**
     * checks whether current bid is last bid of all players
     *
     * @return true if last bid of all players, false else
     */
    public boolean isLastBidOfAllPlayers() {
        final var bs = getStateMachine().getGameBoard().getBiddingSquare();
        return getStateMachine()
                .getGameBoard().getPlayers().size() * 3 == bs.getBiddingInboxQueue().size();
    }

    /**
     * returns the commIds of players in a list
     *
     * @return list of commids
     */
    public List<Integer> getCommIdOfBiddingPlayers() {
        final List<Integer> list = new ArrayList<>();
        getStateMachine().getGameBoard().getPlayers().forEach(p -> {
            if (!isLastBidOfPlayer(p.getCommId())) {
                list.add(p.getCommId());
            }
        });
        return list;
    }

    /**
     * this method checks if there are still players left and if not it changes the state to the end
     * state
     *
     * @return true if everyone left
     */
    public boolean checkAllPlayersLeftAndUpdateState() {
        if (getStateMachine().getGameBoard().getPlayers().isEmpty()) {
            getStateMachine().updateState(new EndState(getStateMachine()));
            getStateMachine().getCurrentState().setCurrentStateEnum(StateEnum.REALEND);
            return true;
        }
        return false;
    }

}
