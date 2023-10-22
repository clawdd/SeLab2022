package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionCommandEnum;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.creatures.ImpTask;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import de.unisaarland.cs.se.selab.statemachine.State;
import de.unisaarland.cs.se.selab.statemachine.StateEnum;
import java.util.List;

/**
 * This class represents a players bid
 */
public class Bid {

    private final BidType bidType;
    private final int nthBid;
    private final DungeonLord player;
    private int biddingSquareSlot;


    /**
     * Constructor for Bid class
     *
     * @param bidType the type of the bid
     * @param nthBid  the nth bid of the player
     * @param player  the player who has created the bid
     */
    public Bid(final BidType bidType, final int nthBid, final DungeonLord player) {
        this.bidType = bidType;
        this.nthBid = nthBid;
        this.player = player;
        this.biddingSquareSlot = 0;
    }

    /**
     * getter for bid type
     *
     * @return the bid type
     */
    public BidType getBidType() {
        return bidType;
    }

    /**
     * getter for the nth bid
     *
     * @return the number of the bid
     */
    public int getNthBid() {
        return nthBid;
    }

    /**
     * getter for the player who has created the bid
     *
     * @return the player who has placed the bid
     */
    public DungeonLord getPlayer() {
        return player;
    }

    /**
     * setter
     *
     * @param biddingSquareSlot slot on the bidding square
     */
    public void setBiddingSquareSlot(final int biddingSquareSlot) {
        this.biddingSquareSlot = biddingSquareSlot;
    }

    /**
     * getter
     *
     * @return slot on the bidding square
     */
    public int getBiddingSquareSlot() {
        return biddingSquareSlot;
    }

    /**
     * this method is evaluating the bid
     *
     * @param itemStock    itemStock
     * @param sc           serverConnection
     * @param players      Player
     * @param gameBoard    GameBoard
     * @param currentState State
     */
    public void eval(final ItemStock itemStock, final ServerConnection<ActionCommand> sc,
                     final List<DungeonLord> players, final GameBoard gameBoard,
                     final State currentState) {
        switch (this.getBidType()) {
            case FOOD -> {
                evalFood();
            }
            case NICENESS -> {
                evalNiceness();
            }
            case TUNNEL -> {
                currentState.setCurrentStateEnum(StateEnum.BIDDINGEVALTUNNELETC);
                evalTunnel(sc, gameBoard);
            }
            case GOLD -> {
                evalGold();
            }
            case IMPS -> {
                evalImps();
            }
            case TRAP -> {
                evalTrap(itemStock, sc, players);
            }
            case MONSTER -> {
                currentState.setCurrentStateEnum(StateEnum.BIDDINGEVALMONSTERETC);
                evalMonster(sc, gameBoard);
            }
            case ROOM -> {
                currentState.setCurrentStateEnum(StateEnum.BIDDINGEVALROOMETC);
                evalRoom(itemStock, sc, gameBoard);
            }
            default -> {
            }
        }
    }

    /**
     * eval Room
     *
     * @param itemStock itemStock
     * @param sc        sc
     * @param gameBoard gameBoard
     */
    private void evalRoom(final ItemStock itemStock, final ServerConnection<ActionCommand> sc,
                          final GameBoard gameBoard) {
        switch (this.getBiddingSquareSlot()) {
            case 1, 2 -> {
                if (player.checkGold(1)) {
                    player.changeGold(-1);
                    possibleToBuildRoom(player, gameBoard, sc);
                }
            }
            case 3 -> {
                if (!itemStock.getShopOfferRooms().isEmpty()) {
                    possibleToBuildRoom(player, gameBoard, sc);
                }
            }
            default -> {
            }
        }
    }

    /**
     * eval Monster
     *
     * @param sc        server connection
     * @param gameBoard gameBoard
     */
    private void evalMonster(final ServerConnection<ActionCommand> sc, final GameBoard gameBoard) {
        switch (this.getBiddingSquareSlot()) {
            case 1, 2 -> possibleToHireMonster(player, gameBoard, sc);
            case 3 -> {
                if (player.checkFood(1)) {
                    player.changeFood(-1);
                    possibleToHireMonster(player, gameBoard, sc);
                }
            }
            default -> {
            }
        }
    }

    /**
     * eval trap
     *
     * @param itemStock item stock
     * @param sc        server connection
     * @param players   all players
     */
    private void evalTrap(final ItemStock itemStock, final ServerConnection<ActionCommand> sc,
                          final List<DungeonLord> players) {
        switch (this.getBiddingSquareSlot()) {
            case 1 -> {
                if (player.checkGold(1)) {
                    player.changeGold(-1);
                    final Trap trap = itemStock.getAllTraps().remove(0);
                    player.getTraps().add(trap);
                    players.forEach(
                            p -> sc.sendTrapAcquired(
                                    p.getCommId(), player.getId(), trap.getId()));
                }
            }
            case 2 -> {
                final Trap trap = itemStock.getAllTraps().remove(0);
                player.getTraps().add(trap);
                players.forEach(p -> sc.sendTrapAcquired(p.getCommId(), player.getId(),
                        trap.getId()));
            }
            case 3 -> {
                if (player.checkGold(2)) {
                    player.changeGold(-2);
                    final Trap trap1 = itemStock.getAllTraps().remove(0);
                    player.getTraps().add(trap1);
                    players.forEach(p -> sc.sendTrapAcquired(p.getCommId(),
                            player.getId(), trap1.getId()));
                    final Trap trap2 = itemStock.getAllTraps().remove(0);
                    player.getTraps().add(trap2);
                    players.forEach(p -> sc.sendTrapAcquired(p.getCommId(),
                            player.getId(), trap2.getId()));
                }
            }
            default -> {
            }
        }
    }

    /**
     * eval imps
     */
    private void evalImps() {
        switch (this.getBiddingSquareSlot()) {
            case 1 -> {
                if (player.checkFood(1)) {
                    player.changeFood(-1);
                    player.employImps(1);
                }
            }
            case 2 -> {
                if (player.checkFood(2)) {
                    player.changeFood(-2);
                    player.employImps(2);
                }
            }
            case 3 -> {
                final boolean condition = player.checkFood(1) && player.checkGold(1);
                if (condition) {
                    player.changeFood(-1);
                    player.changeGold(-1);
                    player.employImps(2);
                }
            }
            default -> {
            }
        }
    }

    /**
     * eval gold
     */
    private void evalGold() {
        switch (this.getBiddingSquareSlot()) {
            case 1 -> {
                evalGoldCase1();
            }
            case 2 -> {
                evalGoldCase2();

            }
            case 3 -> {
                evalGoldCase3();
            }
            default -> {
            }
        }
    }

    /**
     * eval gold case 3
     */
    private void evalGoldCase3() {
        if (player.checkImps(5)
                && amountOfTunnelTilesOfDungeonLord(player) >= 4) {
            player.changeImps(5, ImpTask.GOLD);
        } else if (player.checkImps(3)
                && amountOfTunnelTilesOfDungeonLord(player) == 3) {
            player.changeImps(3, ImpTask.GOLD);
        } else if (player.checkImps(2)
                && amountOfTunnelTilesOfDungeonLord(player) == 2) {
            player.changeImps(2, ImpTask.GOLD);
        } else if (player.checkImps(1)
                && amountOfTunnelTilesOfDungeonLord(player) == 1) {
            player.changeImps(1, ImpTask.GOLD);
        }
    }

    /**
     * eval gold case 2
     */
    private void evalGoldCase2() {
        if (player.checkImps(3)
                && amountOfTunnelTilesOfDungeonLord(player) >= 3) {
            player.changeImps(3, ImpTask.GOLD);
        } else if (player.checkImps(2)
                && amountOfTunnelTilesOfDungeonLord(player) == 2) {
            player.changeImps(2, ImpTask.GOLD);
        } else if (player.checkImps(1)
                && amountOfTunnelTilesOfDungeonLord(player) == 1) {
            player.changeImps(1, ImpTask.GOLD);
        }
    }

    /**
     * eval gold case 1
     */
    private void evalGoldCase1() {
        if (player.checkImps(2)
                && amountOfTunnelTilesOfDungeonLord(player) >= 2) {
            player.changeImps(2, ImpTask.GOLD);
        } else if (player.checkImps(1)
                && amountOfTunnelTilesOfDungeonLord(player) == 1) {
            player.changeImps(1, ImpTask.GOLD);
        }
    }

    /**
     * eval tunnel
     *
     * @param sc        server connection
     * @param gameBoard gameBoard
     */
    private void evalTunnel(final ServerConnection<ActionCommand> sc, final GameBoard gameBoard) {
        switch (this.getBiddingSquareSlot()) {
            case 1 -> {
                evalTunnelCase1(sc, gameBoard);
            }
            case 2 -> {
                evalTunnelCase2(sc, gameBoard);
            }
            case 3 -> {
                evalTunnelCase3(sc, gameBoard);
            }
            default -> {
            }
        }
    }

    /**
     * eval tunnel case 3
     *
     * @param sc        sc
     * @param gameBoard gameBoard
     */
    private void evalTunnelCase3(final ServerConnection<ActionCommand> sc,
                                 final GameBoard gameBoard) {
        if (player.checkImps(5)) {
            sc.sendDigTunnel(player.getCommId());
            if5(sc, gameBoard, 4);
        } else if (player.checkImps(3)) {
            sc.sendDigTunnel(player.getCommId());
            if5(sc, gameBoard, 3);
        } else if (player.checkImps(2)) {
            sc.sendDigTunnel(player.getCommId());
            if5(sc, gameBoard, 2);
        } else if (player.checkImps(1)) {
            sc.sendDigTunnel(player.getCommId());
            amountOfImpsTunnel(player, gameBoard, sc);
        }
    }


    private void if5(final ServerConnection<ActionCommand> sc, final GameBoard gameBoard,
                     final int x) {
        ActionCommandEnum ac;
        for (int i = 0; i < x; i++) {
            ac = amountOfImpsTunnel(player, gameBoard, sc);
            if (ac == ActionCommandEnum.ENDTURN) {
                break;
            }
        }
    }

    /**
     * eval tunnel case 2
     *
     * @param sc        sc
     * @param gameBoard gameBoard
     */
    private void evalTunnelCase2(final ServerConnection<ActionCommand> sc,
                                 final GameBoard gameBoard) {
        if (player.checkImps(3)) {
            sc.sendDigTunnel(player.getCommId());
            if5(sc, gameBoard, 3);
        } else if (player.checkImps(2)) {
            sc.sendDigTunnel(player.getCommId());
            if5(sc, gameBoard, 2);
        } else if (player.checkImps(1)) {
            sc.sendDigTunnel(player.getCommId());
            amountOfImpsTunnel(player, gameBoard, sc);
        }
    }

    /**
     * eval tunnel case 1
     *
     * @param sc        sc
     * @param gameBoard gaameBoard
     */
    private void evalTunnelCase1(final ServerConnection<ActionCommand> sc,
                                 final GameBoard gameBoard) {
        if (player.checkImps(2)) {
            sc.sendDigTunnel(player.getCommId());
            if5(sc, gameBoard, 2);
        } else if (player.checkImps(1)) {
            sc.sendDigTunnel(player.getCommId());
            amountOfImpsTunnel(player, gameBoard, sc);
        }
    }

    /**
     * eval niceness
     */
    private void evalNiceness() {
        switch (this.getBiddingSquareSlot()) {
            case 1 -> {
                if (player.checkEvilness(-1)) {
                    player.changeEvilness(-1);
                }
            }
            case 2 -> {
                if (player.checkEvilness(-2)) {
                    player.changeEvilness(-2);
                }
            }
            case 3 -> {
                if (player.checkGold(1)) {
                    player.changeGold(-1);
                    if (player.checkEvilness(-2)) {
                        player.changeEvilness(-2);
                    }
                }
            }
            default -> {
            }
        }
    }

    /**
     * eval food
     */
    private void evalFood() {
        switch (this.getBiddingSquareSlot()) {
            case 1 -> {
                if (player.checkGold(1)) {
                    player.changeGold(-1);
                    player.changeFood(2);
                }
            }
            case 2 -> {
                if (player.checkEvilness(1)) {
                    player.changeEvilness(1);
                    player.changeFood(3);
                }
            }
            case 3 -> {
                if (player.checkEvilness(2)) {
                    player.changeEvilness(2);
                    player.changeFood(3);
                    player.changeGold(1);
                }
            }
            default -> {
            }
        }
    }

    /**
     * This method evaluates whether a player can build a room.
     *
     * @param dl player
     */
    private void possibleToBuildRoom(final DungeonLord dl, final GameBoard gameBoard,
                                     final ServerConnection<ActionCommand> sc) {
        if (!gameBoard.playerIsInTheGame(dl)) {
            return;
        }
        sc.sendPlaceRoom(dl.getCommId());
        ActionCommand ac;
        ActionCommandEnum actionReturn = null;
        do {
            if (!gameBoard.playerIsInTheGame(player)) {
                return;
            }
            if (actionReturn != ActionCommandEnum.ACTIONWRONGPLAYER
                    && actionReturn != ActionCommandEnum.LEAVE
                    && actionReturn != ActionCommandEnum.ACTIVATEROOMOTHERPLAYER) {
                sc.sendActNow(dl.getCommId());
            }
            try {
                ac = sc.nextAction();
            } catch (TimeoutException e) {
                gameBoard.removePlayer(dl.getCommId());
                return;
            }
            final var listWithCurrentPlayer = List.of(dl.getCommId());
            actionReturn = ac.execute(listWithCurrentPlayer);
        } while (actionReturn != ActionCommandEnum.BUILDROOM
                && actionReturn != ActionCommandEnum.ENDTURN);
    }

    /**
     * This method evaluates whether a player can hireMonster.
     *
     * @param player player
     */
    private void possibleToHireMonster(final DungeonLord player, final GameBoard gameBoard,
                                       final ServerConnection<ActionCommand> sc) {
        if (!gameBoard.playerIsInTheGame(player)) {
            return;
        }
        sc.sendSelectMonster(player.getCommId());
        ActionCommand ac;
        ActionCommandEnum ace = null;
        do {
            if (!gameBoard.playerIsInTheGame(player)) {
                return;
            }
            if (ace != ActionCommandEnum.ACTIONWRONGPLAYER
                    && ace != ActionCommandEnum.LEAVE
                    && ace != ActionCommandEnum.ACTIVATEROOMOTHERPLAYER) {
                sc.sendActNow(player.getCommId());
            }
            try {
                ac = sc.nextAction();
            } catch (TimeoutException e) {
                gameBoard.removePlayer(player.getCommId());
                return;
            }
            final var listWithCurrentPlayer = List.of(player.getCommId());
            ace = ac.execute(listWithCurrentPlayer);
        } while (ace != ActionCommandEnum.HIREMONSTER
                && ace != ActionCommandEnum.ENDTURN);
    }

    /**
     * This method returns the amount of TunnelTiles of a DungeonLord
     *
     * @param dl DungeonLord
     * @return amount of tunnel tiles
     */
    private int amountOfTunnelTilesOfDungeonLord(final DungeonLord dl) {
        return dl.getDungeon().countTunnelTiles();
    }

    /**
     * This method sends imps to tunnel.
     *
     * @param dl dungeonLord
     */
    private ActionCommandEnum amountOfImpsTunnel(final DungeonLord dl, final GameBoard gameBoard,
                                                 final ServerConnection<ActionCommand> sc) {
        if (!gameBoard.playerIsInTheGame(dl)) {
            return ActionCommandEnum.ENDTURN;
        }
        ActionCommand ac;
        ActionCommandEnum ace = null;
        do {
            if (!gameBoard.playerIsInTheGame(player)) {
                return ActionCommandEnum.ENDTURN;
            }
            if (ace != ActionCommandEnum.ACTIONWRONGPLAYER
                    && ace != ActionCommandEnum.LEAVE
                    && ace != ActionCommandEnum.ACTIVATEROOMOTHERPLAYER) {
                sc.sendActNow(dl.getCommId());
            }
            try {
                ac = sc.nextAction();
            } catch (TimeoutException e) {
                gameBoard.removePlayer(dl.getCommId());
                return ActionCommandEnum.ENDTURN;
            }
            final var listWithCurrentPlayer = List.of(dl.getCommId());
            ace = ac.execute(listWithCurrentPlayer);
        } while (ace != ActionCommandEnum.DIGTUNNEL
                && ace != ActionCommandEnum.ENDTURN);
        if (ace == ActionCommandEnum.ENDTURN) {
            return ActionCommandEnum.ENDTURN;
        }
        return ActionCommandEnum.DIGTUNNEL;
    }
}
