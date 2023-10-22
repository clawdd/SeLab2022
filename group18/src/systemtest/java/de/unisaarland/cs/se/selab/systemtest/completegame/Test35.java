package de.unisaarland.cs.se.selab.systemtest.completegame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;

public class Test35 extends AbstractSystemTest {

    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);

    public Test35() {
        super(Test35.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(Test35.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return SEED;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    private void firstBiddingSeason() throws TimeoutException {
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
        sendPlaceBid(3, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 2, 1);
        assertActNow(3);
        sendPlaceBid(3, BidType.NICENESS, 2);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 2, 2);
        assertActNow(3);
        sendPlaceBid(3, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 2, 3);
        sendPlaceBid(4, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 3, 1);
        assertActNow(4);
        sendPlaceBid(4, BidType.NICENESS, 2);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 3, 2);
        assertActNow(4);
        sendPlaceBid(4, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 3, 3);

        assertGoldChangedForXPlayers(4, -1, 0);
        assertFoodChangedForXPlayers(4, 2, 0);
        assertEvilnessChangedForXPlayers(4, 1, 1);
        assertFoodChangedForXPlayers(4, 3, 1);
        assertEvilnessChangedForXPlayers(4, 2, 2);
        assertFoodChangedForXPlayers(4, 3, 2);
        assertGoldChangedForXPlayers(4, 1, 2);

        assertEvilnessChangedForXPlayers(4, -1, 0);
        assertEvilnessChangedForXPlayers(4, -2, 1);
        assertGoldChangedForXPlayers(4, -1, 2);
        assertEvilnessChangedForXPlayers(4, -2, 2);

        assertFoodChangedForXPlayers(4, -1, 0);
        assertImpsChangedForXPlayers(4, 1, 0);
        assertFoodChangedForXPlayers(4, -2, 1);
        assertImpsChangedForXPlayers(4, 2, 1);
        assertFoodChangedForXPlayers(4, -1, 2);
        assertGoldChangedForXPlayers(4, -1, 2);
        assertImpsChangedForXPlayers(4, 2, 2);

        assertBidRetrievedForXPlayers(4, BidType.FOOD, 0);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 1);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 2);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 3);

        assertAdventurerArrivedForXPlayers(4, 1, 0);
        assertAdventurerArrivedForXPlayers(4, 27, 1);
        assertAdventurerArrivedForXPlayers(4, 12, 2);
        assertAdventurerArrivedForXPlayers(4, 29, 3);
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

        firstBiddingSeason();

        assertNextSeasonForXPlayers(4, 2);
        assertAdventurersDrawnForXPlayers(4, adventurers);
        assertMonstersDrawnForXPlayers(4, monsters);
        assertRoomDrawnForXPlayers(4, rooms);
        assertBiddingStartedForXPlayers(4);
        assertActNowForXPlayers(4);

        sendPlaceBid(1, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 0, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 2);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(1, BidType.TRAP, 2);
        assertBidPlacedForXPlayers(4, BidType.TRAP, 0, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.MONSTER, 3);
        assertBidPlacedForXPlayers(4, BidType.MONSTER, 0, 3);

        sendLeave(1);
        sendLeave(2);
        sendLeave(3);
        sendLeave(4);

    }


}
