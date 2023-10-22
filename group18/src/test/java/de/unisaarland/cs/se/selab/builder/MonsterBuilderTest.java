package de.unisaarland.cs.se.selab.builder;


import de.unisaarland.cs.se.selab.gamelogic.creatures.AttackStrategy;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing the methods in Monster
 */
class MonsterBuilderTest {

    final MonsterBuilder monsterBuilder = new MonsterBuilder();

    /**
     * @return Monster with default values
     */
    public Monster buildMonsterDefaultValues() {
        final MonsterBuilder monsterBuilder = new MonsterBuilder();
        return monsterBuilder.build();
    }

    /**
     * @return Monster with default values and Basic attack Strategy
     */
    public Monster buildMonsterDefaultValuesBasic() {
        final MonsterBuilder monsterBuilder = new MonsterBuilder();
        monsterBuilder.withAttackStrategy(AttackStrategy.BASIC);
        return monsterBuilder.build();
    }

    /**
     * @return Monster with default values and Multi attack Strategy
     */
    public Monster buildMonsterDefaultValuesMulti() {
        final MonsterBuilder monsterBuilder = new MonsterBuilder();
        monsterBuilder.withAttackStrategy(AttackStrategy.MULTI);
        return monsterBuilder.build();
    }

    /**
     * @return Monster with default values and Targeted attack Strategy
     */
    public Monster buildMonsterDefaultValuesTargeted() {
        final MonsterBuilder monsterBuilder = new MonsterBuilder();
        monsterBuilder.withAttackStrategy(AttackStrategy.TARGETED);
        return monsterBuilder.build();
    }

    @Test
    void withID() {
        Assertions.assertEquals(0, buildMonsterDefaultValues().getId());
    }

    @Test
    void withID30() {
        monsterBuilder.withDamage(2);
        monsterBuilder.withID(30);
        final Monster monster = monsterBuilder.build();
        Assertions.assertEquals(30, monster.getId());
    }

    @Test
    void withHunger() {
        Assertions.assertEquals(0, buildMonsterDefaultValues().getHunger());
    }

    @Test
    void withHunger4() {
        monsterBuilder.withDamage(2);
        monsterBuilder.withID(35);
        monsterBuilder.withHunger(4);
        final Monster monster = monsterBuilder.build();
        Assertions.assertEquals(4, monster.getHunger());
    }

    @Test
    void withEvilness() {
        Assertions.assertEquals(0, buildMonsterDefaultValues().getEvilness());
    }

    @Test
    void withEvilness10() {
        monsterBuilder.withEvilness(10);
        final Monster monster = monsterBuilder.build();
        Assertions.assertEquals(10, monster.getEvilness());
    }

    @Test
    void withDamage() {
        monsterBuilder.withDamage(2);
        Assertions.assertTrue(monsterBuilder.build().getDamage() >= 1);
    }

    @Test
    void withAttackStrategyBasic() {
        Assertions.assertEquals(AttackStrategy.BASIC,
                buildMonsterDefaultValuesBasic().getAttackStrategy());
    }

    @Test
    void withAttackStrategyMulti() {
        Assertions.assertEquals(AttackStrategy.MULTI,
                buildMonsterDefaultValuesMulti().getAttackStrategy());
    }

    @Test
    void withAttackStrategyTargeted() {
        Assertions.assertEquals(AttackStrategy.TARGETED,
                buildMonsterDefaultValuesTargeted().getAttackStrategy());
    }

    @Test
    void checkAvailabilityTrue() {
        Assertions.assertTrue(buildMonsterDefaultValues().isAvailable());
    }

    @Test
    void checkAvailabilityFalse() {
        final Monster monster = monsterBuilder.build();
        monster.setAvailable(false);
        Assertions.assertFalse(monster.isAvailable());
    }

    @Test
    void checkAvailabilityTrueAgain() {
        final Monster monster = monsterBuilder.build();
        monster.setAvailable(false);
        monster.setAvailable(true);
        Assertions.assertTrue(monster.isAvailable());
    }

    @Test
    void checkCorrectTargetBasic() {
        monsterBuilder.withAttackStrategy(AttackStrategy.BASIC);
        final Monster monster = monsterBuilder.build();
        Assertions.assertEquals(-2, monster.getMonsterTarget());
    }

    @Test
    void checkCorrectTargetMulti() {
        monsterBuilder.withAttackStrategy(AttackStrategy.MULTI);
        final Monster monster = monsterBuilder.build();
        Assertions.assertEquals(-2, monster.getMonsterTarget());
    }

    @Test
    void checkCorrectTargetTargeted3() {
        monsterBuilder.withAttackStrategy(AttackStrategy.TARGETED);
        final Monster monster = monsterBuilder.build();
        monster.setMonsterTarget(2);
        monster.setAvailable(false);
        monster.setAvailable(true);
        monster.setMonsterTarget(3);
        Assertions.assertEquals(2, monster.getMonsterTarget());
    }

}