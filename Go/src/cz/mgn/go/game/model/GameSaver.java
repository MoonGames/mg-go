/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.model;

import cz.mgn.go.game.controller.MainController;
import cz.mgn.go.game.view.InformationPanel;
import cz.mgn.go.game.view.MainGameFrame;
import cz.mgn.go.menu.MainMenu;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author filcik
 */
public class GameSaver {

    TheGame game;

    public void saveGame(TheGame game) {
        this.game = game;
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(InformationPanel.AccessibleJComponent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            saveFile(file.getPath());
        }
    }

    public void loadGame() throws FileNotFoundException, IOException {
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(MainMenu.AccessibleJComponent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            loadFile(file.getPath());
        }
    }

    public void loadFile(String path) {
        FileReader stream = null;
        try {
            stream = new FileReader(path);
            BufferedReader in = new BufferedReader(stream);
            //read playground size
            int size = Integer.parseInt(in.readLine());
            //read playerOne
            String[] line = in.readLine().split(",");
            int[][] playground = new int[size][size];
            for (int i = 0; i < size; i++) {
                String row = in.readLine();
                 for (int j = 0; j < size; j++) {
                    playground[i][j] = Integer.parseInt(""+row.charAt(j));
                }                
            }
            Player playerOne = new Player(Integer.parseInt(line[1]), line[0], Integer.parseInt(line[2]), playground);
            //read palyerTwo
            line = in.readLine().split(",");
            for (int i = 0; i < size; i++) {
                String row = in.readLine();
                 for (int j = 0; j < size; j++) {
                    playground[i][j] = Integer.parseInt(""+row.charAt(j));
                }                
            }
            Player playerTwo = new Player(Integer.parseInt(line[1]), line[0], Integer.parseInt(line[2]), playground);
            //read game stats and playground
            line = in.readLine().split(",");
            for (int i = 0; i < size; i++) {
                String row = in.readLine();
                 for (int j = 0; j < size; j++) {
                    playground[i][j] = Integer.parseInt(""+row.charAt(j));
                }                
            }
            Player onTurn = null;
            if (Integer.parseInt(line[1]) == 1) {
                onTurn = playerOne;
            }else{
                onTurn = playerTwo;
            }
            //starting game
            MainGameFrame mgf = new MainGameFrame(size, playerOne.nick, playerTwo.nick);
            TheGame game = new TheGame(mgf, playground, playerOne, playerTwo);            
            MainController mc = new MainController(game);
            mgf.setMainController(mc);
            game.turnsCount = Integer.parseInt(line[0]);
            game.onTurn = onTurn;
            game.lastGameWasPassed = Boolean.parseBoolean(line[2]);
            game.gameEnded = Boolean.parseBoolean(line[3]);
            game.countScores();
            game.showStonesCount();
            mgf.setOnTurn(onTurn.color);
            mgf.showPlayground(playground);
            mgf.showOnTurn(onTurn.nick);
            in.close();
            
        } catch (IOException ex) {
            Logger.getLogger(GameSaver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(GameSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public void saveFile(String path) {
        try {
            FileWriter stream = new FileWriter(path);
            BufferedWriter out = new BufferedWriter(stream);
            String newline = System.getProperty("line.separator");
            //save playground size
            out.write(Integer.toString(game.desk.playground.length)+newline);
            //save playerOne
            out.write(game.playerOne.nick + "," + Integer.toString(game.playerOne.color) + "," + Integer.toString(game.playerOne.stonesTaken) + newline);
            for (int i = 0; i < game.desk.playground.length; i++) {
                for (int j = 0; j < game.desk.playground.length; j++) {
                    out.write(Integer.toString(game.playerOne.afterLastTurn[i][j]));
                }
                out.write(newline);
            }
            //save playerTwo
            out.write(game.playerTwo.nick + "," + Integer.toString(game.playerTwo.color) + "," + Integer.toString(game.playerTwo.stonesTaken) + newline);
            for (int i = 0; i < game.desk.playground.length; i++) {
                for (int j = 0; j < game.desk.playground.length; j++) {
                    out.write(Integer.toString(game.playerTwo.afterLastTurn[i][j]));
                }
                out.write(newline);
            }
            //save game stats
            out.write(Integer.toString(game.turnsCount) + "," + Integer.toString(game.onTurn.color) + "," + game.lastGameWasPassed + "," + game.gameEnded + newline);
            //save playground
            for (int i = 0; i < game.desk.playground.length; i++) {
                for (int j = 0; j < game.desk.playground.length; j++) {
                    out.write(Integer.toString(game.desk.playground[i][j]));
                }
                out.write(newline);
            }
            out.close();
        } catch (Exception e) {
            game.viewInterface.showGameState("Error: " + e.getMessage());
        }
    }
}
