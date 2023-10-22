package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.RegistrationState;
import de.unisaarland.cs.se.selab.statemachine.State;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * This Action handles a registration of a user
 */
public class RegisterCommand extends ActionCommand {

    private final String playerName;
    // private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCommand.class);

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     * @param playerName       name of player
     */
    public RegisterCommand(final StateMachine stateMachine,
                           final ServerConnection<ActionCommand> serverConnection,
                           final int commId, final String playerName) {
        super(stateMachine, serverConnection, commId);
        this.playerName = playerName;
    }

    /**
     * @return corresponding ActionCommandEnum
     */
    @Override
    public ActionCommandEnum execute(final List<Integer> forbiddenCommIds) {
        LoggerFactory.getLogger(RegisterCommand.class).trace(forbiddenCommIds.toString());
        if (forbiddenCommIds.contains(getCommId())) {
            getServerConnection().sendActionFailed(getCommId(),
                    "You're already registered!!");
            return ActionCommandEnum.ACTIONWRONGPLAYER;
        }
        // inverted check for allowed commIDs here because everyone can register

        // checks for correct State, here REGISTRATION
        if (getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.REGISTRATION) {
            getServerConnection().sendActionFailed(getCommId(),
                    "false state");
            return ActionCommandEnum.ACTIONFAILED;
        }

        // add Player to GameBoard
        super.getStateMachine().getGameBoard().addPlayer(playerName, super.getCommId());

        // send config to new Player
        final State state = getStateMachine().getCurrentState();
        getServerConnection().sendConfig(getCommId(),
                ((RegistrationState) state).getConfigString());

        return ActionCommandEnum.REGISTER;
    }
}
