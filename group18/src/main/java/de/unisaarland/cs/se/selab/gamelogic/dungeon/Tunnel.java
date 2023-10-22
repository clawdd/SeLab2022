package de.unisaarland.cs.se.selab.gamelogic.dungeon;

/**
 * This class represents a tunnel of the dungeon
 */
public class Tunnel extends DungeonTile {

    private final boolean tunnel;

    /**
     * Create a Tunnel
     */
    public Tunnel() {
        super();
        this.tunnel = true;
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
        return false;
    }
}
