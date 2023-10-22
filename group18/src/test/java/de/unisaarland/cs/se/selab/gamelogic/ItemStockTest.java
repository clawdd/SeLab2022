package de.unisaarland.cs.se.selab.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.builder.AdventurerBuilder;
import de.unisaarland.cs.se.selab.builder.MonsterBuilder;
import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import de.unisaarland.cs.se.selab.builder.TrapBuilder;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Adventurer;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import org.junit.jupiter.api.Test;

class ItemStockTest {

    static final ActionFactory<ActionCommand> AF = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC =
            new ServerConnection<>(1234, -1, AF);

    @Test
    void testGetShopOfferMonsters() {
        final ItemStock itemStock = new ItemStock();
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        assertEquals(gameBoard.getBiddingSquare().getItemStock().getShopOfferMonsters(),
                itemStock.getShopOfferMonsters());
    }

    @Test
    void testGetShopOfferRooms() {
        final ItemStock itemStock = new ItemStock();
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        assertEquals(gameBoard.getBiddingSquare().getItemStock().getShopOfferRooms(),
                itemStock.getShopOfferRooms());
    }

    @Test
    void testGetTownSquareAdventurer() {
        final ItemStock itemStock = new ItemStock();
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        assertEquals(gameBoard.getBiddingSquare().getItemStock()
                .getTownSquareAdventurer(), itemStock.getTownSquareAdventurer());
    }

    @Test
    void testAddMonster() {
        final MonsterBuilder mBuilder = new MonsterBuilder();
        final Monster monster = new Monster(mBuilder);
        final ItemStock itemStock = new ItemStock();
        itemStock.addMonster(monster);
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        gameBoard.getBiddingSquare().getItemStock().addMonster(monster);
        assertEquals(gameBoard.getBiddingSquare().getItemStock()
                .getAllMonsters(), itemStock.getAllMonsters());
    }

    @Test
    void testAddRoom() {
        final RoomBuilder rBuilder = new RoomBuilder();
        final Room room = new Room(rBuilder);
        final ItemStock itemStock = new ItemStock();
        itemStock.addRoom(room);
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        gameBoard.getBiddingSquare().getItemStock().addRoom(room);
        assertEquals(gameBoard.getBiddingSquare().getItemStock()
                .getAllRooms(), itemStock.getAllRooms());
    }

    @Test
    void testAddAdventurer() {
        final AdventurerBuilder aBuilder = new AdventurerBuilder();
        final Adventurer a = new Adventurer(aBuilder);
        final ItemStock itemStock = new ItemStock();
        itemStock.addAdventurer(a);
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        gameBoard.getBiddingSquare().getItemStock().addAdventurer(a);
        assertEquals(gameBoard.getBiddingSquare().getItemStock()
                .getAllAdventurer(), itemStock.getAllAdventurer());
    }

    @Test
    void testAddTrap() {
        final TrapBuilder tBuilder = new TrapBuilder();
        final Trap t = new Trap(tBuilder);
        final ItemStock itemStock = new ItemStock();
        itemStock.addTrap(t);
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        gameBoard.getBiddingSquare().getItemStock().addTrap(t);
        assertEquals(gameBoard.getBiddingSquare().getItemStock()
                .getAllTraps(), itemStock.getAllTraps());
    }

    @Test
    void testGetAllMonsters() {
        final ItemStock itemStock = new ItemStock();
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllMonsters(),
                itemStock.getAllMonsters());
    }

    @Test
    void testGetAllTraps() {
        final ItemStock itemStock = new ItemStock();
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllTraps(),
                itemStock.getAllTraps());
    }

    @Test
    void testGetAllRooms() {
        final ItemStock itemStock = new ItemStock();
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllRooms(),
                itemStock.getAllRooms());
    }

    @Test
    void testGetAllAdventurer() {
        final ItemStock itemStock = new ItemStock();
        final GameBoard gameBoard = new GameBoard(SC, 4, 3,
                3, 3, 5, 7, 5);
        assertEquals(gameBoard.getBiddingSquare().getItemStock().getAllAdventurer(),
                itemStock.getAllAdventurer());
    }

    @Test
    void testGetMonsterByIDNull() {
        final ItemStock itemStock = new ItemStock();
        assertNull(itemStock.getMonsterById(15));
    }

    @Test
    void testRoomByIDNull() {
        final ItemStock itemStock = new ItemStock();
        assertNull(itemStock.getRoomById(20));
    }
}