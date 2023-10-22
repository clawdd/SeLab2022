package de.unisaarland.cs.se.selab.gamelogic;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameBoardTestHCP {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC = new ServerConnection<>(5014, -1,
            ACTION_FACTORY);

    @Test
    void registerPlaye1() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        gameBoard.addPlayer("Jonathan", 202);
    }

    @Test
    void registerPlayer2() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        gameBoard.addPlayer("Jonathan", 202);
        gameBoard.removePlayerSilently(202);
    }

    @Test
    void registerPlayer3() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        gameBoard.removePlayerSilently(202);
    }

    @Test
    void checkLastSeason() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        assertFalse(gameBoard.isLastSeason());
    }

    @Test
    void checkLastYear() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        assertFalse(gameBoard.isLastYear());
    }

    @Test
    void setCurrentPlayerEmpty() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Jonathan", 0, 1234, 5,
                30, 1, 6, 13, SC, list);
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        gameBoard.setCurrentPlayer(dl);
        gameBoard.removePlayerSilently(1234);
        gameBoard.setCounterForCurrentPlayer();
    }

    @Test
    void inGame() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Jonathan", 0, 1234, 5,
                30, 1, 6, 13, SC, list);
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        gameBoard.addPlayer("Jonathan", 12);
        assertTrue(gameBoard.playerIsInTheGame(dl));
    }

    @Test
    void notInGame() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Jonathan", 0, 1234, 5,
                30, 1, 6, 13, SC, list);
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        assertFalse(gameBoard.playerIsInTheGame(dl));
    }

    @Test
    void maxYears() {
        final GameBoard gameBoard = new GameBoard(SC, 2, 2, 2, 2, 2, 2, 2);
        assertEquals(2, gameBoard.getMaxYears());
    }

    @Test
    void maxPlyers() {
        final GameBoard gameBoard = new GameBoard(SC, 3, 2, 2, 2, 2, 2, 2);
        assertEquals(3, gameBoard.getMaxPlayers());
    }

    @Test
    void currentSeason() {
        final GameBoard gameBoard = new GameBoard(SC, 3, 2, 2, 2, 2, 2, 2);
        assertEquals(0, gameBoard.getCurrentSeason());
    }

    @Test
    void sideLength() {
        final GameBoard gameBoard = new GameBoard(SC, 3, 2, 2, 2, 2, 2, 5);
        assertEquals(5, gameBoard.getDungeonSideLength());
    }

    @Test
    void checkDungeonLord() {
        final GameBoard gameBoard = new GameBoard(SC, 3, 2, 2, 2, 2, 2, 5);
        gameBoard.addPlayer("Jonathan", 213);
        assertNull(gameBoard.getCurrentPlayer());
    }

}