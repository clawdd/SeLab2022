package de.unisaarland.cs.se.selab.systemtest.completegame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.json.JSONObject;

public class Test129 extends AbstractSystemTest {

    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);

    public Test129() {
        super(Test129.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(Test129.class, "configuration.json");
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

    private void secondBiddingSeason(final List<JSONObject> monsters,
                                     final List<JSONObject> adventurers,
                                     final List<JSONObject> rooms) throws TimeoutException {
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
        sendPlaceBid(2, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 1, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.TRAP, 2);
        assertBidPlacedForXPlayers(4, BidType.TRAP, 1, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.MONSTER, 3);
        assertBidPlacedForXPlayers(4, BidType.MONSTER, 1, 3);
        sendPlaceBid(3, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 2, 1);
        assertActNow(3);
        sendPlaceBid(3, BidType.TRAP, 2);
        assertBidPlacedForXPlayers(4, BidType.TRAP, 2, 2);
        assertActNow(3);
        sendPlaceBid(3, BidType.MONSTER, 3);
        assertBidPlacedForXPlayers(4, BidType.MONSTER, 2, 3);
        sendPlaceBid(4, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 3, 1);
        assertActNow(4);
        sendPlaceBid(4, BidType.TRAP, 2);
        assertBidPlacedForXPlayers(4, BidType.TRAP, 3, 2);
        assertActNow(4);
        sendPlaceBid(4, BidType.MONSTER, 3);
        assertBidPlacedForXPlayers(4, BidType.MONSTER, 3, 3);
    }

    private void secondBiddingEvalSeason(final List<JSONObject> traps)
            throws TimeoutException {

        assertGoldChangedForXPlayers(4, -1, 1);
        assertFoodChangedForXPlayers(4, 2, 1);
        assertEvilnessChangedForXPlayers(4, 1, 2);
        assertFoodChangedForXPlayers(4, 3, 2);
        assertEvilnessChangedForXPlayers(4, 2, 3);
        assertFoodChangedForXPlayers(4, 3, 3);
        assertGoldChangedForXPlayers(4, 1, 3);

        assertGoldChangedForXPlayers(4, -1, 1);
        assertTrapAcquiredForXPlayers(4, 1, traps);
        assertTrapAcquiredForXPlayers(4, 2, traps);
        assertGoldChangedForXPlayers(4, -2, 3);
        assertTrapAcquiredForXPlayers(4, 3, traps);
        assertTrapAcquiredForXPlayers(4, 3, traps);

        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 11);
        assertFoodChangedForXPlayers(4, -2, 1);
        assertEvilnessChangedForXPlayers(4, 1, 1);
        assertMonsterHiredForXPlayers(4, 11, 1);

        assertSelectMonster(3);
        assertActNow(3);
        sendHireMonster(3, 11);
        assertActionFailed(3);
        assertActNow(3);
        sendHireMonster(3, 13);
        assertFoodChangedForXPlayers(4, -1, 2);
        assertEvilnessChangedForXPlayers(4, 1, 2);
        assertMonsterHiredForXPlayers(4, 13, 2);

        assertFoodChangedForXPlayers(4, -1, 3);
        assertSelectMonster(4);
        assertActNow(4);
        sendHireMonster(4, 14);
        assertFoodChangedForXPlayers(4, -1, 3);
        assertEvilnessChangedForXPlayers(4, 1, 3);
        assertMonsterHiredForXPlayers(4, 14, 3);

        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 0);
        assertBidRetrievedForXPlayers(4, BidType.IMPS, 0);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 0);
        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 1);
        assertBidRetrievedForXPlayers(4, BidType.IMPS, 1);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 1);
        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 2);
        assertBidRetrievedForXPlayers(4, BidType.IMPS, 2);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 2);
        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 3);
        assertBidRetrievedForXPlayers(4, BidType.IMPS, 3);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 3);

        assertAdventurerArrivedForXPlayers(4, 8, 0);
        assertAdventurerArrivedForXPlayers(4, 5, 1);
        assertAdventurerArrivedForXPlayers(4, 30, 2);
        assertAdventurerArrivedForXPlayers(4, 15, 3);
    }

    private void thirdBiddingSeason(final List<JSONObject> monsters,
                                    final List<JSONObject> adventurers,
                                    final List<JSONObject> rooms) throws TimeoutException {
        assertNextSeasonForXPlayers(4, 3);
        assertAdventurersDrawnForXPlayers(4, adventurers);
        assertMonstersDrawnForXPlayers(4, monsters);
        assertRoomDrawnForXPlayers(4, rooms);
        assertBiddingStartedForXPlayers(4);
        assertActNowForXPlayers(4);

        sendPlaceBid(1, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 0, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 0, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.ROOM, 3);
        assertBidPlacedForXPlayers(4, BidType.ROOM, 0, 3);
        sendPlaceBid(2, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 1, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 1, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.ROOM, 3);
        assertBidPlacedForXPlayers(4, BidType.ROOM, 1, 3);
        sendPlaceBid(3, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 2, 1);
        assertActNow(3);
        sendPlaceBid(3, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 2, 2);
        assertActNow(3);
        sendPlaceBid(3, BidType.ROOM, 3);
        assertBidPlacedForXPlayers(4, BidType.ROOM, 2, 3);
        sendPlaceBid(4, BidType.FOOD, 1);
        assertBidPlacedForXPlayers(4, BidType.FOOD, 3, 1);
        assertActNow(4);
        sendPlaceBid(4, BidType.GOLD, 2);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 3, 2);
        assertActNow(4);
        sendPlaceBid(4, BidType.ROOM, 3);
        assertBidPlacedForXPlayers(4, BidType.ROOM, 3, 3);
    }

    private void thirdBiddingSeasonEval() throws TimeoutException {
        assertGoldChangedForXPlayers(4, -1, 2);
        assertFoodChangedForXPlayers(4, 2, 2);
        assertEvilnessChangedForXPlayers(4, 1, 3);
        assertFoodChangedForXPlayers(4, 3, 3);
        assertEvilnessChangedForXPlayers(4, 2, 0);
        assertFoodChangedForXPlayers(4, 3, 0);
        assertGoldChangedForXPlayers(4, 1, 0);

        assertImpsChangedForXPlayers(4, -1, 2);
        assertImpsChangedForXPlayers(4, -1, 3);
        assertImpsChangedForXPlayers(4, -1, 0);

        assertGoldChangedForXPlayers(4, -1, 2);
        assertPlaceRoom(3);
        assertActNow(3);
        sendBuildRoom(3, 0, 0, 7);
        assertRoomBuiltForXPlayers(4, 2, 7, 0, 0);

        assertGoldChangedForXPlayers(4, -1, 3);
        assertPlaceRoom(4);
        assertActNow(4);
        sendBuildRoom(4, 0, 0, 8);
        assertActionFailed(4);
        assertActNow(4);
        sendEndTurn(4);
        assertPlaceRoom(1);
        assertActNow(1);
        sendBuildRoom(1, 0, 0, 8);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);

        assertBidRetrievedForXPlayers(4, BidType.TRAP, 0);
        assertBidRetrievedForXPlayers(4, BidType.MONSTER, 0);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 0);
        assertBidRetrievedForXPlayers(4, BidType.TRAP, 1);
        assertBidRetrievedForXPlayers(4, BidType.MONSTER, 1);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 1);
        assertBidRetrievedForXPlayers(4, BidType.TRAP, 2);
        assertBidRetrievedForXPlayers(4, BidType.MONSTER, 2);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 2);
        assertBidRetrievedForXPlayers(4, BidType.TRAP, 3);
        assertBidRetrievedForXPlayers(4, BidType.MONSTER, 3);
        assertBidRetrievedForXPlayers(4, BidType.FOOD, 3);

        assertImpsChangedForXPlayers(4, 1, 0);
        assertGoldChangedForXPlayers(4, 1, 0);
        assertImpsChangedForXPlayers(4, 1, 2);
        assertGoldChangedForXPlayers(4, 1, 2);
        assertImpsChangedForXPlayers(4, 1, 3);
        assertGoldChangedForXPlayers(4, 1, 3);

        assertAdventurerArrivedForXPlayers(4, 25, 1);
        assertAdventurerArrivedForXPlayers(4, 3, 0);
        assertAdventurerArrivedForXPlayers(4, 19, 2);
        assertAdventurerArrivedForXPlayers(4, 13, 3);
    }

    private void fourthBiddingSeason(final List<JSONObject> monsters, final List<JSONObject> rooms)
            throws TimeoutException {
        assertNextSeasonForXPlayers(4, 4);
        assertMonstersDrawnForXPlayers(4, monsters);
        assertRoomDrawnForXPlayers(4, rooms);
        assertBiddingStartedForXPlayers(4);
        assertActNowForXPlayers(4);
        sendPlaceBid(1, BidType.NICENESS, 1);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 0, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.TUNNEL, 2);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 0, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 0, 3);
        sendPlaceBid(2, BidType.NICENESS, 1);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 1, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.TUNNEL, 2);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 1, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 1, 3);
        sendPlaceBid(3, BidType.NICENESS, 1);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 2, 1);
        assertActNow(3);
        sendPlaceBid(3, BidType.TUNNEL, 2);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 2, 2);
        assertActNow(3);
        sendPlaceBid(3, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 2, 3);
        sendPlaceBid(4, BidType.NICENESS, 1);
        assertBidPlacedForXPlayers(4, BidType.NICENESS, 3, 1);
        assertActNow(4);
        sendPlaceBid(4, BidType.TUNNEL, 2);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 3, 2);
        assertActNow(4);
        sendPlaceBid(4, BidType.IMPS, 3);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 3, 3);
    }

    private void fourthBiddingEvalSeason() throws TimeoutException {
        assertEvilnessChangedForXPlayers(4, -1, 3);
        assertEvilnessChangedForXPlayers(4, -2, 0);
        assertGoldChangedForXPlayers(4, -1, 1);
        assertEvilnessChangedForXPlayers(4, -2, 1);
        assertDigTunnel(4);
        assertActNow(4);
        sendDigTunnel(4, 0, 1);
        assertImpsChangedForXPlayers(4, -1, 3);
        assertTunnelDugForXPlayers(4, 3, 0, 1);
        assertActNow(4);
        sendDigTunnel(4, 0, 2);
        assertImpsChangedForXPlayers(4, -1, 3);
        assertTunnelDugForXPlayers(4, 3, 0, 2);
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        assertImpsChangedForXPlayers(4, -1, 0);
        assertTunnelDugForXPlayers(4, 0, 0, 1);
        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        assertImpsChangedForXPlayers(4, -1, 0);
        assertTunnelDugForXPlayers(4, 0, 0, 2);
        assertActNow(1);
        sendDigTunnel(1, 0, 3);
        assertImpsChangedForXPlayers(4, -1, 0);
        assertTunnelDugForXPlayers(4, 0, 0, 3);
        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 0, 1);
        assertImpsChangedForXPlayers(4, -1, 1);
        assertTunnelDugForXPlayers(4, 1, 0, 1);
        assertActNow(2);
        sendDigTunnel(2, 0, 2);
        assertImpsChangedForXPlayers(4, -1, 1);
        assertTunnelDugForXPlayers(4, 1, 0, 2);
        assertActNow(2);
        sendDigTunnel(2, 0, 3);
        assertImpsChangedForXPlayers(4, -1, 1);
        assertTunnelDugForXPlayers(4, 1, 0, 3);
        assertActNow(2);
        sendDigTunnel(2, 0, 4);
        assertImpsChangedForXPlayers(4, -2, 1);
        assertTunnelDugForXPlayers(4, 1, 0, 4);
        assertFoodChangedForXPlayers(4, -1, 3);
        assertImpsChangedForXPlayers(4, 1, 3);
        assertFoodChangedForXPlayers(4, -2, 0);
        assertImpsChangedForXPlayers(4, 2, 0);

        assertBidRetrievedForXPlayers(4, BidType.GOLD, 0);
        assertBidRetrievedForXPlayers(4, BidType.ROOM, 0);
        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 0);
        assertBidRetrievedForXPlayers(4, BidType.GOLD, 1);
        assertBidRetrievedForXPlayers(4, BidType.ROOM, 1);
        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 1);
        assertBidRetrievedForXPlayers(4, BidType.GOLD, 2);
        assertBidRetrievedForXPlayers(4, BidType.ROOM, 2);
        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 2);
        assertBidRetrievedForXPlayers(4, BidType.GOLD, 3);
        assertBidRetrievedForXPlayers(4, BidType.ROOM, 3);
        assertBidRetrievedForXPlayers(4, BidType.NICENESS, 3);

        assertImpsChangedForXPlayers(4, 3, 0);
        assertImpsChangedForXPlayers(4, 5, 1);
        assertImpsChangedForXPlayers(4, 2, 3);
    }

    private void combatPlayerOne() throws TimeoutException {
        assertNextSeasonForXPlayers(4, 1);
        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 0, 0);
        assertBattleGroundSetForXPlayers(4, 0, 0, 0);
        assertDefendYourself(1);
        assertActNow(1);
        sendTrap(1, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendMonster(1, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);
        assertAdventurerDamagedForXPlayers(4, 8, 2);
        assertAdventurerDamagedForXPlayers(4, 1, 2);
        assertAdventurerDamagedForXPlayers(4, 3, 2);
        assertTunnelConqueredForXPlayers(4, 8, 0, 0);
        assertEvilnessChangedForXPlayers(4, -1, 0);
        assertAdventurerHealedForXPlayers(4, 1, 1, 8);
        assertAdventurerHealedForXPlayers(4, 1, 3, 8);
        assertAdventurerHealedForXPlayers(4, 1, 3, 1);

        assertNextSeasonForXPlayers(4, 2);
        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 0, 1);
        assertBattleGroundSetForXPlayers(4, 0, 0, 1);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        assertAdventurerDamagedForXPlayers(4, 8, 2);
        assertAdventurerImprisonedForXPlayers(4, 1);
        assertAdventurerImprisonedForXPlayers(4, 3);
        assertTunnelConqueredForXPlayers(4, 8, 0, 1);
        assertEvilnessChangedForXPlayers(4, -1, 0);

        assertNextSeasonForXPlayers(4, 3);
        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 0, 2);
        assertBattleGroundSetForXPlayers(4, 0, 0, 2);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        assertAdventurerImprisonedForXPlayers(4, 8);

        assertNextSeasonForXPlayers(4, 1);
        assertSetBattleGround(2);
        assertActNow(2);
        sendBattleGround(2, 0, 0);
        assertBattleGroundSetForXPlayers(4, 1, 0, 0);
        assertDefendYourself(2);
        assertActNow(2);
        sendTrap(2, 16); // hier
        assertTrapPlacedForXPlayers(4, 1, 16);
        assertActNow(2);


    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();
        final var monsters = getAndShuffleStock(
                config, RANDOM, "monsters");
        final var adventurers = getAndShuffleStock(
                config, RANDOM, "adventurers");
        final var traps = getAndShuffleStock(
                config, RANDOM, "traps");
        final var rooms = getAndShuffleStock(
                config, RANDOM, "rooms");
        registrationXPlayers(4, adventurers, monsters, rooms);
        assertBiddingStartedForXPlayers(4);
        assertActNowForXPlayers(4);

        firstBiddingSeason();

        secondBiddingSeason(monsters, adventurers, rooms);

        secondBiddingEvalSeason(traps);

        thirdBiddingSeason(monsters, adventurers, rooms);

        thirdBiddingSeasonEval();

        fourthBiddingSeason(monsters, rooms);

        fourthBiddingEvalSeason();

        combatPlayerOne();
        sendLeave(1);
        sendLeave(2);
        sendLeave(3);
        sendLeave(4);

    }


}
