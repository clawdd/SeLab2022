package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;

/**
 * This Action removes a player from the game
 */
public class LeaveCommand extends ActionCommand {

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId
     */
    public LeaveCommand(final StateMachine stateMachine,
                        final ServerConnection<ActionCommand> serverConnection,
                        final int commId) {
        super(stateMachine, serverConnection, commId);
    }

    /**
     * @return corresponding ActionCommandEnum
     */
    @Override
    public ActionCommandEnum execute(final List<Integer> redundantList) {
        LoggerFactory.getLogger(LeaveCommand.class).trace(
                "Redundant List: " + redundantList.toString());

        final GameBoard gameBoard = getStateMachine().getGameBoard();
        final Optional<DungeonLord> dungeonLordMaybe =
                gameBoard.getDungeonLordByCommId(getCommId());

        dungeonLordMaybe.ifPresent(playerWhoLeft -> {
            if (getStateMachine().getCurrentState().getCurrentStateEnum()
                    == StateEnum.REGISTRATION) {
                gameBoard.removePlayerSilently(getCommId());
            } else {
                gameBoard.removePlayer(getCommId());
            }
        });

        return ActionCommandEnum.LEAVE;
    }
}
