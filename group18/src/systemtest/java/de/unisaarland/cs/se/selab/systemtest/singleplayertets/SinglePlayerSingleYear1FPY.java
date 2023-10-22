package de.unisaarland.cs.se.selab.systemtest.singleplayertets;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;
import org.slf4j.LoggerFactory;

public class SinglePlayerSingleYear1FPY extends AbstractSystemTest {

    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);
    private static final int P_1 = 0;
    private static final BidType FOOD = BidType.FOOD;
    private static final BidType NICENESS = BidType.NICENESS;
    private static final BidType TUNNEL = BidType.TUNNEL;
    private static final BidType TRAP = BidType.TRAP;
    //private static final BidType Room = BidType.ROOM;
    private static final BidType MONSTER = BidType.MONSTER;
    private static final BidType GOLD = BidType.GOLD;
    private static final BidType IMPS = BidType.IMPS;


    public SinglePlayerSingleYear1FPY() {
        super(SinglePlayerSingleYear1FPY.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(SinglePlayerSingleYear1FPY.class, "config_for_single_player"
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

        sendRegister(1, "Fisch");
        assertConfig(1, config);
        assertGameStarted(1);
        assertPlayer(1, "Fisch", 0);
        assertNextYear(1, 1);
        assertNextRound(1, 1);

        assertAdventurersDrawnForXPlayers(1, adventurers);
        assertMonstersDrawnForXPlayers(1, monsters);
        assertRoomDrawnForXPlayers(1, rooms);

        LoggerFactory.getLogger(SinglePlayerSingleYear1FPY.class).trace(monsters.toString());

        assertBiddingStartedForXPlayers(1);
        firstBiddingSeason();

        assertAdventurersDrawnForXPlayers(1, adventurers);
        assertMonstersDrawnForXPlayers(1, monsters);
        assertRoomDrawnForXPlayers(1, rooms);
        assertBiddingStartedForXPlayers(1);
        secondBiddingSeason();

        assertAdventurersDrawnForXPlayers(1, adventurers);
        assertMonstersDrawnForXPlayers(1, monsters);
        assertRoomDrawnForXPlayers(1, rooms);
        assertBiddingStartedForXPlayers(1);
        thirdBiddingSeason();

        assertMonstersDrawnForXPlayers(1, monsters);
        assertRoomDrawnForXPlayers(1, rooms);
        assertBiddingStartedForXPlayers(1);
        fourthBiddingSeason();

        assertNextRound(1, 1);
        firstCombatRound();
        assertNextRound(1, 2);
        secondCombatRound();

        /*assertNextRound(1, 3);
        thirdCombatRound();
        assertNextRound(1, 4);
        fourthCombatRound();*/

        //assertNextYear(1, 2);

        assertGameEnd(1, P_1, 27);

    }


    protected void firstCombatRound() throws TimeoutException, AssertionError {

        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 1, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 0, 0);
        assertBattleGroundSet(1, P_1, 0, 0);

        assertDefendYourself(1);
        assertActNow(1);
        sendMonsterTargeted(1, 10, 3);
        assertMonsterPlaced(1, 10, P_1);
        assertActNow(1);
        sendMonsterTargeted(1, 21, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);
        //Monster
        assertAdventurerDamaged(1, 1, 1);
        //Fatigue
        assertAdventurerDamaged(1, 12, 2);
        assertAdventurerDamaged(1, 27, 2);
        //Rest
        assertAdventurerImprisoned(1, 1);
        assertTunnelConquered(1, 12, 0, 0);
        assertEvilnessChanged(1, -1, P_1);
        assertAdventurerHealed(1, 2, 27, 12);


    }

    protected void secondCombatRound() throws TimeoutException, AssertionError {

        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 0, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 3, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 0, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 2, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 1, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 1, 0);
        assertBattleGroundSet(1, P_1, 1, 0);

        assertDefendYourself(1);
        assertActNow(1);
        sendMonsterTargeted(1, 21, 1);
        assertMonsterPlaced(1, 21, P_1);
        assertActNow(1);
        sendEndTurn(1);
        //Monster
        assertAdventurerDamaged(1, 12, 3);
        //Fatigue
        assertAdventurerImprisoned(1, 12);
        assertAdventurerImprisoned(1, 27);

    }

    /*protected void thirdCombatRound() throws TimeoutException, AssertionError {

        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 1, 1);
        assertBattleGroundSet(1, P_1, 1, 1);

        assertDefendYourself(1);
        assertActNow(1);
        sendMonster(1, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendMonsterTargeted(1, 21, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendTrap(1, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);

        assertAdventurerDamaged(1, 27, 2);
        assertTunnelConquered(1, 27, 1, 1);
        assertEvilnessChanged(1, -1, P_1);
        assertAdventurerHealed(1, 2, 27, 0);

    }

    protected void fourthCombatRound() throws TimeoutException, AssertionError {

        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 2, 0);
        assertBattleGroundSet(1, P_1, 2, 0);

        assertDefendYourself(1);
        assertActNow(1);
        sendTrap(1, 16);
        assertTrapPlaced(1, P_1, 16);
        assertActNow(1);
        sendEndTurn(1);

        assertAdventurerDamaged(1, 27, 1);
        assertAdventurerDamaged(1, 27, 2);
        assertAdventurerImprisoned(1, 27);

    }*/

    protected void firstBiddingSeason() throws TimeoutException, AssertionError {

        assertActNow(1);
        sendPlaceBid(1, FOOD, 1);
        assertBidPlacedForXPlayers(1, FOOD, P_1, 1);

        assertActNow(1);
        sendPlaceBid(1, IMPS, 2);
        assertBidPlacedForXPlayers(1, IMPS, P_1, 2);

        assertActNow(1);
        sendPlaceBid(1, NICENESS, 3);
        assertBidPlacedForXPlayers(1, NICENESS, P_1, 3);

        //eval

        assertGoldChanged(1, -1, P_1);
        assertFoodChanged(1, 2, P_1);

        assertEvilnessChanged(1, -1, P_1);

        assertFoodChanged(1, -1, P_1);
        assertImpsChanged(1, 1, P_1);

        assertBidRetrieved(1, FOOD, P_1);

        assertAdventurerArrived(1, 27, P_1);

        assertNextRound(1, 2);

    }

    protected void secondBiddingSeason() throws TimeoutException, AssertionError {

        assertActNow(1);
        sendPlaceBid(1, FOOD, 1);
        assertBidPlacedForXPlayers(1, FOOD, P_1, 1);

        assertActNow(1);
        sendPlaceBid(1, GOLD, 2);
        assertBidPlacedForXPlayers(1, GOLD, P_1, 2);

        assertActNow(1);
        sendPlaceBid(1, TUNNEL, 3);
        assertBidPlacedForXPlayers(1, TUNNEL, P_1, 3);

        //eval

        assertGoldChanged(1, -1, P_1);
        assertFoodChanged(1, 2, P_1);

        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 1, 0);
        assertActNow(1);
        sendDigTunnel(1, 1, 1);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 1, 1);

        assertImpsChanged(1, -2, P_1);

        assertBidRetrieved(1, IMPS, P_1);
        assertBidRetrieved(1, NICENESS, P_1);
        assertBidRetrieved(1, FOOD, P_1);

        assertImpsChanged(1, 4, P_1);
        assertGoldChanged(1, 2, P_1);
        assertAdventurerArrived(1, 12, P_1);

        assertNextRound(1, 3);
    }

    protected void thirdBiddingSeason() throws TimeoutException, AssertionError {

        assertActNow(1);
        sendPlaceBid(1, MONSTER, 1);
        assertBidPlacedForXPlayers(1, MONSTER, P_1, 1);

        assertActNow(1);
        sendPlaceBid(1, IMPS, 2);
        assertBidPlacedForXPlayers(1, IMPS, P_1, 2);

        assertActNow(1);
        sendPlaceBid(1, TRAP, 3);
        assertBidPlacedForXPlayers(1, TRAP, P_1, 3);

        //eval

        assertFoodChanged(1, -1, P_1);
        assertImpsChanged(1, 1, P_1);

        assertGoldChanged(1, -1, P_1);
        assertTrapAcquired(1, P_1, 16);

        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 10);
        assertFoodChanged(1, -2, P_1);
        //no evilness assertEvilnessChanged(1, 1, P_1);
        assertMonsterHired(1, 10, P_1);

        assertBidRetrieved(1, GOLD, P_1);
        assertBidRetrieved(1, TUNNEL, P_1);
        assertBidRetrieved(1, MONSTER, P_1);

        assertAdventurerArrived(1, 1, P_1);

        assertNextRound(1, 4);
    }

    protected void fourthBiddingSeason() throws TimeoutException, AssertionError {

        assertActNow(1);
        sendPlaceBid(1, MONSTER, 1);
        assertBidPlacedForXPlayers(1, MONSTER, P_1, 1);

        assertActNow(1);
        sendPlaceBid(1, GOLD, 2);
        assertBidPlacedForXPlayers(1, GOLD, P_1, 2);

        assertActNow(1);
        sendPlaceBid(1, TUNNEL, 3);
        assertBidPlacedForXPlayers(1, TUNNEL, P_1, 3);

        //eval

        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 2, 0);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 2, 0);
        assertActNow(1);
        sendDigTunnel(1, 3, 0);
        assertImpsChanged(1, -1, P_1);
        assertTunnelDug(1, P_1, 3, 0);

        assertImpsChanged(1, -2, P_1);

        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 21);
        assertFoodChanged(1, -1, P_1);
        assertEvilnessChanged(1, 1, P_1);
        assertMonsterHired(1, 21, P_1);

        assertBidRetrieved(1, IMPS, P_1);
        assertBidRetrieved(1, TRAP, P_1);
        assertBidRetrieved(1, MONSTER, P_1);

        assertImpsChanged(1, 4, P_1);
        assertGoldChanged(1, 2, P_1);
    }
}
