package de.unisaarland.cs.se.selab.gamelogic.creatures;

import de.unisaarland.cs.se.selab.builder.MonsterBuilder;

/**
 * This class represents a monster
 */
public class Monster {

    private final int id;

    private final int hunger;

    private final int evilness;

    private final int damage;

    private boolean available;

    private final AttackStrategy attackStrategy;

    private int monsterTarget;

    /**
     * @param builder MonsterConstructor
     */
    public Monster(final MonsterBuilder builder) {
        this.id = builder.getId();
        this.hunger = builder.getHunger();
        this.available = true;
        this.damage = builder.getDamage();
        this.evilness = builder.getEvilness();
        this.attackStrategy = builder.getAttackStrategy();
        this.monsterTarget = -1;
    }

    /**
     * @return returns the unique id of the monster
     */
    public int getId() {
        return id;
    }

    /**
     * @return returns the hunger of the Monster
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * @return returns Evilness of the Monster
     */
    public int getEvilness() {
        return evilness;
    }

    /**
     * @return returns the Monster damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return returns if a Monster is currently available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @return returns AttackStrategy
     */
    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }

    /**
     * @param available available
     */
    public void setAvailable(final boolean available) {
        this.available = available;
    }

    /**
     * returns the current Target of this monster
     *
     * @return 1, 2, 3 or -1 if monster has no target
     */
    public int getMonsterTarget() {
        return monsterTarget - 1;
    }

    /**
     * set the target of a monster
     *
     * @param monsterTarget set target
     */
    public void setMonsterTarget(final int monsterTarget) {
        if (attackStrategy == AttackStrategy.TARGETED) {
            this.monsterTarget = monsterTarget;
        }
    }

    @Override
    public String toString() {
        return "Monster(id " + getId() + ", hunger " + getHunger()
                + ", evilness " + getEvilness() + ", damage " + getDamage()
                + ", available " + isAvailable() + ", attackStrategy"
                + getAttackStrategy().toString() + ", monsterTarget " + getMonsterTarget()
                + ")";
    }
}
