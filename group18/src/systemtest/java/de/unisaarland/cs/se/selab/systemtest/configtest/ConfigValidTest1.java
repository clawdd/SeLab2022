package de.unisaarland.cs.se.selab.systemtest.configtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Load empty Config, Server should fail
 */
public class ConfigValidTest1 extends AbstractSystemTest {

    public ConfigValidTest1() {
        super(ConfigValidTest1.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(ConfigValidTest1.class,
                "config_valid1.json");
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
        // nothing
    }
}
