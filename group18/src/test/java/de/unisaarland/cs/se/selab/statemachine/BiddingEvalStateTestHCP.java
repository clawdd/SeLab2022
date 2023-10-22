package de.unisaarland.cs.se.selab.statemachine;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.GameBoard;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BiddingEvalStateTestHCP {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(6666, -1, ACTION_FACTORY);

    @Test
    void run() throws TimeoutException {
        final GameBoard gameBoard = new GameBoard(SC, 2, 4, 3,
                3, 3, 5, 5);
        final StateMachine stateMachine = new StateMachine(gameBoard, SC);
        final BiddingEvalState biddingEvalState = new BiddingEvalState(stateMachine);
        stateMachine.updateState(biddingEvalState);
        final List<DungeonLord> playerList = new ArrayList<>();
        final DungeonLord nyt = new DungeonLord("Dummy", 18, 18, 1,
                10, 5, 20, 15, SC, playerList);
        //final Room room = new RoomBuilder().withId(10).build();
        playerList.add(nyt);
        biddingEvalState.run();
    }
}