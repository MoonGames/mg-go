/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.view;

import cz.mgn.go.game.controller.MainController;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author filcicyr
 */
public class MainGameFrame extends JFrame implements ViewInterface {

    protected PlaygroundPanel playgroundPanel = null;
    protected MainController mainController = null;
    protected InformationPanel informatoinsPanel = null;

    public MainGameFrame(int playgroundSize, String playerOne, String playerTwo) {
        init(playgroundSize, playerOne, playerTwo);
    }
    /**
     * creates main frame where playground and information panel is
     * @param playgroundSize size of playground
     * @param playerOne name of first player
     * @param playerTwo name of second player
     */
    protected void init(int playgroundSize, String playerOne, String playerTwo) {
        setVisible(true);
        setTitle("Go " + playerOne + " vs. " + playerTwo);
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        int playgroundSizePhysical = playgroundSize * 34;
        int verticalBorder = 30;
        int horizontalBorder = 2;
        int frameHeight = verticalBorder + 20 + playgroundSizePhysical;
        if (frameHeight < 450) {
            frameHeight = 450;
        }
        setPreferredSize(new Dimension(horizontalBorder + playgroundSizePhysical + 30 + 300, frameHeight));
        setSize(getPreferredSize());
        getContentPane().setLayout(null);

        JPanel mainPanel = new JPanel();
        Dimension size = getPreferredSize();
        size.width -= 30;
        size.height -= 20;
        mainPanel.setPreferredSize(size);
        mainPanel.setLayout(null);
        mainPanel.setBounds(10, 10, size.width, size.height);
        getContentPane().add(mainPanel);

        playgroundPanel = new PlaygroundPanel(playgroundSize);
        playgroundPanel.setPreferredSize(new Dimension(playgroundSizePhysical, playgroundSizePhysical));
        playgroundPanel.setBounds(0, 0, playgroundSizePhysical, playgroundSizePhysical);
        mainPanel.add(playgroundPanel);
        playgroundPanel.repaint();

        addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });

        informatoinsPanel = new InformationPanel(playerOne, playerTwo);
        Dimension iSize = new Dimension(mainPanel.getWidth() - 10 - playgroundPanel.getWidth(), mainPanel.getHeight());
        informatoinsPanel.setPreferredSize(iSize);
        informatoinsPanel.setBounds(playgroundPanel.getWidth() + 10, 0, iSize.width, iSize.height);
        mainPanel.add(informatoinsPanel);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        playgroundPanel.setMainController(mainController);
        informatoinsPanel.setMainController(mainController);
    }

    public void showPlayground(int[][] playground) {
        playgroundPanel.setPlayground(playground);
    }

    public void showOnTurn(String nick) {
        informatoinsPanel.showOnTurn(nick);
    }

    public void showScores(int playerOneScore, int playerTwoScore) {
        informatoinsPanel.showPlayersScores(playerOneScore, playerTwoScore);
    }

    public void showGameState(String gameState) {
        informatoinsPanel.showGameState(gameState);
    }

    public void showStonesCounts(int playerOneCount, int playerOneTaken, int playerTwoCount, int playerTwoTaken) {
        informatoinsPanel.showStonesCounts(playerOneCount, playerOneTaken, playerTwoCount, playerTwoTaken);
    }

    public void showTurn(int turn) {
        informatoinsPanel.showTurn(turn);
    }

    public void setOnTurn(int color) {
        informatoinsPanel.setOnTurn(color);
        playgroundPanel.setOnTurn(color);
    }
}
