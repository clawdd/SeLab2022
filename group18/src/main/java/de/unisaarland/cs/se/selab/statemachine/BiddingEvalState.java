package de.unisaarland.cs.se.selab.statemachine;

import static de.unisaarland.cs.se.selab.statemachine.StateEnum.BIDDINGEVAL;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.creatures.ImpTask;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This state handles the evaluation of the bids
 */
public class BiddingEvalState extends State {


    /**
     * BIDDINGEVAL hard-coded enum-value
     *
     * @param stateMachine in order to access the StateMachine and e.g. updateState
     */
    public BiddingEvalState(final StateMachine stateMachine) {
        super(stateMachine, BIDDINGEVAL);
    }

    /**
     * This method returns the DungeonLord to a given Room
     *
     * @param room Room
     * @return DungeonLord
     */
    private DungeonLord getDungeonLordForRoom(final Room room) {
        for (final DungeonLord dl : getStateMachine().getGameBoard().getPlayers()) {
            for (final Room r : dl.getDungeon().getPlayersOwnedRooms()) {
                if (r.getId() == room.getId()) {
                    return dl;
                }
            }
        }
        return null;
    }

    /**
     * pays out the yield to the player
     *
     * @param room room of the yield
     */
    private void payOutRoom(final Room room) {
        final DungeonLord dl = getDungeonLordForRoom(room);
        if (dl == null) {
            return;
        }
        dl.changeImps(room.getActivationCost(), ImpTask.REST);
        if (room.getFood() > 0) {
            dl.changeFood(room.getFood());
        }
        if (room.getNiceness() > 0) {
            if (dl.checkEvilness(-room.getNiceness())) {
                dl.changeEvilness(-room.getNiceness());
            }
        }
        if (room.getGold() > 0) {
            dl.changeGold(room.getGold());
        }
        if (room.getImps() > 0) {
            dl.employImps(room.getImps());
        }
    }

    /**
     * concrete implementation of run in this state: - wait for nextAction - - Events and Actions: -
     * invoke execute-method
     */
    @Override
    public void run() throws TimeoutException {
        if (checkAllPlayersLeftAndUpdateState()) {
            return;
        }

        final var gameBoard = super.getStateMachine().getGameBoard();

        // evaluate Bids
        gameBoard.getBiddingSquare().evaluateBids(gameBoard.getCurrentPlayer(),
                gameBoard.getPlayers(), this);

        // retrieve Bids
        gameBoard.getPlayers().forEach(d -> {
            if (d.getFirstLockedBidType() != null) {
                gameBoard.getPlayers().forEach(p -> super.getStateMachine().getSc()
                        .sendBidRetrieved(p.getCommId(), d.getFirstLockedBidType(), d.getId()));
            }
            if (d.getSecondLockedBidType() != null) {
                gameBoard.getPlayers().forEach(p -> super.getStateMachine().getSc()
                        .sendBidRetrieved(p.getCommId(), d.getSecondLockedBidType(), d.getId()));
            }
            if (d.getFirstPlacedBidType() != null) {
                gameBoard.getPlayers().forEach(p -> super.getStateMachine().getSc()
                        .sendBidRetrieved(p.getCommId(), d.getFirstPlacedBidType(), d.getId()));
            }
        });

        // lock new Bids
        gameBoard.getPlayers().forEach(d ->
                gameBoard.getBiddingSquare().getBiddingInboxQueue()
                        .stream().filter(b -> b.getPlayer() == d && b.getNthBid() == 2)
                        .findFirst().ifPresent(value ->
                                d.setFirstLockedBidType(value.getBidType())));

        gameBoard.getPlayers().forEach(d ->
                gameBoard.getBiddingSquare().getBiddingInboxQueue()
                        .stream().filter(b -> b.getPlayer() == d && b.getNthBid() == 3)
                        .findFirst().ifPresent(value ->
                                d.setSecondLockedBidType(value.getBidType())));

        // evaluate Imps
        gameBoard.getPlayers().forEach(DungeonLord::evalImps);

        // evaluate Rooms
        final List<Room> roomsToEvaluate = new ArrayList<>();
        gameBoard.getPlayers().forEach(d ->
                roomsToEvaluate.addAll(d.getDungeon().getPlayersOwnedRooms().stream()
                        .filter(Room::isActive).toList()));
        final List<Room> evalList =
                roomsToEvaluate.stream().sorted(Comparator.comparing(Room::getId)).toList();
        evalList.forEach(this::payOutRoom);
        //deactivate rooms
        roomsToEvaluate.forEach(Room::deactivate);

        gameBoard.getBiddingSquare().getBiddingInboxQueue().clear();

        // check for season 4; sets respective state (BattleGround or BiddingPlace)
        if (gameBoard.getCurrentSeason() == 4) {
            stateMachine.updateState(new SetBattleGroundState(stateMachine));
            super.getStateMachine().getGameBoard().setCurrentSeason(0);
            stateMachine.updateCombatPlayerStack();
        } else {

            gameBoard.getBiddingSquare().getItemStock().assignAdventurer(gameBoard.getPlayers(),
                    stateMachine.getSc());

            stateMachine.updateState(new BiddingPlaceState(stateMachine));
        }
    }
}
