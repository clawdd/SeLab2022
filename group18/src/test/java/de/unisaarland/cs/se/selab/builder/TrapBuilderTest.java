package de.unisaarland.cs.se.selab.builder;

import de.unisaarland.cs.se.selab.gamelogic.creatures.AttackStrategy;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing all functionalities of Trap
 */
class TrapBuilderTest {

    final TrapBuilder trapBuilder = new TrapBuilder();

    @Test
    void withID55() {
        trapBuilder.withID(55);
        final Trap trap = trapBuilder.build();
        Assertions.assertEquals(55, trap.getId());
    }

    @Test
    void withDamage() {
        trapBuilder.withDamage(12);
        final Trap trap = trapBuilder.build();
        Assertions.assertEquals(12, trap.getDamage());
    }

    @Test
    void withTarget0() {
        trapBuilder.withTarget(1);
        final Trap trap = trapBuilder.build();
        Assertions.assertEquals(0, trap.getTarget());
    }

    @Test
    void withTarget2() {
        trapBuilder.withTarget(3);
        final Trap trap = trapBuilder.build();
        Assertions.assertEquals(2, trap.getTarget());
    }

    @Test
    void withAttackStrategyBasic() {
        trapBuilder.withAttackStrategy(AttackStrategy.BASIC);
        final Trap trap = trapBuilder.build();
        Assertions.assertEquals(AttackStrategy.BASIC, trap.getAttackStrategy());
    }

    @Test
    void withAttackStrategyMulti() {
        trapBuilder.withAttackStrategy(AttackStrategy.MULTI);
        final Trap trap = trapBuilder.build();
        Assertions.assertEquals(AttackStrategy.MULTI, trap.getAttackStrategy());
    }

    @Test
    void withAttackStrategyTargeted() {
        trapBuilder.withAttackStrategy(AttackStrategy.TARGETED);
        final Trap trap = trapBuilder.build();
        Assertions.assertEquals(AttackStrategy.TARGETED, trap.getAttackStrategy());
    }
}