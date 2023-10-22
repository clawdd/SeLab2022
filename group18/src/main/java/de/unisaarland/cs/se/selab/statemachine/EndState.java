package de.unisaarland.cs.se.selab.statemachine;

import static de.unisaarland.cs.se.selab.statemachine.StateEnum.END;
import static de.unisaarland.cs.se.selab.statemachine.StateEnum.REALEND;

import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import de.unisaarland.cs.se.selab.gamelogic.dungeon.DungeonTile;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the evaluation of the game points:
 * - calculates individual scores
 * - determines winner(s)
 * - sends out broadcast event
 */
public class EndState extends State {

    /*
    monster                 +1              1 field
    room                    +2              1
    tile conquered          -2              1
    imprisoned adventurer   +2              1
    titles                  +3 || +2        7           => 11 fields
        The Lord of Dark Deeds: the dungeon lord who ranks the highest on the Evil-O-Meter
        The Lord of Halls: the dungeon lord who has the most rooms
        The Tunnel Lord: the dungeon lord who has the longest tunnel
        The Monster Lord: the dungeon lord with the most monsters
        The Lord of Imps: the dungeon lord with the most imps
        The Lord of Riches: the dungeon lord with the most food and gold left
        The Battle-lord: the dungeon lord with the most unconquered tiles
     */

    final List<DungeonLord> dungeonLordList = stateMachine.getGameBoard().getPlayers();

    final int[][] tableOfAllScores = new int[11][dungeonLordList.size()];
    // row 0: conquered tile, row 1: unconquered tile,
    // row 2: tunnel tile, row 3: rooms
    final int[][] evaluationDungeon = new int[5][dungeonLordList.size()];

    /**
     * END hard-coded enum-value
     *
     * @param stateMachine in order to access the StateMachine and e.g. updateState
     */
    public EndState(final StateMachine stateMachine) {
        super(stateMachine, END);
    }

    /**
     * concrete implementation of run in this state: - wait for nextAction - - Events and Actions: -
     * invoke execute-method
     */
    @Override
    public void run() {
        if (getStateMachine().getGameBoard().getPlayers().isEmpty()) {
            stateMachine.getCurrentState().setCurrentStateEnum(REALEND);
            return;
        }

        // calculate dungeon properties as specified in evaluationDungeon
        calculateDungeonProperties1();
        calculateDungeonProperties2();
        calculateDungeonProperties3();

        calculatePoints();

        // calculates individual score / sum of single player
        int[] individualScore = new int[dungeonLordList.size()];
        for (int j = 0; j < dungeonLordList.size(); j++) {
            for (int i = 0; i < 11; i++) {
                individualScore[j] += tableOfAllScores[i][j]; // goes down 2D array vertically
            }
        }

        // broadcast winner(s)
        for (int i = 0; i < dungeonLordList.size(); i++) {
            // winner is everyone OR single player who has the highest score
            if (Arrays.stream(individualScore).max().getAsInt() == individualScore[i]) {
                final int finalI = i;
                final int finalI1 = i;
                dungeonLordList.forEach(p ->
                        stateMachine.getSc().sendGameEnd(
                                p.getCommId(), dungeonLordList.get(finalI).getId(),
                                individualScore[finalI1]));
            }
        }
        stateMachine.getCurrentState().setCurrentStateEnum(REALEND);
    }

    /**
     * Calculates the following Dungeon properties: row 0: conquered tile, row 1: unconquered tile
     */
    public void calculateDungeonProperties1() {

        // for-loops: evaluate individual tiles for every dungeon lord
        for (int i = 0; i < dungeonLordList.size(); i++) {
            for (final DungeonTile[] subarray :
                    dungeonLordList.get(i).getDungeon().getDungeonSquare()) {
                for (final DungeonTile tile : subarray) {
                    // check whether tile is conquered or unconquered
                    if (tile != null && tile.getConquered()) {
                        evaluationDungeon[0][i] += 1;
                    } else if (tile != null && !tile.getConquered()) {
                        evaluationDungeon[1][i] += 1;
                    }
                }
            }
        }
    }

    /**
     * Calculates the following Dungeon properties: row 2: tunnel tile, row 3: rooms
     */
    public void calculateDungeonProperties2() {

        // for-loops: evaluate individual tiles for every dungeon lord
        for (int i = 0; i < dungeonLordList.size(); i++) {
            for (final DungeonTile[] subarray :
                    dungeonLordList.get(i).getDungeon().getDungeonSquare()) {
                for (final DungeonTile tile : subarray) {
                    // check whether tile is a tunnel tile or room
                    if (tile != null && tile.isTunnel()) {
                        evaluationDungeon[2][i] += 1;
                    } else if (tile != null && !tile.isTunnel()) {
                        evaluationDungeon[3][i] += 1;
                    }
                }
            }
        }
    }

    /**
     * This method calculates the dungeon properties 3.
     */
    public void calculateDungeonProperties3() {

        // for-loops: evaluate individual tiles for every dungeon lord
        for (int i = 0; i < dungeonLordList.size(); i++) {
            for (final DungeonTile[] subarray :
                    dungeonLordList.get(i).getDungeon().getDungeonSquare()) {
                for (final DungeonTile tile : subarray) {
                    // check whether tunnel tile is conquered
                    if (tile != null && tile.isTunnel() && !tile.getConquered()) {
                        evaluationDungeon[4][i] += 1;
                    }
                }
            }
        }
    }

    /**
     * used to calculate all score points
     */
    public void calculatePoints() {
        // invokes all relevant methods for final scoreboard
        // 4 basic scores
        calculatePointsBasic();
        // 7 titles
        calculateTitleLordOfDarkDeeds();
        calculateTitleLordOfHalls();
        calculateTitleTunnelLord();
        calculatePointsMonsterLord();
        calculatePointsImpLord();
        calculatePointsGoldAndFood();
        calculateUnconqueredTiles();
    }

    /**
     * calculate the final scores of monsters, rooms, conquered tiles, imprisoned adventurers
     */
    public void calculatePointsBasic() {
        for (int i = 0; i < dungeonLordList.size(); i++) {
            // monsters
            tableOfAllScores[0][i] = dungeonLordList.get(i).getMonster().size();
            // rooms
            tableOfAllScores[1][i] = evaluationDungeon[3][i] * 2;
            // conquered tiles
            tableOfAllScores[2][i] = evaluationDungeon[0][i] * (-2);
            // imprisoned adventurers
            tableOfAllScores[3][i] = dungeonLordList.get(i).getDungeon().getPrison().size() * 2;
        }
    }

    /**
     * The Lord of Dark Deeds: the dungeon lord who ranks the highest on the Evil-O-Meter used to
     * determine the final bonus points, first title
     */
    public void calculateTitleLordOfDarkDeeds() {
        int highestValue = -1; // dummy value -> will be initialised later
        int positionHighestValue = -1; // dummy value -> will be initialised later

        // calculates the highest evilness; saves max and player position
        for (int i = 0; i < dungeonLordList.size(); i++) {
            if (dungeonLordList.get(i).getEvilness() > highestValue) {
                highestValue = dungeonLordList.get(i).getEvilness();
                positionHighestValue = i;
            }
        }

        // tried out a helper method; only used for this title
        givePointsToWinnerS(dungeonLordList, tableOfAllScores, 4,
                highestValue, positionHighestValue);
    }

    /**
     * gives the correct score to winner(s) by comparison of the highest value
     * only works for getEvilness, tried to make it work for others but was unable to succeed
     *
     * @param dungeonLords         List of DungeonLord
     * @param scoreBoard           2D array in which all scores are stored
     * @param rowOfArray           row in which to insert score into scoreBoard
     * @param highestValue         as handed over by function
     * @param positionHighestValue position of DungeonLord with the highest score
     */
    public void givePointsToWinnerS(final List<DungeonLord> dungeonLords,
                                    final int[][] scoreBoard, final int rowOfArray,
                                    final int highestValue,
                                    final int positionHighestValue) {

        // assigns 3 points preliminary to winner; may be adjusted later
        scoreBoard[rowOfArray][positionHighestValue] = 3;

        // compares individual score with max
        for (int j = 0; j < dungeonLords.size(); j++) {
            // checks for possible tie; if so, adjusts points of winners
            if (dungeonLords.get(j).getEvilness() == highestValue
                    && positionHighestValue != j) {
                scoreBoard[rowOfArray][j] = 2;
                scoreBoard[rowOfArray][positionHighestValue] = 2;
            }
        }
    }

    /**
     * The Lord of Halls: the dungeon lord who has the most rooms used to determine the final bonus
     * points, second title
     */
    public void calculateTitleLordOfHalls() {
        int highestValue = -1; // dummy value -> will be initialised later
        int positionHighestValue = -1; // dummy value -> will be initialised later

        // calculates the number of rooms owned by player; saves max and player position
        for (int i = 0; i < dungeonLordList.size(); i++) {
            if (dungeonLordList.get(i).getDungeon().getPlayersOwnedRooms().size()
                    > highestValue) {
                highestValue = dungeonLordList.get(i).getDungeon().getPlayersOwnedRooms().size();
                positionHighestValue = i;
            }
        }

        // assigns 3 points preliminary to winner; may be adjusted later
        tableOfAllScores[5][positionHighestValue] = 3;
        // compares individual score with max
        for (int j = 0; j < dungeonLordList.size(); j++) {
            // checks for possible tie; if so, adjusts points of winners
            if (dungeonLordList.get(j).getDungeon().getPlayersOwnedRooms().size() == highestValue
                    && positionHighestValue != j) {
                tableOfAllScores[5][j] = 2;
                tableOfAllScores[5][positionHighestValue] = 2;
            }
        }
    }

    /**
     * The Tunnel Lord: the dungeon lord who has the longest tunnel used to determine the final
     * bonus points, third title
     */
    public void calculateTitleTunnelLord() {
        int highestValue = -1;
        int positionHighestValue = -1;
        int[] scoreTunnelIndividual = new int[dungeonLordList.size()];

        // calculates the number of tunnel tiles owned by player; saves max and player position
        for (int i = 0; i < dungeonLordList.size(); i++) {
            scoreTunnelIndividual[i] = // number of conquered + #unconquered tiles - #rooms
                    evaluationDungeon[0][i] + evaluationDungeon[1][i] - evaluationDungeon[3][i];
            if (scoreTunnelIndividual[i] > highestValue) {
                highestValue = scoreTunnelIndividual[i];
                positionHighestValue = i;
            }
        }

        // assigns 3 points preliminary to winner; may be adjusted later
        tableOfAllScores[6][positionHighestValue] = 3;

        // compares individual score with max
        for (int j = 0; j < dungeonLordList.size(); j++) {
            // checks for possible tie; if so, adjusts points of winners
            if (scoreTunnelIndividual[j] == highestValue && j != positionHighestValue) {
                tableOfAllScores[6][j] = 2;
                tableOfAllScores[6][positionHighestValue] = 2;
            }
        }
    }

    /**
     * The Monster Lord: the dungeon lord with the most imps
     */
    public void calculatePointsMonsterLord() {
        int highestValue = -1;
        int positionHighestValue = -1;

        // calculates the number of monsters, reuses 0th row which was calculated before
        for (int i = 0; i < dungeonLordList.size(); i++) {
            if (tableOfAllScores[0][i] > highestValue) {
                highestValue = tableOfAllScores[0][i];
                positionHighestValue = i;
            }
        }

        // assigns 3 points preliminary to winner; may be adjusted later
        tableOfAllScores[8][positionHighestValue] = 3;

        // compares individual score with max
        for (int j = 0; j < dungeonLordList.size(); j++) {
            // checks for possible tie; if so, adjusts points of winners
            if (tableOfAllScores[0][j] == highestValue
                    && positionHighestValue != j) {
                tableOfAllScores[8][j] = 2;
                tableOfAllScores[8][positionHighestValue] = 2;
            }
        }
    }


    /**
     * The Lord of Imps: the dungeon lord with the most monsters
     */
    public void calculatePointsImpLord() {
        int highestValue = -1;
        int positionHighestValue = -1;

        // calculates the number of Imps owned by single player
        for (int i = 0; i < dungeonLordList.size(); i++) {
            if (dungeonLordList.get(i).getImps().size() > highestValue) {
                highestValue = dungeonLordList.get(i).getImps().size();
                positionHighestValue = i;
            }
        }

        // assigns 3 points preliminary to winner; may be adjusted later
        tableOfAllScores[7][positionHighestValue] = 3;

        // compares individual score with max
        for (int j = 0; j < dungeonLordList.size(); j++) {
            // checks for possible tie; if so, adjusts points of winners
            if (dungeonLordList.get(j).getImps().size() == highestValue
                    && positionHighestValue != j) {
                tableOfAllScores[7][j] = 2;
                tableOfAllScores[7][positionHighestValue] = 2;
            }
        }
    }


    /**
     * The Lord of Riches: the dungeon lord with the most food and gold left
     */
    public void calculatePointsGoldAndFood() {
        int highestValue = -1;
        int positionHighestValue = -1;

        // checks for the highest number of gold and food combined
        for (int i = 0; i < dungeonLordList.size(); i++) {
            final int sumGoldAndFood = dungeonLordList.get(i).getFood()
                    + dungeonLordList.get(i).getGold();
            if (sumGoldAndFood > highestValue) {
                highestValue = sumGoldAndFood;
                positionHighestValue = i;
            }
        }

        // assigns 3 points preliminary to winner; may be adjusted later
        tableOfAllScores[9][positionHighestValue] = 3;

        // compares individual score with max
        for (int j = 0; j < dungeonLordList.size(); j++) {
            final int sumGoldAndFood = dungeonLordList.get(j).getFood()
                    + dungeonLordList.get(j).getGold();
            // checks for possible tie; if so, adjusts points of winners
            if (sumGoldAndFood == highestValue
                    && positionHighestValue != j) {
                tableOfAllScores[9][j] = 2;
                tableOfAllScores[9][positionHighestValue] = 2;
            }
        }
    }

    /**
     * The Battle-lord: the dungeon lord with the most unconquered tiles
     */
    public void calculateUnconqueredTiles() {
        int highestUnconqueredTiles = -1;
        int positionOfHighest = -1;

        // checks for the highest number of unconquered tiles
        for (int i = 0; i < dungeonLordList.size(); i++) {
            if (evaluationDungeon[1][i] > highestUnconqueredTiles) {
                highestUnconqueredTiles = evaluationDungeon[1][i];
                positionOfHighest = i;
            }
        }

        // assigns 3 points preliminary to winner; may be adjusted later
        tableOfAllScores[10][positionOfHighest] = 3;

        // compares individual score with max
        for (int j = 0; j < dungeonLordList.size(); j++) {
            // checks for possible tie; if so, adjusts points of winners
            if (evaluationDungeon[1][j] == highestUnconqueredTiles
                    && positionOfHighest != j) {
                tableOfAllScores[10][j] = 2;
                tableOfAllScores[10][positionOfHighest] = 2;
            }
        }
    }

    /**
     * @return tableOfAllScores
     */
    public int[][] getTableOfAllScores() {
        return tableOfAllScores.clone();
    }

    /**
     * @return evaluationDungeon
     */
    public int[][] getEvaluationDungeon() {
        return evaluationDungeon.clone();
    }

}

