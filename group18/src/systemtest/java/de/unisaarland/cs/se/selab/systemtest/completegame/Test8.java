package de.unisaarland.cs.se.selab.systemtest.completegame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;

public class Test8 extends AbstractSystemTest {

    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);

    public Test8() {
        super(Test8.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(Test8.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return SEED;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();
        final var monsters = getAndShuffleStock(
                config, RANDOM, "monsters");
        final var adventurers = getAndShuffleStock(
                config, RANDOM, "adventurers");
        getAndShuffleStock(
                config, RANDOM, "traps");
        final var rooms = getAndShuffleStock(
                config, RANDOM, "rooms");
        registrationXPlayers(4, adventurers, monsters, rooms);
        assertBiddingStartedForXPlayers(4);
        assertActNowForXPlayers(4);

        sendPlaceBid(1, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 0, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 0, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 0, 3);
        sendPlaceBid(2, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 1, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 2);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 1, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 1, 3);

        sendLeave(1);
        sendLeave(2);
        sendLeave(3);
        sendLeave(4);

    }


}
