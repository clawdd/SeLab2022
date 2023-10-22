package de.unisaarland.cs.se.selab.builder;

import de.unisaarland.cs.se.selab.gamelogic.creatures.AttackStrategy;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;

/**
 * This class builds Monsters
 */
public class MonsterBuilder {

    //set default values
    private int id;

    private int hunger; // hunger >= 0

    private int evilness; // evilness >= 0

    private int damage; // damage >= 1

    private AttackStrategy attackStrategy;

    /**
     * @param id unique id of the monster
     * @return this
     */
    public MonsterBuilder withID(final int id) {
        this.id = id;
        return this;
    }

    /**
     * @param hunger hunger value of the monster
     * @return this
     */
    public MonsterBuilder withHunger(final int hunger) {
        this.hunger = hunger;
        return this;
    }

    /**
     * @param evilness evilness value of the monster
     * @return this
     */
    public MonsterBuilder withEvilness(final int evilness) {
        this.evilness = evilness;
        return this;
    }

    /**
     * @param damage damage value of the monster
     * @return this
     */
    public MonsterBuilder withDamage(final int damage) {
        this.damage = damage;
        return this;
    }

    /**
     * @param attackStrategy the attackStrategy of the monser
     * @return this
     */
    public MonsterBuilder withAttackStrategy(final AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
        return this;
    }

    /**
     * @return returns a Monster
     */
    public Monster build() {
        return new Monster(this);
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @return hunger
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * @return evilness
     */
    public int getEvilness() {
        return evilness;
    }

    /**
     * @return damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return attackStrategy
     */
    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }
}
