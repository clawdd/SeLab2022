package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.List;

/**
 * This Action hires a Monster
 */
public class HireMonsterCommand extends ActionCommand {

    private final int monsterId;

    /**
     * Constructor
     *
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId
     * @param monsterId        monsterId
     */
    public HireMonsterCommand(final StateMachine stateMachine,
                              final ServerConnection<ActionCommand> serverConnection,
                              final int commId, final int monsterId) {
        super(stateMachine, serverConnection, commId);
        this.monsterId = monsterId;
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

        final var playerMaybe =
                super.getStateMachine().getGameBoard().getDungeonLordByCommId(getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (super.getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.BIDDINGEVALMONSTERETC) {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "monsters can only be hired during BIDDINGEVAL");
            return ActionCommandEnum.ACTIONFAILED;
        }


        final var monster = super.getStateMachine().getGameBoard().getBiddingSquare()
                .getItemStock().getMonsterById(monsterId);
        if (monster == null) {
            super.getServerConnection().sendActionFailed(super.getCommId(),
                    "monster does not exist");
            return ActionCommandEnum.ACTIONFAILED;
        }
        final var player = playerMaybe.get();
        if (monster.getHunger() > 0) {
            if (!player.checkFood(monster.getHunger())) {
                super.getServerConnection().sendActionFailed(super.getCommId(),
                        "to less food");
                return ActionCommandEnum.ACTIONFAILED;
            }
        }
        if (monster.getEvilness() > 0) {
            if (!player.checkEvilness(monster.getEvilness())) {
                super.getServerConnection().sendActionFailed(super.getCommId(),
                        "not enough niceness");
                return ActionCommandEnum.ACTIONFAILED;
            }
        }
        if (monster.getHunger() != 0) {
            player.changeFood(-monster.getHunger());
        }
        if (monster.getEvilness() != 0) {
            player.changeEvilness(monster.getEvilness());
        }
        player.addMonster(monster);
        super.getStateMachine().getGameBoard().getBiddingSquare()
                .getItemStock().getShopOfferMonsters().remove(monster);
        getStateMachine().getGameBoard().getPlayers().forEach(p ->
                getServerConnection().sendMonsterHired(p.getCommId(), monsterId, player.getId()));
        return ActionCommandEnum.HIREMONSTER;
    }
}
