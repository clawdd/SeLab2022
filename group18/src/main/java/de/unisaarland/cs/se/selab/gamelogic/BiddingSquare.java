package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class handles the bids
 */
public class BiddingSquare {

    private final ServerConnection<ActionCommand> sc;
    private final List<Bid> biddingInboxQueue;
    private final ItemStock itemStock;
    private final GameBoard gameBoard;

    /**
     * The constructor of the Bidding Square
     *
     * @param sc        the server connection handling events
     * @param gameBoard GameBoard
     */
    public BiddingSquare(final ServerConnection<ActionCommand> sc, final GameBoard gameBoard) {
        this.sc = sc;
        this.biddingInboxQueue = new ArrayList<>();
        this.itemStock = new ItemStock();
        this.gameBoard = gameBoard;
    }

    /**
     * This method evaluates all placed bids
     *
     * @param currentPlayer currentPlayer
     * @param players       players
     * @param currentState  State
     */
    public void evaluateBids(final DungeonLord currentPlayer,
                             final List<DungeonLord> players, final State currentState) {
        if (!biddingInboxQueue.isEmpty()) {
            final List<Bid> foodList = new ArrayList<>();
            final List<Bid> nicenessList = new ArrayList<>();
            final List<Bid> tunnelList = new ArrayList<>();
            final List<Bid> goldList = new ArrayList<>();
            final List<Bid> impList = new ArrayList<>();
            final List<Bid> trapList = new ArrayList<>();
            final List<Bid> monsterList = new ArrayList<>();
            final List<Bid> roomList = new ArrayList<>();
            final List<Bid> firstBids = biddingInboxQueue.stream()
                    .filter(b -> b.getNthBid() == 1).toList();
            final List<Bid> secondBids = biddingInboxQueue.stream()
                    .filter(b -> b.getNthBid() == 2).toList();
            final List<Bid> thirdBids = biddingInboxQueue.stream()
                    .filter(b -> b.getNthBid() == 3).toList();

            final List<List<Bid>> allbids = placeBidingSquareSlot(players, currentPlayer, foodList,
                    nicenessList, tunnelList,
                    goldList,
                    impList, trapList,
                    monsterList, roomList, firstBids, secondBids, thirdBids);

            evaluateAllBidsActually(allbids, players, currentState);
        }
    }

    /**
     * This method evaluates all actually bids.
     *
     * @param allBids allBids
     * @param players players
     */
    private void evaluateAllBidsActually(final List<List<Bid>> allBids,
                                         final List<DungeonLord> players,
                                         final State currentState) {
        final List<Bid> reallyAllBids = allBids.stream().flatMap(Collection::stream).toList();
        for (final Bid bid : reallyAllBids) {
            final var player = bid.getPlayer();
            if (!gameBoard.playerIsInTheGame(player)) {
                continue;
            }
            bid.eval(itemStock, sc, players, gameBoard, currentState);
        }
    }

    /**
     * sets the biddingsquare slot in all bids
     *
     * @param foodList     foodlist
     * @param nicenessList nicenessList
     * @param tunnelList   tunnelList
     * @param goldList     goldList
     * @param impList      impList
     * @param trapList     trapList
     * @param monsterList  monsterList
     * @param roomList     roomList
     */
    private List<List<Bid>> placeBidingSquareSlot(final List<DungeonLord> players,
                                                  final DungeonLord currentPlayer,
                                                  final List<Bid> foodList,
                                                  final List<Bid> nicenessList,
                                                  final List<Bid> tunnelList,
                                                  final List<Bid> goldList,
                                                  final List<Bid> impList,
                                                  final List<Bid> trapList,
                                                  final List<Bid> monsterList,
                                                  final List<Bid> roomList,
                                                  final List<Bid> firstBids,
                                                  final List<Bid> secondBids,
                                                  final List<Bid> thirdBids) {

        final List<List<Bid>> allThreeLists = List.of(firstBids, secondBids, thirdBids);
        allThreeLists.forEach(listBids -> {
            int i = players.indexOf(currentPlayer);
            do {
                final int finalI = i;
                listBids.forEach(b -> {
                    if (b.getPlayer().getId() == players.get(finalI).getId()) {
                        switch (b.getBidType()) {
                            case FOOD -> foodList.add(b);
                            case NICENESS -> nicenessList.add(b);
                            case TUNNEL -> tunnelList.add(b);
                            case GOLD -> goldList.add(b);
                            case IMPS -> impList.add(b);
                            case TRAP -> trapList.add(b);
                            case MONSTER -> monsterList.add(b);
                            case ROOM -> roomList.add(b);
                            default -> {
                            }
                        }
                    }
                });
                i = (i + 1) % players.size();
            } while (i != players.indexOf(currentPlayer));
        });
        final List<List<Bid>> allBidTypeLists = List.of(foodList, nicenessList, tunnelList,
                goldList, impList, trapList, monsterList, roomList);
        setBiddingSquareSlotInBids(allBidTypeLists);
        return allBidTypeLists;
    }

    /**
     * set biddingSquare slot in bids
     *
     * @param allBidTypeLists allBidTypeLists
     */
    private static void setBiddingSquareSlotInBids(final List<List<Bid>> allBidTypeLists) {
        allBidTypeLists.forEach(list -> {
            for (int i = 0; i < list.size(); i++) {
                if (i < 3) {
                    list.get(i).setBiddingSquareSlot(i + 1);
                }
            }
        });
    }

    /**
     * This method adds a bid to the Bidding Square
     *
     * @param bid this is the bid to be added
     */
    public void addBid(final Bid bid) {
        biddingInboxQueue.add(bid);
    }

    /**
     * Get ItemStock
     *
     * @return itemStock
     */
    public ItemStock getItemStock() {
        return itemStock;
    }

    /**
     * getter of Bidding inbox
     *
     * @return list
     */
    public List<Bid> getBiddingInboxQueue() {
        return biddingInboxQueue;
    }
}
