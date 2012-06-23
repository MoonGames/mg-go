/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.model;

import cz.mgn.go.game.view.ViewInterface;

/**
 *
 * @author filcicyr
 */
public class TheGame implements GameInterface {    
    
    protected ViewInterface viewInterface = null;
    protected Desk desk = null;
    protected Player playerOne = null;
    protected Player playerTwo = null;
    protected Player onTurn = null;
    protected boolean lastGameWasPassed = false;
    protected boolean gameEnded = false;
    protected int turnsCount = 0;
    /**
     * Constructor
     * @param viewInterface object of viewInterface, in this case its MainGameFrame
     * @param playgroundSize size of playground 
     * @param playerOne name of first player
     * @param playerTwo name of second player
     */
    public TheGame(ViewInterface viewInterface, int playgroundSize, String playerOne, String playerTwo) {
        this.viewInterface = viewInterface;
        desk = new Desk(playgroundSize);
        this.playerOne = new Player(1, playerOne);
        this.playerTwo = new Player(2, playerTwo);
        setOnTurn(this.playerTwo);
    }
    public TheGame(ViewInterface viewInterface, int[][] playground, Player playerOne, Player playerTwo) {
        this.viewInterface = viewInterface;
        desk = new Desk(playground);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        setOnTurn(this.playerTwo);
    }
    /**
     * if player passed turn set lastGameWasPassed true
     */
    public void playerPass() {
        if (!gameEnded) {
            nextTurn();
            if (lastGameWasPassed) {
                endGame();
            } else {
                onTurn.setAfterLastTurn(desk.getPlaygroundValues());
                setOnTurn(opositePlayer());
                lastGameWasPassed = true;
            }
        }
    }
    /**
     * if invoked players cant play (game ends) and prints who won or if its draw
     */
    protected void endGame() {
        int[] scores = countScores();
        viewInterface.showScores(scores[0], scores[1]);
        showStonesCount();
        if (scores[0] == scores[1]) {
            viewInterface.showGameState("Game ends! Players draw!");
        } else {
            String p = playerOne.getNick();
            if (scores[1] > scores[0]) {
                p = playerTwo.getNick();
            }
            viewInterface.showGameState("Game ends! Player " + p + " win!");
        }
        viewInterface.showOnTurn("nobody");
        gameEnded = true;
    }
    /**
     * gives turn to next player and shows rounds number
     */
    protected void nextTurn() {
        turnsCount++;
        viewInterface.showTurn(turnsCount);
    }
    /**
     * show how many stones have each player
     */
    protected void showStonesCount() {
        int[] stonesCount = desk.countStonesCount(playerOne, playerTwo);
        viewInterface.showStonesCounts(stonesCount[0], playerOne.getStonesTaken(), stonesCount[1], playerTwo.getStonesTaken());
    }
    /**
     * show and set which player is on turn
     * @param player object of Player
     */
    protected void setOnTurn(Player player) {
        if (onTurn != player) {
            lastGameWasPassed = false;
        }
        this.onTurn = player;
        viewInterface.showOnTurn(player.getNick());
        viewInterface.setOnTurn(player.getColour());
    }

    /** returns player that is not currently playing */
    protected Player opositePlayer() {
        if (onTurn == playerOne) {
            return playerTwo;
        }
        return playerOne;
    }
    /**
     * count scores for each player
     * @return array where each index belong to each player
     */
    protected int[] countScores() {
        int[] scores = desk.countTerritories(playerOne, playerTwo);
        scores[0] += playerTwo.getStonesTaken();
        scores[1] += playerOne.getStonesTaken();
        return scores;
    }
    /**
     * plays stone on inputed position, catches invalid moves, saves playground
     * shows player on turn, played playground, stone count, scores and
     * @param x x-coordinate that is played
     * @param y y-coordinate that is played
     */
    public void play(int x, int y) {
        if (!gameEnded) {
            State state = desk.play(x, y, onTurn, opositePlayer());
            if (state.isPlayableTurn()) {
                nextTurn();
                int[] scores = countScores();
                state.setPlayersScore(scores[0], scores[1]);
                onTurn.setAfterLastTurn(desk.getPlaygroundValues());
                setOnTurn(opositePlayer());
                viewInterface.showPlayground(desk.getPlaygroundValues());
                viewInterface.showOnTurn(onTurn.getNick());
                showStonesCount();
                viewInterface.showScores(state.getPlayerOneScore(), state.getPlayerTwoScore());
                viewInterface.showGameState("Valid turn!");
            } else {
                viewInterface.showGameState("Not valid turn!");
            }
        }
    }
    public void saveGame() {
       new GameSaver().saveGame(this);
    }
}
