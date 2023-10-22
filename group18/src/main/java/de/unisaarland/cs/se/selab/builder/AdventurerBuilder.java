package de.unisaarland.cs.se.selab.builder;

import de.unisaarland.cs.se.selab.gamelogic.creatures.Adventurer;

/**
 * This class builds the Adventurer
 */
public class AdventurerBuilder {

    //set default values
    private int id;

    private int difficulty; // difficulty >= 0

    private int healthPoints; // healthPoints >= 0

    private boolean charge;

    private int healValue; // healValue >= 0

    private int defuseValue; // defuseValue >= 0

    /**
     * @param id unique id of the adventurer
     * @return this
     */
    public AdventurerBuilder withId(final int id) {
        this.id = id;
        return this;
    }

    /**
     * @param difficulty difficultyValue of the adventurer
     * @return this
     */
    public AdventurerBuilder withDifficulty(final int difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    /**
     * @param healthPoints amount of healthPoints
     * @return this
     */
    public AdventurerBuilder withHealthPoints(final int healthPoints) {
        this.healthPoints = healthPoints;
        return this;
    }

    /**
     * @param charge charge true or false
     * @return this
     */
    public AdventurerBuilder withCharge(final boolean charge) {
        this.charge = charge;
        return this;
    }

    /**
     * @param healValue healValue of the adventurer
     * @return this
     */
    public AdventurerBuilder withHealValue(final int healValue) {
        this.healValue = healValue;
        return this;
    }

    /**
     * @param defuseValue defuseValue of the adventurer
     * @return this
     */
    public AdventurerBuilder withDefuseValue(final int defuseValue) {
        this.defuseValue = defuseValue;
        return this;
    }

    /**
     * @return retunrs a new Adventurer
     */
    public Adventurer build() {
        return new Adventurer(this);
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @return healthPoints
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * @return charge
     */
    public boolean getCharge() {
        return charge;
    }

    /**
     * @return healValue
     */
    public int getHealValue() {
        return healValue;
    }

    /**
     * @return defuseValue
     */
    public int getDefuseValue() {
        return defuseValue;
    }
}
