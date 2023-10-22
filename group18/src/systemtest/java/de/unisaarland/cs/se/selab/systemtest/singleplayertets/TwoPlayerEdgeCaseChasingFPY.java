package de.unisaarland.cs.se.selab.systemtest.singleplayertets;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.BiddingRoundRobinTest;
//import java.util.Random;
import java.util.Set;

public class TwoPlayerEdgeCaseChasingFPY extends AbstractSystemTest {


    private static final long SEED = 43;
    //private static final Random RANDOM = new Random(SEED);

    public TwoPlayerEdgeCaseChasingFPY() {
        super(TwoPlayerEdgeCaseChasingFPY.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(BiddingRoundRobinTest.class, "configuration.json");
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

        sendRegister(1, "Fisch");
        assertConfig(1, config);

        sendLeave(1);

    }
}
