package de.unisaarland.cs.se.selab.statemachine;

import static de.unisaarland.cs.se.selab.statemachine.StateEnum.BIDDINGPLACE;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionCommandEnum;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.List;

/**
 * This state handles the placing of the bids
 */
public class BiddingPlaceState extends State {

    /**
     * BIDDINGPLACE hard-coded enum-value
     *
     * @param stateMachine in order to access the StateMachine and e.g. updateState
     */
    public BiddingPlaceState(final StateMachine stateMachine) {
        super(stateMachine, BIDDINGPLACE);
    }

    /**
     * concrete implementation of run in this state: - wait for nextAction - - Events and Actions:
     * Adventurer, Monsters and Rooms drawn; biddingStarted() --> placeBid(), BidPlaced(), actNow()
     * --> activateRoom possible; - invoke execute-method in PlaceBid to let players place their
     * bids (3 per player per season) - invoke execute-method in ActivateRoom (1 per actNow()
     * possible) - invoke execute-method in EndTurn
     */
    @Override
    public void run() throws TimeoutException {
        if (checkAllPlayersLeftAndUpdateState()) {
            return;
        }

        final var gameBoard = super.getStateMachine().getGameBoard();
        if (gameBoard.getCurrentSeason() == 0) {
            gameBoard.nextYear();
            if (gameBoard.getCurrentYear() > 1) {
                // send events bidRetrieved (broadcast)
                gameBoard.getPlayers().forEach(p ->
                        gameBoard.getPlayers().forEach(pp -> {
                            getStateMachine().getSc().sendBidRetrieved(
                                    pp.getCommId(), p.getFirstLockedBidType(), p.getId());
                            getStateMachine().getSc().sendBidRetrieved(
                                    pp.getCommId(), p.getSecondLockedBidType(), p.getId());
                        })
                );

                // unlock bids of all players
                gameBoard.getPlayers().forEach(d -> d.setFirstLockedBidType(null));
                gameBoard.getPlayers().forEach(d -> d.setSecondLockedBidType(null));
            }
        }
        gameBoard.nextSeason();


        // draw Adventurers, Monsters and Rooms
        if (!gameBoard.isLastSeason()) { // only in season 1 to 3
            gameBoard.getBiddingSquare().getItemStock()
                    .drawAdventurer(super.getStateMachine().getSc(), gameBoard.getPlayers());
        }
        gameBoard.getBiddingSquare().getItemStock()
                .updateMonster(super.getStateMachine().getSc(), gameBoard.getPlayers());
        gameBoard.getBiddingSquare().getItemStock()
                .updateRooms(super.getStateMachine().getSc(), gameBoard.getPlayers());

        // Bidding started
        gameBoard.getPlayers().forEach(p -> {
            p.setFirstPlacedBidType(null);
            p.setSecondPlacedBidType(null);
            p.setThirdPlacedBidType(null);
        });
        gameBoard.getPlayers().forEach(p ->
                super.stateMachine.getSc().sendBiddingStarted(p.getCommId()));
        gameBoard.getPlayers().forEach(p ->
                super.stateMachine.getSc().sendActNow(p.getCommId()));

        // while until all bids have been received
        while (!isLastBidOfAllPlayers()) {
            final ActionCommand nextCommandClient = stateMachine.getSc().nextAction();
            final List<Integer> listOfCommIds = getCommIdOfBiddingPlayers();
            final ActionCommandEnum returnValueEnum = nextCommandClient.execute(listOfCommIds);
            if (returnValueEnum != ActionCommandEnum.PLACEBID
                    && checkAllPlayersLeftAndUpdateState()) {
                return;
            }
            if (!isLastBidOfPlayer(nextCommandClient.getCommId())) {
                super.getStateMachine().getSc()
                        .sendActNow(nextCommandClient.getCommId());
            }
        }
        // update StateMachine
        stateMachine.updateState(new BiddingEvalState(stateMachine));

    }
}
