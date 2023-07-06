/*
Menu.java
Isaac Jiang
controls and dispays menu screens based on what stage of the game the player is in. These menu screens include
Main Menu, Level Beat Menu, Lose Menu, Gameover Menu, and Name Choose Menu. It allows you to see the score, highscores,
level, and choose a name to save your score with.
*/

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    private final Image win, lose, menuimg, gamecomplete; // png overlay images
    private final Image[] menuAnim = new Image[48]; // background animation
    private double menuAnimFrame = 0; // background animation frame
    private static int[] highscores; // highscores array
    private static String[] players; // player name array
    private static Rectangle[] nameRects = new Rectangle[3]; // rectangle for name selection
    private static String[] randomNames; // random kahoot names for player to choose from
    private final Image buttonUp, buttonDown; // button images
    double frameVel; // how fast frame changes in animation
    // random verbs and nouns
    private static final String[] verbs = {"Running","Jogging","Measly","Epic","Cool","Short","Dark","Wide","Great"};
    private static final String[] nouns = {"Birds","Rabbits","Sheep","Wolves","Corals","Chumps","Torches","Tusks","Fishes"};
    // file name for animation
    String menuAnimFile = "src/intro/test/frame_";
    public Menu() {
        // loading all animation frames
        for (int i = 2; i < 50; i++) {
            menuAnim[i-2] = new ImageIcon(menuAnimFile+i+".jpg").getImage();
        }
        // loading menu overlays and buttons
        win = new ImageIcon("src/images/win.png").getImage();
        lose = new ImageIcon("src/images/gameover.png").getImage();
        menuimg = new ImageIcon("src/images/menu.png").getImage();
        gamecomplete = new ImageIcon("src/images/gamecomplete.png").getImage();
        buttonUp = new ImageIcon("src/images/buttonup.png").getImage();
        buttonDown = new ImageIcon("src/images/buttondown.png").getImage();
        // highscore array
        highscores = new int[3];
        // player names
        players = new String[3];
        // rects for picking name
        nameRects = new Rectangle[3];
        randomNames = new String[]{"", "", ""}; // random names to choose from
        frameVel = 1;
        // creating rectangles for buttons
        for (int i = 0; i < nameRects.length; i++) {
            nameRects[i] = new Rectangle(50+(i*250), 650, buttonUp.getWidth(null), buttonUp.getHeight(null));
        }
    }
    // load highscores from file
    public static void loadHighscores() {
        try {
            Scanner f = new Scanner(new BufferedReader(new FileReader("src/images/highscore.txt")));
            for (int i = 0; i < 3; i++) { // getting 3 highscores
                players[i] = f.nextLine(); // getting name
                highscores[i] = Integer.parseInt(f.nextLine()); // getting scores
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }
    // creating random names
    public static void createNames() {
        Random rand = new Random(); // create random object
        for (int i = 0; i < nameRects.length; i++) {
            // pick random verb and noun
            int randVerb = rand.nextInt(0,verbs.length);
            int randNoun = rand.nextInt(0,nouns.length);
            // put verb and noun together
            randomNames[i] = verbs[randVerb] + " " + nouns[randNoun];
        }
    }
    // drawing image inside rectangle
    public static void imageInRect(Graphics g, Image img, Rectangle area){
        g.drawImage(img, area.x, area.y, area.width, area.height, null);
    }
    // drawing menu for choosing name
    public void drawMenu(Graphics g) {
        g.setFont(GamePanel.fontLocalSmall);
        for (int i = 0; i < nameRects.length; i++) {
            // checking for mouse hovering
            if (nameRects[i].contains(new Point(GamePanel.getTarX(), GamePanel.getTarY()))) {
                imageInRect(g, buttonUp, nameRects[i]); // change displayed button
            } else {
                imageInRect(g, buttonDown, nameRects[i]); // no hover button
            }
            g.drawString(randomNames[i], 80+(i*250), 700); // drawing random name on button
        }
    }
    // get name rectangles
    public static Rectangle[] getNameRects() {
        return nameRects;
    }
    // get random generated names
    public static String[] getRandomNames() {
        return randomNames;
    }
    // call menu, calls all individual menu methods
    // displays animation
    public void callMenu(Graphics g) {
        if (GamePanel.gameover) {
            menuAnimFrame = menuAnimFrame < menuAnim.length - Math.ceil(frameVel) ? menuAnimFrame + frameVel : 0;
            g.drawImage(menuAnim[(int)menuAnimFrame],0,0,null);
            // calling individual menu methods
            mainMenu(g);
            gameBeat(g);
            levelBeat(g);
            gameOver(g);
        }
    }
    // main menu screen, shows highscores
    public void mainMenu(Graphics g) {
        // if game or level not beat and not lost, you have to be in the main menu
        if (!GamePanel.gamebeat && !GamePanel.levelbeat && !GamePanel.lost) {
            g.setFont(GamePanel.fontLocalHS);
            g.setColor(Color.WHITE);
            // draw overlay
            g.drawImage(menuimg,0,0,null);
            // display top 3 highscores
            for (int i = 0; i < 3; i++) {
                g.drawString((players[i] + ":" + highscores[i]),
                        400-((players[i].length()+String.valueOf(highscores[i]).length())*12), 60+((i+1)*40));
            }
        }
    }
    // menu for beating level
    public void levelBeat(Graphics g) {
        // if not gamebeat or lost but levelbeat, display this screen
        if (!GamePanel.gamebeat && GamePanel.levelbeat && !GamePanel.lost) {
            g.setFont(GamePanel.fontLocalBig);
            g.setColor(Color.WHITE);
            // draw overlay
            g.drawImage(win,0,0,null);
            // displays current level and current score
            g.drawString(String.valueOf(GamePanel.getLevel()+1), 703,120);
            g.drawString(String.valueOf(Blocks.getScore()), 87-(String.valueOf(Blocks.getScore()).length()*18), 120);
        }
    }
    // if gamebeat but not levelbeat and not lost, game is complete and ask user to select username
    public void gameBeat(Graphics g) {
        if (GamePanel.gamebeat && !GamePanel.levelbeat && !GamePanel.lost) {
            g.setFont(GamePanel.fontLocalBig);
            g.setColor(Color.WHITE);
            // draw overlay
            g.drawImage(gamecomplete, 0, 0, null);
            // username selection method
            drawMenu(g);
        }
    }
    // if lost, gameover. shows the level the player got to and the score they got (not counted towards high scores)
    public void gameOver(Graphics g) {
        if (!GamePanel.gamebeat && !GamePanel.levelbeat && GamePanel.lost) {
            g.setFont(GamePanel.fontLocalBig);
            g.setColor(Color.WHITE);
            // overlay
            g.drawImage(lose,0,0,null);
            // level and score
            g.drawString(String.valueOf(GamePanel.getLevel()+1), 703,120);
            g.drawString(String.valueOf(Blocks.getScore()), 33, 120);
        }
    }
}