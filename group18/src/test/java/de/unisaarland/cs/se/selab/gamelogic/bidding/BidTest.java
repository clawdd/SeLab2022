package de.unisaarland.cs.se.selab.gamelogic.bidding;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.gamelogic.Bid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BidTest {

    @Test
    void testBidTypes1() {

        final Bid bid = new Bid(BidType.NICENESS, 0, null);
        Assertions.assertEquals(BidType.NICENESS, bid.getBidType());
    }

    @Test
    void testBidTypes2() {

        final Bid bid = new Bid(BidType.FOOD, 0, null);
        Assertions.assertEquals(BidType.FOOD, bid.getBidType());
    }

    @Test
    void testBidTypes3() {

        final Bid bid = new Bid(BidType.GOLD, 0, null);
        Assertions.assertEquals(BidType.GOLD, bid.getBidType());
    }


    @Test
    void testBidTypes4() {

        final Bid bid = new Bid(BidType.IMPS, 0, null);
        Assertions.assertEquals(BidType.IMPS, bid.getBidType());
    }


    @Test
    void testBidTypes5() {

        final Bid bid = new Bid(BidType.TRAP, 0, null);
        Assertions.assertEquals(BidType.TRAP, bid.getBidType());
    }


    @Test
    void testBidTypes6() {

        final Bid bid = new Bid(BidType.TUNNEL, 0, null);
        Assertions.assertEquals(BidType.TUNNEL, bid.getBidType());
    }


    @Test
    void testBidTypes7() {

        final Bid bid = new Bid(BidType.MONSTER, 0, null);
        Assertions.assertEquals(BidType.MONSTER, bid.getBidType());
    }


    @Test
    void testBidTypes8() {

        final Bid bid = new Bid(BidType.ROOM, 0, null);
        Assertions.assertEquals(BidType.ROOM, bid.getBidType());
    }

    @Test
    void testNthBid() {
        final Bid bid = new Bid(BidType.NICENESS, 0, null);
        Assertions.assertEquals(0, bid.getNthBid());
    }

    @Test
    void testNthBid2() {
        final Bid bid = new Bid(BidType.NICENESS, 4, null);
        Assertions.assertEquals(4, bid.getNthBid());
    }

    @Test
    void playerTest() {
        final Bid bid = new Bid(BidType.NICENESS, 4, null);
        Assertions.assertEquals(null, bid.getPlayer());
    }

}
