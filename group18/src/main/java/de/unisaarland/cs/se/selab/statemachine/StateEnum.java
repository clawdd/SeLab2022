package de.unisaarland.cs.se.selab.statemachine;

/**
 * This enum represents the states
 */
public enum StateEnum {
    /**
     * used to indicate current state RegistrationState
     */
    REGISTRATION,
    /**
     * used to indicate current state BiddingPlaceState
     */
    BIDDINGPLACE,
    /**
     * used to indicate current state BiddingEvalState
     */
    BIDDINGEVAL,
    /**
     * used to indicate current state BiddingEvalState at Tunnel
     */
    BIDDINGEVALTUNNELETC,
    /**
     * used to indicate current state BiddingEvalState at Monster
     */
    BIDDINGEVALMONSTERETC,
    /**
     * used to indicate current state BiddingEvalState at Room
     */
    BIDDINGEVALROOMETC,
    /**
     * used to indicate current state SetBattleGroundState
     */
    SETBATTLEGROUND,
    /**
     * used to indicate current state DefendState
     */
    DEFEND,
    /**
     * used to indicate current state EndState
     */
    END,
    /**
     * used to indicate the real end!
     */
    REALEND;
}
