package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.Bid;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;

/**
 * This Action places a Bid
 */
public class PlaceBidCommand extends ActionCommand {

    private final BidType bidType;
    private final int slot;

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     * @param bidType          type of bid
     * @param slot             number of player's bid
     */
    public PlaceBidCommand(final StateMachine stateMachine,
                           final ServerConnection<ActionCommand> serverConnection,
                           final int commId, final BidType bidType, final int slot) {
        super(stateMachine, serverConnection, commId);
        this.bidType = bidType;
        this.slot = slot;
    }

    /**
     * Checks whether the bidding slot of the player is still free
     *
     * @param dl   player
     * @param slot slot
     * @return true if it's free, else false
     */
    private boolean checkIfSlotIsFree(final DungeonLord dl, final int slot) {
        return switch (slot) {
            case 1 -> dl.getFirstPlacedBidType() == null;
            case 2 -> dl.getSecondPlacedBidType() == null;
            case 3 -> dl.getThirdPlacedBidType() == null;
            default -> false;
        };
    }

    /**
     * This method checks if a players bid violates the BidType of his other bids
     *
     * @return true if everything is alright
     */
    private boolean checkIfBidTypeIsNotLockedOrUsed(final DungeonLord dl, final BidType bidType) {
        boolean temp = true;
        if (dl.getFirstLockedBidType() != null && dl.getSecondLockedBidType() != null) {
            temp = bidType != dl.getFirstLockedBidType() && bidType != dl.getSecondLockedBidType();
        }
        if (dl.getFirstPlacedBidType() != null) {
            temp = dl.getFirstPlacedBidType() != bidType && temp;
        }
        if (dl.getSecondPlacedBidType() != null) {
            temp = dl.getSecondPlacedBidType() != bidType && temp;
        }
        if (dl.getThirdPlacedBidType() != null) {
            temp = dl.getThirdPlacedBidType() != bidType && temp;
        }
        return temp;
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

        final var playerMaybe = super.getStateMachine()
                .getGameBoard().getDungeonLordByCommId(super.getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        // check for correct State, here BIDDINGPLACE
        if (super.getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.BIDDINGPLACE) {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "You're in the wrong " + "state");
            return ActionCommandEnum.ACTIONFAILED;
        }

        final var player = playerMaybe.get();


        // check whether bid is valid (or locked)
        if (!checkIfBidTypeIsNotLockedOrUsed(player, bidType)) {
            getServerConnection().sendActionFailed(getCommId(), "Bid is locked!");
            return ActionCommandEnum.ACTIONFAILED;
        }

        // check whether bid slot is free
        if (!checkIfSlotIsFree(player, slot)) {
            getServerConnection().sendActionFailed(getCommId(), "Bid slot is already done!");
            return ActionCommandEnum.ACTIONFAILED;
        }

        switch (slot) {
            case 1 -> player.setFirstPlacedBidType(bidType);
            case 2 -> player.setSecondPlacedBidType(bidType);
            case 3 -> player.setThirdPlacedBidType(bidType);
            default -> {
                getServerConnection().sendActionFailed(getCommId(), "false slot number!");
                return ActionCommandEnum.ACTIONFAILED;
            }
        }
        final Bid bid = new Bid(bidType, slot, player);

        super.getStateMachine().getGameBoard().getBiddingSquare().addBid(bid);

        // Broadcast event BidPlaced
        getStateMachine().getGameBoard().getPlayers().forEach(p ->
                getServerConnection().sendBidPlaced(
                        p.getCommId(), bid.getBidType(),
                        bid.getPlayer().getId(), bid.getNthBid()));

        return ActionCommandEnum.PLACEBID;
    }
}
