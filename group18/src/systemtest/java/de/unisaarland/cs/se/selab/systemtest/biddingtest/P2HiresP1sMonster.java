package de.unisaarland.cs.se.selab.systemtest.biddingtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class P2HiresP1sMonster extends AbstractSystemTest {
    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);

    private static final int P_1 = 0;
    private static final int P_2 = 1;

    public P2HiresP1sMonster() {
        super(P2HiresP1sMonster.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(NormalBiddingTest.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return SEED;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2);
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
        final List<String> playerList = registerXPlayer(2, config);

        sendStartGame(1);
        assertGameStartedForXPlayers(2);
        assertPlayerForXPlayers(2, playerList);
        assertNextYearForXPlayers(2, 1);
        assertNextSeasonForXPlayers(2, 1);
        assertAdventurersDrawnForXPlayers(2, adventurers);
        assertMonstersDrawnForXPlayers(2, monsters);
        assertRoomDrawnForXPlayers(2, rooms);

        assertBiddingStartedForXPlayers(2);
        assertActNowForXPlayers(2);

        sendPlaceBid(1, BidType.MONSTER, 1);
        assertBidPlacedForXPlayers(2, BidType.MONSTER, P_1, 1);
        assertActNow(1);
        sendPlaceBid(2, BidType.NICENESS, 1);
        assertBidPlacedForXPlayers(2, BidType.NICENESS, P_2, 1);
        assertActNow(2);

        sendPlaceBid(1, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(2, BidType.GOLD, P_1, 2);
        assertActNow(1);
        sendPlaceBid(2, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(2, BidType.GOLD, P_2, 2);
        assertActNow(2);

        sendPlaceBid(1, BidType.FOOD, 3);
        assertBidPlacedForXPlayers(2, BidType.FOOD, P_1, 3);
        sendPlaceBid(2, BidType.FOOD, 3);
        assertBidPlacedForXPlayers(2, BidType.FOOD, P_2, 3);

        assertGoldChangedForXPlayers(2, -1, P_1);
        assertFoodChangedForXPlayers(2, 2, P_1);
        assertEvilnessChangedForXPlayers(2, 1, P_2);
        assertFoodChangedForXPlayers(2, 3, P_2);

        assertEvilnessChangedForXPlayers(2, -1, P_2);

        assertImpsChangedForXPlayers(2, -1, P_1);
        assertImpsChangedForXPlayers(2, -1, P_2);

        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(2, 1);
        assertEvilnessChangedForXPlayers(2, 3, P_2);

        sendLeave(1);
        sendLeave(2);
    }
}
