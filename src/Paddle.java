/*
Paddle.java
Isaac Jiang
Contains methods that display, move, and track the paddle (vaus spacecraft) in arkanoid.
displays paddle lives (# of vaus remaining). Draws vaus differently based on powerups.
For example, when a laser powerup is acquired, the vaus transforms.
 */

import javax.swing.*;
import java.awt.*;

public class Paddle {
    private static double x, y;
    // base width to reset to when width modified
    private static int WIDTH, HEIGHT, BASEWIDTH;
    // vaus images (normal and laser versions)
    private final Image[] vausAnim = new Image[10];
    private final Image[] vausLaserAnim = new Image[10];
    // life count display image
    Image vausLife;
    // animation frame - double (to modify frame rate)
    private double vausAnimFrame = 0;

    // constructor loads images and sets width & height, as well as starting coords
    public Paddle() {
        String vausAnimFile = "src/vaus/test/frame_";
        for (int i = 0; i < vausAnim.length/2; i++) {
            vausAnim[i] = new ImageIcon(vausAnimFile+i+".png").getImage();
            vausAnim[i+5] = new ImageIcon(vausAnimFile+i+".png").getImage().getScaledInstance(195, 74, Image.SCALE_SMOOTH);
        }
        for (int i = 0; i < vausAnim.length/2; i++) {
            vausLaserAnim[i] = new ImageIcon("src/vaus/laser/"+i+".png").getImage();
        }
        vausLife = new ImageIcon("src/vaus/test/lifeimg.png").getImage();
        WIDTH = 130;
        HEIGHT = 49;
        BASEWIDTH = WIDTH;
        x = 300;
        y = 700;
    }
    // getter and setters for coords and dimensions (used for collision detection)
    public static double getX() {
        return x;
    }
    public static double getY() {
        return y;
    }
    public static int getWIDTH() {
        return WIDTH;
    }
    public static void setWIDTH(int n) {
        WIDTH = n;
    }
    public static int getHEIGHT() {
        return HEIGHT;
    }
    public static int getBASEWIDTH() {
        return BASEWIDTH;
    }

    // moving paddle with mouse position because playing with keyboard is too hard and pushing on buttons requires too much effort
    public void move() {
        x = GamePanel.getTarX()-WIDTH/2.0;
        x = Math.max(1, (int) x);
        x = Math.min(GamePanel.gameWIDTH-WIDTH, (int) x);
    }
    // shows lives at bottom left
    public void displayLives(Graphics g) {
        for (int i = 0; i < GamePanel.getLives()+1; i++) {
            g.drawImage(vausLife,20*i+10,760,null);
        }
    }
    // displays vaus on screen depending on whether it is enlarged, in laser mode, or just normal
    public void draw(Graphics g) {
        if (Powerups.getActivePower(Powerups.ENLARGE)) {
            g.drawImage(vausAnim[(int)vausAnimFrame+5],(int)(x),(int)(y),null);
        } else if (Powerups.getActivePower(Powerups.LASER)) {
            g.drawImage(vausLaserAnim[(int)vausAnimFrame],(int)(x),(int)(y),null);
        } else {
            g.drawImage(vausAnim[(int)vausAnimFrame],(int)(x),(int)(y),null);
        }
        // reset frame so no out of bounds
        vausAnimFrame = vausAnimFrame < vausAnim.length/2.0 - 1 ? vausAnimFrame + 0.2 : 0;
    }
}
