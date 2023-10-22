package de.unisaarland.cs.se.selab.actioncommand;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.creatures.AttackStrategy;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.awt.Point;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an abstract Action
 */
public abstract class ActionCommand {

    private final int commId;
    private final StateMachine stateMachine;
    private final ServerConnection<ActionCommand> serverConnection;

    /**
     * Constructor
     *
     * @param stateMachine     stateMachine
     * @param serverConnection serverConnection
     * @param commId           commId
     */
    public ActionCommand(final StateMachine stateMachine,
                         final ServerConnection<ActionCommand> serverConnection,
                         final int commId) {
        this.stateMachine = stateMachine;
        this.serverConnection = serverConnection;
        this.commId = commId;
    }

    /**
     * executes the Action Command
     *
     * @param commIds allowed / forbidden commIds that can execute this command (depends on the
     *                command)
     * @return the Enum of the ActionCommand
     */
    public abstract ActionCommandEnum execute(List<Integer> commIds);

    /**
     * Getter for commId
     *
     * @return commId
     */
    public int getCommId() {
        return commId;
    }

    /**
     * Getter for StateMachine
     *
     * @return stateMachine
     */
    public StateMachine getStateMachine() {
        return stateMachine;
    }

    /**
     * Getter for ServerConnection
     *
     * @return serverConnection
     */
    public ServerConnection<ActionCommand> getServerConnection() {
        return serverConnection;
    }

    /**
     * This method handles the placing of monster
     *
     * @param isTargeted check if the monster is a target monster
     * @param monsterId  monsterId
     * @param position   position of the target
     * @return ActionCommandEnum
     */
    public ActionCommandEnum monsterAction(final boolean isTargeted, final int monsterId,
                                           final int position) {
        final var playerMaybe = getStateMachine()
                .getGameBoard().getDungeonLordByCommId(getCommId());
        if (playerMaybe.isEmpty()) {
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (getStateMachine()
                .getCurrentState().getCurrentStateEnum() != StateEnum.DEFEND) {
            getServerConnection().sendActionFailed(getCommId(),
                    "false state");
            return ActionCommandEnum.ACTIONFAILED;
        }

        final var player = playerMaybe.get();

        Monster placedMonster;
        final Optional<Monster> t = player.getMonster().stream().filter(monster ->
                monster.getId() == monsterId).findFirst();
        if (t.isPresent()) {
            placedMonster = t.get();
        } else {
            getServerConnection().sendActionFailed(getCommId(),
                    "wrong monster selected");
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (placedMonster.getAttackStrategy() == AttackStrategy.TARGETED && !isTargeted
                || placedMonster.getAttackStrategy() != AttackStrategy.TARGETED && isTargeted) {
            getServerConnection().sendActionFailed(getCommId(),
                    "wrong monster selected");
            return ActionCommandEnum.ACTIONFAILED;
        }

        if (!placedMonster.isAvailable()) {
            getServerConnection().sendActionFailed(getCommId(),
                    "monster not available");
            return ActionCommandEnum.ACTIONFAILED;
        }
        final Point point = new Point(player.getDungeon().getBattleGroundPosition());
        if (player.getDungeon().getDungeonSquare()[point.x][point.y].isRoom()
                && player.getDungeon().getPlacedMonsters().size() >= 2) {
            getServerConnection().sendActionFailed(getCommId(),
                    "battlegroundFull");
            return ActionCommandEnum.ACTIONFAILED;
        } else if (player.getDungeon().getDungeonSquare()[point.x][point.y].isTunnel()
                && player.getDungeon().getPlacedMonsters().size() >= 1) {
            getServerConnection().sendActionFailed(getCommId(),
                    "battlegroundFull");
            return ActionCommandEnum.ACTIONFAILED;
        }
        player.getDungeon().getPlacedMonsters().add(placedMonster);
        placedMonster.setAvailable(false);
        getStateMachine().getGameBoard().getPlayers().forEach(p ->
                getServerConnection().sendMonsterPlaced(p.getCommId(), monsterId, player.getId()));
        return checkIfTarget(isTargeted, position, placedMonster);
    }

    @NotNull
    private static ActionCommandEnum checkIfTarget(final boolean isTargeted, final int position,
                                                   final Monster placedMonster) {
        if (isTargeted) {
            placedMonster.setMonsterTarget(position);
            return ActionCommandEnum.MONSTERTARGETED;
        } else {
            return ActionCommandEnum.MONSTER;
        }
    }

}
