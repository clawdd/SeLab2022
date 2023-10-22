package de.unisaarland.cs.se.selab.gamelogic.creatures;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.builder.AdventurerBuilder;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import java.util.List;

/**
 * This class handles Adventurer
 */

public class Adventurer {
    private final int id;

    private final int difficulty; // difficulty >= 0

    private int healthPoints; // healthPoints >= 0

    private final int maxHealthPoints;
    private final boolean charge;

    private final int healValue; // healValue >= 0

    private final int defuseValue; // defuseValue >= 0

    /**
     * @param builder creates Adventurer
     */
    public Adventurer(final AdventurerBuilder builder) {
        this.id = builder.getId();
        this.difficulty = builder.getDifficulty();
        this.healthPoints = builder.getHealthPoints();
        this.maxHealthPoints = builder.getHealthPoints();
        this.charge = builder.getCharge();
        this.healValue = builder.getHealValue();
        this.defuseValue = builder.getDefuseValue();
    }
    /**
     * @param adventurer Adventurer of which the healthPoints are checked
     * @return returns if fully healed or not
     */

    /**
     * @param damage          damage
     * @param sc              server connection
     * @param players         players
     * @param dl              DungeonLord
     * @param adventurersList AdventurerList
     */
    public void takeDamage(final int damage, final ServerConnection<ActionCommand> sc,
                           final List<DungeonLord> players, final DungeonLord dl,
                           final List<Adventurer> adventurersList) {
        if (damage <= 0) {
            return;
        }
        if (getHealthPoints() - damage <= 0) {

            players.forEach(p ->
                    sc.sendAdventurerImprisoned(p.getCommId(), getId()));

            setHealthPoints(0);
            dl.getDungeon().imprisonAdventurer(adventurersList, adventurersList.indexOf(this));

        }
        if (getHealthPoints() - damage > 0) {
            players.forEach(p -> sc.sendAdventurerDamaged(p.getCommId(), getId(), damage));
            setHealthPoints(getHealthPoints() - damage);
        }
    }

    /**
     * @return returns the unique id of an Adventurer
     */
    public int getId() {
        return id;
    }

    /**
     * @return returns the difficulty of the Adventurer
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @return returns the amount of Healpoints
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * @return returns if a Adventurer has charge or not
     */
    public boolean isCharge() {
        return charge;
    }

    /**
     * @return returns the HealValue of the Adventurer
     */
    public int getHealValue() {
        return healValue;
    }

    /**
     * @return gives us the max HealthPoints an Adventurer can have
     */
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    /**
     * @return returns the defuse value of the Adventurer
     */
    public int getDefuseValue() {
        return defuseValue;
    }

    /**
     * @param healthPoints Sets the healthPoints
     */
    public void setHealthPoints(final int healthPoints) {
        this.healthPoints = healthPoints;
    }

    @Override
    public String toString() {
        return "Adventurer(id " + getId() + ", difficulty " + getDifficulty()
                + ", healthPoints " + getHealthPoints()
                + ", maxHealthPoints " + getMaxHealthPoints() + ", charge " + isCharge()
                + ", healValue " + getHealValue() + ", defuseValue " + getDefuseValue() + ")";
    }

}