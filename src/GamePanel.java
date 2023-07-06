/*
GamePanel.java
Isaac Jiang
Contains game logic and user interaction logic - such as mouse events. Contains paint class which draws
menu and main game. Mouse events determine where the paddle is, and what screen the user is on.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, MouseListener {
    private static final Image[] background = new Image[30]; // array of images to store game background animation
    private int backgroundFrame = 0; // frame tracker to keep track of and switch frames for background
    private String playerName = "Player"; // sets default playername to "Player"
    public static Font fontLocal, fontLocalSmall, fontLocalBig, fontLocalHS, fontLocalStory; // create font variables
    public static int HITBLOCK = -1; // number to check help with hitting blocks
    // booleans to track game state, string was def the way to go but luckily the game complexity wasn't through the roof
    public static boolean gameover = true, gamebeat = false, levelbeat = false, lost = false, storyTime = false, storyTimeComplete = false;
    // initializing classes
    Timer timer;
    Ball ball;
    Paddle paddle;
    Blocks blocks;
    Powerups powerups;
    Menu menu;
    Sound sound;
    // set starting values for mouse pos, level, and lives
    private static int tarX, tarY, level, livesCount, lives;
    // width and height of game
    public static int gameWIDTH = 800, gameHEIGHT = 800;

    public GamePanel() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension(gameWIDTH, gameHEIGHT));
        // creating instances of class
        menu = new Menu();
        ball = new Ball();
        paddle = new Paddle();
        blocks = new Blocks();
        powerups = new Powerups();
        sound = new Sound();
        tarX = 0; tarY = 0; level = 0; livesCount = 2; lives = livesCount;
        // loading & setting cursor
        Image cursorImage = new ImageIcon("src/images/sponge1.png").getImage();
        Cursor c = tk.createCustomCursor(cursorImage, new Point( 15, 3 ), "My Cursor");
        setCursor(c);
        // "requesting" focus
        setFocusable(true);
        requestFocus();
        // adding mouse listener for mouse events
        addMouseListener(this);
        // loading background images
        for (int i = 0; i < background.length; i ++) {
            background[i] = new ImageIcon("src/background/"+i+".png").getImage();
        }
        // loading fonts
        Util.loadFonts();
        // loading current high scores
        Menu.loadHighscores();
        // starting menu music
        Sound.menuMusic("START");
        // create and start timer
        timer = new Timer(20, this); // manages frames
        timer.start();
    }
    // getter and setter for mouse pos, lives, and level
    public static int getTarX() {return tarX;}
    public static int getTarY() {return tarY;}
    public static int getLives() {return lives;}
    public static void setLives(int n) {lives = n;}
    public static int getLevel() {return level;}

    // control ball & paddle movement based on active boolean
    public void move(){
        if (Ball.getActive()) {
            ball.move();
        } else {
            ball.onPaddle();
        }
        paddle.move();
    }

    // MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (storyTime) { // if story being displayed, skip a line
            Story.skipLine();
        }
        // if click when story complete and ball not active, activate ball and reset story for next level
        if (!Ball.getActive() && storyTimeComplete) {
            storyTimeComplete = false;
            storyTime = false;
            Story.resetStory();
            Sound.nextScreen();
            Ball.setActive(true);
        }
        // make ball active if died (ball on paddle but not in story) or when caught
        if ((!Ball.getActive() && !storyTime) || Powerups.getActivePower(Powerups.CATCH)) {
            Ball.setActive(true);
        }
        // if in a menu screen
        if (gameover) {
            if (!gamebeat && !levelbeat && !lost) { // clicked in main menu, gameover no more, back into the game
                Sound.typingMusic("START");
                gameover = false;
                storyTime = true;
                Blocks.setScore(0); // reset score, either won or lost if back at main menu
                Sound.menuMusic("STOP");
                Sound.gameMusic("START");
                Sound.ballActive();
            } else if (!gamebeat && levelbeat && !lost) { // click after beating level, move onto next storytime and next level
                Sound.typingMusic("START");
                gameover = false;
                levelbeat = false;
                storyTime = true;
                level++;
                Sound.winMusic("STOP");
                Sound.gameMusic("START");
                Sound.ballActive();
            } else if (!gamebeat && !levelbeat && lost) { // click after losing, reset to main menu instead of going back into game
                lost = false;
                level = 0;
                Blocks.setScore(0);
                lives = livesCount;
                Sound.menuMusic("START");
            } else if (gamebeat && !levelbeat && !lost) { // click after beating game, go back to main menu
                gamebeat = false;
                level = 0;
                lives = livesCount;
                // checking for new high score
                Util.highScore(playerName, Blocks.getScore());
                Menu.loadHighscores();
                playerName = "Player";
                Sound.winMusic("STOP");
                Sound.menuMusic("START");
            }
            // resets always carried out after level beaten, lost, or game won (HIGHSCORE ONLY UPDATED IF GAME BEAT)
            Blocks.setBlocksLeft(Blocks.getBlockNums(level));
            Ball.setActive(false);
            // reset all powerups
            Powerups.resetDrops();
            Powerups.resetCoordX();
            Powerups.resetCoordY();
            Powerups.setDropCoords(0,0);
            // reset everything else
            ball = new Ball();
            paddle = new Paddle();
            blocks = new Blocks();
            powerups = new Powerups();
            Powerups.destroyPowers();
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        ;
    }
    @Override
    public void mouseExited(MouseEvent e) {
        ;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // letting player choose random name after beating game
        for (int i = 0; i < Menu.getNameRects().length; i++) {
            if (Menu.getNameRects()[i].contains(new Point(GamePanel.tarX, GamePanel.tarY))) {
                // default is "Player", but changes when you hover
                playerName = Menu.getRandomNames()[i]; // if click on name, set as playername
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        ;
    }
    // ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        Point mouse = MouseInfo.getPointerInfo().getLocation(); // loc of mouse on screen
        Point offset = getLocationOnScreen(); // loc of panel
        // getting mouse pos
        tarX = mouse.x - offset.x;
        tarY = mouse.y - offset.y;
        // move ball & paddle
        move();
        repaint();
    }
    @Override
    public void paint(Graphics g) {
        // !gameover, in game
        if (!gameover) {
            // updating background animation
            g.drawImage(background[backgroundFrame],0,0,null);
            if (backgroundFrame == background.length-1) {
                backgroundFrame = 0;
            } else {
                backgroundFrame++;
            }
            // drawing all visual components
            blocks.draw(g);
            blocks.collision();
            blocks.blockCount();
            powerups.checkDrop();
            powerups.powerDrop(g);
            powerups.callPowers(g);
            paddle.draw(g);
            paddle.displayLives(g);
            ball.draw(g);
            // displaying block and score info
            g.setColor(Color.WHITE);
            g.setFont(fontLocal);
            g.drawString("Score:" + Blocks.getScore(), 10, 38);
            g.drawString("Blocks Remaining:" + Blocks.getBlocksLeft(), 370, 785);
            // reads story if start of level storytime true
            if (storyTime) {
                Story.readStory(g);
            }
        } else {
            // in menu
            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,800);
            // calls for menu screens, logic determined in menu class
            menu.callMenu(g);
        }
    }
}