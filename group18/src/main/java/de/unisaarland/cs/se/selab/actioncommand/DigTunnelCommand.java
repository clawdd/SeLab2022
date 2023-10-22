package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Imp;
import de.unisaarland.cs.se.selab.gamelogic.creatures.ImpTask;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.awt.Point;
import java.util.List;

/**
 * This Action digs a Tunnel
 */
public class DigTunnelCommand extends ActionCommand {

    private final int posX;
    private final int posY;

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     * @param x                position x
     * @param y                position y
     */
    public DigTunnelCommand(final StateMachine stateMachine,
                            final ServerConnection<ActionCommand> serverConnection,
                            final int commId, final int x, final int y) {
        super(stateMachine, serverConnection, commId);
        this.posX = x;
        this.posY = y;
    }

    /**
     * @return corresponding ActionCommandEnum
     */
    @Override
    public ActionCommandEnum execute(final List<Integer> allowedCommIds) {
        if (!allowedCommIds.contains(getCommId())) {
            getServerConnection().sendActionFailed(getCommId(),
                    "You're not allowed to do this now!!");
            return ActionCommandEnum.ACTIONWRONGPLAYER;
        }

        final var playerMaybe =
                super.getStateMachine().getGameBoard()
                        .getDungeonLordByCommId(super.getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (super.getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.BIDDINGEVALTUNNELETC) {
            super.getServerConnection().sendActionFailed(super.getCommId(), "You're in "
                    + "the wrong state");
            return ActionCommandEnum.ACTIONFAILED;
        }

        final var player = playerMaybe.get();

        if (player.getNumberOfImpsDoingThisTask(ImpTask.TUNNEL) == 3) {
            if (player.getUnemployedNumberOfImps() >= 2
                    && player.digTunnel(new Point(posX, posY))) {
                return changeTwoImpsForSupervise(player);
            } else {
                super.getServerConnection().sendActionFailed(super.getCommId(),
                        "not possible to place tunnel");
                return ActionCommandEnum.ACTIONFAILED;
            }
        }
        if (player.getUnemployedNumberOfImps() >= 1 && player.digTunnel(new Point(posX, posY))) {
            player.changeImps(1, ImpTask.TUNNEL);
            getStateMachine().getGameBoard().getPlayers().forEach(p ->
                    getServerConnection().sendTunnelDug(p.getCommId(), player.getId(),
                            posX, posY));
            return ActionCommandEnum.DIGTUNNEL;
        } else {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "not possible to place tunnel");
            return ActionCommandEnum.ACTIONFAILED;
        }
    }

    private ActionCommandEnum changeTwoImpsForSupervise(final DungeonLord player) {
        for (final Imp i : player.getImps()) {
            if (i.getCurrentTask() == ImpTask.REST) {
                i.setCurrentTask(ImpTask.TUNNEL);
                break;
            }
        }
        for (final Imp i : player.getImps()) {
            if (i.getCurrentTask() == ImpTask.REST) {
                i.setCurrentTask(ImpTask.SUPERVISE);
                break;
            }
        }

        if (getStateMachine().getGameBoard().playerIsInTheGame(player)) {
            getStateMachine().getGameBoard().getPlayers().forEach(p ->
                    getServerConnection().sendImpsChanged(p.getCommId(), -2,
                            player.getId()));
            getStateMachine().getGameBoard().getPlayers().forEach(p ->
                    getServerConnection().sendTunnelDug(p.getCommId(), player.getId(),
                            posX, posY));
        }
        return ActionCommandEnum.DIGTUNNEL;
    }
}
