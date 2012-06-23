/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game;

import cz.mgn.go.game.controller.MainController;
import cz.mgn.go.game.model.TheGame;
import cz.mgn.go.game.view.MainGameFrame;

/**
 *
 * @author filcicyr
 */
public class GameStarter {
    /**
     * Starts the game
     * creates main game frame where is playground
     * creates TheGame class (game logic)
     * creates and sets main controller
     * @param playgroundSize size of playground
     * @param playerOne name of first player
     * @param playerTwo name of second player
     */
    public static void startGame(int playgroundSize, String playerOne, String playerTwo) {
        MainGameFrame mgf = new MainGameFrame(playgroundSize, playerOne, playerTwo);
        TheGame game = new TheGame(mgf, playgroundSize, playerOne, playerTwo);
        MainController mc = new MainController(game);
        mgf.setMainController(mc);
    }
}
