package de.unisaarland.cs.se.selab.statemachine;

import static de.unisaarland.cs.se.selab.statemachine.StateEnum.DEFEND;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommandEnum;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

/**
 * This state handles the combat phase
 */
public class DefendState extends State {


    /**
     * DEFENDSTATE hard-coded enum-value
     *
     * @param stateMachine in order to access the StateMachine and e.g. updateState
     */
    public DefendState(final StateMachine stateMachine) {
        super(stateMachine, DEFEND);
    }

    /**
     * concrete implementation of run in this state: - wait for nextAction - - Events and Actions: -
     * invoke execute-method
     */
    @Override
    public void run() throws TimeoutException {
        // check if there are players left who need to run through the defend state
        if (stateMachine.getCombatPlayerStack().isEmpty()) {
            stateMachine.updateState(new BiddingPlaceState(stateMachine));
            super.getStateMachine().getGameBoard().setCurrentSeason(0);
            return;
        }

        // send event DefendYourself to respective player
        final var dl = stateMachine.getCombatPlayerStack().peek();
        stateMachine.getSc().sendDefendYourself(dl.getCommId());

        // execute next action until ENDTURN (inclusive) or the player left
        if (stateMachine.doCombatAndCheckIfLeft(ActionCommandEnum.ENDTURN)) {
            return;
        }

        dl.getDungeon().evalTrap(getStateMachine().getSc(), dl);
        dl.getDungeon().evalMonsters(getStateMachine().getSc(), dl,
                dl.getDungeon().getAdventurers());
        dl.getDungeon().evalFatigue(getStateMachine().getSc(), dl,
                dl.getDungeon().getAdventurers());
        dl.getDungeon().conquerTunnel(getStateMachine().getSc(), dl);

        // check for healer and heal, if applicable
        if (dl.getDungeon().checkForHealer()) {
            dl.getDungeon().healAdventurer(getStateMachine().getSc());
        }

        dl.getDungeon().clearPlacedDefenses();

        stateMachine.updateState(new SetBattleGroundState(stateMachine));
    }
}
