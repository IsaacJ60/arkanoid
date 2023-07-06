import javax.swing.*;
/*
Arkanoid.java
Simple Game Assignment ICS4U-01
Isaac Jiang
Executable java file that runs the Arkanoid game.

In arkanoid, the player controls a spacecraft that sends a ball into a bunch of blocks, that get destroyed if hit by the ball.
There are different coloured blocks that give different amounts of points and may or not be destructible. By destroying all the blocks,
the player moves on to the next level. During a level, players can receive powerups by destroying blocks. Powerups are random and
do not appear in a particular order.
 */

public class Arkanoid extends JFrame { // frame
    GamePanel game;
    public Arkanoid() {
        super("Arkanoid");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // set X to exit
        game = new GamePanel();
        add(game);
        pack();
        setVisible(true); // make panel visible
    }

    public static void main(String[] args) {
        new Arkanoid();
    }
}

