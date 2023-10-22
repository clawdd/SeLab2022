package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;
import java.util.Optional;

/**
 * This Action ends a turn
 */
public class EndTurnCommand extends ActionCommand {

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId
     */
    public EndTurnCommand(final StateMachine stateMachine,
                          final ServerConnection<ActionCommand> serverConnection,
                          final int commId) {
        super(stateMachine, serverConnection, commId);
    }

    /**
     * checks whether EndTurn could be executed or not
     * -> checks whether EndTurn is valid
     * -> sends out ACTIONFAILED or returns correct ActionCommandEnum
     *
     * @return corresponding ActionCommandEnum
     */
    @Override
    public ActionCommandEnum execute(final List<Integer> allowedCommIds) {
        if (!allowedCommIds.contains(getCommId())) {
            getServerConnection().sendActionFailed(getCommId(),
                    "You're not allowed to do this now!!");
            return ActionCommandEnum.ACTIONWRONGPLAYER;
        }

        final Optional<DungeonLord> playerOptional =
                getStateMachine().getGameBoard().getDungeonLordByCommId(getCommId());

        // DungeonLord could not be found -> send ActionFailed
        if (playerOptional.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        // invalid EndTurn from client in BiddingPlaceState
        if (getStateMachine().getCurrentState().getCurrentStateEnum()
                == StateEnum.BIDDINGPLACE
                && !getStateMachine().getCurrentState().isLastBidOfPlayer(getCommId())) {
            getServerConnection().sendActionFailed(getCommId(),
                    "all 3 bids required per player");
            return ActionCommandEnum.ACTIONFAILED;

            // invalid EndTurn from client in SelectBattleGround
        } else if (getStateMachine().getCurrentState().getCurrentStateEnum()
                == StateEnum.SETBATTLEGROUND
            /*&& playerOptional.get().getDungeon().checkBattleGround()*/) {
            getServerConnection().sendActionFailed(getCommId(),
                    "battleground must be set");
            return ActionCommandEnum.ACTIONFAILED;
        }

        return ActionCommandEnum.ENDTURN;
    }
}
