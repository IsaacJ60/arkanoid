/*
Blocks.java
Isaac Jiang
The blocks class loads, tracks, and checks for collisions regarding the blocks in arkanoid.
It loads blocks, checks for collision between the blocks and the ball, and checks the block
count whereby it makes decisions to end the game.
 */
import javax.swing.*;
import java.awt.*;

public class Blocks {
    // main array to store all blocks with info (x coord, y coord, colour, health (for silver blocks))
    private static final int[][] blockArr = new int[49][4];
    // block dimension and side constants
    private static final int WIDTH = 100, HEIGHT = 50, TOP = 0, BOTTOM = 1, LEFT = 2, RIGHT = 3;
    // colour numbers, public
    public static final int WHITE = 0, ORANGE = 1, AQUA = 2, GREEN = 3, RED = 4, BLUE = 5, PURPLE = 6, YELLOW = 7, GOLD = 8, SILVER = 9;
    // level layouts, starts at top right, displays column by column
    private static final int[][] levelBlocks = {{GREEN,6,5,6,5,6,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,6,5,6,5,6,GREEN,9,9,9,9,9,9,
            9,6,5,6,5,6,GREEN,GREEN,GREEN,6,5,6,5,6,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN},
            {GOLD,GOLD,GOLD,GOLD,GOLD,GOLD,-1,0,1,2,3,4,SILVER,-1,6,7,0,1,2,4,-1,4,5,6,7,0,SILVER,-1,2,3,4,5,6,0,-1,0,1,2,3,4,SILVER,-1,GOLD,GOLD,GOLD,GOLD,GOLD,GOLD,-1}};
    // number of blocks per level (not including gold blocks)
    private static final int[] levelBlockNums = {49,30};
    // score and the number of blocks not yet destroyed
    private static int score = 0, blocksRemaining = levelBlockNums[0];
    // collision variable, activates when block destroyed to trigger possible powerup
    private static boolean collide = false;
    // variable for different types of block layouts didn't end up using this
    private static final int FULL = 0;
    // array to store all colour values
    private static final int[] colourValues = {WHITE, ORANGE, AQUA, GREEN, RED, BLUE, PURPLE, YELLOW, GOLD, SILVER};
    // block images
    private static final Image[] cols = new Image[colourValues.length];
    // file names
    private static final String[] colourFiles = {"src/images/Arkanoid_Brick_White.png","src/images/Arkanoid_Brick_Orange.png","src/images/Arkanoid_Brick_Cyan.png",
            "src/images/Arkanoid_Brick_Green.png","src/images/Arkanoid_Brick_Red.png","src/images/Arkanoid_Brick_Blue.png","src/images/Arkanoid_Brick_Violet.png",
            "src/images/Arkanoid_Brick_Yellow.png","src/images/Arkanoid_Brick_Gold.png", "src/images/Arkanoid_Brick_Silver.png"};
    public Blocks() {
        // loading block images
        for (int i = 0; i < colourValues.length; i++) {
            cols[i] = new ImageIcon(colourFiles[i]).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        }
        // loading blocks with position other info
        loadBlocks(FULL, levelBlocks[GamePanel.getLevel()]);
    }
    public void loadBlocks(int type, int[] arr) {
        if (type == 0) {
            int c1 = 0;
            // getting all position data
            for (int i = 50; i < 750; i += 100) {
                for (int j = 50; j < 400; j += 50) {
                    // if block is empty, give it default empty pos data
                    if (arr[c1] == -1) {
                        blockArr[c1][0] = -101;
                        blockArr[c1][1] = 0;
                    } else { // storing block data
                        blockArr[c1][0] = i;
                        blockArr[c1][1] = j;
                        blockArr[c1][2] = arr[c1];
                        if (blockArr[c1][2] == SILVER) { // calculating silver block health based on level
                            blockArr[c1][3] = GamePanel.getLevel()+2;
                        } else {
                            blockArr[c1][3] = 1; // 1 health for normal blocks
                        }
                    }
                    c1++;
                }
            }
        }
    }
    // get block array length
    public static int getBlockArrLength() {
        return blockArr.length;
    }
    // change block array
    public static void setBlockArr(int i, int[] arr) {
        blockArr[i] = arr;
    }
    // get x coord of block
    public static int getBlockX(int i) {return blockArr[i][0];}
    // get y coord of block
    public static int getBlockY(int i) {
        return blockArr[i][1];
    }
    // get colour of block
    public static int getBlockCol(int i) {
        return blockArr[i][2];
    }
    // get lives of block
    public static int getBlockLives(int i) {
        return blockArr[i][3];
    }
    // set lives of block
    public static void setBlockLives(int i, int n) {
        blockArr[i][3]+=n;
    }
    // get width of block
    public static int getWIDTH() {
        return WIDTH;
    }
    // get collision status
    public static boolean getCollide() {
        return collide;
    }
    // set collision status
    public static void setCollide(boolean b) {
        collide = b;
    }
    // get colour values array
    public static int[] getColourValues() {
        return colourValues;
    }
    // increase score based on colour
    public static void increaseScore(int col) {
        score += (col + 4) * 10;
    }
    // set score
    public static void setScore(int n) {
        score = n;
    }
    // get score
    public static int getScore() {
        return score;
    }
    // get the blocks left
    public static int getBlocksLeft() {
        return blocksRemaining;
    }
    // set blocks left
    public static void setBlocksLeft(int n) {
        blocksRemaining = n;
    }
    // update block count
    public static void updateBlockCount(int n, int i) {
        blocksRemaining += n;
        decreaseBlockLives(i);
    }
    // decrease block lives
    public static void decreaseBlockLives(int i) {
        Blocks.blockArr[i][3]--;
    }
    // get block numbers
    public static int getBlockNums(int i) {
        return levelBlockNums[i];
    }
    // check for collision between block and ball
    public void collision() {
        collide = false;
        int[] collisionSide = new int[]{0, 0, 0, 0};
        // go through all blocks
        for (int i = 0; i < blockArr.length; i++) {
            // checking if collision with block happens
            if ((new Rectangle((int) Ball.getX(), (int) Ball.getY(), Ball.getWIDTH(), Ball.getHEIGHT()).intersects(new Rectangle(blockArr[i][0], blockArr[i][1], WIDTH, HEIGHT)))) {
                Sound.hitblock();
                // finding which sides collide with lines
                if (new Rectangle((int) Ball.getX(), (int) Ball.getY(), Ball.getWIDTH(), Ball.getHEIGHT()).intersectsLine(blockArr[i][0], blockArr[i][1], blockArr[i][0]+WIDTH, blockArr[i][1])) {
                    collisionSide[TOP] = 1;
                } else if (new Rectangle((int) Ball.getX(), (int) Ball.getY(), Ball.getWIDTH(), Ball.getHEIGHT()).intersectsLine(blockArr[i][0], blockArr[i][1]+HEIGHT, blockArr[i][0]+WIDTH, blockArr[i][1]+HEIGHT)) {
                    collisionSide[BOTTOM] = 1;
                }
                if (new Rectangle((int) Ball.getX(), (int) Ball.getY(), Ball.getWIDTH(), Ball.getHEIGHT()).intersectsLine(blockArr[i][0], blockArr[i][1], blockArr[i][0], blockArr[i][1]+HEIGHT)) {
                    collisionSide[LEFT] = 1;
                } else if (new Rectangle((int) Ball.getX(), (int) Ball.getY(), Ball.getWIDTH(), Ball.getHEIGHT()).intersectsLine(blockArr[i][0]+WIDTH, blockArr[i][1], blockArr[i][0]+WIDTH, blockArr[i][1]+HEIGHT)) {
                    collisionSide[RIGHT] = 1;
                }
                // doing corner collision, a ball shouldn't bounce back in the same direction if it hits the top right of a block that has
                // blocks above it. similarly, a ball shouldn't bounce back when it hits the bottom right of a block when there is a block
                // beneath it. it should continue with the same y velocity but change its x dir.
                if (collisionSide[TOP] == 1 && collisionSide[RIGHT] == 1) {
                    if (blockArr[Math.abs(i-1)][2] == -1 || i%7 == 0) { // changes if there is no block or if top block
                        Ball.setVy(Math.abs(Ball.getVy()) * (-1));
                    }
                    Ball.setVx(Math.abs(Ball.getVx()));
                } else if (collisionSide[BOTTOM] == 1 && collisionSide[RIGHT] == 1) {
                    if (blockArr[Math.abs(i+1)%49][2] == -1 || i%6 == 0) { // change if last block
                        Ball.setVy(Math.abs(Ball.getVy()));
                    }
                    Ball.setVx(Math.abs(Ball.getVx()));
                } else if (collisionSide[TOP] == 1 && collisionSide[LEFT] == 1) {
                    if (blockArr[Math.abs(i-1)][2] == -1 || i%7 == 0) {
                        Ball.setVy(Math.abs(Ball.getVy()) * (-1));
                    }
                    Ball.setVx(Math.abs(Ball.getVx()) * (-1));
                } else if (collisionSide[BOTTOM] == 1 && collisionSide[LEFT] == 1) {
                    if (blockArr[Math.abs(i+1)%49][2] == -1 || i%6 == 0) {
                        Ball.setVy(Math.abs(Ball.getVy()));
                    }
                    Ball.setVx(Math.abs(Ball.getVx()) * (-1));
                } else {
                    // handling basic top left bottom right collision not involving corners
                    if (collisionSide[TOP] == 1 || collisionSide[BOTTOM] == 1) {
                        Ball.setVy(-Ball.getVy());
                    } else if (collisionSide[LEFT] == 1 || collisionSide[RIGHT] == 1) {
                        Ball.setVx(-Ball.getVx());
                    }
                }
                // check what colour the block is and if it is able to be destroyed or not
                for (int colourValue : colourValues) {
                    if (blockArr[i][2] == colourValue && colourValue != GOLD) {
                        if (blockArr[i][3]-1 == 0) {
                            blockArr[i][3]--;
                            collide = true;
                            blocksRemaining--;
                            Powerups.setDropCoords(blockArr[i][0],blockArr[i][1]); // set the coords in case powerup gets dropped
                            increaseScore(colourValue); // increase score
                            blockArr[i] = new int[]{-100,0,-1,0};
                        } else {
                            blockArr[i][3]--; // decrease health block
                        }
                    }
                }
                break;
            }
        }
    }
    // drawing blocks
    public void draw(Graphics g) {
        // going through all blocks in arr, checking if they are still active or not
        for (int[] ints : blockArr) {
            if (!(ints[0] == -100 && ints[1] == 0)) {
                for (int i = 0; i < colourValues.length; i++) { // find colour to display as
                    if (ints[2] == colourValues[i]) {
                        g.drawImage(cols[i], ints[0], ints[1], null);
                    }
                }
            }
        }
    }
    // check if blocks remaining is 0
    public void blockCount() {
        if (blocksRemaining == 0) {
            Sound.gameMusic("STOP");
            Sound.winMusic("START");
            if (GamePanel.getLevel()+1 == levelBlockNums.length) { // if final level complete
                // let player choose name
                Ball.setActive(false);
                GamePanel.gameover = true;
                GamePanel.gamebeat = true;
                Menu.createNames();
            } else { // move on to next level
                Ball.setActive(false);
                GamePanel.gameover = true;
                GamePanel.levelbeat = true;
            }
        }
    }
}
