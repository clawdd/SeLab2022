package de.unisaarland.cs.se.selab.gamelogic.biddingsquare;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.Bid;
import de.unisaarland.cs.se.selab.gamelogic.BiddingSquare;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BiddingSquareTest {

    static final ActionFactory<ActionCommand> AF = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(3214, -1, AF);

    @Test
    void testAddBid() {
        final List<DungeonLord> players = new ArrayList<>();
        final DungeonLord player = new DungeonLord("Martin", 1, 124,
                6, 3, 4, 2, 3, SC, players);
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        final Bid b = new Bid(BidType.FOOD, 1, player);
        final BiddingSquare biddingSquare = new BiddingSquare(SC, gameBoard);
        biddingSquare.addBid(b);
        gameBoard.getBiddingSquare().addBid(b);
        Assertions.assertEquals(gameBoard.getBiddingSquare().getBiddingInboxQueue(),
                biddingSquare.getBiddingInboxQueue());
    }

    @Test
    void testGetItemStock() {
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        final BiddingSquare biddingSquare = new BiddingSquare(SC, gameBoard);
        Assertions.assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllAdventurer(),
                biddingSquare.getItemStock().getAllAdventurer());
        Assertions.assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllTraps(),
                biddingSquare.getItemStock().getAllTraps());
        Assertions.assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllRooms(),
                biddingSquare.getItemStock().getAllRooms());
        Assertions.assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllMonsters(),
                biddingSquare.getItemStock().getAllMonsters());
        Assertions.assertEquals(gameBoard.getBiddingSquare().getItemStock()
                        .getTownSquareAdventurer(),
                biddingSquare.getItemStock().getTownSquareAdventurer());
        Assertions.assertEquals(gameBoard.getBiddingSquare().getItemStock().getShopOfferMonsters(),
                biddingSquare.getItemStock().getShopOfferMonsters());
        Assertions.assertEquals(gameBoard.getBiddingSquare().getItemStock().getShopOfferRooms(),
                biddingSquare.getItemStock().getShopOfferRooms());
    }

    @Test
    void testGetBiddingInboxQueue() {
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        final BiddingSquare biddingSquare = new BiddingSquare(SC, gameBoard);
        Assertions.assertEquals(gameBoard.getBiddingSquare().getBiddingInboxQueue(),
                biddingSquare.getBiddingInboxQueue());
    }
}