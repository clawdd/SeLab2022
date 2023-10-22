package de.unisaarland.cs.se.selab.gamelogic.creatures;

import de.unisaarland.cs.se.selab.builder.TrapBuilder;

/**
 * This class represents a trap
 */
public class Trap {

    private final int id;

    private final int damage;

    private final int target;

    private final AttackStrategy attackStrategy;

    /**
     * @param builder TrapConstructor
     */
    public Trap(final TrapBuilder builder) {
        this.id = builder.getId();
        this.damage = builder.getDamage();
        this.target = builder.getTarget();
        this.attackStrategy = builder.getAttackStrategy();
    }

    /**
     * @return returns the unique id of a trap
     */
    public int getId() {
        return id;
    }

    /**
     * @return returns the Trap damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return returns the chosen target position
     */
    public int getTarget() {
        return target - 1;
    }

    /**
     * @return returns the AttackStrategy of the trap
     */
    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }

    @Override
    public String toString() {
        return "Trap(id " + getId() + ", damage " + getDamage()
                + ", target " + getTarget() + ")";
    }

}