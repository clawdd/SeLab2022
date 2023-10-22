package de.unisaarland.cs.se.selab.builder;

import de.unisaarland.cs.se.selab.gamelogic.creatures.AttackStrategy;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;

/**
 * This class builds Traps
 * !!! WARNING: To target with targeted traps use 1, 2 or 3 to
 * target the index 0 , 1 or 2
 */
public class TrapBuilder {

    //Set default values
    private int id;

    private int damage; // int damage >= 0

    private int target;

    private AttackStrategy attackStrategy;

    /**
     * @param id unique id of the trap
     * @return this
     */
    public TrapBuilder withID(final int id) {
        this.id = id;
        return this;
    }

    /**
     * @param damage damage of the trap
     * @return this
     */
    public TrapBuilder withDamage(final int damage) {
        this.damage = damage;
        return this;
    }

    /**
     * @param target chosen target of the trap
     * @return this
     */
    public TrapBuilder withTarget(final int target) {
        this.target = target;
        return this;
    }

    /**
     * @param attackStrategy the attackStrategy of the trap
     * @return this
     */
    public TrapBuilder withAttackStrategy(final AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
        return this;
    }

    /**
     * @return returns a Trap
     */
    public Trap build() {
        return new Trap(this);
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @return damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return target
     */
    public int getTarget() {
        return target;
    }

    /**
     * @return attackStrategy
     */
    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }
}
