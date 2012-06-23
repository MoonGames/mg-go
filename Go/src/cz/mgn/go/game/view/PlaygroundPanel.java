/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.go.game.view;

import cz.mgn.go.game.controller.MainController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author filcicyr
 */
public class PlaygroundPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    protected int onTurn = 1;
    protected MainController mainController = null;
    public static BufferedImage blackStone = null;
    public static BufferedImage whiteStone = null;
    protected static BufferedImage blackStoneHighlight = null;
    protected static BufferedImage whiteStoneHighlight = null;
    protected int[][] playground = null;
    protected int highlightX = -1;
    protected int highlightY = -1;
    protected int size = 9;
    protected int spaceX = 2;
    protected int spaceY = 2;
    /**
     * Constructor
     * @param size size of "physical" playground
     */
    public PlaygroundPanel(int size) {
        loadImages();
        this.size = size;
        playground = new int[size][size];
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    /**
     * sets dimensions of playground
     * @param x x-coordinate of northwest upper corner
     * @param y y-coordinate of northwest upper corner
     * @param w width of playground
     * @param h height of playground
     */
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        spaceX = w / size;
        spaceY = h / size;
    }
    /**
     * loads images of stones and highlights
     */
    protected static void loadImages() {
        if (whiteStone == null) {
            try {
                blackStone = ImageIO.read(PlaygroundPanel.class.getResourceAsStream("/resources/images/black_stone.png"));
                whiteStone = ImageIO.read(PlaygroundPanel.class.getResourceAsStream("/resources/images/white_stone.png"));
                blackStoneHighlight = ImageIO.read(PlaygroundPanel.class.getResourceAsStream("/resources/images/black_stone_highlight.png"));
                whiteStoneHighlight = ImageIO.read(PlaygroundPanel.class.getResourceAsStream("/resources/images/white_stone_highlight.png"));
            } catch (IOException ex) {
                Logger.getLogger(PlaygroundPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    /**
     * setter of main controller
     * @param mainController object of MainController
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    /**
     * count x-coordinate where star will be placed according to inputed x-coordinate of line intersection
     * @param x x-coordinate by intersection of playground lines 
     * @return physical x-coordinate
     */
    protected int countX(int x) {
        return (int) (((float) x + 0.5) * (float) spaceX);
    }
    /**
     * count y-coordinate where star will be placed according to inputed y-coordinate of line intersection
     * @param y y-coordinate by intersection of playground lines 
     * @return physical y-coordinate
     */
    protected int countY(int y) {
        return (int) (((float) y + 0.5) * (float) spaceY);
    }
    /**
     * paints playground, looks after highlighting and painting stones
     * @param g2 object of Graphic
     */
    @Override
    protected void paintComponent(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setColor(new Color(205, 162, 78));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        int startX = spaceY / 2;
        for (int x = 0; x < size; x++) {
            int xx = countX(x);
            g.drawLine(xx, startX, xx, getHeight() - startX);
        }

        int startY = spaceX / 2;
        for (int y = 0; y < size; y++) {
            int yy = countY(y);
            g.drawLine(startY, yy, getWidth() - startY, yy);
        }

        if(size >= 13) {
            g.fillOval(countX(3) - 3, countY(3) - 3, 6, 6);
            g.fillOval(countX(3) - 3, countY(size - 4) - 3, 6, 6);
            g.fillOval(countX(size - 4) - 3, countY(3) - 3, 6, 6);
            g.fillOval(countX(size - 4) - 3, countY(size - 4) - 3, 6, 6);
        }
        if((size % 2) == 1) {
            g.fillOval(countX(size / 2) - 3, countY(size / 2) - 3, 6, 6);
        }

        if (highlightX != -1) {
            g.setColor(new Color(0, 0, 0));
            BufferedImage hi = blackStoneHighlight;
            if (onTurn == 1) {
                hi = whiteStoneHighlight;
            }
            int xx = startX + highlightX * spaceX - (hi.getWidth() / 2);
            int yy = startY + highlightY * spaceY - (hi.getHeight() / 2);
            g.drawImage(hi, xx, yy, null);
        }

        for (int x = 0; x < playground.length; x++) {
            for (int y = 0; y < playground[0].length; y++) {
                int sI = playground[x][y];
                if (sI != 0) {
                    BufferedImage stone = null;
                    if (sI == 1) {
                        stone = whiteStone;
                    } else {
                        stone = blackStone;
                    }
                    int xx = startX + x * spaceX - (stone.getWidth() / 2);
                    int yy = startY + y * spaceY - (stone.getHeight() / 2);
                    g.drawImage(stone, xx, yy, null);
                }
            }
        }
    }
    /**
     * repainting the playground
     * @param playground playground matrix
     */
    public void setPlayground(int[][] playground) {
        this.playground = playground;
        repaint();
    }
    /**
     * sets player who is currently playing
     * @param color integer symbolising player colour or empty position
     */
    public void setOnTurn(int color) {
        onTurn = color;
    }

    /**
     * counts coordinates on playground according to coordinates in pixels
     * @param x x-coordinate on playground in pixels
     * @param y y-coordinate on playground in pixels
     */
    public Point countMouseOver(int x, int y) {
        return new Point(x / spaceX, y / spaceY);
    }
    /**
     * gets position on witch mouse clicked and send it to main controller
     * @param e mouse event
     */
    public void mouseClicked(MouseEvent e) {
        if (mainController != null) {
            Point on = countMouseOver(e.getX(), e.getY());
            mainController.clickOnPosition(on.x, on.y);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }
    /**
     * if mouse is not in playground do not paint highlight
     * @param e mouse event
     */
    public void mouseExited(MouseEvent e) {
        highlightX = -1;
        highlightY = -1;
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
    }
    /**
     * paint highlight on current mouse position
     * @param e mouse event
     */
    public void mouseMoved(MouseEvent e) {
        Point on = countMouseOver(e.getX(), e.getY());
        highlightX = on.x;
        highlightY = on.y;
        repaint();
    }
}
