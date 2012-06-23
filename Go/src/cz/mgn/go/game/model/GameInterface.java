/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.model;

/**
 *
 * @author filcicyr
 */
public interface GameInterface {

    public void playerPass();

    public void play(int x, int y);
    
    public void saveGame();
}
