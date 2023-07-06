/*
Contains powerup information and powerup array to determine what powerups are active.
Contains individual powerup code for each powerup, except for laser, which is in a seperate class.
Checks for collision with paddle and going out of bounds.
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class Powerups {
    static int SPEED; // speed at which powerups travel down
    private final int WIDTH, LONGPADDLE; // width of powerup, width of paddle when enlarged
    private int currFrameAdd = 0; // int to slow down frame movement, frame of animation only changes after this int gets to 2
    // arraylists for powerups (store the powerup dropped, its coords, and its animation frame)
    private static ArrayList<Integer> currDrops = new ArrayList<>(), currCoordX = new ArrayList<>(), currCoordY = new ArrayList<>();
    private static final ArrayList<Integer> currFrame = new ArrayList<>();
    // coords that powerups are initially dropped at are stored here whenever block destroyed
    private static int[] dropCoords = new int[2];
    // array to track what power(s) are active
    private static int[] activePowers = {0,0,0,0,0,0,0};
    // no magic numbers for powerups! correspond to index in activePowers array
    public static final int ENLARGE = 1, LASER = 0 , CATCH = 2, SLOW = 3, BREAK = 4, DISRUPTION = 5, PLAYER = 6;
    // animation images for all powerups
    private static final Image[][] powerImages = new Image[activePowers.length][8];
    // seperate laser class
    Laser laser;
    // common file name for each powerup
    private static final String[] powerFiles = {"src/laser/laser","src/enlarge/enlarge",
            "src/catch/catch","src/slow/slow","src/break/break",
            "src/disruption/disruption","src/gainlife/player"};
    public Powerups() {
        // setting speed, width, longpaddle length, and loading images
        laser = new Laser();
        SPEED = 5;
        WIDTH = 80;
        LONGPADDLE = (int) (Paddle.getWIDTH()*1.5);
        for (int i = 0; i < activePowers.length; i++) {
            for (int j = 0; j < 8; j++) {
                powerImages[i][j] = new ImageIcon(powerFiles[i] + j + ".png").getImage();
            }
        }
    }
    // helper functions to reset powerup drops, coords
    public static void resetDrops() {
        currDrops = new ArrayList<>();
    }
    public static void resetCoordX() {
        currCoordX = new ArrayList<>();
    }
    public static void resetCoordY() {
        currCoordY = new ArrayList<>();
    }
    // function to set powerup initial drop coords
    public static void setDropCoords(int n1, int n2) {
        dropCoords = new int[]{n1,n2};
    }
    // checking for active power
    public static boolean getActivePower(int n) {
        return activePowers[n] == 1;
    }
    // random drop chance
    public void checkDrop() {
        int rand = Util.randint(0, 0);
        boolean randomDrop = Blocks.getCollide() && rand == 0;
        // if random drop is true, chose power to drop and add to arraylists
        if (randomDrop) {
            int powerChoice = Util.randint(0, 6);
            currDrops.add(powerChoice);
            currFrame.add(0);
            currCoordX.add(dropCoords[0]+((Blocks.getWIDTH()-WIDTH)/2));
            currCoordY.add(dropCoords[1]);
        }
    }
    // dropping the powers and checking for collisions
    public void powerDrop(Graphics g) {
        // go through backwards to avoid index issues when removing from arraylist
        for (int i = currDrops.size()-1; i > 0; i--) {
            // controlling frame rate at which animation plays
            if (currFrame.get(i) < 7) {
                if (currFrameAdd < 2) {
                    currFrameAdd++;
                } else {
                    currFrameAdd = 0;
                    currFrame.set(i, (currFrame.get(i)+1));
                }
            } else {
                currFrame.set(i, 0);
            }
            // drawing and setting new coords of powerup
            g.drawImage(powerImages[currDrops.get(i)][currFrame.get(i)],currCoordX.get(i),currCoordY.get(i),null);
            currCoordY.set(i, currCoordY.get(i)+SPEED);
            // check collision
            powerEliminated(i);
        }
    }
    public void powerEliminated(int n) {
        // checks for out of bounds or a hit from paddle
        if (dropCoords[1] > GamePanel.gameHEIGHT || hitPaddle(currCoordX.get(n), currCoordY.get(n))) {
            if (hitPaddle(currCoordX.get(n), currCoordY.get(n))) {
                // play sound when powerup activated
                if (currDrops.get(n) == LASER) {
                    Sound.transform();
                } else {
                    Sound.powerup();
                }
            }
            destroyPowers(); // get rid of previous powers
            activePowers[currDrops.get(n)] = 1; // turn on powerup
            // get rid of this powerups' info
            currDrops.remove(n);
            currCoordX.remove(n);
            currCoordY.remove(n);
        }
    }
    // check for paddle collision with powerup
    public boolean hitPaddle(int x, int y) {
        return (new Rectangle(x, y, 100, 50).intersects(new Rectangle((int) (Paddle.getX()),(int)(Paddle.getY()), Paddle.getWIDTH(), Paddle.getHEIGHT())));
    }
    // enlarging paddle to long paddle size
    public void enlargePower() {
        Paddle.setWIDTH(LONGPADDLE);
    }
    // sets ball activity to false after bouncing on paddle
    public void catchPower() {
        if (Ball.getPaddleBounce() != 0) {
            Ball.setActive(false);
        }
    }
    // multiplies ball vy by 0.7 to slow ball down
    public void slowPower() {
        Ball.setVy(Ball.getVy()*0.7);
        activePowers[SLOW] = 0;
    }
    // breaks a life, but doesn't kill
    public void breakPower() {
        if (GamePanel.getLives()>0) {
            GamePanel.setLives(GamePanel.getLives()-1);
        }
        activePowers[BREAK] = 0;
    }
    // destroys current powerup
    public void disruptionPower() {
        ;
    }
    // gain additional life
    public void playerPower() {
        GamePanel.setLives(GamePanel.getLives()+1);
        activePowers[PLAYER] = 0;
    }
    // calling powerups based on status from activePowers array
    public void callPowers(Graphics g) {
        if (activePowers[LASER] == 1) {
            // calling from laser class
            Laser.laserPower(g);
            Laser.explodeBlock(Laser.getExplodeInfo(), g);
        }
        if (activePowers[ENLARGE] == 1) {
            enlargePower();
        }
        if (activePowers[CATCH] == 1) {
            catchPower();
        }
        if (activePowers[SLOW] == 1) {
            slowPower();
        }
        if (activePowers[BREAK] == 1) {
            breakPower();
        }
        if (activePowers[DISRUPTION] == 1) {
            disruptionPower();
        }
        if (activePowers[PLAYER] == 1) {
            playerPower();
        }
    }
    // reset powerups fully, also making sure that lasers are reset, width is reset, and speed is fully reset
    public static void destroyPowers() {
        activePowers = new int[]{0,0,0,0,0,0,0};
        Paddle.setWIDTH(Paddle.getBASEWIDTH());
        Laser.resetLasers();
        if (Ball.getVy() < 0) {
            Ball.setVy(-Ball.getBASEY());
        } else {
            Ball.setVy(Ball.getBASEY());
        }
    }
    // inapplicable function when only one powerup active at a time
//    private void destroyPowers(int n) {
//        activePowers[n] = 0;
//    }
}