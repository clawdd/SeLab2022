package de.unisaarland.cs.se.selab.systemtest.configtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Load empty Config, Server should fail
 */
public class ConfigUnuniqueRoomIdsTest extends AbstractSystemTest {

    public ConfigUnuniqueRoomIdsTest() {
        super(ConfigUnuniqueRoomIdsTest.class, true);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(ConfigUnuniqueRoomIdsTest.class,
                "config_ununique_room_ids.json");
    }

    @Override
    protected long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        //Config is Empty so server should fail
    }
}
