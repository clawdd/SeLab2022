package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;

/**
 * This class creates Actions
 */
public class ActionFactoryImplementation
        implements de.unisaarland.cs.se.selab.comm.ActionFactory<ActionCommand> {

    private StateMachine stateMachine;
    private ServerConnection<ActionCommand> serverConnection;

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     */
    public ActionFactoryImplementation(final StateMachine stateMachine,
                                       final ServerConnection<ActionCommand> serverConnection) {
        this.stateMachine = stateMachine;
        this.serverConnection = serverConnection;
    }

    /**
     * Create new ActionFactory. stateMachine and serverConnection have to be set afterwards!
     */
    public ActionFactoryImplementation() {

    }

    /**
     * Set stateMachine only once!
     *
     * @param stateMachine stateMachine
     */
    public void setStateMachine(final StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    /**
     * Set serverConnection only once!
     *
     * @param serverConnection serverConnection
     */
    public void setServerConnection(final ServerConnection<ActionCommand> serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * @param commId commId of player
     * @param roomId roomId
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createActivateRoom(final int commId, final int roomId) {
        return new ActivateRoomCommand(stateMachine, serverConnection, commId, roomId);
    }

    /**
     * @param commId commId of player
     * @param x      position x
     * @param y      position y
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createBattleGround(final int commId, final int x, final int y) {
        return new BattleGroundCommand(stateMachine, serverConnection, commId, x, y);
    }

    /**
     * @param commId commId of player
     * @param x      position x
     * @param y      position y
     * @param roomId roomId
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createBuildRoom(final int commId, final int x, final int y,
                                         final int roomId) {
        return new BuildRoomCommand(stateMachine, serverConnection, commId, x, y, roomId);
    }

    /**
     * @param commId commId of player
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createEndTurn(final int commId) {
        return new EndTurnCommand(stateMachine, serverConnection, commId);
    }

    /**
     * @param commId    commId of player
     * @param monsterId monsterId
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createHireMonster(final int commId, final int monsterId) {
        return new HireMonsterCommand(stateMachine, serverConnection, commId, monsterId);
    }

    /**
     * @param commId commId of player
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createLeave(final int commId) {
        return new LeaveCommand(stateMachine, serverConnection, commId);
    }

    /**
     * @param commId    commId of player
     * @param monsterId monsterId
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createMonster(final int commId, final int monsterId) {
        return new MonsterCommand(stateMachine, serverConnection, commId, monsterId);
    }

    /**
     * @param commId    commId of player
     * @param monsterId monsterId
     * @param position  target position
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createMonsterTargeted(final int commId, final int monsterId,
                                               final int position) {
        return new MonsterTargetedCommand(stateMachine, serverConnection,
                commId, monsterId, position);
    }

    /**
     * @param commId  commId of player
     * @param bidType type of bid
     * @param slot    number of player's bid
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createPlaceBid(final int commId, final BidType bidType,
                                        final int slot) {
        return new PlaceBidCommand(stateMachine, serverConnection, commId, bidType, slot);
    }

    /**
     * @param commId commId of player
     * @param x      position x
     * @param y      position y
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createDigTunnel(final int commId, final int x, final int y) {
        return new DigTunnelCommand(stateMachine, serverConnection, commId, x, y);
    }

    /**
     * @param commId     commId of player
     * @param playerName name of player
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createRegister(final int commId, final String playerName) {
        return new RegisterCommand(stateMachine, serverConnection, commId, playerName);
    }

    /**
     * @param commId commId of player
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createStartGame(final int commId) {
        return new StartGameCommand(stateMachine, serverConnection, commId);
    }

    /**
     * @param commId commId of player
     * @param trapId trapId
     * @return the corresponding ActionCommand
     */
    @Override
    public ActionCommand createTrap(final int commId, final int trapId) {
        return new TrapCommand(stateMachine, serverConnection, commId, trapId);
    }
}
