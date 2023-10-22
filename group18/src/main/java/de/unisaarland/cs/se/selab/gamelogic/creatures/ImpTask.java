package de.unisaarland.cs.se.selab.gamelogic.creatures;

/**
 * This enum contains all tasks an imp can take
 */
public enum ImpTask {
    /**
     * REST the imps is currently resting and available for all tasks
     */
    REST,
    /**
     * GOLD the imps are mining gold
     */
    GOLD,
    /**
     * TUNNEL the imps are mining tunnels
     */
    TUNNEL,
    /**
     * SUPERVISE the imp is supervising other imps
     */
    SUPERVISE,
    /**
     * ROOM the imps are producing in a room
     */
    ROOM
}
