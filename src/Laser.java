/*
Laser.java
Isaac Jiang
Creates lasers that detect for collisions with blocks to destroy them. Contains
explosion methods that display explosions when a block is destroyed by a laser.
Moves, deletes, and checks for collision of lasers.
 */
import javax.swing.*;
import java.awt.*;
public class Laser {
    static int[] l1, l2; // laser positions
    private static int LASERLENGTH, LASERSPEED; // length of laser and speed at which it travels
    private static final Image[] explodeImages = new Image[12]; // explosion animation images
    private static final int[][] explodeInfo = new int[2][3]; // explosion information (position)
    static Image laserImg; // image of laser

    // laser constructor
    public Laser() {
        l1 = new int[]{0,0};
        l2 = new int[]{0,0};
        LASERLENGTH = 40; LASERSPEED = 10; // setting length and sped
        laserImg = new ImageIcon("src/vaus/laser.png").getImage();
        for (int i = 0; i < explodeImages.length; i++) {
            // loading explode images
            explodeImages[i] = new ImageIcon("src/laser/explosion" + i + ".png").getImage(); //.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        }
    }
    // reset laser positions
    public static void resetLasers() {
        l1 = new int[]{0,0};
        l2 = new int[]{0,0};
    }
    // gets explosion info
    public static int[][] getExplodeInfo() {
        return explodeInfo;
    }
    // checks for laser colliding with block
    public static void laserCollide(int[] l, int n) {
        for (int i = 0; i < Blocks.getBlockArrLength(); i++) {
            if (new Rectangle(Blocks.getBlockX(i), Blocks.getBlockY(i), 100, 50).intersectsLine(l[0],l[1],l[0],l[1]+LASERLENGTH)) {
                // getting new laser coords
                if (n == 1) {
                    l[0] = (int) Paddle.getX(); // laser 1
                } else {
                    l[0] = (int) Paddle.getX()+Paddle.getWIDTH()-5; // laser 2
                }
                l[1] = (int) Paddle.getY(); // y coords same no matter the laser
                destroyBlock(i, n); // destroy block being hit
                // play sfx
                Sound.explosion();
                Sound.laser();
                break;
            }
        }
    }
    // helper function to set the explosion coords
    private static void setExplodeInfo(int n, int i) {
        explodeInfo[n-1][0] = Blocks.getBlockX(i);
        explodeInfo[n-1][1] = Blocks.getBlockY(i);
        explodeInfo[n-1][2] = 0;
    }
    // checks if block is destroyable and adds points by finding colour
    public static void destroyBlock(int i, int n) {
        // same logic as blocks being destroyed in block class
        for (int colourValue : Blocks.getColourValues()) {
            if (Blocks.getBlockCol(i) == colourValue && colourValue != Blocks.GOLD) {
                if (Blocks.getBlockLives(i)-1 == 0) {
                    Blocks.setCollide(true);
                    Blocks.updateBlockCount(GamePanel.HITBLOCK, i);
                    Powerups.setDropCoords(Blocks.getBlockX(i), Blocks.getBlockY(i));
                    Blocks.increaseScore(colourValue);
                    setExplodeInfo(n, i); // adding explosion info
                    Blocks.setBlockArr(i, new int[]{-100,0,-1,0});
                } else {
                    Blocks.setBlockLives(i, -1);
                }
            }
        }
    }
    // playing explosion animation
    public static void explodeBlock(int[][] info, Graphics g) {
        for (int i = 0; i < info.length; i++) { // going through both lasers
            if (info[i][0] != 0 && info[i][1] != 0) { // checking if explosion data changed that should be displayed
                g.drawImage(explodeImages[info[i][2]], info[i][0], info[i][1], null);
                // frame logic
                if (info[i][2] == 11) {
                    info[i][2] = 0; info[i][0] = 0; info[i][1] = 0; // resets explosion info so animation not repeated
                } else {
                    info[i][2]++;
                }
            }
        }
    }
    // main code for lasers - gets laser position and moves them
    public static void laserPower(Graphics g) {
        if (l1[0] == 0 && l1[1] == 0 && l2[0] == 0 && l2[1] == 0) {
            l1[0] = (int) Paddle.getX();
            l1[1] = (int) Paddle.getY();
            l2[0] = (int) Paddle.getX()+Paddle.getWIDTH()-5;
            l2[1] = (int) Paddle.getY();
        }
        // move lasers
        l1[1] -= LASERSPEED;
        l2[1] -= LASERSPEED;
        // drawing laser
        g.drawImage(laserImg, l1[0],l1[1],null);
        laserCollide(l1, 1); // checks for collision
        // checking if laser out of bounds
        if (l1[1] < 0) {
            Sound.laser();
            // resetting laser
            l1[0] = (int) Paddle.getX();
            l1[1] = (int) Paddle.getY();
        }
        g.drawImage(laserImg, l2[0],l2[1],null);
        laserCollide(l2, 2);
        // checking for laser out of bounds
        if (l2[1] < 0) {
            Sound.laser();
            l2[0] = (int) Paddle.getX()+Paddle.getWIDTH()-5;
            l2[1] = (int) Paddle.getY();
        }
    }
}
