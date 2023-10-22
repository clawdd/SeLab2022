package de.unisaarland.cs.se.selab.systemtest.biddingtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;

public class PlaceDungeonTilesMax extends AbstractSystemTest {

    private static final long SEED = 40;
    private static final Random RANDOM = new Random(SEED);

    public PlaceDungeonTilesMax() {
        super(PlaceDungeonTilesMax.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(PlaceDungeonTilesMax.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return SEED;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
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

        // Bids on Tunnel, Gold, Imps
        sendPlaceBid(1, BidType.GOLD, 1);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 0, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 2);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 0, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.TUNNEL, 3);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 0, 3);
        sendPlaceBid(2, BidType.GOLD, 1);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 1, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 2);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 1, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.TUNNEL, 3);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 1, 3);
        sendPlaceBid(3, BidType.GOLD, 1);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 2, 1);
        assertActNow(3);
        sendPlaceBid(3, BidType.IMPS, 2);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 2, 2);
        assertActNow(3);
        sendPlaceBid(3, BidType.TUNNEL, 3);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 2, 3);
        sendPlaceBid(4, BidType.GOLD, 1);
        assertBidPlacedForXPlayers(4, BidType.GOLD, 3, 1);
        assertActNow(4);
        sendPlaceBid(4, BidType.IMPS, 2);
        assertBidPlacedForXPlayers(4, BidType.IMPS, 3, 2);
        assertActNow(4);
        sendPlaceBid(4, BidType.TUNNEL, 3);
        assertBidPlacedForXPlayers(4, BidType.TUNNEL, 3, 3);

        extracted();
    }


    private void extracted() throws TimeoutException {
        //Player one    (2 to place)

        //placing on 0,1 -> allowed
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        assertImpsChangedForXPlayers(4, -1, 0);
        assertTunnelDugForXPlayers(4, 0, 0, 1);


        //Placing on 1,0 -> allowed

        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        assertImpsChangedForXPlayers(4, -1, 0);
        assertTunnelDugForXPlayers(4, 0, 1, 0);


        //Player two placing tunnels    (3 to place)

        //Placing on 0,0 -> not allowed
        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 0, 0);
        assertActionFailed(2);

        //placing on 1,0 -> allowed
        assertActNow(2);
        sendDigTunnel(2, 1, 0);
        assertImpsChangedForXPlayers(4, -1, 1);
        assertTunnelDugForXPlayers(4, 1, 1, 0);

        //placing on 0,1 -> allowed
        assertActNow(2);
        sendDigTunnel(2, 0, 1);
        assertImpsChangedForXPlayers(4, -1, 1);
        assertTunnelDugForXPlayers(4, 1, 0, 1);

        //placing on 1,1 -> not allowed
        assertActNow(2);
        sendDigTunnel(2, 1, 1);
        assertActionFailed(2);

        //placing on 4,0 -> not allowed
        assertActNow(2);
        sendDigTunnel(2, 4, 0);
        assertActionFailed(2);

        //placing on 0,2 -> allowed
        assertActNow(2);
        sendDigTunnel(2, 0, 2);
        assertImpsChangedForXPlayers(4, -1, 1);
        assertTunnelDugForXPlayers(4, 1, 0, 2);

        //Player three (could dig 4 but only has 3 imps)

        //placing on 0,1 -> allowed
        assertDigTunnel(3);
        assertActNow(3);
        sendDigTunnel(3, 0, 1);
        assertImpsChangedForXPlayers(4, -1, 2);
        assertTunnelDugForXPlayers(4, 2, 0, 1);

        //Placing on 0,2 -> allowed
        assertActNow(3);
        sendDigTunnel(3, 0, 2);
        assertImpsChangedForXPlayers(4, -1, 2);
        assertTunnelDugForXPlayers(4, 2, 0, 2);

        //Placing on 0,3 -> allowed
        assertActNow(3);
        sendDigTunnel(3, 0, 3);
        assertImpsChangedForXPlayers(4, -1, 2);
        assertTunnelDugForXPlayers(4, 2, 0, 3);

        //Placing 4th tile to be completed

        //Player four (has no slot on tunnels)


        sendLeave(1);
        sendLeave(2);
        sendLeave(3);
        sendLeave(4);
    }
}