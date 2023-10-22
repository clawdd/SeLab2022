package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PlayerStartsGameTest extends AbstractSystemTest {

    private static final long SEED = 42;
    private static final Random RANDOM = new Random(SEED);

    public PlayerStartsGameTest() {
        super(PlayerStartsGameTest.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(PlayerStartsGameTest.class, "configuration.json");
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
        final List<String> l;

        l = registerXPlayer(1, config);

        final var monsters = getAndShuffleStock(
                config, RANDOM, "monsters");
        final var adventurers = getAndShuffleStock(
                config, RANDOM, "adventurers");
        getAndShuffleStock(
                config, RANDOM, "traps");
        final var rooms = getAndShuffleStock(
                config, RANDOM, "rooms");

        this.sendStartGame(1);
        assertGameStartedForXPlayers(1);
        assertPlayerForXPlayers(1, l);
        assertNextYearForXPlayers(1, 1);
        assertNextSeasonForXPlayers(1, 1);

        assertAdventurersDrawnForXPlayers(1, adventurers);
        assertMonstersDrawnForXPlayers(1, monsters);
        assertRoomDrawnForXPlayers(1, rooms);

        assertBiddingStartedForXPlayers(1);
        assertActNowForXPlayers(1);
        this.sendLeave(1);
    }
}
