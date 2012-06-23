/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.model;

/**
 *
 * @author filcicyr
 */
public class State {

    protected Player playerTurn = null;
    protected boolean playableTurn = false;
    protected boolean removedEnemyStones = false;
    protected int playerOneStonesCount = 0;
    protected int playerOneScore = 0;
    protected int playerTwoStonesCount = 0;
    protected int playerTwoScore = 0;
    
    public State(Player playerTurn, boolean playableTurn) {
        this.playableTurn = playableTurn;
    }

    public void setRemovedEnemyStones(boolean removedEnemyStones) {
        this.removedEnemyStones = removedEnemyStones;
    }

    public void setPlayersScore(int playerOneScore, int playerTwoScore) {
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
    }

    public void setPlayersStonesCount(int playerOneStonesCount, int playerTwoStonesCount) {
        this.playerOneStonesCount = playerOneStonesCount;
        this.playerTwoStonesCount = playerTwoStonesCount;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public boolean isPlayableTurn() {
        return playableTurn;
    }

    public boolean isRemovedEnemyStones() {
        return removedEnemyStones;
    }

    public int getPlayerOneStonesCount() {
        return playerOneStonesCount;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public int getPlayerTwoStonesCount() {
        return playerTwoStonesCount;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }
}
