package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Set;

public class TwoPlayersStartGameTest extends AbstractSystemTest {

    private static final long SEED = 43;
    // private static final Random RANDOM = new Random(SEED);

    public TwoPlayersStartGameTest() {
        super(TwoPlayersStartGameTest.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(TwoPlayersStartGameTest.class, "configuration.json");
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
        final List<String> l;

        l = registerXPlayer(2, config);
        this.sendStartGame(2);
        assertGameStartedForXPlayers(2);
        assertPlayerForXPlayers(2, l);
        assertNextYearForXPlayers(2, 1);
        assertNextSeasonForXPlayers(2, 1);
        /*assertAdventurersDrawnForXPlayers(1, config, RANDOM);
        assertMonstersDrawnForXPlayers(1, config, RANDOM);
        assertRoomDrawnForXPlayers(1, config, RANDOM);*/
        // assert adventurer
        assertEvent(1);
        assertEvent(2);
        assertEvent(1);
        assertEvent(2);
        // assert monsters
        assertEvent(1);
        assertEvent(2);
        assertEvent(1);
        assertEvent(2);
        assertEvent(1);
        assertEvent(2);
        // assert rooms
        assertEvent(1);
        assertEvent(2);
        assertEvent(1);
        assertEvent(2);
        assertBiddingStartedForXPlayers(2);
        assertActNowForXPlayers(2);
        this.sendLeave(1);
        this.sendLeave(2);
    }
}
