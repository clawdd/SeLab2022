package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.awt.Point;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This Action handles the activation of a Trap
 */
public class TrapCommand extends ActionCommand {

    private final int trapId;

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     * @param trapId           trapId
     */
    public TrapCommand(final StateMachine stateMachine,
                       final ServerConnection<ActionCommand> serverConnection,
                       final int commId, final int trapId) {
        super(stateMachine, serverConnection, commId);
        this.trapId = trapId;
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

        final var playerMaybe = getStateMachine()
                .getGameBoard().getDungeonLordByCommId(getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.DEFEND) {
            getServerConnection().sendActionFailed(getCommId(),
                    "false state");
            return ActionCommandEnum.ACTIONFAILED;
        }

        final var player = playerMaybe.get();

        Trap placedTrap;
        try {
            placedTrap = player.getTraps().stream().filter(trap -> trap.getId() == trapId)
                    .findFirst().get();
        } catch (NoSuchElementException e) {
            getServerConnection().sendActionFailed(getCommId(),
                    "wrong trap selected");
            return ActionCommandEnum.ACTIONFAILED;
        }
        if (!player.placeTrap(placedTrap)) {
            getServerConnection().sendActionFailed(getCommId(),
                    "trap already placed");
            return ActionCommandEnum.ACTIONFAILED;
        }
        final Point point = new Point(player.getDungeon().getBattleGroundPosition());
        if (player.getDungeon().getDungeonSquare()[point.x][point.y].isRoom()) {
            if (!player.checkGold(1)) {
                getServerConnection().sendActionFailed(getCommId(),
                        "not enough gold");
                return ActionCommandEnum.ACTIONFAILED;
            }
            player.changeGold(-1);
        }
        // remove trap
        player.getTraps().remove(placedTrap);
        getStateMachine().getGameBoard().getPlayers().forEach(p ->
                getStateMachine().getSc().sendTrapPlaced(p.getCommId(),
                        player.getId(), placedTrap.getId()));
        return ActionCommandEnum.TRAP;
    }

}
