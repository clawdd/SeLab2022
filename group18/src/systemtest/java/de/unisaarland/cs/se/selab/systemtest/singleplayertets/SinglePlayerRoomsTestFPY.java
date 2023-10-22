package de.unisaarland.cs.se.selab.systemtest.singleplayertets;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;

public class SinglePlayerRoomsTestFPY extends AbstractSystemTest {
    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);
    private static final int P_1 = 0;
    private static final BidType FOOD = BidType.FOOD;
    private static final BidType NICENESS = BidType.NICENESS;
    private static final BidType TUNNEL = BidType.TUNNEL;
    //private static final BidType TRAP = BidType.TRAP;
    private static final BidType ROOM = BidType.ROOM;
    //private static final BidType MONSTER = BidType.MONSTER;
    //private static final BidType GOLD = BidType.GOLD;
    private static final BidType IMPS = BidType.IMPS;

    public SinglePlayerRoomsTestFPY() {
        super(SinglePlayerRoomsTestFPY.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(SinglePlayerRoomsTestFPY.class, "config_for_single_player"
                + ".json");
    }

    @Override
    protected long createSeed() {
        return SEED;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
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

        sendRegister(1, "Julian");
        assertConfig(1, config);
        assertGameStarted(1);
        assertPlayer(1, "Julian", P_1);
        assertNextYear(1, 1);
        assertNextRound(1, 1);

        assertAdventurersDrawnForXPlayers(1, adventurers);
        assertMonstersDrawnForXPlayers(1, monsters);
        assertRoomDrawnForXPlayers(1, rooms);

        assertBiddingStartedForXPlayers(1);
        firstBiddingSeason();

        assertAdventurersDrawnForXPlayers(1, adventurers);
        assertMonstersDrawnForXPlayers(1, monsters);
        assertRoomDrawnForXPlayers(1, rooms);

        assertBiddingStartedForXPlayers(1);
        secBiddingSeason();

        sendLeave(1);

    }

    protected void firstBiddingSeason() throws TimeoutException, AssertionError {

        assertActNow(1);
        sendPlaceBid(1, TUNNEL, 1);
        assertBidPlacedForXPlayers(1, TUNNEL, P_1, 1);

        assertActNow(1);
        sendPlaceBid(1, ROOM, 2);
        assertBidPlacedForXPlayers(1, ROOM, P_1, 2);

        assertActNow(1);
        sendPlaceBid(1, IMPS, 3);
        assertBidPlacedForXPlayers(1, IMPS, P_1, 3);

        //eval

        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 0, 1);
        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 0, 2);

        assertFoodChanged(1, -1, P_1);
        assertImpsChanged(1, 1, P_1);

        assertGoldChanged(1, -1, P_1);
        assertPlaceRoom(1);
        assertActNow(1);
        sendBuildRoom(1, 1, 0, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendBuildRoom(1, 5, 5, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendBuildRoom(1, 3, 3, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendBuildRoom(1, 0, 0, 11);
        assertRoomBuilt(1, P_1, 11, 0, 0);

        assertBidRetrieved(1, TUNNEL, P_1);

        assertImpsChanged(1, 2, P_1);

        assertAdventurerArrived(1, 27, P_1);

        assertNextRound(1, 2);

    }

    protected void secBiddingSeason() throws TimeoutException, AssertionError {

        assertActNow(1);
        sendPlaceBid(1, TUNNEL, 1);
        assertBidPlaced(1, TUNNEL, P_1, 1);
        assertActNow(1);
        sendPlaceBid(1, IMPS, 2);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        assertBidPlaced(1, NICENESS, P_1, 2);
        assertActNow(1);
        sendActivateRoom(1, 11);
        assertImpsChanged(1, -2, P_1);
        assertRoomActivated(1, P_1, 11);
        assertActNow(1);
        sendPlaceBid(1, FOOD, 3);
        assertBidPlaced(1, FOOD, P_1, 3);

        assertGoldChanged(1, -1, P_1);
        assertFoodChanged(1, 2, P_1);

        assertEvilnessChanged(1, -1, P_1);

        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 3);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 0, 3);
        assertActNow(1);
        sendDigTunnel(1, 0, 4);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 0, 4);

        assertBidRetrieved(1, ROOM, P_1);
        assertBidRetrieved(1, IMPS, P_1);
        assertBidRetrieved(1, TUNNEL, P_1);

        assertImpsChanged(1, 2, P_1);

        assertImpsChanged(1, 2, P_1);

        assertFoodChanged(1, 1, P_1);
    }
}

