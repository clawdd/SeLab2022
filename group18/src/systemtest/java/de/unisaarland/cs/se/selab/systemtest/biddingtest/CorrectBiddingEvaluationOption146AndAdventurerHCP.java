package de.unisaarland.cs.se.selab.systemtest.biddingtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;

public class CorrectBiddingEvaluationOption146AndAdventurerHCP extends AbstractSystemTest {

    private static final long SEED = 42;
    private static final Random RANDOM = new Random(SEED);

    public CorrectBiddingEvaluationOption146AndAdventurerHCP() {
        super(CorrectBiddingEvaluationOption146AndAdventurerHCP.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return SEED;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        // Registration and GameStart
        register3Players();

        // Broadcast: GameStarted
        for (int i = 1; i < 4; i++) {
            assertGameStarted(i);
        }

        // Broadcast: Player
        for (int i = 1; i < 4; i++) {
            assertPlayer(i, "Amanda", 0);
            assertPlayer(i, "Jane", 1);
            assertPlayer(i, "Hilary", 2);
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
        final var traps = getAndShuffleStock(
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

        make3BidsFor3Players(BidType.FOOD, BidType.GOLD, BidType.TRAP);

        // Evaluation Food
        assertGoldChangedForXPlayers(3, -1, 0); // 1st slot
        assertFoodChangedForXPlayers(3, 2, 0);
        assertEvilnessChangedForXPlayers(3, 1, 1); // 2nd slot
        assertFoodChangedForXPlayers(3, 3, 1);
        assertEvilnessChangedForXPlayers(3, 2, 2); // 3rd slot
        assertFoodChangedForXPlayers(3, 3, 2);
        assertGoldChangedForXPlayers(3, 1, 2);

        // Evaluation GOLD
        assertImpsChangedForXPlayers(3, -1, 0);
        assertImpsChangedForXPlayers(3, -1, 1);
        assertImpsChangedForXPlayers(3, -1, 2);

        // Evaluation Traps
        assertGoldChangedForXPlayers(3, -1, 0);     // 1st slot
        assertTrapAcquiredForXPlayers(3, 0, traps);
        assertTrapAcquiredForXPlayers(3, 1, traps);     // 2nd slot
        assertGoldChangedForXPlayers(3, -2, 2);     // 3rd slot
        assertTrapAcquiredForXPlayers(3, 2, traps);
        assertTrapAcquiredForXPlayers(3, 2, traps);

        // retrieve Bids
        assertBidRetrievedForXPlayers(3, BidType.FOOD, 0);
        assertBidRetrievedForXPlayers(3, BidType.FOOD, 1);
        assertBidRetrievedForXPlayers(3, BidType.FOOD, 2);

        // Imps return
        assertImpsChangedForXPlayers(3, 1, 0);
        assertGoldChangedForXPlayers(3, 1, 0);
        assertImpsChangedForXPlayers(3, 1, 1);
        assertGoldChangedForXPlayers(3, 1, 1);
        assertImpsChangedForXPlayers(3, 1, 2);
        assertGoldChangedForXPlayers(3, 1, 2);

        for (int i = 1; i < 4; i++) {
            sendLeave(i);
        }
    }

    public void register3Players() throws TimeoutException {
        final String config = createConfig();
        sendRegister(1, "Amanda");
        assertConfig(1, config);

        sendRegister(2, "Jane");
        assertConfig(2, config);

        sendRegister(3, "Hilary");
        assertConfig(3, config);

        sendStartGame(3);
    }

    public void make3BidsFor3Players(final BidType fstBid,
                                     final BidType sndBid,
                                     final BidType thirdBid
    ) throws TimeoutException {
        /* Placing Bids */
        // 1st Bid
        sendPlaceBid(1, fstBid, 1);
        assertBidPlacedForXPlayers(3, fstBid, 0, 1);
        assertActNow(1);

        sendPlaceBid(2, fstBid, 1);
        assertBidPlacedForXPlayers(3, fstBid, 1, 1);
        assertActNow(2);

        sendPlaceBid(3, fstBid, 1);
        assertBidPlacedForXPlayers(3, fstBid, 2, 1);
        assertActNow(3);

        // 2nd bid
        sendPlaceBid(1, sndBid, 2);
        assertBidPlacedForXPlayers(3, sndBid, 0, 2);
        assertActNow(1);

        sendPlaceBid(2, sndBid, 2);
        assertBidPlacedForXPlayers(3, sndBid, 1, 2);
        assertActNow(2);

        sendPlaceBid(3, sndBid, 2);
        assertBidPlacedForXPlayers(3, sndBid, 2, 2);
        assertActNow(3);

        // 3rd bid
        sendPlaceBid(1, thirdBid, 3);
        assertBidPlacedForXPlayers(3, thirdBid, 0, 3);

        sendPlaceBid(2, thirdBid, 3);
        assertBidPlacedForXPlayers(3, thirdBid, 1, 3);

        sendPlaceBid(3, thirdBid, 3);
        assertBidPlacedForXPlayers(3, thirdBid, 2, 3);
    }
}
