package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import de.unisaarland.cs.se.selab.builder.RoomBuilder;

/**
 * This class represents a room in the players dungeon
 */
public class Room extends DungeonTile {

    private final int id;
    private final int activationCost;
    private final Restriction restriction;
    private final int food;
    private final int gold;
    private final int imps;
    private final int niceness;
    private final boolean tunnel;
    private boolean active;

    /**
     * Create a new Room by using the Builder only
     *
     * @param roomBuilder room builder
     */
    public Room(final RoomBuilder roomBuilder) {
        super();
        this.id = roomBuilder.getId();
        this.activationCost = roomBuilder.getActivation();
        this.restriction = roomBuilder.getRestriction();
        this.food = roomBuilder.getFood();
        this.gold = roomBuilder.getGold();
        this.imps = roomBuilder.getImps();
        this.niceness = roomBuilder.getNiceness();
        this.tunnel = false;
        this.active = false;
    }

    /**
     * @return isTunnel
     */
    @Override
    public boolean isTunnel() {
        return tunnel;
    }

    @Override
    public boolean isRoom() {
        return true;
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @return activationCost
     */
    public int getActivationCost() {
        return activationCost;
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

    /**
     * @return isActive
     */
    public boolean isActive() {
        return active;
    }

    /**
     * activate Room
     */
    public void activate() {
        active = true;
    }

    /**
     * deactivate Room
     */
    public void deactivate() {
        active = false;
    }

    @Override
    public String toString() {
        return "Room(id " + getId() + ", activationCost " + getActivationCost()
                + ", restriction " + getRestriction().toString()
                + ", food " + getFood() + ", gold " + getGold()
                + ", imps " + getImps() + ", niceness " + getNiceness()
                + ", isTunnel " + isTunnel() + ", isActive " + isActive()
                + ")";
    }

}
