package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;

/**
 * This Action handles a targeted attack of a Monster
 */
public class MonsterTargetedCommand extends ActionCommand {

    private final int monsterId;
    private final int position;

    /**
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId of player
     * @param monsterId        monsterId
     * @param position         position of target
     */
    public MonsterTargetedCommand(final StateMachine stateMachine,
                                  final ServerConnection<ActionCommand> serverConnection,
                                  final int commId, final int monsterId,
                                  final int position) {
        super(stateMachine, serverConnection, commId);
        this.monsterId = monsterId;
        this.position = position;
    }

    /**
     * @return corresponding ActionCommandEnum
     */
    @Override
    public ActionCommandEnum execute(final List<Integer> allowedCommIds) {
        if (!allowedCommIds.contains(getCommId())) {
            getServerConnection().sendActionFailed(getCommId(),
                    "You're not allowed to do this now!!");
            return ActionCommandEnum.ACTIONWRONGPLAYER;
        }

        return monsterAction(true, monsterId, position);
    }
}
