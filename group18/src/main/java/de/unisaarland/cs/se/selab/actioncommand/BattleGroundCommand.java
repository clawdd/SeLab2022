package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.awt.Point;
import java.util.List;

/**
 * This Action selects a BattleGround
 */
public class BattleGroundCommand extends ActionCommand {

    private final int posX;
    private final int posY;

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     * @param x                position x
     * @param y                position y
     */
    public BattleGroundCommand(final StateMachine stateMachine,
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

        final var playerMaybe = getStateMachine().getGameBoard()
                .getDungeonLordByCommId(getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (super.getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.SETBATTLEGROUND) {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "false state");
            return ActionCommandEnum.ACTIONFAILED;
        }

        final var player = playerMaybe.get();

        if (!player.getDungeon().placeBattleGround(new Point(posX, posY))) {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "place can not be a battle ground");
            return ActionCommandEnum.ACTIONFAILED;
        }
        getStateMachine().getGameBoard().getPlayers().forEach(p ->
                getServerConnection().sendBattleGroundSet(
                        p.getCommId(), player.getId(), posX, posY));
        return ActionCommandEnum.BATTLEGROUND;
    }
}
