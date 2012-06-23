/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.model;

/**
 *
 * @author filcicyr
 */
public class Player {
    
    protected String nick = "player";
    protected int color = 0;
    protected int stonesTaken = 0;
    protected int[][] afterLastTurn = null;
    /**
     * Constructor
     * @param color integer representing player colour
     * @param nick string containing name of player
     */
    public Player(int color, String nick) {
        this.color = color;
        this.nick = nick;
    }
    public Player(int color, String nick, int stonesTaken, int[][] afterLastTurn){
        this.nick = nick;
        this.color = color;
        this.stonesTaken = stonesTaken;
        this.afterLastTurn = afterLastTurn;
    }
    /**
     * getter for nick
     */
    public String getNick() {
        return nick;
    }
    /**
     * getter for colour
     */
    public int getColour() {
        return color;
    }
    /**
     * getter for taken stones
     */
    public int getStonesTaken() {
        return stonesTaken;
    }
    /**
     * getter for saved playground 
     */
    public int[][] getAfterLastTurn() {
        return afterLastTurn;
    }
    /**
     * saves playground (setter)
     * @param afterLastTurn playground matrix
     */
    public void setAfterLastTurn(int[][] afterLastTurn) {
        this.afterLastTurn = afterLastTurn;
    }
    /**
     * add taken stones
     * @param stonesTaken number of taken stones to be counted
     */
    public void addStonesTaken(int stonesTaken) {
        this.stonesTaken += stonesTaken;
    }
}
