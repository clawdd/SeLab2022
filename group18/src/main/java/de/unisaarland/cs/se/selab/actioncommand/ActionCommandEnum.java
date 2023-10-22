package de.unisaarland.cs.se.selab.actioncommand;

/**
 * This enum represents the type of action which is used as return value of ActionCommand
 */
public enum ActionCommandEnum {
    /**
     * return type of the execute-method of the ActionCommand,
     * called from TrapCommand
     */
    TRAP,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from BuildRoom
     */
    BUILDROOM,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from BattleGround
     */
    BATTLEGROUND,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from ActivateRoom
     */
    ACTIVATEROOM,
    /**
     * uzg
     */
    ACTIVATEROOMOTHERPLAYER,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from MonsterTargeted
     */
    MONSTERTARGETED,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from EndTurn
     */
    ENDTURN,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from Leave
     */
    LEAVE,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from Monster
     */
    MONSTER,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from HireMonster
     */
    HIREMONSTER,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from Register
     */
    REGISTER,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from PlaceBid
     */
    PLACEBID,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from StartGame
     */
    STARTGAME,
    /**
     * return type of the execute-method of the ActionCommand,
     * called from DigTunnel
     */
    DIGTUNNEL,
    /**
     * used to indicate that the intended action could not be performed
     */
    ACTIONFAILED,
    /**
     * used to indicate that the action came from a wrong player
     */
    ACTIONWRONGPLAYER
}
