package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Imp;
import de.unisaarland.cs.se.selab.gamelogic.creatures.ImpTask;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Tunnel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the player
 */
public class DungeonLord {

    private final String name;
    private final ServerConnection<ActionCommand> sc;
    private final int id;
    private final int commId;
    private final List<Imp> imps;
    private int food;
    private int gold;
    private final List<Monster> monster;
    private final List<Trap> traps;
    private int evilness;
    private final Dungeon dungeon;
    private final BidType[] placedBidTypes;
    private final BidType[] lockedBidType;
    private final List<DungeonLord> players;

    /**
     * This constructor creates a Dungeon Lord
     *
     * @param name            name of the player
     * @param id              id of the player
     * @param commId          commId of the player
     * @param sc              serverConnection
     * @param dungeonLength   length of the players dungeon
     * @param initialFood     initial Food
     * @param initialGold     initial gold
     * @param initialImps     initial imps
     * @param initialEvilness initial evilness
     * @param players         players
     */
    public DungeonLord(final String name, final int id, final int commId,
                       final int dungeonLength, final int initialGold, final int initialFood,
                       final int initialImps,
                       final int initialEvilness, final ServerConnection<ActionCommand> sc,
                       final List<DungeonLord> players) {
        this.name = name;
        this.sc = sc;
        this.id = id;
        this.commId = commId;
        this.imps = new ArrayList<>(initialImps);
        for (int i = 0; i < initialImps; i++) {
            this.imps.add(new Imp());
        }
        this.food = initialFood;
        this.gold = initialGold;
        this.monster = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.evilness = initialEvilness;
        this.dungeon = new Dungeon(dungeonLength, sc, players);
        this.placedBidTypes = new BidType[3];
        this.lockedBidType = new BidType[2];
        this.players = players;
    }

    /**
     * checks if gold can be changed and if so it gets changed and the event send
     *
     * @param gold the amount of gold to be changed
     */
    public void changeGold(final int gold) {
        this.gold += gold;
        if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
            players.forEach(p ->
                    sc.sendGoldChanged(p.getCommId(), gold, this.getId()));
        }
    }

    /**
     * checks if gold can be changed
     *
     * @param gold gold
     * @return true or false
     */
    public boolean checkGold(final int gold) {
        return (this.gold >= gold);
    }

    /**
     * checks if food can be changed and if so it gets changed and the event send
     *
     * @param food the amount of food to be changed
     */
    public void changeFood(final int food) {
        this.food += food;
        if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
            players.forEach(p ->
                    sc.sendFoodChanged(p.getCommId(), food, this.getId()));
        }
    }

    /**
     * checks if food can be changed
     *
     * @param food food
     * @return true or false
     */
    public boolean checkFood(final int food) {
        return (this.food >= food);
    }

    /**
     * tries to dig a tunnel in the dungeon and sets the imp task to TUNNEL
     *
     * @param point where the tunnel is to be dug
     * @return returns if the tunnel is dug successfully
     */
    public boolean digTunnel(final Point point) {
        return dungeon.placeTunnel(new Tunnel(), point);
    }

    /**
     * This method lets all imp return and evaluates their work
     */
    public void evalImps() {
        int goldCounter = 0;
        int impCounter = 0;
        for (final Imp imp : imps) {
            if (imp.getCurrentTask() == ImpTask.GOLD) {
                goldCounter++;
                impCounter++;
                imp.setCurrentTask(ImpTask.REST);
            } else if (imp.getCurrentTask() == ImpTask.TUNNEL
                    || imp.getCurrentTask() == ImpTask.SUPERVISE) {
                impCounter++;
                imp.setCurrentTask(ImpTask.REST);
            }
        }
        if (impCounter > 0) {
            final int finalImpCounter = impCounter;
            players.forEach(p -> sc.sendImpsChanged(p.getCommId(), finalImpCounter, id));
        }
        if (goldCounter > 0) {
            changeGold(goldCounter);
        }
    }

    /**
     * This adds new imps to the player
     *
     * @param amount number of imps to be added
     */
    public void employImps(final int amount) {
        for (int i = 0; i < amount; i++) {
            imps.add(new Imp());
        }
        if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
            players.forEach(p ->
                    sc.sendImpsChanged(p.getCommId(), amount, this.getId()));
        }
    }

    /**
     * checks how many imps are in rest
     *
     * @return the amount of unemployed imps
     */
    public int getUnemployedNumberOfImps() {
        int counter = 0;
        for (final Imp imp : imps) {
            if (imp.getCurrentTask() == ImpTask.REST) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * This method changes the given imps  to a specific task NOT FOR SETTING IMPS TO REST
     *
     * @param amount the number of imps
     * @param task   the task
     * @return true if it was successfully
     */
    public boolean changeImps(final int amount, final ImpTask task) {
        if (task == ImpTask.REST) {
            return checkImpRoom(amount, task);
        }
        if (amount > getUnemployedNumberOfImps()) {
            return false;
        }

        return changeImpsHelp(amount, task);
    }

    /**
     * helper method for activate roompayout
     *
     * @param amount num of imp
     * @param task   to rest
     * @return boolean if worked
     */
    public boolean checkImpRoom(final int amount, final ImpTask task) {
        int counter = 0;
        for (final Imp imp : imps) {
            if (amount <= counter) {
                break;
            }
            if (imp.getCurrentTask() == ImpTask.ROOM) {
                imp.setCurrentTask(task);
                counter++;
            }
        }
        if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
            players.forEach(p ->
                    sc.sendImpsChanged(p.getCommId(), amount, this.getId()));
        }
        return true;
    }

    /**
     * helper method for all casses of imp
     *
     * @param amount num of imp
     * @param task   task of imp
     * @return boolean if worked
     */
    public boolean changeImpsHelp(final int amount, final ImpTask task) {
        int counter = 0;
        if (amount == 5 && task != ImpTask.REST) {
            for (final Imp imp : imps) {
                if (imp.getCurrentTask() == ImpTask.REST) {
                    imp.setCurrentTask(ImpTask.SUPERVISE);
                    counter++;
                    break;
                }
            }
        }
        for (final Imp imp : imps) {
            if (amount <= counter) {
                break;
            }
            if (imp.getCurrentTask() == ImpTask.REST) {
                imp.setCurrentTask(task);
                counter++;
            }
        }
        if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
            players.forEach(p ->
                    sc.sendImpsChanged(p.getCommId(), -amount, this.getId()));
        }
        return true;
    }

    /**
     * get the number of imps doing a specific task
     *
     * @param impTask the task
     * @return the number of imps doing the task
     */
    public int getNumberOfImpsDoingThisTask(final ImpTask impTask) {
        int counter = 0;
        for (final Imp imp : imps) {
            if (imp.getCurrentTask() == impTask) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * checks if the given imps can work
     *
     * @param amount amount
     * @return true or false
     */
    public boolean checkImps(final int amount) {
        return (getUnemployedNumberOfImps() >= amount);
    }

    /**
     * This method changed the evilness of the player
     *
     * @param amount the amount of evilness to be changed
     */
    public void changeEvilness(final int amount) {
        evilness += amount;
        if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
            players.forEach(p ->
                    sc.sendEvilnessChanged(p.getCommId(), amount, this.getId()));
        }
    }

    /**
     * checks if enough evilness is available
     *
     * @param amount amount
     * @return true or false
     */
    public boolean checkEvilness(final int amount) {
        return (evilness + amount <= 15 && evilness + amount >= 0);
    }

    /**
     * Adds monster to the collection of the player
     *
     * @param monster the monster to be added
     */
    public void addMonster(final Monster monster) {
        this.monster.add(monster);
    }

    /**
     * Tries to place a trap in the players dungeon
     *
     * @param trap the trap to be placed
     * @return true if the trap could be placed successfully
     */
    public boolean placeTrap(final Trap trap) {
        if (getDungeon().getPlacedTrap() != null) {
            return false;
        }
        getDungeon().setTrap(trap);
        return true;
    }

    /**
     * This method used to place a room in the dungeon
     *
     * @param room  the room to be placed
     * @param point Point where the room should be placed
     * @return true if the room could be placed
     */
    public boolean placeRoom(final Room room, final Point point) {
        if (dungeon.placeRoom(room, point)) {
            dungeon.getPlayersOwnedRooms().add(room);
            if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
                players.forEach(p ->
                        sc.sendRoomBuilt(p.getCommId(), id, room.getId(), point.x, point.y));
            }
            return true;
        }
        return false;
    }

    /**
     * This method activates the room with the given roomId
     *
     * @param roomId the roomId of the room to be activated
     * @return true if the room could be activated
     */
    public boolean activateRoom(final int roomId) {
        Room searchedRoom = null;
        for (final Room room : dungeon.getPlayersOwnedRooms()) {
            if (room.getId() == roomId) {
                searchedRoom = room;
                break;
            }
        }
        if (searchedRoom == null) {
            return false;
        }
        // if (!checkGold(1)) {
        // return false;
        // }
        if (!changeImps(searchedRoom.getActivationCost(), ImpTask.ROOM)) {
            return false;
        }
        /*if (checkEvilness(-searchedRoom.getNiceness())) {
            changeEvilness(-searchedRoom.getNiceness());
        }*/ //only in payout

        // changeGold(-1);
        searchedRoom.activate();
        if (players.stream().anyMatch(p -> p.getId() == this.getId())) {
            players.forEach(p ->
                    sc.sendRoomActivated(p.getCommId(), id, roomId));
        }
        return true;
    }

    /**
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * @return player id
     */
    public int getId() {
        return id;
    }

    /**
     * @return player commId
     */
    public int getCommId() {
        return commId;
    }

    /**
     * @return list of players imps
     */
    public List<Imp> getImps() {
        return imps;
    }

    /**
     * @return available players food
     */
    public int getFood() {
        return food;
    }

    /**
     * @return available players gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @return available players monsters
     */
    public List<Monster> getMonster() {
        return monster;
    }

    /**
     * @return available players traps
     */
    public List<Trap> getTraps() {
        return traps;
    }

    /**
     * @return players evilness
     */
    public int getEvilness() {
        return evilness;
    }

    /**
     * @return players dungeon
     */
    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * @return players first placed bid
     */
    public BidType getFirstPlacedBidType() {
        return placedBidTypes[0];
    }

    /**
     * @return players second placed bid
     */
    public BidType getSecondPlacedBidType() {
        return placedBidTypes[1];
    }

    /**
     * @return players third placed bid
     */
    public BidType getThirdPlacedBidType() {
        return placedBidTypes[2];
    }

    /**
     * set first placed bid
     *
     * @param bidType BidType
     */
    public void setFirstPlacedBidType(final BidType bidType) {
        placedBidTypes[0] = bidType;
    }

    /**
     * set second placed
     *
     * @param bidType BidType
     */
    public void setSecondPlacedBidType(final BidType bidType) {
        placedBidTypes[1] = bidType;
    }

    /**
     * set third placed
     *
     * @param bidType BidType
     */
    public void setThirdPlacedBidType(final BidType bidType) {
        placedBidTypes[2] = bidType;
    }

    /**
     * @return players first locked bid
     */
    public BidType getFirstLockedBidType() {
        return lockedBidType[0];
    }

    /**
     * @return players second locked bid
     */
    public BidType getSecondLockedBidType() {
        return lockedBidType[1];
    }

    /**
     * set first locked bid
     *
     * @param bidType BidType
     */
    public void setFirstLockedBidType(final BidType bidType) {
        lockedBidType[0] = bidType;
    }

    /**
     * set second locked bid
     *
     * @param bidType BidType
     */
    public void setSecondLockedBidType(final BidType bidType) {
        lockedBidType[1] = bidType;
    }

}
