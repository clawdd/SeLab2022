package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;

/**
 * This Action activates a Room
 */
public class ActivateRoomCommand extends ActionCommand {

    private final int roomId;

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     * @param roomId           roomId
     */
    public ActivateRoomCommand(
            final StateMachine stateMachine,
            final ServerConnection<ActionCommand> serverConnection,
            final int commId, final int roomId) {
        super(stateMachine, serverConnection, commId);
        this.roomId = roomId;
    }

    /**
     * @return corresponding ActionCommandEnum
     */
    @Override
    public ActionCommandEnum execute(final List<Integer> allowedCommIds) {

        final var playerMaybe =
                super.getStateMachine().getGameBoard().getDungeonLordByCommId(getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (getStateMachine().getCurrentState().getCurrentStateEnum()
                == StateEnum.BIDDINGEVAL
                || getStateMachine().getCurrentState().getCurrentStateEnum()
                == StateEnum.BIDDINGPLACE
                || getStateMachine().getCurrentState().getCurrentStateEnum()
                == StateEnum.BIDDINGEVALROOMETC
                || getStateMachine().getCurrentState().getCurrentStateEnum()
                == StateEnum.BIDDINGEVALTUNNELETC
                || getStateMachine().getCurrentState().getCurrentStateEnum()
                == StateEnum.BIDDINGEVALMONSTERETC) {
            final var player = playerMaybe.get();

            if (!player.activateRoom(roomId)) {
                super.getServerConnection().sendActionFailed(super.getCommId(),
                        "could not activate room");
                return ActionCommandEnum.ACTIONFAILED;
            }

            if (!allowedCommIds.contains(getCommId())) {
                return ActionCommandEnum.ACTIVATEROOMOTHERPLAYER;
            } else {
                return ActionCommandEnum.ACTIVATEROOM;
            }
        } else {
            getServerConnection().sendActionFailed(getCommId(),
                    "false state");
            return ActionCommandEnum.ACTIONFAILED;
        }

    }

}
