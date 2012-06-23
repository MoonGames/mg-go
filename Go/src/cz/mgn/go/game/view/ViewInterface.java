/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.view;

/**
 *
 * @author filcicyr
 */
public interface ViewInterface {

    public void showPlayground(int[][] playground);

    public void showOnTurn(String nick);

    public void showStonesCounts(int playerOneCount, int playerOneTaken, int playerTwoCount, int playerTwoTaken);

    public void showScores(int playerOneScore, int playerTwoScore);

    public void showGameState(String gameState);

    public void showTurn(int turn);

    public void setOnTurn(int color);
}
