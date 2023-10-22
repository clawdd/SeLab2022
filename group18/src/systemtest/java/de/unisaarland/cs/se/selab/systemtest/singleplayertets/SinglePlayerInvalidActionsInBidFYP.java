package de.unisaarland.cs.se.selab.systemtest.singleplayertets;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Random;
import java.util.Set;

public class SinglePlayerInvalidActionsInBidFYP extends AbstractSystemTest {


    private static final long SEED = 43;
    private static final Random RANDOM = new Random(SEED);

    public SinglePlayerInvalidActionsInBidFYP() {
        super(SinglePlayerInvalidActionsInBidFYP.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(SinglePlayerInvalidActionsInBidFYP.class,
                "config_for_single_player" + ".json");
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

        assertBiddingStartedForXPlayers(1);

        //------------------------- check if statemachine works -------------------------//

        assertActNow(1);
        sendActivateRoom(1, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendTrap(1, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendStartGame(1);
        assertActionFailed(1);
        assertActNow(1);
        sendRegister(1, "daniel");
        assertActionFailed(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        assertActionFailed(1);
        assertActNow(1);
        sendMonster(1, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendMonsterTargeted(1, 0, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendHireMonster(1, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendBuildRoom(1, 1, 0, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 0, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendActivateRoom(1, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);
        assertActionFailed(1);
        assertActNow(1);

        sendLeave(1);

    }
}
