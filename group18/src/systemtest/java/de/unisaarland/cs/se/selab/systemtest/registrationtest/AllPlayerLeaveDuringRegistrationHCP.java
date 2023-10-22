package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class AllPlayerLeaveDuringRegistrationHCP extends AbstractSystemTest {

    public AllPlayerLeaveDuringRegistrationHCP() {
        super(AllPlayerLeaveDuringRegistrationHCP.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(AllPlayerLeaveDuringRegistrationHCP.class,
                "configuration.json");
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

        sendRegister(1, "Toto");
        assertConfig(1, config);

        sendRegister(2, "Bono");
        assertConfig(2, config);

        sendRegister(3, "Lewis");
        assertConfig(3, config);

        sendLeave(1);

        sendLeave(2);

        sendLeave(3);

    }
}
