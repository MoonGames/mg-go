/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.model;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author filcicyr
 */
public class Desk {

    protected int[][] playground = null;

    public Desk(int playgroundSize) {
        playground = new int[playgroundSize][playgroundSize];
    }
    
    public Desk(int[][] playground){
        this.playground = playground;
    }

    /**returns copy of playground*/
    public int[][] getPlaygroundValues() {
        return Desk.copyPlayground(playground);
    }

    /** 
     * return boolean playground where true positions is searched group
     * @param parent playground in progress of finding group
     * @param x at first starting position X then positions of neighbors
     * @param y same as for parameter x
     * @param color of searched group
     * @param playgroundL copy of playground
     * @param secondaryColor of searched group
     * @return boolean matrix 
     */
    protected boolean[][] getGroupNeighbors(boolean[][] parent, int x, int y, int color, int[][] playgroundL, int secondaryColor) {
        if (isInPlayground(x, y)) {
            if (!parent[x][y] && (playgroundL[x][y] == color || playgroundL[x][y] == secondaryColor)) {
                parent[x][y] = true;
                getGroupNeighbors(parent, x + 1, y, color, playgroundL, secondaryColor);
                getGroupNeighbors(parent, x - 1, y, color, playgroundL, secondaryColor);
                getGroupNeighbors(parent, x, y + 1, color, playgroundL, secondaryColor);
                getGroupNeighbors(parent, x, y - 1, color, playgroundL, secondaryColor);
            }
        }
        return parent;
    }

    /**
     * returns group in field (vertically and horizontally merged territory of same colour or empty positions)
     * @param x starting position X (which position must group contain) according to type of stone (empty position) on this position the group is being searched
     * @param y starting point Y (which position must group contain)
     * @param playgroundL playground where searching is being conducted
     * @param secondaryColor another colour that will be counted into the group, -1 if no another colour
     * @return array of positions in group
     */
    protected ArrayList<Point> getGroup(int x, int y, int[][] playgroundL, int secondaryColor) {
        ArrayList<Point> points = null;
        boolean[][] group1 = new boolean[playgroundL.length][playgroundL.length];
        for (int i = 0; i < playgroundL.length; i++) {
            for (int j = 0; j < playgroundL.length; j++) {
                group1[i][j] = false;
            }
        }
        getGroupNeighbors(group1, x, y, playgroundL[x][y], playgroundL, secondaryColor);
        points = getGroupPointsFromArray(group1);
        return points;
    }

    /**
     * converts true positions of boolean matrix into array list
     * @param array boolean matrix
     * @return array of groups positions
     */
    protected ArrayList<Point> getGroupPointsFromArray(boolean[][] array) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < playground.length; i++) {
            for (int j = 0; j < playground.length; j++) {
                if (array[i][j]) {
                    points.add(new Point(i, j));
                }
            }
        }
        return points;
    }

    /**
     * if position is in playground
     * @param x x-coordinate valuated
     * @param y y-coordinate valuated
     * @return boolean
     */
    protected boolean isInPlayground(int x, int y) {
        return (x >= 0 && y >= 0 && x < playground.length && y < playground.length);
    }

    /**
     * if position is empty
     * @param x x-coordinate valuated
     * @param y y-coordinate valuated
     * @param playgroundL playground on witch is position valuated
     * @return boolean
     */
    protected boolean isEmpty(int x, int y, int[][] playgroundL) {
        if (isInPlayground(x, y)) {
            return playgroundL[x][y] == 0;
        }
        return false;
    }

    /** 
     * if group contains empty position return true and if group is whole playground return false
     * @param group
     * @param playgroundL
     * @return boolean
     */
    protected boolean isGroupDead(ArrayList<Point> group, int[][] playgroundL) {
        for (Point p : group) {
            if (isEmpty(p.x + 1, p.y, playgroundL) || isEmpty(p.x - 1, p.y, playgroundL) || isEmpty(p.x, p.y + 1, playgroundL) || isEmpty(p.x, p.y - 1, playgroundL)) {
                return false;
            }
        }
        if (group.size() == (playgroundL.length * playgroundL.length)) {
            return false;
        }
        return true;
    }

    /**
     * checks if x, y is valid move
     * if players move would "kill" his stones (group) move is not valid
     * if playground would be the same as playground before opponent move, this move is not valid
     * @param x position X where player wants to put his stone
     * @param y position Y where player wants to put his stone
     * @param player current player colour
     * @param playgroundL copy of playground
     * @return yes if move on position x, y is valid
     */
    protected boolean addStone(int x, int y, Player player, int[][] playgroundL) {
        if (playgroundL[x][y] != 0) {
            return false;
        }

        int[][] playgroundL2 = Desk.copyPlayground(playgroundL);
        playgroundL2[x][y] = player.getColour();
        int[][] playgroundL3 = Desk.copyPlayground(playgroundL2);

        boolean b = !testNewStone(x, y, player, null, true, playgroundL3, false);
        if ((isGroupDead(getGroup(x, y, playgroundL2, -1), playgroundL2) && b) || isSame(player.getAfterLastTurn(), playgroundL3)) {
            return false;
        }
        playgroundL[x][y] = player.getColour();
        return true;
    }

    /**
     * if playgrounds are same
     * @param playgroundL1 
     * @param playgroundL2
     * @return boolean
     */
    protected boolean isSame(int[][] playgroundL1, int[][] playgroundL2) {
        if (playgroundL1 == null || playgroundL2 == null) {
            return false;
        }
        for (int i = 0; i < playgroundL1.length; i++) {
            for (int j = 0; j < playgroundL1.length; j++) {
                if (playgroundL1[i][j] != playgroundL2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * create state, finds if wanted move is playable if yes then finds if some stones were taken
     * @param x x-coordinate of wanted input of stone
     * @param y y-coordinate of wanted input of stone
     * @param player player that placed the last stone
     * @param enemy enemy of player
     * @return playground state
     * @see PlaygroundState
     */
    protected PlaygroundState addStoneAll(int x, int y, Player player, Player enemy) {
        int[][] playgroundL = getPlaygroundValues();
        State state = new State(player, addStone(x, y, player, playgroundL));
        if (state.isPlayableTurn()) {
            state.setRemovedEnemyStones(testNewStone(x, y, player, enemy, true, playgroundL, true));
        }
        return new PlaygroundState(playgroundL, state);
    }

    /**
     * finds group and if is dead returns it
     * @param x x-coordinate of stone that is in potential dead group
     * @param y y-coordinate of stone that is in potential dead group
     * @param playgroundL playground on which is group being searched
     * @return group of dead stone (if there is one) else null
     */
    protected ArrayList<Point> getGroupIfDead(int x, int y, int[][] playgroundL) {
        ArrayList<Point> group = null;
        if (isInPlayground(x, y)) {
            group = getGroup(x, y, playgroundL, -1);
            if (!isGroupDead(group, playgroundL)) {
                group = null;
            }
        }
        return group;
    }

    /**
     * removes stones from playground and count them to appropriate player as score
     * @param group group that is meant to be removed
     * @param player player that placed the last stone
     * @param enemy opponent of player
     * @param playgroundL playground where stones are removed 
     * @param count stones are counted as score if count has value true
     */
    protected void removeGroup(ArrayList<Point> group, Player player, Player enemy, int[][] playgroundL, boolean count) {
        if (count && group.size() > 0) {
            Point p = group.get(0);
            if (playgroundL[p.x][p.y] == player.getColour()) {
                player.addStonesTaken(group.size());
            } else if (playgroundL[p.x][p.y] == enemy.getColour()) {
                enemy.addStonesTaken(group.size());
            }
        }
        for (Point p : group) {
            playgroundL[p.x][p.y] = 0;
        }
    }

    /**
     * finds dead group (if there is one) and call method removeGroup
     * @param x x-coordinate that is tested for being in dead group
     * @param y y-coordinate that is tested for being in dead group
     * @param player player that placed last stone
     * @param enemy opponent of player
     * @param autoRemove if group should be removed
     * @param playgroundL playground where the testing is conducted
     * @param count if took stones will be counted as score
     * @return boolean
     */
    protected boolean testGroup(int x, int y, Player player, Player enemy, boolean autoRemove, int[][] playgroundL, boolean count) {
        ArrayList<Point> group = getGroupIfDead(x, y, playgroundL);
        if (group != null && group.size() > 0 && playgroundL[group.get(0).x][group.get(0).y] != player.getColour()) {
            if (autoRemove) {
                removeGroup(group, player, enemy, playgroundL, count);
            }
            return true;
        }
        return false;
    }

    /**
     * tests neighbors of placed stone if any of them is in dead group
     * @param x x-coordinate of placed stone
     * @param y y-coordinate of placed stone
     * @param player player that placed last stone 
     * @param enemy opponent of player
     * @param autoRemove if group should be removed
     * @param playgroundL playground where the testing is conducted
     * @param count if took stones will be counted as score
     * @return if any of neighbors was taken (was in dead group)
     */
    protected boolean testNewStone(int x, int y, Player player, Player enemy, boolean autoRemove, int[][] playgroundL, boolean count) {
        boolean any = false;
        if (playgroundL[x][y] == player.getColour()) {
            if (testGroup(x + 1, y, player, enemy, autoRemove, playgroundL, count)) {
                any = true;
            }
            if (testGroup(x - 1, y, player, enemy, autoRemove, playgroundL, count)) {
                any = true;
            }
            if (testGroup(x, y + 1, player, enemy, autoRemove, playgroundL, count)) {
                any = true;
            }
            if (testGroup(x, y - 1, player, enemy, autoRemove, playgroundL, count)) {
                any = true;
            }
        }
        return any;
    }

    /** 
     * uses class territory (defined by group of points and player to whom it belongs)
     * finds territories - finds group for each player and compare them, the one smaller
     * belongs to player for whom was counted and thats comes for every position except
     * ones that are already counted
     * sort territories by size and count them 
     * @param playerOne object Player (first player)
     * @param playerTwo object Player (second player)
     * @return array of two indexes where first index belongs to first player and second to second player
     */
    protected int[] countTerritories(Player playerOne, Player playerTwo) {
        int[] tr = new int[2];
        tr[0] = 0;
        tr[1] = 0;
        boolean[][] counted = new boolean[playground.length][playground.length];
        for (int i = 0; i < counted.length; i++) {
            for (int j = 0; j < counted.length; j++) {
                counted[i][j] = false;
            }
        }

        ArrayList<Territory> territories = new ArrayList<Territory>();
        for (int i = 0; i < counted.length; i++) {
            for (int j = 0; j < counted.length; j++) {
                if (!counted[i][j] && playground[i][j] == 0) {
                    ArrayList<Point> gEmpty = getGroup(i, j, playground, -1);
                    for (Point p : gEmpty) {
                        counted[p.x][p.y] = true;
                    }

                    ArrayList<Point> gPlayer1 = getGroup(i, j, playground, playerTwo.getColour());
                    ArrayList<Point> gPlayer2 = getGroup(i, j, playground, playerOne.getColour());
                    if (gPlayer1.size() != gPlayer2.size()) {
                        ArrayList<Point> gPlayer = getSmaller(gPlayer1, gPlayer2);
                        Player player = null;
                        if (gPlayer == gPlayer1) {
                            player = playerOne;
                        } else {
                            player = playerTwo;
                        }
                        territories.add(new Territory(gPlayer, player));
                    }
                }
            }
        }
        ArrayList<Territory> territoriesSorted = new ArrayList<Territory>();
        for (Territory t : territories) {
            boolean add = false;
            for (int i = 0; i < territoriesSorted.size() && !add; i++) {
                if (territoriesSorted.get(i).getSize() > t.getSize()) {
                    territoriesSorted.add(i, t);
                    add = true;
                }
            }
            if (!add) {
                territoriesSorted.add(t);
            }
        }

        for (int i = 0; i < counted.length; i++) {
            for (int j = 0; j < counted.length; j++) {
                counted[i][j] = false;
            }
        }
        for (int i = 0; i < territoriesSorted.size(); i++) {
            Territory t = territoriesSorted.get(i);
            boolean count = true;
            for (Point p : t.getGroup()) {
                if (counted[p.x][p.y]) {
                    count = false;
                }
            }
            if (count) {
                for (Point p : t.getGroup()) {
                    counted[p.x][p.y] = true;
                }
                if (t.getPlayer() == playerOne) {
                    for (Point p : t.getGroup()) {

                        if (playground[p.x][p.y] == playerTwo.getColour()) {
                            tr[0]++;
                        }
                    }
                    tr[0] += t.getSize();
                } else {
                    for (Point p : t.getGroup()) {
                        if (playground[p.x][p.y] == playerOne.getColour()) {
                            tr[1]++;
                        }
                    }
                    tr[1] += t.getSize();
                }
            }
        }
        return tr;
    }

    /**
     * counts stones on desk for each player
     * @param playerOne object Player (first player)
     * @param playerTwo object Player (second player)
     * @return array where each index belong to different player
     */
    public int[] countStonesCount(Player playerOne, Player playerTwo) {
        int[] counts = new int[2];
        for (int i = 0; i < playground.length; i++) {
            for (int j = 0; j < playground.length; j++) {
                if (playground[i][j] == playerOne.getColour()) {
                    counts[0]++;
                } else if (playground[i][j] == playerTwo.getColour()) {
                    counts[1]++;
                }
            }
        }
        return counts;
    }

    /**
     * compares two arrays by size
     * @param g1 first array
     * @param g2 second array
     * @return the smaller array
     */
    protected ArrayList<Point> getSmaller(ArrayList<Point> g1, ArrayList<Point> g2) {
        if (g2.size() < g1.size()) {
            return g2;
        }
        return g1;
    }

    /**
     * uses class PlaygroundState (defined by playground and state)
     * gets current state of play
     * @param x x-coordinate of wanted input of stone
     * @param y y-coordinate of wanted input of stone
     * @param player player that placed the last stone
     * @param enemy opponent of player
     * @return State
     */
    public State play(int x, int y, Player player, Player enemy) {
        PlaygroundState ps = addStoneAll(x, y, player, enemy);
        playground = ps.getPlaygroundCopy();
        return ps.getState();
    }

    /**
     * creates copy of inputed playground
     * @param playground playground to copy
     * @return copy of playground
     */
    public static int[][] copyPlayground(int[][] playground) {
        int[][] playgroundL = new int[playground.length][playground.length];
        for (int i = 0; i < playgroundL.length; i++) {
            System.arraycopy(playground[i], 0, playgroundL[i], 0, playground[i].length);
        }
        return playgroundL;
    }

    class PlaygroundState {

        protected int[][] playground = null;
        protected State state = null;

        public PlaygroundState(int[][] playground, State state) {
            this.playground = playground;
            this.state = state;
        }

        public int[][] getPlaygroundCopy() {
            return Desk.copyPlayground(playground);
        }

        public State getState() {
            return state;
        }
    }
    
    class Territory {

        protected ArrayList<Point> group = null;
        protected Player player = null;

        public Territory(ArrayList<Point> group, Player player) {
            this.group = group;
            this.player = player;
        }

        public ArrayList<Point> getGroup() {
            return group;
        }

        public Player getPlayer() {
            return player;
        }

        public int getSize() {
            return group.size();
        }
    }
}
