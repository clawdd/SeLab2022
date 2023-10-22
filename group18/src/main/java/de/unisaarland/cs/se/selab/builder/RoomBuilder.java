package de.unisaarland.cs.se.selab.builder;

import de.unisaarland.cs.se.selab.gamelogic.dungeon.Restriction;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;

/**
 * This class builds Rooms
 */
public class RoomBuilder {

    //set default values
    private int id;

    private int activation;

    private Restriction restriction;

    private int food;

    private int gold;

    private int imps;

    private int niceness;

    /**
     * @param id unique id of the room
     * @return this
     */
    public RoomBuilder withId(final int id) {
        this.id = id;
        return this;
    }

    /**
     * @param activation activation value
     * @return this
     */
    public RoomBuilder withActivation(final int activation) {
        this.activation = activation;
        return this;
    }

    /**
     * @param restriction place Restriction of a room
     * @return this
     */
    public RoomBuilder withRestriction(final Restriction restriction) {
        this.restriction = restriction;
        return this;
    }

    /**
     * @param food amount of food produced
     * @return this
     */
    public RoomBuilder withFood(final int food) {
        this.food = food;
        return this;
    }

    /**
     * @param gold amount of gold produced
     * @return this
     */
    public RoomBuilder withGold(final int gold) {
        this.gold = gold;
        return this;
    }

    /**
     * @param  imps amount of imps produced
     * @return this
     */
    public RoomBuilder withImps(final int imps) {
        this.imps = imps;
        return this;
    }

    /**
     * @param niceness amount of niceness produced
     * @return this
     */
    public RoomBuilder withNiceness(final int niceness) {
        this.niceness = niceness;
        return this;
    }

    /**
     * @return returns a Room
     */
    public Room build() {
        return new Room(this);
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @return activation
     */
    public int getActivation() {
        return activation;
    }

    /**
     * @return restriction
     */
    public Restriction getRestriction() {
        return restriction;
    }

    /**
     * @return food
     */
    public int getFood() {
        return food;
    }

    /**
     * @return gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @return imps
     */
    public int getImps() {
        return imps;
    }

    /**
     * @return niceness
     */
    public int getNiceness() {
        return niceness;
    }
}
