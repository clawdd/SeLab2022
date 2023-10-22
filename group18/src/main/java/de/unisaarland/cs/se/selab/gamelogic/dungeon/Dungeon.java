package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Adventurer;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Monster;
import de.unisaarland.cs.se.selab.gamelogic.creatures.Trap;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is class represents the Dungeon of the DungeonLord
 * ----------------------------------------------------------- it implements every method which is
 * needed and used during the game to manipulate the Dungeon of a player and change it attributes.
 * ----------------------------------------------------------
 */
public class Dungeon {

    private DungeonTile[][] dungeonSquare;
    private Point battleGroundPosition;
    private final List<Adventurer> adventurers;   // always length 3!
    private final List<Adventurer> prison;
    private final List<Monster> placedMonsters;
    private Trap placedTrap;
    private final List<Room> playersOwnedRooms;
    private final ServerConnection<ActionCommand> sc;
    private final List<DungeonLord> players;

    /**
     * Creates a new square Dungeon with sideLength
     *
     * @param sideLength the side length of the square dungeon
     * @param sc         ServerConnection
     * @param players    players
     */
    public Dungeon(final int sideLength, final ServerConnection<ActionCommand> sc,
                   final List<DungeonLord> players) {
        this.dungeonSquare = new DungeonTile[sideLength][sideLength];
        this.dungeonSquare[0][0] = new Tunnel();
        this.battleGroundPosition = new Point(0, 0);
        this.adventurers = new ArrayList<>();
        this.prison = new LinkedList<>();
        this.placedMonsters = new LinkedList<>();
        this.placedTrap = null;
        this.playersOwnedRooms = new LinkedList<>();
        this.sc = sc;
        this.players = players;
    }

    /**
     * @return true if there's a healer under the adventurers, false else
     */
    public boolean checkForHealer() {
        return adventurers.stream().anyMatch(a -> a.getHealValue() > 0);
    }

    /**
     * clears all defenses
     */
    public void clearPlacedDefenses() {
        placedMonsters.clear();
        placedTrap = null;
    }

    /**
     * evaluate the placed Trap
     *
     * @param sc     ServerConnection
     * @param player DungeonLord
     */
    public void evalTrap(final ServerConnection<ActionCommand> sc, final DungeonLord player) {

        if (placedTrap == null) {
            return;
        }
        final int defuseValue = calculateAllDefuseValues();

        switch (placedTrap.getAttackStrategy()) {
            case BASIC -> evalTrapBasic(defuseValue, player, adventurers);
            case TARGETED -> evalTrapTargeted(defuseValue, player, adventurers);
            case MULTI -> {
                final int multiTrapDamage = calculateDamage(placedTrap.getDamage(), defuseValue);
                final int trapDamage = placedTrap.getDamage();
                int leftoverDefuseValue = defuseValue;
                final boolean condition = defuseValue > placedTrap.getDamage();
                if (condition) {
                    for (final Adventurer adventurer : adventurers) {
                        final int damageToDeal = Math.max(trapDamage - leftoverDefuseValue, 0);
                        adventurer.takeDamage(damageToDeal, sc, players, player,
                                adventurers);
                        leftoverDefuseValue =
                                Math.max(leftoverDefuseValue - placedTrap.getDamage(), 0);
                        //imprisonAdventurer(adventurers, i);
                    }
                } else {
                    final int temp = adventurers.size();
                    adventurers.get(0).takeDamage(multiTrapDamage, sc, players, player,
                            adventurers);
                    final List<Adventurer> adventurersCopy = List.copyOf(adventurers);
                    final int startIndex = (temp - adventurers.size() + 1) % 2;
                    for (int i = startIndex; i < adventurersCopy.size(); i++) {
                        adventurersCopy.get(i).takeDamage(
                                placedTrap.getDamage(), sc, players, player,
                                adventurers);
                    }
                }
            }
            default -> sc.sendActionFailed(player.getCommId(), "evalTrap failed");
        }
    }

    /**
     * @return sum of defuse values, possibly 0
     */
    private int calculateAllDefuseValues() {
        int defuseValue = 0;
        for (final Adventurer adventurer : this.adventurers) {
            defuseValue += adventurer.getDefuseValue();
        }
        return defuseValue;
    }

    /**
     * @param defuseValue    sum of defuse value
     * @param player         respective player
     * @param adventurerList AdventurerList
     */
    public void evalTrapBasic(final int defuseValue, final DungeonLord player,
                              final List<Adventurer> adventurerList) {
        final int basicTrapDamage = calculateDamage(placedTrap.getDamage(), defuseValue);
        adventurers.get(0).takeDamage(basicTrapDamage, sc, players, player, adventurerList);
        //imprisonAdventurer(adventurers, 0);
    }

    /**
     * @param defuseValue    sum of defuse value
     * @param player         respective player
     * @param adventurerList AdventurerList
     */
    // TODO: 04.10.22 @Colin
    public void evalTrapTargeted(final int defuseValue, final DungeonLord player,
                                 final List<Adventurer> adventurerList) {
        final boolean condition = placedTrap.getTarget() >= adventurers.size();
        if (condition) {
            sc.sendActionFailed(player.getCommId(), "evalTrap failed");
        }
        final int targetedTrapDamage = calculateDamage(placedTrap.getDamage(), defuseValue);
        adventurers.get(placedTrap.getTarget()).takeDamage(
                targetedTrapDamage, sc, players, player, adventurerList);
        //imprisonAdventurer(adventurers, placedTrap.getTarget());
    }

    /**
     * calculates damages after traps were defused
     *
     * @param damage      initial trap damage
     * @param defuseValue sum of defuse value
     * @return concrete damage to be applied
     */
    private int calculateDamage(final int damage, final int defuseValue) {
        return Math.max(damage - defuseValue, 0);
    }

    /**
     * This method imprisons an adventurer if his healPoints go below 1 // or equal 0
     *
     * @param adventurers adventurer to be checked
     * @param index       index of the checked adventurer
     */
    public void imprisonAdventurer(final List<Adventurer> adventurers, final int index) {
        final boolean condition = adventurers.get(index).getHealthPoints() <= 0;
        if (condition) {
            prison.add(adventurers.get(index));
            adventurers.remove(adventurers.get(index));
        }
    }

    /**
     * @param trap trap
     */
    public void setTrap(final Trap trap) {
        placedTrap = trap;
    }

    /**
     * Set the battleground position to the Point
     *
     * @param point the location chosen to place the Battleground
     * @return true if point can be battleground, else false
     */
    public boolean placeBattleGround(final Point point) {

        if (!checkInBounds(point)) {
            return false;
        }

        final int x = point.x;
        final int y = point.y;

        if (dungeonSquare[x][y] == null || dungeonSquare[x][y].getConquered()) {
            return false;
        }

        if (checkBattleGround(point)) {
            battleGroundPosition = point;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param point point to check
     * @return true if a battleGround can be placed
     */
    public boolean checkBattleGround(final Point point) {
        return (PathFinder.findClosestUnconqueredTiles(
                dungeonSquare, new Point(0, 0)).contains(point));
    }

    /**
     * @param tile     Tile to place // tunnel or room
     * @param position the chosen position to place the tile
     * @return returns true if the tile is placed successfully, else  false
     */
    public boolean placeTunnel(final DungeonTile tile, final Point position) {
        if (tile == null) {
            // ActionFailed -> sent in DigTunnelCommand
            return false;
        }

        // check for placement out of bounds
        final int x = position.x;
        final int y = position.y;
        final boolean checkOutOfBounds =
                (x >= dungeonSquare.length || y >= dungeonSquare.length || x < 0 || y < 0);
        if (checkOutOfBounds) {
            return false;
        }

        if (isDungeonFullyConquered()
                && !PathFinder.findClosestNullPointsTo00(dungeonSquare).contains(position)) {
            // ActionFailed -> sent in DigTunnelCommand
            return false;
        }

        // check whether new tile is connected to existing dungeon and valid position
        final boolean connectedToDungeon = checkIfTileConnected(position, dungeonSquare);
        final boolean viableSpot = checkNo2By2Square(position);
        final boolean checkPossibleToPlace =
                dungeonSquare[x][y] == null && connectedToDungeon && viableSpot;
        if (checkPossibleToPlace) {
            dungeonSquare[x][y] = tile;
            return true;
        }

        // ActionFailed -> sent in DigTunnelCommand
        return false;
    }

    /**
     * new method taking care of proper room placement #redtape
     *
     * @param tile     room
     * @param position where room shall be placed
     * @return true if successful
     */
    public boolean placeRoom(final DungeonTile tile, final Point position) {

        // check for valid tile
        if (tile == null) {
            // ActionFailed -> sent in DigTunnelCommand
            return false;
        }

        // check for placement out of bounds
        final int x = position.x;
        final int y = position.y;
        final boolean condition2 = (x >= dungeonSquare.length
                || y >= dungeonSquare.length || x < 0 || y < 0);
        if (condition2) {
            return false;
        }

        // todo 4.10.2022 remove room from room list once conquered
        final boolean validTunnelIsThere =
                dungeonSquare[x][y] != null && dungeonSquare[x][y].isTunnel()
                        && !dungeonSquare[x][y].getConquered();
        // special case: Dungeon just 1x1
        if (dungeonSquare.length == 1) {
            if (validTunnelIsThere) {
                dungeonSquare[x][y] = tile;     // only possible to place room
                return true;
            }
            // ActionFailed -> sent in DigTunnelCommand
            return false;
        }

        // check whether there is an adjacent exists room
        final boolean roomNeighbour = checkRoomNeighbour(position, dungeonSquare);
        if (!roomNeighbour && validTunnelIsThere) {
            //placeDungeonRoom places the dungeon and returns true, else false
            final Room room = (Room) tile;
            return placeDungeonRoom(x, y, room);
        }
        // ActionFailed -> sent in DigTunnelCommand
        return false;
    }

    /**
     * @param x    x parameter of a point
     * @param y    y parameter of a point
     * @param tile room chosen to place
     * @return returns true if the room is placed successfully, else false
     */
    private boolean placeDungeonRoom(final int x, final int y, final Room tile) {

        // both helpers for check for InnerRing
        final boolean restrictionOne = (x > 0 && x < dungeonSquare.length - 1);
        final boolean restrictionTwo = (y > 0 && y < dungeonSquare.length - 1);

        final Restriction restriction = tile.getRestriction();

        switch (restriction) {
            case UPPER_HALF -> {
                if (x <= (dungeonSquare.length / 2) - 1) {
                    dungeonSquare[x][y] = tile;
                    return true;
                }
                return false;
            }
            case LOWER_HALF -> {
                if (x > (dungeonSquare.length / 2) - 1) {
                    dungeonSquare[x][y] = tile;
                    return true;
                }
                return false;
            }
            case INNER_RING -> {
                if (restrictionOne && restrictionTwo) {
                    dungeonSquare[x][y] = tile;
                    return true;
                }
                return false;
            }
            case OUTER_RING -> {
                if (!(restrictionOne && restrictionTwo)) {
                    dungeonSquare[x][y] = tile;
                    return true;
                }
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * @param point         the chosen location to place the tile on
     * @param dungeonSquare dungeon of the player
     * @return returns true if the chosen location is connected to the rest of the dungeon
     */
    public boolean checkIfTileConnected(final Point point, final DungeonTile[][] dungeonSquare) {

        final int x = point.x;
        final int y = point.y;

        final Point[] pointsFromAllSides = new Point[4]; // above, right, below, left from tile
        pointsFromAllSides[0] = new Point(x, y + 1);
        pointsFromAllSides[1] = new Point(x + 1, y);
        pointsFromAllSides[2] = new Point(x, y - 1);
        pointsFromAllSides[3] = new Point(x - 1, y);
        // Upper limit of OutOfBounds is checked in ensuing if-statement
        for (final Point p : pointsFromAllSides) {
            if (checkInBounds(p) && checkConqueredAndExists(p, dungeonSquare)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param point         point that is checked to be a part of the existing dungeon
     * @param dungeonSquare dungeon of the player
     * @return returns true if the tile is a room or tunnel and not conquered, else false
     */
    private boolean checkConqueredAndExists(final Point point,
                                            final DungeonTile[][] dungeonSquare) {
        final int x = point.x;
        final int y = point.y;
        return dungeonSquare[x][y] != null && !dungeonSquare[x][y].getConquered();
    }


    /**
     * @param point point that is checked to be a part of the dungeon
     * @return returns true if x and y parameters of the point are not out of bounds, else false
     */
    public boolean checkInBounds(final Point point) {
        final int x = point.x;
        final int y = point.y;
        return x < dungeonSquare.length && y < dungeonSquare.length && x >= 0 && y >= 0;
    }

    /**
     * @param point         chosen location of the room wished to place on
     * @param dungeonSquare dungeon of the player
     * @return returns true if there is a neighbour room, else false
     */
    public boolean checkRoomNeighbour(final Point point,
                                      final DungeonTile[][] dungeonSquare) {

        final Point[] pointsAroundPoint = new Point[4]; // Above, right, below, left
        pointsAroundPoint[0] = new Point(point.x, point.y + 1);
        pointsAroundPoint[1] = new Point(point.x + 1, point.y);
        pointsAroundPoint[2] = new Point(point.x, point.y - 1);
        pointsAroundPoint[3] = new Point(point.x - 1, point.y);

        for (final Point pointToCheck : pointsAroundPoint) {
            if (checkInBounds(pointToCheck)
                    && checkRoom(pointToCheck, dungeonSquare)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param point         point to be checked
     * @param dungeonSquare dungeon of the player
     * @return returns true if there is a room, else false
     */
    private boolean checkRoom(final Point point, final DungeonTile[][] dungeonSquare) {
        final int x = point.x;
        final int y = point.y;

        final boolean condition = dungeonSquare[x][y] == null;
        if (condition) {
            return false;
        } else {
            return !dungeonSquare[x][y].isTunnel();
        }
    }


    /**
     * remove adventurer from prison
     *
     * @return true if adventurer could be removed
     */
    public boolean adventurerFlee() {
        final boolean condition = !prison.isEmpty();
        if (condition) {
            players.forEach(p ->
                    sc.sendAdventurerFled(p.getCommId(), prison.get(0).getId()));
            prison.remove(0);
            return true;
        }
        return false;
    }

    // todo: Liste mit allen Tunnel im Dungeon hinzufügen sowie Liste mit Tunnel und Room
    // todo: 2te Methode für Dungeon fullyConquered -> End of year for corresponding player

    /**
     * checks if the dungeon is fully conquered
     *
     * @return true if dungeon is fully conquered
     */
    public boolean isDungeonFullyConquered() {
        for (final DungeonTile[] dungeonTiles : dungeonSquare) {
            for (final DungeonTile tile : dungeonTiles) {
                if (tile != null && !tile.getConquered()) {
                    return false;
                }
            }
        }
        return true;
    }

    // todo continue

    /**
     * @return amount of tunnel tiles
     */
    public int countTunnelTiles() {
        int tunnelCounter = 0;

        for (final DungeonTile[] dungeonTiles : dungeonSquare) {
            for (final DungeonTile tile : dungeonTiles) {
                if (tile != null && tile.isTunnel()) {
                    tunnelCounter++;
                }
            }
        }
        return tunnelCounter;
    }

    /**
     * evaluates the damage which the monsters make to the adventurers, and if applicable, put them
     * into prison
     *
     * @param sc             ServerConnection
     * @param dl             DungeonLord
     * @param adventurerList AdventurerList
     */
    public void evalMonsters(final ServerConnection<ActionCommand> sc, final DungeonLord dl,
                             final List<Adventurer> adventurerList) {

        for (final Monster monster : placedMonsters) {

            // no monster which can attack OR no adventurer who can be attacked
            final boolean condition = adventurers.isEmpty() || placedMonsters.isEmpty();
            if (condition) {
                return;
            }

            switch (monster.getAttackStrategy()) {
                case BASIC -> {
                    final int monsterDamage = monster.getDamage();
                    // broadcast about damage
                    adventurers.get(0).takeDamage(
                            monsterDamage, sc, players, dl, adventurerList);
                    //imprisonAdventurer(adventurers, 0);
                    monster.setAvailable(false);
                }
                case TARGETED -> {
                    final int monsterDamage = monster.getDamage();
                    adventurers.get(monster.getMonsterTarget()).takeDamage(
                            monsterDamage, sc, players, dl, adventurerList);
                    //imprisonAdventurer(adventurers, monster.getMonsterTarget());
                    monster.setAvailable(false);
                }
                case MULTI -> {
                    final int monsterDamage = monster.getDamage();

                    for (final Adventurer adventurer : adventurers) {
                        adventurer.takeDamage(
                                monsterDamage, sc, players, dl, adventurerList);
                        //imprisonAdventurer(adventurers, i);
                    }
                    monster.setAvailable(false);
                }
                default -> sc.sendActionFailed(dl.getCommId(), "eval Monsters failed");
            }
        }
    }

    /**
     * adventurers conquer a tunnel tile
     *
     * @param sc ServerConnection
     * @param dl DungeonLord
     */
    public void conquerTunnel(final ServerConnection<ActionCommand> sc, final DungeonLord dl) {
        final int x = battleGroundPosition.x;
        final int y = battleGroundPosition.y;

        // check whether adventurers conquer tunnel, if so conquer tunnel and send broadcast
        final boolean condition = !adventurers.isEmpty();
        if (condition) {
            dungeonSquare[x][y].setConquered(true);
            players.forEach(player ->
                    sc.sendTunnelConquered(player.getCommId(), adventurers.get(0).getId(), x, y));
            players.forEach(player -> sc.sendEvilnessChanged(player.getCommId(), -1,
                    dl.getId()));
        }
    }

    /**
     * @param adventurer Adventurer damaged
     * @return returns value to heal
     */
    private int damageToHeal(final Adventurer adventurer) {
        return (adventurer.getMaxHealthPoints()) - adventurer.getHealthPoints();
    }

    /**
     * heal Adventurer
     *
     * @param sc ServerConnection
     */
    public void healAdventurer(final ServerConnection<ActionCommand> sc) {

        int toHeal = 0; // index of adventurer to be healed
        int priest = -1; // index for priest id

        for (final Adventurer adventurer : adventurers) {

            priest++;

            if (adventurer.getHealValue() > 0) {

                int currentHealValue = adventurer.getHealValue();

                while (currentHealValue > 0 && toHeal < adventurers.size()) {


                    if (isFullyHealed(adventurers.get(toHeal))) {

                        toHeal++;

                    } else if (!isFullyHealed(adventurers.get(toHeal))
                            && adventurers.get(toHeal).getHealthPoints() + currentHealValue
                            >= adventurers.get(toHeal).getMaxHealthPoints()) {

                        final int damage = damageToHeal(adventurers.get(toHeal));
                        currentHealValue = currentHealValue - damageToHeal(adventurers.get(toHeal));

                        adventurers.get(toHeal).setHealthPoints(
                                adventurers.get(toHeal).getMaxHealthPoints());

                        final int priestfinal = priest;
                        final int toHealFinal = toHeal;

                        players.forEach(p ->
                                sc.sendAdventurerHealed(p.getCommId(),
                                        damage, adventurers.get(priestfinal).getId(),
                                        adventurers.get(toHealFinal).getId()));

                        toHeal++;
                    } else {

                        final int healValue = currentHealValue;

                        adventurers.get(toHeal).setHealthPoints(
                                adventurers.get(toHeal).getHealthPoints() + currentHealValue
                        );

                        currentHealValue = 0;

                        final int priestfinal = priest;
                        final int toHealFinal = toHeal;

                        players.forEach(p ->
                                sc.sendAdventurerHealed(p.getCommId(),
                                        healValue, adventurers.get(priestfinal).getId(),
                                        adventurers.get(toHealFinal).getId()));
                    }
                }
            }
        }
    }

    /**
     * @param adventurer adventurer
     * @return true or false
     */
    public boolean isFullyHealed(final Adventurer adventurer) {

        return adventurer.getHealthPoints() == adventurer.getMaxHealthPoints();

    }

    /*                sc.sendAdventurerHealed(p.getCommId(),
    damageToHeal(adventurers.get(finalToHeal)),
    finalPriest,
    finalToHeal));
    */

    /**
     * credit: Max
     *
     * @param point point
     * @return true if there's no 2x2 square
     */
    public boolean checkNo2By2Square(final Point point) {
        final int x = point.x;
        final int y = point.y;
        final List<List<Point>> pointsToCheck = new ArrayList<>();

        pointsToCheck.add(List.of(
                new Point(x + 1, y),
                new Point(x + 1, y + 1),
                new Point(x, y + 1)
        ));
        pointsToCheck.add(List.of(
                new Point(x - 1, y),
                new Point(x - 1, y + 1),
                new Point(x, y + 1)
        ));
        pointsToCheck.add(List.of(
                new Point(x - 1, y),
                new Point(x - 1, y - 1),
                new Point(x, y - 1)
        ));
        pointsToCheck.add(List.of(
                new Point(x + 1, y),
                new Point(x + 1, y - 1),
                new Point(x, y - 1)
        ));

        return pointsToCheck.stream().noneMatch(points ->
                points.stream().allMatch(p ->
                        checkInBounds(p) && dungeonSquare[p.x][p.y] != null));
    }

    /**
     * clear the list of adventurers
     */
    public void clearAdventurers() {
        adventurers.clear();
    }

    /**
     * @param adventurer adventurer
     */
    public void addAdventurer(final Adventurer adventurer) {
        adventurers.add(adventurer);
    }

    /**
     * @return battleGroundPosition
     */
    public Point getBattleGroundPosition() {
        return battleGroundPosition;
    }

    /**
     * @return adventurers
     */
    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    /**
     * @return prison
     */
    public List<Adventurer> getPrison() {
        return prison;
    }

    /**
     * @return placedMonsters
     */
    public List<Monster> getPlacedMonsters() {
        return placedMonsters;
    }

    /**
     * @return placedTrap
     */
    public Trap getPlacedTrap() {
        return placedTrap;
    }


    /**
     * get a list of all rooms owned by this player
     *
     * @return list
     */
    public List<Room> getPlayersOwnedRooms() {
        return playersOwnedRooms;
    }

    /**
     * @return dungeonSquare, e.g. for evaluation of tiles in EndState
     */
    public DungeonTile[][] getDungeonSquare() {
        return dungeonSquare.clone();
    }

    /**
     * @param dungeonSquare dungeonSquare
     */
    public void setDungeonSquare(final DungeonTile[][] dungeonSquare) {
        this.dungeonSquare = dungeonSquare;
    }

    /**
     * This method evaluates the fatigue damage
     *
     * @param sc             ServerConnection
     * @param dl             DungeonLord
     * @param adventurerList AdventurerList
     */
    public void evalFatigue(final ServerConnection<ActionCommand> sc, final DungeonLord dl,
                            final List<Adventurer> adventurerList) {
        final List<Adventurer> temp = adventurerList.stream().toList();
        temp.forEach(a -> a.takeDamage(2, sc, players, dl, adventurerList));
        //for (final Adventurer adv : adventurers) {
        //adv.takeDamage(2, sc, players, dl, adventurerList);
        //}
    }
}