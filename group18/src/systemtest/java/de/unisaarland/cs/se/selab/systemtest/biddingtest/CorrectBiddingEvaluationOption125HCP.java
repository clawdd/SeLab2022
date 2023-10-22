package de.unisaarland.cs.se.selab.systemtest.biddingtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;

public class CorrectBiddingEvaluationOption125HCP extends AbstractSystemTest {

    private static final long SEED = 42;
    private static final Random RANDOM = new Random(SEED);

    public CorrectBiddingEvaluationOption125HCP() {
        super(CorrectBiddingEvaluationOption125HCP.class, false);
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

        make3BidsFor3Players(BidType.FOOD, BidType.NICENESS, BidType.IMPS);


        // Evaluation Food
        assertGoldChangedForXPlayers(3, -1, 0); // 1st slot
        assertFoodChangedForXPlayers(3, 2, 0);

        assertEvilnessChangedForXPlayers(3, 1, 1); // 2nd slot
        assertFoodChangedForXPlayers(3, 3, 1);

        assertEvilnessChangedForXPlayers(3, 2, 2); // 3rd slot
        assertFoodChangedForXPlayers(3, 3, 2);
        assertGoldChangedForXPlayers(3, 1, 2);

        // Evaluation Niceness
        assertEvilnessChangedForXPlayers(3, -1, 1); // 1st slot

        assertEvilnessChangedForXPlayers(3, -2, 2); // 2nd slot

        assertGoldChangedForXPlayers(3, -1, 0); // 3rd slot
        assertEvilnessChangedForXPlayers(3, -2, 0);

        // Evaluation Imps
        assertFoodChangedForXPlayers(3, -1, 2); // 1st slot
        assertImpsChangedForXPlayers(3, 1, 2);

        assertFoodChangedForXPlayers(3, -2, 0); // 2nd slot
        assertImpsChangedForXPlayers(3, 2, 0);

        assertFoodChangedForXPlayers(3, -1, 1); // 3rd slot
        assertGoldChangedForXPlayers(3, -1, 1);
        assertImpsChangedForXPlayers(3, 2, 1);

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
                                     final BidType thrdBid
    ) throws TimeoutException {
        /* Placing Bids */
        // 1st Bid
        sendPlaceBid(1, fstBid, 1);
        assertBidPlacedForXPlayers(3, fstBid, 0, 1);
        //assertBidPlacedBroadcast(1, 1, 0, BidType.fstBid);
        assertActNow(1);
        sendPlaceBid(2, fstBid, 2);
        assertBidPlacedForXPlayers(3, fstBid, 1, 2);
        assertActNow(2);
        sendPlaceBid(3, fstBid, 3);
        assertBidPlacedForXPlayers(3, fstBid, 2, 3);
        assertActNow(3);

        // 2nd bid
        sendPlaceBid(1, sndBid, 3);
        assertBidPlacedForXPlayers(3, sndBid, 0, 3);
        assertActNow(1);
        sendPlaceBid(2, sndBid, 1);
        assertBidPlacedForXPlayers(3, sndBid, 1, 1);
        assertActNow(2);
        sendPlaceBid(3, sndBid, 2);
        assertBidPlacedForXPlayers(3, sndBid, 2, 2);
        assertActNow(3);

        // 3rd bid
        sendPlaceBid(1, thrdBid, 2);
        assertBidPlacedForXPlayers(3, thrdBid, 0, 2);
        sendPlaceBid(2, thrdBid, 3);
        assertBidPlacedForXPlayers(3, thrdBid, 1, 3);
        sendPlaceBid(3, thrdBid, 1);
        assertBidPlacedForXPlayers(3, thrdBid, 2, 1);
    }
}
