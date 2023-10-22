package de.unisaarland.cs.se.selab.systemtest.specifictest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.json.JSONObject;

public class MoreThan4Players extends AbstractSystemTest {

    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);

    public MoreThan4Players() {
        super(MoreThan4Players.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(MoreThan4Players.class, "config_random_created.json");
    }

    @Override
    protected long createSeed() {
        return SEED;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4, 5, 6);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        final var monsters = getAndShuffleStock(
                config, RANDOM, "monsters");
        final var adventurers = getAndShuffleStock(
                config, RANDOM, "adventurers");
        final var traps = getAndShuffleStock(config, RANDOM, "traps");
        final var rooms = getAndShuffleStock(
                config, RANDOM, "rooms");

        registrationXPlayers(6, adventurers, monsters, rooms);
        assertBiddingStartedForXPlayers(6);
        assertActNowForXPlayers(6);

        placingBidsSeason1();
        evaluationBidsSeason1(traps);

        assertBidRetrievedForXPlayers(6, BidType.FOOD, 0);
        assertBidRetrievedForXPlayers(6, BidType.NICENESS, 1);
        assertBidRetrievedForXPlayers(6, BidType.IMPS, 2);
        assertBidRetrievedForXPlayers(6, BidType.MONSTER, 3);
        assertBidRetrievedForXPlayers(6, BidType.TRAP, 4);
        assertBidRetrievedForXPlayers(6, BidType.MONSTER, 5);

        assertAdventurerArrivedForXPlayers(6, 3, 0);
        assertAdventurerArrivedForXPlayers(6, 13, 1);
        assertAdventurerArrivedForXPlayers(6, 14, 3);
        assertAdventurerArrivedForXPlayers(6, 7, 4);
        assertAdventurerArrivedForXPlayers(6, 17, 5);
        assertAdventurerArrivedForXPlayers(6, 11, 2);

        sendLeave(1);
        sendLeave(2);
        sendLeave(3);
        sendLeave(4);
        sendLeave(5);
        sendLeave(6);
    }

    private void placingBidsSeason1() throws TimeoutException {
        sendPlaceBid(1, BidType.IMPS, 4);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(1, BidType.ROOM, 23);
        assertActionFailed(1);
        assertActNow(1);
        sendHireMonster(1, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(6, BidType.IMPS, 0, 3);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 3);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        assertBidPlacedForXPlayers(6, BidType.NICENESS, 0, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(6, BidType.FOOD, 0, 1);
        sendPlaceBid(2, BidType.FOOD, 2);
        assertBidPlacedForXPlayers(6, BidType.FOOD, 1, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.ROOM, 3);
        assertBidPlacedForXPlayers(6, BidType.ROOM, 1, 3);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 1);
        assertBidPlacedForXPlayers(6, BidType.NICENESS, 1, 1);
        sendPlaceBid(3, BidType.FOOD, 3);
        assertBidPlacedForXPlayers(6, BidType.FOOD, 2, 3);
        assertActNow(3);
        sendPlaceBid(3, BidType.ROOM, 2);
        assertBidPlacedForXPlayers(6, BidType.ROOM, 2, 2);
        assertActNow(3);
        sendPlaceBid(3, BidType.IMPS, 1);
        assertBidPlacedForXPlayers(6, BidType.IMPS, 2, 1);
        sendPlaceBid(6, BidType.ROOM, 3);
        assertBidPlacedForXPlayers(6, BidType.ROOM, 5, 3);
        assertActNow(6);
        sendPlaceBid(4, BidType.MONSTER, 1);
        assertBidPlacedForXPlayers(6, BidType.MONSTER, 3, 1);
        assertActNow(4);
        sendPlaceBid(4, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(6, BidType.GOLD, 3, 2);
        assertActNow(4);
        sendPlaceBid(4, BidType.TRAP, 3);
        assertBidPlacedForXPlayers(6, BidType.TRAP, 3, 3);
        sendPlaceBid(6, BidType.MONSTER, 1);
        assertBidPlacedForXPlayers(6, BidType.MONSTER, 5, 1);
        assertActNow(6);
        sendPlaceBid(5, BidType.TRAP, 1);
        assertBidPlacedForXPlayers(6, BidType.TRAP, 4, 1);
        assertActNow(5);
        sendPlaceBid(5, BidType.IMPS, 2);
        assertBidPlacedForXPlayers(6, BidType.IMPS, 4, 2);
        assertActNow(5);
        sendPlaceBid(5, BidType.GOLD, 3);
        assertBidPlacedForXPlayers(6, BidType.GOLD, 4, 3);
        sendPlaceBid(6, BidType.IMPS, 2);
        assertBidPlacedForXPlayers(6, BidType.IMPS, 5, 2);
    }

    private void evaluationBidsSeason1(final List<JSONObject> traps) throws TimeoutException {
        //eval FOOD
        assertGoldChangedForXPlayers(6, -1, 0);
        assertFoodChangedForXPlayers(6, 2, 0);
        assertEvilnessChangedForXPlayers(6, 1, 1);
        assertFoodChangedForXPlayers(6, 3, 1);
        assertEvilnessChangedForXPlayers(6, 2, 2);
        assertFoodChangedForXPlayers(6, 3, 2);
        assertGoldChangedForXPlayers(6, 1, 2);

        //eval NICENESS
        assertEvilnessChangedForXPlayers(6, -1, 1);
        assertEvilnessChangedForXPlayers(6, -2, 0);

        //eval TUNNEL

        //eval GOLD

        //eval IMPS
        assertFoodChangedForXPlayers(6, -1, 2);
        assertImpsChangedForXPlayers(6, 1, 2);
        assertFoodChangedForXPlayers(6, -2, 4);
        assertImpsChangedForXPlayers(6, 2, 4);
        assertFoodChangedForXPlayers(6, -1, 5);
        assertGoldChangedForXPlayers(6, -1, 5);
        assertImpsChangedForXPlayers(6, 2, 5);

        //eval TRAP
        assertGoldChangedForXPlayers(6, -1, 4);
        assertTrapAcquiredForXPlayers(6, 4, traps);
        assertTrapAcquiredForXPlayers(6, 3, traps);

        //eval MONSTER
        assertSelectMonster(4);
        assertActNow(4);
        sendEndTurn(4);
        assertSelectMonster(6);
        assertActNow(6);
        sendEndTurn(6);

        //eval ROOM
        assertGoldChangedForXPlayers(6, -1, 2);
        assertPlaceRoom(3);
        assertActNow(3);
        sendBuildRoom(3, 0, 0, 11);
        assertRoomBuiltForXPlayers(6, 2, 11, 0, 0);
        assertGoldChangedForXPlayers(6, -1, 1);
        assertPlaceRoom(2);
        assertActNow(2);
        sendBuildRoom(2, 0, 0, 6);
        assertRoomBuiltForXPlayers(6, 1, 6, 0, 0);
    }
}
