package de.unisaarland.cs.se.selab.systemtest.biddingtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.RegistrationTest;
import java.util.Random;
import java.util.Set;

public class EvaluationFoodFor3PlayersHCP extends AbstractSystemTest {

    private static final long SEED = 42;
    private static final Random RANDOM = new Random(SEED);

    private static final int P_1 = 0;
    private static final int P_2 = 1;
    private static final int P_3 = 2;


    public EvaluationFoodFor3PlayersHCP() {
        super(EvaluationFoodFor3PlayersHCP.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();
        // Registration and GameStart
        sendRegister(1, "Lewis");
        assertConfig(1, config);
        sendRegister(2, "Max");
        assertConfig(2, config);
        sendRegister(3, "George");
        assertConfig(3, config);
        sendStartGame(1);
        // Broadcast: GameStarted
        for (int i = 1; i < 4; i++) {
            assertGameStarted(i);
        }
        // Broadcast: Player
        for (int i = 1; i < 4; i++) {
            assertPlayer(i, "Lewis", P_1);
            assertPlayer(i, "Max", P_2);
            assertPlayer(i, "George", P_3);
        }
        // Broadcast: NextYear
        for (int i = 1; i < 4; i++) {
            assertNextYear(i, 1);
        }
        // Broadcast: NextSeason
        for (int i = 1; i < 4; i++) {
            assertNextRound(i, 1);
        }
        final var monsters = getAndShuffleStock(
                config, RANDOM, "monsters");
        final var adventurers = getAndShuffleStock(
                config, RANDOM, "adventurers");
        getAndShuffleStock(
                config, RANDOM, "traps");
        final var rooms = getAndShuffleStock(
                config, RANDOM, "rooms");
        // Broadcast: Creatures Drawn
        assertAdventurersDrawnForXPlayers(3, adventurers);
        assertMonstersDrawnForXPlayers(3, monsters);
        assertRoomDrawnForXPlayers(3, rooms);
        // Broadcast: BiddingStarted and ActNow
        assertBiddingStartedForXPlayers(3);
        for (int i = 1; i < 4; i++) {
            assertActNow(i);
        }
        /* Placing Bids */
        // 1st Bid
        sendPlaceBid(1, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(3, BidType.FOOD, P_1, 1);
        assertActNow(1);
        sendPlaceBid(2, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(3, BidType.FOOD, P_2, 1);
        assertActNow(2);
        sendPlaceBid(3, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(3, BidType.FOOD, P_3, 1);
        assertActNow(3);
        // 2nd bid
        sendPlaceBid(1, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(3, BidType.GOLD, P_1, 2);
        assertActNow(1);
        sendPlaceBid(2, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(3, BidType.GOLD, P_2, 2);
        assertActNow(2);
        sendPlaceBid(3, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(3, BidType.GOLD, P_3, 2);
        assertActNow(3);
        // 3rd bid
        sendPlaceBid(1, BidType.NICENESS, 3);
        assertBidPlacedForXPlayers(3, BidType.NICENESS, P_1, 3);
        sendPlaceBid(2, BidType.NICENESS, 3);
        assertBidPlacedForXPlayers(3, BidType.NICENESS, P_2, 3);
        sendPlaceBid(3, BidType.NICENESS, 3);
        assertBidPlacedForXPlayers(3, BidType.NICENESS, P_3, 3);

        evaluateBids();
        for (int i = 1; i < 4; i++) {
            sendLeave(i);
        }
    }

    private void evaluateBids() throws TimeoutException, AssertionError {
        // Evaluate Bids
        assertGoldChangedForXPlayers(3, -1, P_1);
        assertFoodChangedForXPlayers(3, 2, P_1);
        assertEvilnessChangedForXPlayers(3, 1, P_2);
        assertFoodChangedForXPlayers(3, 3, P_2);
        assertEvilnessChangedForXPlayers(3, 2, P_3);
        assertFoodChangedForXPlayers(3, 3, P_3);
        assertGoldChangedForXPlayers(3, 1, P_3);
        assertEvilnessChangedForXPlayers(3, -1, P_1);
        assertEvilnessChangedForXPlayers(3, -2, P_2);
        assertGoldChangedForXPlayers(3, -1, P_3);
        assertEvilnessChangedForXPlayers(3, -2, P_3);
        assertImpsChangedForXPlayers(3, -1, P_1);
        assertImpsChangedForXPlayers(3, -1, P_2);
        assertImpsChangedForXPlayers(3, -1, P_3);
        assertBidRetrievedForXPlayers(3, BidType.FOOD, P_1);
        assertBidRetrievedForXPlayers(3, BidType.FOOD, P_2);
        assertBidRetrievedForXPlayers(3, BidType.FOOD, P_3);
        assertImpsChangedForXPlayers(3, 1, P_1);
        assertGoldChangedForXPlayers(3, 1, P_1);
        assertImpsChangedForXPlayers(3, 1, P_2);
        assertGoldChangedForXPlayers(3, 1, P_2);
        assertImpsChangedForXPlayers(3, 1, P_3);
        assertGoldChangedForXPlayers(3, 1, P_3);
    }
}
