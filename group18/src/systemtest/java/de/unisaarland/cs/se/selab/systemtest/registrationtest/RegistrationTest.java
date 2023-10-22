package de.unisaarland.cs.se.selab.systemtest.registrationtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.AbstractSystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Register 2 Players and Leave
 */
public class RegistrationTest extends AbstractSystemTest {

    // private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationTest.class);

    public RegistrationTest() {
        super(RegistrationTest.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();
        this.sendRegister(1, "Niklas");
        this.assertConfig(1, config);
        //        LOGGER.trace("Niklas IST ANGEMELDET!!!1!1!!1");
        //        this.sendRegister(2, "Max");
        //        this.assertConfig(2, config);
        //        LOGGER.trace("Max IST ANGEMELDET!!!1!1!!1");
        //        this.sendRegister(3, "Tobias");
        //        this.assertConfig(3, config);
        //        LOGGER.trace("TOBIAS IST ANGEMELDET!!!1!1!!1");
        //        this.sendRegister(4, "Colin");
        //        this.assertConfig(4, config);
        //        LOGGER.trace("Colin IST ANGEMELDET!!!1!1!!1");
        this.sendLeave(1);
        //        this.assertAdventurerArrived(1, 1, 1);
        //        this.sendLeave(2);
        //        this.sendLeave(3);
        //        this.sendLeave(4);
    }
}
