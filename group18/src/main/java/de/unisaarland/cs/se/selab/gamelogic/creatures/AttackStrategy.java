package de.unisaarland.cs.se.selab.gamelogic.creatures;

/**
 * This enum handles the respective AttackStrategy of traps and monsters
 */
public enum AttackStrategy {
    /**
     * attack on first adventurer
     */
    BASIC,
    /**
     * attack on all adventurers simultaneously
     */
    MULTI,
    /**
     * player can choose target of monster
     */
    TARGETED;

    /**
     * @param s the String to convert
     * @return the corresponding AttackStrategy, or null if the String doesn't match
     */
    public static AttackStrategy getAttackStrategyByString(final String s) {
        return switch (s) {
            case "BASIC" -> BASIC;
            case "MULTI" -> MULTI;
            case "TARGETED" -> TARGETED;
            default -> null;
        };
    }
}
