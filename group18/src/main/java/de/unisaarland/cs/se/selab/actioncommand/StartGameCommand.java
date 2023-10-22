package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;

/**
 * This Action handles the start of the game
 */
public class StartGameCommand extends ActionCommand {

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     */
    public StartGameCommand(final StateMachine stateMachine,
                            final ServerConnection<ActionCommand> serverConnection,
                            final int commId) {
        super(stateMachine, serverConnection, commId);
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

        if (getStateMachine().getCurrentState().getCurrentStateEnum() != StateEnum.REGISTRATION) {
            getServerConnection().sendActionFailed(getCommId(), "wrong state!!!!");
            return ActionCommandEnum.ACTIONFAILED;
        }

        return ActionCommandEnum.STARTGAME;
    }
}
