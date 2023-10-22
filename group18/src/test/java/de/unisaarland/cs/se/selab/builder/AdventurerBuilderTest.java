package de.unisaarland.cs.se.selab.builder;

import de.unisaarland.cs.se.selab.gamelogic.creatures.Adventurer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class tests AdventurerBuilder
 */
class AdventurerBuilderTest {

    /**
     * @return list of 3 adventurers
     */
    public List<Adventurer> createListAdventurers() {
        final List<Adventurer> adventurers = new ArrayList<>();

        final AdventurerBuilder adventurerBuilder0 = new AdventurerBuilder();
        adventurerBuilder0.withHealthPoints(50);
        adventurerBuilder0.withHealValue(30);
        final Adventurer adventurer0 = adventurerBuilder0.build();
        adventurers.add(adventurer0);

        final AdventurerBuilder adventurerBuilder1 = new AdventurerBuilder();
        adventurerBuilder1.withHealthPoints(40);
        final Adventurer adventurer1 = adventurerBuilder1.build();
        adventurers.add(adventurer1);

        final AdventurerBuilder adventurerBuilder2 = new AdventurerBuilder();
        adventurerBuilder2.withHealthPoints(60);
        final Adventurer adventurer2 = adventurerBuilder2.build();
        adventurers.add(adventurer2);

        return adventurers;
    }

    @Test
    void createNormalAdventurer() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertFalse(adventurer.isCharge());
    }

    @Test
    void createWarrior() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withCharge(true);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertTrue(adventurer.isCharge());
    }

    @Test
    void createNormalAdventurerValidDifficulty() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertTrue(adventurer.getDifficulty() >= 0);
    }

    @Test
     void createNormalAdventurerInvalidDifficulty() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertFalse(adventurer.getDifficulty() < 0);
    }

    @Test
     void createNormalAdventurerDifficulty90() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withDifficulty(90);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(90, adventurer.getDifficulty());
    }

    @Test
     void createNormalAdventurerDefaultID() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withDifficulty(8);
        adventurerBuilder.withHealthPoints(20);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(0, adventurer.getId());
    }

    @Test
     void createNormalAdventurerID() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withId(42);
        adventurerBuilder.withDifficulty(20);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(42, adventurer.getId());
    }

    @Test
     void createThief() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withId(27);
        adventurerBuilder.withDifficulty(15);
        adventurerBuilder.withCharge(false);
        adventurerBuilder.withDefuseValue(10);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(10, adventurer.getDefuseValue());
    }

    @Test
     void checkForZeroDefuseValue() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withId(0);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(0, adventurer.getDefuseValue());
    }

    @Test
     void checkForZeroHealValue() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(0, adventurer.getHealValue());
    }

    @Test
     void checkForPriestHealValue() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withHealValue(15);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(15, adventurer.getHealValue());
    }

    @Test
     void checkHealthPoints() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(0, adventurer.getHealthPoints());
    }

    @Test
     void check20HealthPoints() {
        final AdventurerBuilder adventurerBuilder = new AdventurerBuilder();
        adventurerBuilder.withHealthPoints(20);
        final Adventurer adventurer = adventurerBuilder.build();
        Assertions.assertEquals(20, adventurer.getHealthPoints());
    }
    /*
    @Test
     void checkTakeDamageFirstAdventurer20() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(0).takeDamage(
            20,
            new ServerConnection<>(12345,1, new DungeonLord()),
            ); // has HP 50
        Assertions.assertEquals(30, adventurers.get(0).getHealthPoints());
    }

    @Test
     void checkTakeDamageFirstAdventurer50() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(0).takeDamage(50); // has HP 50
        Assertions.assertEquals(0, adventurers.get(0).getHealthPoints());
    }

    @Test
     void checkTakeDamageGreaterFirstAdventurer50() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(0).takeDamage(80); // has HP 50
        Assertions.assertEquals(0, adventurers.get(0).getHealthPoints());
    }

    @Test
     void checkTakeDamageSecondAdventurer20() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(0).takeDamage(20); // has HP 50
        Assertions.assertEquals(30, adventurers.get(0).getHealthPoints());
        Assertions.assertEquals(40, adventurers.get(1).getHealthPoints());
    }

    @Test
     void checkTakeDamageSecondAdventurer50() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(0).takeDamage(50); // has HP 50
        Assertions.assertEquals(0, adventurers.get(0).getHealthPoints());
        Assertions.assertEquals(40, adventurers.get(1).getHealthPoints());
    }

    @Test
     void checkTakeDamageGreaterSecondAdventurer50() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(1).takeDamage(80); // has HP 40
        Assertions.assertEquals(50, adventurers.get(0).getHealthPoints());
        Assertions.assertEquals(0, adventurers.get(1).getHealthPoints());
    }

    @Test
     void checkTakeDamageThirdAdventurer20() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(2).takeDamage(20); // has HP 50
        Assertions.assertEquals(40, adventurers.get(2).getHealthPoints());
    }

    @Test
     void checkTakeDamageThirdAdventurer50() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(2).takeDamage(50); // has HP 60
        Assertions.assertEquals(10, adventurers.get(2).getHealthPoints());
    }

    @Test
     void checkTakeDamageGreaterThirdAdventurer50() {
        final List<Adventurer> adventurers = createListAdventurers();
        adventurers.get(2).takeDamage(80); // has HP 40
        Assertions.assertEquals(0, adventurers.get(2).getHealthPoints());
    }
    */

    /*
    todo: reactivate and test --> currently ending in infinite loop
    @Test
    void checkHealPriestOnePatient() {
        final List<Adventurer> adventurers = createListAdventurers(); // adventurer1, healValue 30
        adventurers.get(1).takeDamage(20);
        adventurers.get(0).heal(adventurers);
        Assertions.assertEquals(20, adventurers.get(1).getHealthPoints());
    }

    @Test
    void priestHealing() {
        final List<Adventurer> adventurers = createListAdventurers(); // adventurer1, healValue 30
        adventurers.get(1).takeDamage(20); // HP 30 left
        adventurers.get(0).takeDamage(15); // HP 25 left
        adventurers.get(0).heal(adventurers);
        Assertions.assertEquals(50, adventurers.get(0).getHealthPoints());
        Assertions.assertEquals(35, adventurers.get(1).getHealthPoints());
    }
     */

}