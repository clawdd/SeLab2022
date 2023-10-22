package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Adventurer;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.Room;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class handles all creatures
 */
public class ItemStock {
    private final List<Monster> allMonsters;
    private final List<Monster> shopOfferMonsters;
    private final List<Trap> allTraps;
    private final List<Room> allRooms;
    private final List<Room> shopOfferRooms;
    private final List<Adventurer> allAdventurer;
    private final List<Adventurer> townSquareAdventurer;

    /**
     * This constructor creates an ItemStock.
     */
    public ItemStock() {
        this.allMonsters = new LinkedList<>();
        this.shopOfferMonsters = new ArrayList<>();
        this.allTraps = new LinkedList<>();
        this.allRooms = new LinkedList<>();
        this.shopOfferRooms = new ArrayList<>();
        this.allAdventurer = new LinkedList<>();
        this.townSquareAdventurer = new ArrayList<>();
    }

    /**
     * This method returns the list shopOfferMonsters.
     *
     * @return returns the list of monsters which are available for the shop
     */
    public List<Monster> getShopOfferMonsters() {
        return shopOfferMonsters;
    }

    /**
     * This method returns the list shopOfferRooms.
     *
     * @return returns the list of rooms which are available for the shop
     */
    public List<Room> getShopOfferRooms() {
        return shopOfferRooms;
    }

    /**
     * This method returns the monster with the given ID and if it does not exist it returns null
     *
     * @param monsterId monsterId
     * @return monster
     */
    public Monster getMonsterById(final int monsterId) {
        for (final Monster monster : shopOfferMonsters) {
            if (monster.getId() == monsterId) {
                return monster;
            }
        }
        return null;
    }

    /**
     * This method returns the room with the given ID and if it does not exist it returns null
     *
     * @param roomId roomId
     * @return room
     */
    public Room getRoomById(final int roomId) {
        for (final Room room : shopOfferRooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null;
    }

    /**
     * This method returns the list shopOfferAdventurer.
     *
     * @return returns the list of adventurer which are available for the shop
     */
    public List<Adventurer> getTownSquareAdventurer() {
        return townSquareAdventurer;
    }

    /**
     * This method clear the List shopOfferMonsters and updates it with new Monsters from
     * allMonsters.
     *
     * @param sc      ServerConnection
     * @param players List of players
     */
    public void updateMonster(final ServerConnection<ActionCommand> sc,
                              final List<DungeonLord> players) {
        this.shopOfferMonsters.clear();
        for (int i = 0; i < 3; i++) {
            this.shopOfferMonsters.add(this.allMonsters.remove(0));
        }
        players.forEach(p -> this.shopOfferMonsters
                .forEach(m -> sc.sendMonsterDrawn(p.getCommId(), m.getId())));
    }

    /**
     * This method clear the List shopOfferRooms and updates it with new Rooms from allRooms.
     *
     * @param sc      ServerConnection
     * @param players list of players
     */
    public void updateRooms(final ServerConnection<ActionCommand> sc,
                            final List<DungeonLord> players) {
        this.shopOfferRooms.clear();
        for (int i = 0; i < 2; i++) {
            this.shopOfferRooms.add(this.allRooms.remove(0));
        }
        players.forEach(p -> this.shopOfferRooms
                .forEach(r -> sc.sendRoomDrawn(p.getCommId(), r.getId())));
    }

    /**
     * This method clear the List townSquareAdventurers and updates it with new Adventurer from
     * allAdventurer.
     *
     * @param sc      ServerConnection
     * @param players list of players
     */
    public void drawAdventurer(final ServerConnection<ActionCommand> sc,
                               final List<DungeonLord> players) {
        this.townSquareAdventurer.clear();
        final Logger logger = LoggerFactory.getLogger(ItemStock.class);
        logger.trace(allAdventurer.toString());
        players.forEach(player -> this.townSquareAdventurer.add(this.allAdventurer.remove(0)));
        players.forEach(p -> this.townSquareAdventurer
                .forEach(ad -> sc.sendAdventurerDrawn(p.getCommId(), ad.getId())));
    }

    /**
     * This method assign an adventurer to the corresponding player.
     *
     * @param players players
     * @param sc      ServerConnection
     */
    public void assignAdventurer(final List<DungeonLord> players,
                                 final ServerConnection<ActionCommand> sc) {
        final List<Adventurer> advSortedByDifficulty = this.townSquareAdventurer.stream()
                .sorted(Comparator.comparing(Adventurer::getDifficulty)
                        .thenComparing(Adventurer::getId)).toList();
        final List<DungeonLord> playersSortedByEvilness = players.stream()
                .sorted(Comparator.comparing(DungeonLord::getEvilness)).toList();

        // number of adventurers and players are identical, hence no checks required
        int counter = 0;
        for (final DungeonLord player : playersSortedByEvilness) {
            final Adventurer adventurer = advSortedByDifficulty.get(counter);
            if (adventurer.isCharge()) {
                player.getDungeon().getAdventurers()
                        .add(0, advSortedByDifficulty.get(counter));
            } else {
                player.getDungeon().getAdventurers().add(advSortedByDifficulty.get(counter));
            }
            // sendAdventurerArrived with respective Adventurer
            players.forEach(p ->
                    sc.sendAdventurerArrived(
                            p.getCommId(),
                            adventurer.getId(),
                            player.getId()
                    ));
            counter++;
        }


    }

    /**
     * This method adds a Monster to the List allMonsters.
     *
     * @param m monster which is added to allMonsters
     */
    public void addMonster(final Monster m) {
        this.allMonsters.add(m);
    }

    /**
     * This method adds an adventurer to the list allAdventurer
     *
     * @param a adventurer which is added to allAdventurers
     */
    public void addAdventurer(final Adventurer a) {
        this.allAdventurer.add(a);
    }

    /**
     * This method adds a trap to the list allTraps.
     *
     * @param t trap which is added to allTraps
     */
    public void addTrap(final Trap t) {
        this.allTraps.add(t);
    }

    /**
     * This method adds a room to the list allRooms.
     *
     * @param r room which is added to allRooms
     */
    public void addRoom(final Room r) {
        this.allRooms.add(r);
    }

    /**
     * This method shuffles all Lists.
     *
     * @param seed random seed from game
     */
    public void shuffle(final long seed) {
        final Random r = new Random(seed);
        Collections.shuffle(this.allMonsters, r);
        Collections.shuffle(this.allAdventurer, r);
        Collections.shuffle(this.allTraps, r);
        Collections.shuffle(this.allRooms, r);
    }

    /**
     * returns the list of all monsters
     *
     * @return list of all monsters
     */
    public List<Monster> getAllMonsters() {
        return allMonsters;
    }

    /**
     * returns the list of all traps
     *
     * @return list of all traps
     */
    public List<Trap> getAllTraps() {
        return allTraps;
    }

    /**
     * returns list of all rooms
     *
     * @return list of all rooms
     */
    public List<Room> getAllRooms() {
        return allRooms;
    }

    /**
     * returns list of all adventurers
     *
     * @return list of all adventurers
     */
    public List<Adventurer> getAllAdventurer() {
        return allAdventurer;
    }
}
