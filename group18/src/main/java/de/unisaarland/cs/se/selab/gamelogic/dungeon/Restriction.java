package de.unisaarland.cs.se.selab.gamelogic.dungeon;

/**
 * This enum contains the room restrictions
 */
public enum Restriction {
    /**
     * only placeable in upper half
     */
    UPPER_HALF,
    /**
     * only placeable in lower half
     */
    LOWER_HALF,
    /**
     * only placeable in outer ring
     */
    OUTER_RING,
    /**
     * only placeable in inner half
     */
    INNER_RING;

    /**
     * @param s the String to convert
     * @return the corresponding Restriction, or null if the String doesn't match
     */
    public static Restriction getRestrictionByString(final String s) {
        return switch (s) {
            case "UPPER_HALF" -> UPPER_HALF;
            case "LOWER_HALF" -> LOWER_HALF;
            case "INNER_RING" -> INNER_RING;
            case "OUTER_RING" -> OUTER_RING;
            default -> null;
        };
    }
}
