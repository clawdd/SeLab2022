package de.unisaarland.cs.se.selab;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.builder.AdventurerBuilder;
import de.unisaarland.cs.se.selab.builder.MonsterBuilder;
import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import de.unisaarland.cs.se.selab.builder.TrapBuilder;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import de.unisaarland.cs.se.selab.gamelogic.ItemStock;
import de.unisaarland.cs.se.selab.gamelogic.creatures.AttackStrategy;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Restriction;
import de.unisaarland.cs.se.selab.statemachine.RegistrationState;
import de.unisaarland.cs.se.selab.statemachine.State;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main class
 */
public class Main {
    /**
     * This is the main method.
     *
     * @param args terminal arguments
     * @throws IOException    when reading the file goes wrong
     * @throws ParseException when command line arguments are invalid
     */
    public static void main(final String[] args) throws ParseException, IOException {

        final CommandLine commandLine = getCommandLineArgs(args);

        // Setup JSON Parser

        final Path configPath = Path.of(commandLine.getOptionValue("config"));
        final String configString = Files.readString(configPath);

        final JSONObject configObject = new JSONObject(configString);

        // check config with schema
        //ConfigChecker.checkJSON(configObject, "../src/main/resources/schema/main.schema");
        ConfigChecker.getSchema(Main.class, "main.schema").validate(configObject);

        // check config properties that schema doesn't check
        ConfigChecker.checkOtherConfigProperties(configObject);

        final ActionFactoryImplementation actionFactory = new ActionFactoryImplementation();

        // Create ServerConnection in try-with-statement
        // to ensure that it's properly closed

        try (final ServerConnection<ActionCommand> sc = new ServerConnection<>(
                Integer.parseInt(commandLine.getOptionValue("port")),
                Integer.parseInt(commandLine.getOptionValue("timeout")) * 1000,
                actionFactory)) {

            actionFactory.setServerConnection(sc);
            // setStateMachine is done later in this method

            // Create GameBoard (automatically creates BiddingSquare and ItemStock)
            final GameBoard gameBoard = new GameBoard(
                    sc,
                    configObject.getInt("maxPlayers"),
                    configObject.getInt("years"),
                    configObject.optInt("initialFood", 3),
                    configObject.optInt("initialGold", 3),
                    configObject.optInt("initialImps", 3),
                    configObject.optInt("initialEvilness", 5),
                    configObject.getInt("dungeonSideLength")
            );
            // ^ throws exception if key not found

            final ItemStock itemStock = gameBoard.getBiddingSquare().getItemStock();

            // Create Monsters, Traps, Adventurers, Rooms from config and add to ItemStock
            loadStock(itemStock, configObject);

            // Shuffle lists, update the shop offer and draw adventurers
            itemStock.shuffle(Long.parseLong(commandLine.getOptionValue("seed")));

            // Start StateMachine and send it to the ActionFactory
            final StateMachine stateMachine = new StateMachine(gameBoard, sc);
            actionFactory.setStateMachine(stateMachine);
            final State registrationState = new RegistrationState(stateMachine, configString);
            stateMachine.updateState(registrationState);
            try {
                stateMachine.runMachine();
            } catch (TimeoutException e) {
                final Logger logger = LoggerFactory.getLogger(Main.class);
                logger.trace(e.getMessage());
            }

        }

    }

    /**
     * loads the stock from configObject into the itemStock
     *
     * @param itemStock    itemStock to add the stock to
     * @param configObject configObject to read from
     */
    public static void loadStock(final ItemStock itemStock, final JSONObject configObject) {
        final JSONArray monstersArray = configObject.getJSONArray("monsters");
        final JSONArray trapsArray = configObject.getJSONArray("traps");
        final JSONArray adventurersArray = configObject.getJSONArray("adventurers");
        final JSONArray roomsArray = configObject.getJSONArray("rooms");

        for (final Object m : monstersArray) {
            final JSONObject monsterObject = (JSONObject) m;
            final MonsterBuilder monsterBuilder = new MonsterBuilder();
            monsterBuilder.withID(monsterObject.getInt("id"))
                    .withHunger(monsterObject.optInt("hunger", 0))
                    .withEvilness(monsterObject.optInt("evilness", 0))
                    .withDamage(monsterObject.getInt("damage"))
                    .withAttackStrategy(
                            AttackStrategy.getAttackStrategyByString(monsterObject.getString(
                                    "attackStrategy"))
                    );
            itemStock.addMonster(monsterBuilder.build());
        }

        for (final Object t : trapsArray) {
            final JSONObject trapObject = (JSONObject) t;
            final TrapBuilder trapBuilder = new TrapBuilder();
            trapBuilder.withID(trapObject.getInt("id"))
                    .withDamage(trapObject.getInt("damage"))
                    .withAttackStrategy(
                            AttackStrategy.getAttackStrategyByString(trapObject.getString(
                                    "attackStrategy"))
                    );
            if (trapBuilder.getAttackStrategy() == AttackStrategy.TARGETED) {
                trapBuilder.withTarget(trapObject.getInt("target"));
            }
            itemStock.addTrap(trapBuilder.build());
        }

        for (final Object a : adventurersArray) {
            final JSONObject adventurerObject = (JSONObject) a;
            final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
            adventurerBuilder.withId(adventurerObject.getInt("id"))
                    .withDifficulty(adventurerObject.getInt("difficulty"))
                    .withHealthPoints(adventurerObject.getInt("healthPoints"))
                    .withHealValue(adventurerObject.optInt("healValue", 0))
                    .withDefuseValue(
                            adventurerObject.optInt("defuseValue", 0)
                    )
                    .withCharge(adventurerObject.optBoolean("charge", false));
            itemStock.addAdventurer(adventurerBuilder.build());
        }

        for (final Object r : roomsArray) {
            final JSONObject roomObject = (JSONObject) r;
            final RoomBuilder roomBuilder = new RoomBuilder();
            roomBuilder.withId(roomObject.getInt("id"))
                    .withActivation(roomObject.getInt("activation"))
                    .withRestriction(
                            Restriction.getRestrictionByString(
                                    roomObject.getString("restriction"))
                    )
                    .withFood(roomObject.optInt("food", 0))
                    .withGold(roomObject.optInt("gold", 0))
                    .withImps(roomObject.optInt("imps", 0))
                    .withNiceness(roomObject.optInt("niceness", 0));
            itemStock.addRoom(roomBuilder.build());
        }
    }

    /**
     * parses the command line arguments
     *
     * @param args the raw String arguments
     * @return commandLine
     * @throws ParseException when command line arguments are invalid
     */
    public static CommandLine getCommandLineArgs(final String[] args) throws ParseException {
        final Option optionConfig = Option.builder().longOpt("config").required(true)
                .hasArg().build();
        final Option optionPort = Option.builder().longOpt("port").required(true)
                .hasArg().build();
        final Option optionSeed = Option.builder().longOpt("seed").required(true).hasArg()
                .build();
        final Option optionTimeout = Option.builder().longOpt("timeout").required(true).hasArg()
                .build();
        final Options options = new Options();
        options.addOption(optionConfig).addOption(optionPort)
                .addOption(optionSeed).addOption(optionTimeout);

        final CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

}
