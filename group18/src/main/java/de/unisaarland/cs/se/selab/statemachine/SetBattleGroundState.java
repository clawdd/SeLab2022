package de.unisaarland.cs.se.selab.statemachine;

import static de.unisaarland.cs.se.selab.statemachine.StateEnum.SETBATTLEGROUND;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommandEnum;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

/**
 * This states handles the setting of the battleground
 */
public class SetBattleGroundState extends State {


    /**
     * SETBATTLEGROUNDSTATE hard-coded enum-value
     *
     * @param stateMachine in order to access the StateMachine and e.g. updateState
     */
    public SetBattleGroundState(final StateMachine stateMachine) {
        super(stateMachine, SETBATTLEGROUND);
    }

    /**
     * concrete implementation of run in this state: - wait for nextAction - - Events and Actions: -
     * invoke execute-method
     */
    @Override
    public void run() throws TimeoutException {
        if (stateMachine.getCombatPlayerStack().isEmpty()) {
            if (stateMachine.getGameBoard().isLastYear()) {
                stateMachine.updateState(new EndState(stateMachine));
                return;
            }
            stateMachine.updateState(new BiddingPlaceState(stateMachine));
            super.getStateMachine().getGameBoard().setCurrentSeason(0);
            return;
        }
        final var gb = super.getStateMachine().getGameBoard();
        if (gb.isLastSeason()) {
            stateMachine.getCombatPlayerStack().pop();
            setStateToBattleGround();
            return;
        } else if (stateMachine.getCombatPlayerStack().peek().getDungeon()
                .getAdventurers().isEmpty()) {
            stateMachine.getCombatPlayerStack().pop();
            setStateToBattleGround();
            return;
        }
        final var dl = stateMachine.getCombatPlayerStack().peek().getDungeon();
        gb.nextSeason();
        if (dl.isDungeonFullyConquered()) {
            if (dl.adventurerFlee()) {
                stateMachine.getCombatPlayerStack().peek().changeEvilness(-1);
                stateMachine.updateState(new SetBattleGroundState(stateMachine));
            } else {
                stateMachine.getCombatPlayerStack().pop();
                setStateToBattleGround();
            }
            return;
        }
        stateMachine.getSc().sendSetBattleGround(stateMachine
                .getCombatPlayerStack().peek().getCommId());

        // execute next action until BATTLEGROUND (inclusive) or the player left
        if (stateMachine.doCombatAndCheckIfLeft(ActionCommandEnum.BATTLEGROUND)) {
            return;
        }

        stateMachine.updateState(new DefendState(stateMachine));
    }


    private void setStateToBattleGround() {
        stateMachine.updateState(new SetBattleGroundState(stateMachine));
        super.getStateMachine().getGameBoard().setCurrentSeason(0);
    }
}
