package de.unisaarland.cs.se.selab.actioncommand;

import static org.junit.jupiter.api.Assertions.assertSame;

import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import de.unisaarland.cs.se.selab.statemachine.StateMachine;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


class PlaceBidCommandTestHCP {
    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(3230, -1, ACTION_FACTORY);

    @Test
    void execute() {

        final List<DungeonLord> playerList = new ArrayList<>();
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final DungeonLord dl = new DungeonLord("Han Solo", 42, 38, 1,
                10, 5, 20, 15, SC, playerList);
        final DungeonLord nyt = new DungeonLord("Wrong.IO", 18, 18, 1,
                10, 5, 20, 15, SC, playerList);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final List<Integer> allowedCommIds = new ArrayList<>();
        allowedCommIds.add(30);
        playerList.add(dl);
        playerList.add(nyt);
        final PlaceBidCommand placeBidCommand =
                new PlaceBidCommand(stateMachine, SC, 30, BidType.FOOD, 1);
        assertSame(ActionCommandEnum.ACTIONFAILED, placeBidCommand.execute(allowedCommIds));
    }
}