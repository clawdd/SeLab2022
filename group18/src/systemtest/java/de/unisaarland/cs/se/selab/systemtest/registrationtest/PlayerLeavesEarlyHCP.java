package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class PlayerLeavesEarlyHCP extends AbstractSystemTest {

    public PlayerLeavesEarlyHCP() {
        super(PlayerLeavesEarlyHCP.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(PlayerLeavesEarlyHCP.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        final String config = createConfig();

        sendRegister(1, "Lewis");
        assertConfig(1, config);

        sendRegister(2, "Max");
        assertConfig(2, config);

        sendLeave(2); // Left broadcast

        sendRegister(2, "Sergio");
        assertConfig(2, config);

        sendRegister(3, "George");
        assertConfig(3, config);

        sendLeave(3); // Left broadcast


        sendRegister(3, "Lando");
        assertConfig(3, config);

        sendStartGame(2);

        assertGameStartedForXPlayers(3);

        for (int i = 1; i < 4; i++) {
            sendLeave(i);
        }

    }
}
