package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import java.util.Set;

public class MaxPlayerRegistrationTest extends AbstractSystemTest {

    private static final long SEED = 43;

    public MaxPlayerRegistrationTest() {
        super(MaxPlayerRegistrationTest.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(MaxPlayerRegistrationTest.class, "configuration.json");
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

        l = registerXPlayer(4, config);
        assertGameStartedForXPlayers(4);
        assertPlayerForXPlayers(4, l);
        assertNextYearForXPlayers(4, 1);
        assertNextSeasonForXPlayers(4, 1);
        /*assertAdventurersDrawnForXPlayers(1, config, RANDOM);
        assertMonstersDrawnForXPlayers(1, config, RANDOM);
        assertRoomDrawnForXPlayers(1, config, RANDOM);*/
        // assert adventurer
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        // assert monsters
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        // assert rooms
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        assertEvent(1);
        assertEvent(2);
        assertEvent(3);
        assertEvent(4);
        assertBiddingStartedForXPlayers(4);
        assertActNowForXPlayers(4);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
        this.sendLeave(4);

    }
}
