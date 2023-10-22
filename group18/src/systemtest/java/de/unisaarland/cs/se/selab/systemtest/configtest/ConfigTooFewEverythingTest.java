package de.unisaarland.cs.se.selab.systemtest.configtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Load empty Config, Server should fail
 */
public class ConfigTooFewEverythingTest extends AbstractSystemTest {

    public ConfigTooFewEverythingTest() {
        super(ConfigTooFewEverythingTest.class, true);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(ConfigTooFewEverythingTest.class,
                "config_too_few_everything.json");
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
