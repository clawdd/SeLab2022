package de.unisaarland.cs.se.selab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * checks if properties read from the config are valid
 */
public class ConfigChecker {

    /**
     * @param configObject config as JSONObject
     * @throws IOException when the properties are invalid
     */
    static void checkOtherConfigProperties(final JSONObject configObject) throws IOException {
        checkMinimumAmountOfItems(configObject);
        final JSONArray monsters = configObject.getJSONArray("monsters");
        checkUniqueIds(monsters);
        final JSONArray adventurers = configObject.getJSONArray("adventurers");
        checkUniqueIds(adventurers);
        final JSONArray traps = configObject.getJSONArray("traps");
        checkUniqueIds(traps);
        final JSONArray rooms = configObject.getJSONArray("rooms");
        checkUniqueIds(rooms);
    }

    /**
     * @param items items as JSONArray
     * @throws IOException if IDs are not unique
     */
    static void checkUniqueIds(final JSONArray items) throws IOException {
        for (int i = 0; i < items.length(); i++) {
            for (int j = 0; j < items.length(); j++) {
                if (i == j) {
                    continue;
                }
                final JSONObject item1 = items.getJSONObject(i);
                final JSONObject item2 = items.getJSONObject(j);
                if (item1.getInt("id") == item2.getInt("id")) {
                    throw new IOException("Item ids not unique!");
                }
            }
        }
    }

    /**
     * @param configObject config as JSONObject
     * @throws IOException if there are too few items
     */
    static void checkMinimumAmountOfItems(final JSONObject configObject) throws IOException {
        final JSONArray monsters = configObject.getJSONArray("monsters");
        if (monsters.length()
                < 12 * configObject.getInt("years")) {
            throw new IOException("Too few monsters!");
        }
        final JSONArray adventurers = configObject.getJSONArray("adventurers");
        if (adventurers.length()
                < 3 * configObject.getInt("years") * configObject.getInt("maxPlayers")) {
            throw new IOException("Too few adventurers!");
        }
        final JSONArray traps = configObject.getJSONArray("traps");
        if (traps.length()
                < 16 * configObject.getInt("years")) {
            throw new IOException("Too few traps!");
        }
        final JSONArray rooms = configObject.getJSONArray("rooms");
        if (rooms.length()
                < 8 * configObject.getInt("years")) {
            throw new IOException("Too few rooms!");
        }
    }

    /**
     * @param subclass subclass
     * @param name     name of schema
     * @return the schema
     */
    static Schema getSchema(final Class<?> subclass, final String name) {
        return SchemaLoader.builder()
                .schemaClient(SchemaClient.classPathAwareClient()).schemaJson(new JSONObject(
                        Objects.requireNonNull(
                                loadResource(subclass, "schema/" + name))))
                .resolutionScope("classpath://schema/").build().load().build();
    }

    /**
     * load resource
     *
     * @param subclass subclass
     * @param name     name
     * @return string
     */
    public static String loadResource(final Class<?> subclass, final String name) {
        LoggerFactory.getLogger(subclass)
                .trace("loading {}", subclass.getClassLoader().getResource(name));
        try (final var input = new InputStreamReader(
                Objects.requireNonNull(subclass.getClassLoader().getResourceAsStream(name)),
                StandardCharsets.UTF_8)) {
            try (final BufferedReader reader = new BufferedReader(input)) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (final IOException e) {
            LoggerFactory.getLogger(subclass).error("{}", e.getMessage(), e);
            return null;
        }
    }
}
