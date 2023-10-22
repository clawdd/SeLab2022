package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MaxPlayerStartGameTest extends AbstractSystemTest {

    private static final long SEED = 42;
    private static final Random RANDOM = new Random(SEED);

    public MaxPlayerStartGameTest() {
        super(MaxPlayerStartGameTest.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(MaxPlayerStartGameTest.class, "configuration.json");
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
        final List<String> l;
        final var monsters = getAndShuffleStock(
                config, RANDOM, "monsters");
        final var adventurers = getAndShuffleStock(
                config, RANDOM, "adventurers");
        getAndShuffleStock(
                config, RANDOM, "traps");
        final var rooms = getAndShuffleStock(
                config, RANDOM, "rooms");

        l = registerXPlayer(4, config);
        assertGameStartedForXPlayers(4);
        assertPlayerForXPlayers(4, l);
        assertNextYearForXPlayers(4, 1);
        assertNextSeasonForXPlayers(4, 1);
        assertAdventurersDrawnForXPlayers(4, adventurers);
        assertMonstersDrawnForXPlayers(4, monsters);
        assertRoomDrawnForXPlayers(4, rooms);
        assertBiddingStartedForXPlayers(4);
        assertActNowForXPlayers(4);

        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
        this.sendLeave(4);
    }
}
