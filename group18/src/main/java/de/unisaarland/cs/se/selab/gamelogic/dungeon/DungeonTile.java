package de.unisaarland.cs.se.selab.gamelogic.dungeon;

/**
 * This class represents a dungeon tile of the dungeon
 */
public abstract class DungeonTile {

    private boolean conquered;

    /**
     * Create a DungeonTile
     */
    public DungeonTile() {
        this.conquered = false;
    }

    /**
     * @return conquered
     */
    public boolean getConquered() {
        return this.conquered;
    }

    /**
     * set conquered
     *
     * @param conquered conquered
     */
    public void setConquered(final boolean conquered) {
        this.conquered = true;
    }

    /**
     * @return isTunnel
     */
    public abstract boolean isTunnel();

    /**
     * @return isRoom
     */
    public abstract boolean isRoom();

}
