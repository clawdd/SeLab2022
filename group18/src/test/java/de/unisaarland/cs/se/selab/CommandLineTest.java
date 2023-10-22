package de.unisaarland.cs.se.selab;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandLineTest {

    @Test
    void testGetCommandLineArgsValid1() {
        try {
            final CommandLine commandLine = Main.getCommandLineArgs(new String[]{
                    "--config", "src/main/resources/configuration.json",
                    "--port", "8080",
                    "--seed", "12345678",
                    "--timeout", "300"
            });

            Assertions.assertEquals("src/main/resources/configuration.json",
                    commandLine.getOptionValue("config"));
            Assertions.assertEquals("8080", commandLine.getOptionValue("port"));
            Assertions.assertEquals("12345678", commandLine.getOptionValue("seed"));
            Assertions.assertEquals("300", commandLine.getOptionValue("timeout"));
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void testGetCommandLineArgsValid2() {
        try {
            final CommandLine commandLine = Main.getCommandLineArgs(new String[]{
                    "--seed", "12345678",
                    "--port", "8080",
                    "--timeout", "300",
                    "--config", "src/main/resources/configuration.json"
            });

            Assertions.assertEquals("src/main/resources/configuration.json",
                    commandLine.getOptionValue("config"));
            Assertions.assertEquals("8080", commandLine.getOptionValue("port"));
            Assertions.assertEquals("12345678", commandLine.getOptionValue("seed"));
            Assertions.assertEquals("300", commandLine.getOptionValue("timeout"));
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }
    }
}