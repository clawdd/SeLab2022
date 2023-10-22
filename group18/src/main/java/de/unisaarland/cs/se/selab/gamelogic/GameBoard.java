package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents the game board
 */
public class GameBoard {

    private final ServerConnection<ActionCommand> sc;
    private final List<DungeonLord> players;
    private final Map<Integer, DungeonLord> commIdDungeonLordMap;
    private final BiddingSquare biddingSquare;
    private final int maxPlayers;
    private int currentYear;
    private final int maxYears;
    private int currentSeason;
    private final int dungeonSideLength;
    private DungeonLord currentPlayer;
    private final int initialGold;
    private final int initialFood;
    private final int initialImps;
    private final int initialEvilness;
    private int counterForPlayerID;

    /**
     * This Constructor creates the GameBoard.
     *
     * @param sc                ServerConnection
     * @param maxPlayers        maxPlayers
     * @param maxYears          maxYears
     * @param initialFood       initial Food
     * @param initialGold       initial Gold
     * @param initialImps       initial imps
     * @param initialEvilness   initialEvilness
     * @param dungeonSideLength dungeonSideLength
     */
    public GameBoard(final ServerConnection<ActionCommand> sc, final int maxPlayers,
                     final int maxYears, final int initialFood,
                     final int initialGold, final int initialImps,
                     final int initialEvilness, final int dungeonSideLength) {
        this.sc = sc;
        this.players = new ArrayList<>();
        this.commIdDungeonLordMap = new HashMap<>();
        this.biddingSquare = new BiddingSquare(sc, this);
        this.maxPlayers = maxPlayers;
        this.currentYear = 0;
        this.maxYears = maxYears;
        this.currentSeason = 0;
        this.dungeonSideLength = dungeonSideLength;
        this.currentPlayer = null;
        this.initialFood = initialFood;
        this.initialGold = initialGold;
        this.initialImps = initialImps;
        this.initialEvilness = initialEvilness;
    }

    /**
     * This method adds a player (dungeonLord) into the player list.
     *
     * @param name   name of dungeonLord
     * @param commId commId of dungeonLord
     */
    public void addPlayer(final String name, final int commId) {
        this.players.add(// creates new DungeonLord
                new DungeonLord(name, counterForPlayerID, commId, this.dungeonSideLength,
                        initialGold, initialFood, initialImps, initialEvilness, sc, players));

        // maps DungeonLord to commID
        this.commIdDungeonLordMap.put(commId, this.players.get(players.size() - 1));

        counterForPlayerID++;
    }

    /**
     * This method removes the player with the given commId
     *
     * @param commId commId of the player
     */
    public void removePlayer(final int commId) {
        final var playerMaybe = getDungeonLordByCommId(commId);
        if (playerMaybe.isEmpty()) {
            return;
        }
        final var playerWhoLeft = playerMaybe.get();
        if (playerWhoLeft.equals(currentPlayer)) {
            setCounterForCurrentPlayer();
        }
        players.remove(playerWhoLeft);
        // broadcast events Left, then AdventurerFled
        players.forEach(p -> {
            sc.sendLeft(p.getCommId(), playerWhoLeft.getId());
            playerWhoLeft.getDungeon().getPrison().forEach(a ->
                    sc.sendAdventurerFled(p.getCommId(), a.getId()));
        });
    }

    /**
     * This method removes the player with the given commId
     *
     * @param commId commId of the player
     */
    public void removePlayerSilently(final int commId) {
        final var playerMaybe = getDungeonLordByCommId(commId);
        if (playerMaybe.isEmpty()) {
            return;
        }
        final var playerWhoLeft = playerMaybe.get();
        if (playerWhoLeft.equals(currentPlayer)) {
            setCounterForCurrentPlayer();
        }
        players.remove(playerWhoLeft);
        // no broadcast event in this case
    }

    /**
     * This method returns the dungeonLord by commId.
     *
     * @param commId commId of searched dungeonLord
     * @return dungeonLord
     */
    public Optional<DungeonLord> getDungeonLordByCommId(final int commId) {
        return Optional.ofNullable(this.commIdDungeonLordMap.get(commId));
    }

    /**
     * This method checks, if season is 4.
     *
     * @return true or false, depends on season
     */
    public boolean isLastSeason() {
        return (this.currentSeason == 4);
    }

    /**
     * This method checks, if isLastSeason, then currentSeason 1, else currentSeason++.
     */
    public void nextSeason() {
        if (isLastSeason()) {
            this.currentSeason = 1;
        } else {
            this.currentSeason++;
        }
        players.forEach(p -> this.sc.sendNextRound(p.getCommId(), this.currentSeason));
        setCounterForCurrentPlayer();
    }

    /**
     * This method checks, if year is 4.
     *
     * @return true or false, depends on year
     */
    public boolean isLastYear() {
        return (this.currentYear == this.maxYears);
    }

    /**
     * This method checks, if isLastSeason, then currentSeason 1, else currentSeason++.
     */
    public void nextYear() {
        this.currentYear++;
        makeMonstersAvailable();
        players.forEach(p -> this.sc.sendNextYear(p.getCommId(), this.currentYear));
    }

    /**
     * This setter sets the current player.
     *
     * @param currentPlayer currentPlayer
     */
    public void setCurrentPlayer(final DungeonLord currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * This method updates the currentPlayer to a new one.
     */
    public void setCounterForCurrentPlayer() {
        if (this.currentPlayer == null) {
            this.currentPlayer = players.get(0);
        } else {
            if (players.isEmpty()) {
                return;
            }
            final int i = this.players.indexOf(this.currentPlayer);
            currentPlayer = players.get((i + 1) % players.size());
        }
    }

    /**
     * player is in the game
     *
     * @param player player
     * @return boolean
     */
    public boolean playerIsInTheGame(final DungeonLord player) {
        return getPlayers().stream().anyMatch(p ->
                p.getId() == player.getId());
    }

    /**
     * This method make all monsters of a dungeonLord available again.
     */
    public void makeMonstersAvailable() {
        players.forEach(p -> p.getMonster().forEach(m -> m.setAvailable(true)));
    }

    /**
     * This method returns list of players.
     *
     * @return players
     */
    public List<DungeonLord> getPlayers() {
        return players;
    }

    /**
     * This method returns biddingSquare.
     *
     * @return biddingSquare
     */
    public BiddingSquare getBiddingSquare() {
        return biddingSquare;
    }

    /**
     * This method returns maxPlayers.
     *
     * @return maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * This method returns currentYear.
     *
     * @return currentYear
     */
    public int getCurrentYear() {
        return currentYear;
    }

    /**
     * This method returns maxYears.
     *
     * @return maxYears
     */
    public int getMaxYears() {
        return maxYears;
    }

    /**
     * This method returns currentSeason.
     *
     * @return currentSeason
     */
    public int getCurrentSeason() {
        return currentSeason;
    }

    /**
     * This method returns dungeonSideLength.
     *
     * @return dungeonSideLength
     */
    public int getDungeonSideLength() {
        return dungeonSideLength;
    }

    /**
     * This method returns currentPlayer.
     *
     * @return currentPlayer
     */
    public DungeonLord getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * sets current Season to a specific number
     *
     * @param i season
     */
    public void setCurrentSeason(final int i) {
        this.currentSeason = i;
    }
}
