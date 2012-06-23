/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.controller;

import cz.mgn.go.game.model.GameInterface;

/**
 *
 * @author filcicyr
 */
public class MainController {
    
    protected GameInterface gameInterface = null;

    public MainController(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
    }

    public void clickOnPosition(int x, int y) {
        gameInterface.play(x, y);
    }

    public void passGame() {
        gameInterface.playerPass();
    }
    public void saveGame(){
        gameInterface.saveGame();
    }
}
