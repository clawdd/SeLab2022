package de.unisaarland.cs.se.selab;

import de.unisaarland.cs.se.selab.gamelogic.ItemStock;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void loadStock1() {
        final var itemStock = new ItemStock();

        try {
            final JSONObject configObject = new JSONObject(Files.readString(
                    Path.of("src/main/resources/configuration.json")));
            Main.loadStock(itemStock, configObject);
            Assertions.assertEquals(24, itemStock.getAllMonsters().size());
            Assertions.assertEquals(32, itemStock.getAllAdventurer().size());
            Assertions.assertEquals(51, itemStock.getAllTraps().size());
            Assertions.assertEquals(16, itemStock.getAllRooms().size());
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    /*@Test
    void checkConfigValid() {
        final String fileString;
        try {
            fileString = Files.readString(Path.of("src/main/resources/configuration.json"));
            final JSONObject configObject = new JSONObject(fileString);
            Assertions.assertDoesNotThrow(() ->
                    ConfigChecker.checkJSON(
                            configObject, "src/main/resources/schema/main.schema"));
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /*@Test
    void checkConfigInvalid() {
        final String fileString;
        try {
            fileString = Files.readString(Path.of("src/main/resources/real_config_broken.json"));
            final JSONObject configObject = new JSONObject(fileString);
            Assertions.assertThrows(ValidationException.class, () ->
                    ConfigChecker.checkJSON(
                            configObject, "src/main/resources/schema/main.schema"));
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }*/
}