package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.awt.Point;
import java.util.List;

/**
 * This Action builds a Room
 */
public class BuildRoomCommand extends ActionCommand {

    private final int posX;
    private final int posY;
    private final int roomId;

    /**
     * Constructor
     *
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId
     * @param x                x coordinate
     * @param y                y coordinate
     * @param roomId           roomId
     */
    public BuildRoomCommand(final StateMachine stateMachine,
                            final ServerConnection<ActionCommand> serverConnection,
                            final int commId, final int x, final int y,
                            final int roomId) {
        super(stateMachine, serverConnection, commId);
        this.posX = x;
        this.posY = y;
        this.roomId = roomId;
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

        final var playerMaybe = super.getStateMachine().getGameBoard()
                .getDungeonLordByCommId(getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (super.getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.BIDDINGEVALROOMETC) {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "false state");
            return ActionCommandEnum.ACTIONFAILED;
        }

        final var player = playerMaybe.get();

        final var room =
                super.getStateMachine().getGameBoard()
                        .getBiddingSquare().getItemStock().getRoomById(roomId);
        if (room != null) {
            if (player.placeRoom(room, new Point(posX, posY))) {
                super.getStateMachine().getGameBoard().getBiddingSquare().getItemStock()
                        .getShopOfferRooms().remove(room);
                return ActionCommandEnum.BUILDROOM;
            } else {
                super.getServerConnection().sendActionFailed(super.getCommId(),
                        "could not place Room");
                return ActionCommandEnum.ACTIONFAILED;
            }
        } else {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "room not found");
            return ActionCommandEnum.ACTIONFAILED;
        }

    }
}
