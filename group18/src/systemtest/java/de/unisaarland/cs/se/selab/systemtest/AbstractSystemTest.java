package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSystemTest extends SystemTest {

    public AbstractSystemTest(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    protected List<String> registerXPlayer(final int x, final String config)
            throws TimeoutException {
        final List<String> l = new LinkedList<>();
        for (int i = 1; i <= x; i++) {
            l.add(String.valueOf(Math.random()));
            final String temp = l.get(l.size() - 1);
            this.sendRegister(i, temp);
            this.assertConfig(i, config);
        }
        return l;
    }

    protected void assertGameStartedForXPlayers(final int x) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            this.assertGameStarted(i);
        }
    }

    protected void assertPlayerForXPlayers(final int x, final List<String> list)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= x; j++) {
                this.assertPlayer(i, list.get(j - 1), j - 1);
            }
        }
    }

    protected void assertNextSeasonForXPlayers(final int x, final int round)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            this.assertNextRound(i, round);
        }
    }

    protected void assertNextYearForXPlayers(final int x, final int year) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            this.assertNextYear(i, year);
        }
    }

    protected List<JSONObject> getAndShuffleStock(final String config, final Random random,
                                                  final String nameOfStock) {
        final JSONArray stockJSONArray = (new JSONObject(config)).getJSONArray(nameOfStock);
        final List<JSONObject> stockList = new LinkedList<>();
        stockJSONArray.forEach(a -> stockList.add((JSONObject) a));
        Collections.shuffle(stockList, random);
        return stockList;
    }

    protected void assertAdventurersDrawnForXPlayers(final int x,
                                                     final List<JSONObject> stockList)
            throws TimeoutException {

        final Logger logger = LoggerFactory.getLogger(AbstractSystemTest.class);
        logger.trace(stockList.toString());
        for (int i = 0; i < x; i++) {
            for (int j = 1; j <= x; j++) {
                this.assertAdventurerDrawn(j, stockList.get(i).getInt("id"));
            }
        }
        stockList.subList(0, x).clear();
    }

    protected void assertMonstersDrawnForXPlayers(final int x, final List<JSONObject> stockList)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            this.assertMonsterDrawn(i, stockList.get(0).getInt("id"));
            this.assertMonsterDrawn(i, stockList.get(1).getInt("id"));
            this.assertMonsterDrawn(i, stockList.get(2).getInt("id"));
        }
        stockList.subList(0, 3).clear();
    }

    protected void assertRoomDrawnForXPlayers(final int x, final List<JSONObject> stockList)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            this.assertRoomDrawn(i, stockList.get(0).getInt("id"));
            this.assertRoomDrawn(i, stockList.get(1).getInt("id"));
        }
        stockList.subList(0, 2).clear();
    }

    protected void assertBiddingStartedForXPlayers(final int x) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            this.assertBiddingStarted(i);
        }
    }

    protected void assertActNowForXPlayers(final int x) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            this.assertActNow(i);
        }
    }

    /**
     * register X players and assert everything up to adventurers, monsters, rooms drawn
     *
     * @param x must be maxPlayers!!!
     */
    protected List<String> registrationXPlayers(final int x, final List<JSONObject> adventurers,
                                                final List<JSONObject> monsters,
                                                final List<JSONObject> rooms)
            throws TimeoutException {
        final String config = createConfig();
        final List<String> l;

        l = registerXPlayer(x, config);
        assertGameStartedForXPlayers(x);
        assertPlayerForXPlayers(x, l);
        assertNextYearForXPlayers(x, 1);
        assertNextSeasonForXPlayers(x, 1);

        assertAdventurersDrawnForXPlayers(x, adventurers);
        assertMonstersDrawnForXPlayers(x, monsters);
        assertRoomDrawnForXPlayers(x, rooms);

        return l;
    }

    protected void assertBidPlacedForXPlayers(final int x, final BidType bidType,
                                              final int player, final int slot)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertBidPlaced(i, bidType, player, slot);
        }
    }

    protected void assertFoodChangedForXPlayers(final int x, final int amount, final int player)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertFoodChanged(i, amount, player);
        }
    }

    protected void assertGoldChangedForXPlayers(final int x, final int amount, final int player)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertGoldChanged(i, amount, player);
        }
    }

    protected void assertEvilnessChangedForXPlayers(final int x, final int amount,
                                                    final int player) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertEvilnessChanged(i, amount, player);
        }
    }

    protected void assertImpsChangedForXPlayers(final int x, final int amount, final int player)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertImpsChanged(i, amount, player);
        }
    }

    protected void assertTunnelDugForXPlayers(final int x, final int player, final int xaxis,
                                              final int yaxis) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertTunnelDug(i, player, xaxis, yaxis);
        }
    }

    protected void assertBattleGroundSetForXPlayers(final int x, final int player,
                                                    final int xaxis, final int yaxis)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertBattleGroundSet(i, player, xaxis, yaxis);
        }
    }

    protected void assertGameEndForXPlayers(final int x, final int player, final int points)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertGameEnd(i, player, points);
        }
    }

    protected void assertLeftForXPlayers(final int x, final int player) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertLeft(i, player);
        }
    }

    protected void assertMonsterHiredForXPlayers(final int x, final int monster,
                                                 final int player) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertMonsterHired(i, monster, player);
        }
    }

    protected void assertBidRetrievedForXPlayers(final int x, final BidType bid, final int player)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertBidRetrieved(i, bid, player);
        }
    }

    protected void assertRegistrationAbortedForXPlayers(final int x) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertRegistrationAborted(i);
        }
    }

    protected void assertRoomActivatedForXPlayers(final int x, final int player, final int room)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertRoomActivated(i, player, room);
        }
    }

    protected void assertRoomBuiltForXPlayers(final int x, final int player, final int room,
                                              final int xaxis, final int yaxis)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertRoomBuilt(i, player, room, xaxis, yaxis);
        }
    }

    protected void assertTrapAcquiredForXPlayers(final int x, final int player,
                                                 final List<JSONObject> stockList)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertTrapAcquired(i, player, stockList.get(0).getInt("id"));
        }
        stockList.subList(0, 1).clear();
    }

    protected void assertTrapPlacedForXPlayers(final int x, final int player, final int trap)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertTrapPlaced(i, player, trap);
        }
    }

    protected void assertTunnelConqueredForXPlayers(final int x, final int adventurer,
                                                    final int xaxis, final int yaxis)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertTunnelConquered(i, adventurer, xaxis, yaxis);
        }
    }

    protected void assertAdventurerArrivedForXPlayers(final int x, final int adventurer,
                                                      final int player)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertAdventurerArrived(i, adventurer, player);
        }
    }

    protected void assertAdventurerDamagedForXPlayers(final int x, final int adventurer,
                                                      final int amount) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertAdventurerDamaged(i, adventurer, amount);
        }
    }

    protected void assertAdventurerFledForXPlayers(final int x, final int adventurer)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertAdventurerFled(i, adventurer);
        }
    }

    protected void assertAdventurerHealedForXPlayers(final int x, final int amount,
                                                     final int priest, final int target)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertAdventurerHealed(i, amount, priest, target);
        }
    }

    protected void assertAdventurerImprisonedForXPlayers(final int x, final int adventurer)
            throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertAdventurerImprisoned(i, adventurer);
        }
    }

    protected void assertMonsterPlacedForXPlayers(final int x, final int monster,
                                                  final int player) throws TimeoutException {
        for (int i = 1; i <= x; i++) {
            assertMonsterPlaced(i, monster, player);
        }
    }

}
