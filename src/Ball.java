/*
Ball.java
Isaac Jiang
Contains methods that control movement and collision and bounds for the ball found in arkanoid.
This class checks for collisions with the paddle and if the ball has gone out of bounds or hit a wall.
It also controls speed and location of the ball.
 */
import javax.swing.*;
import java.awt.*;

public class Ball {
    // position and speed variables
    private static double x, y, vx, vy;
    // base speeds for reset
    private static final double BASEX = 1, BASEY = 10;
    // track if ball is active or on paddle
    private static boolean active = false;
    // width and height of ball
    private static final int WIDTH = 12, HEIGHT = 12;
    // double to track angle that ball lands on paddle at
    private static double paddleBounce = 0;
    Image ballImage;
    public Ball() {
        ballImage  = new ImageIcon("src/images/ball.png").getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        // initializing ball position based on paddle pos
        x = Paddle.getX()+Paddle.getWIDTH()/2.0-WIDTH/2.0;
        y = Paddle.getY()-HEIGHT;
        vx = BASEX;
        vy = -BASEY;
    }
    // getter and setters for position and speed
    public static double getX() {return x;} // get x pos
    public static double getY() {return y;} // get y pos
    public static void setVx(double n) {vx = n;} // set x vel
    public static double getVx() {return vx;} // get x vel
    public static void setVy(double n) {vy = n;} // set y vel
    public static double getVy() {return vy;} // get y vel
    public static double getBASEY() {return BASEY;} // get base y vel
    public static double getPaddleBounce() {return paddleBounce;} // get bounce position relative to paddle
    public static int getWIDTH() {return WIDTH;} // get width of ball
    public static int getHEIGHT() {return HEIGHT;} // get ball height
    public static boolean getActive() {return active;} // get whether ball is active or not
    public static void setActive(boolean a) {active = a;} // set ball active to true or false
    // moving ball and checking for death or wall collision
    public void move() {
        // add to pos
        x += vx;
        y += vy;
        loselife();
        // check if hit walls
        if(y <= 0) { // hit ceiling
            Sound.wallBounce();
            vy *= -1;
        } else if (x <= 0 || x > GamePanel.gameWIDTH-WIDTH){
            if (x <= 0) {
                x = 1; // put 1 pixel to right to avoid stuck in wall
            } else {
                x = GamePanel.gameWIDTH-WIDTH-1; // put 1 pixel to the left to avoid being stuck in wall
            }
            vx *= -1;
            Sound.wallBounce();
        }
        bounceVaus(); // checking for paddle collision
    }
    // keeps ball on the paddle
    public void onPaddle() {
        if (Powerups.getActivePower(2)) {
            x = Paddle.getX() + paddleBounce;
            y = Paddle.getY() - WIDTH;
        } else {
            x = Paddle.getX() + Paddle.getWIDTH()/2.0 - WIDTH/2.0;
            y = Paddle.getY() - WIDTH;
        }
    }
    // checking if ball falls out of bounds and decreases lives
    public void loselife() {
        if (y > GamePanel.gameHEIGHT) {
            GamePanel.setLives(GamePanel.getLives()-1);
            // if player has lives to expend, don't die yet
            if (GamePanel.getLives() >= 0) {
                Sound.loselife();
                // reset position
                x = Paddle.getX()+Paddle.getWIDTH()/2.0-WIDTH/2.0;
                y = Paddle.getY()-HEIGHT;
                // reset velocity
                vx = BASEX;
                vy = BASEY;
                Powerups.destroyPowers(); // get rid of powerups
                active = false; // set ball back to paddle
            } else if (GamePanel.getLives() == -1) {
                Sound.die();
                GamePanel.setLives(-2);
            }
            if (GamePanel.getLives() < 0) { // no more lives
                GamePanel.gameover = true;
                GamePanel.lost = true;
            }
        }
    }
    // check if paddle collide with ball, find direction of bounce using cos
    public void bounceVaus() {
        paddleBounce = 0;
        if (new Rectangle((int)x, (int)y, WIDTH, HEIGHT).intersectsLine((int)Paddle.getX(),(int)Paddle.getY(),(int)Paddle.getX()+Paddle.getWIDTH(),Paddle.getY())) {
            Sound.hit();
            paddleBounce = x-Paddle.getX(); // saving bounce x coord for catch powerup to retain bounce position
            vy = -1 * Math.abs(vy);
            int dir = (int) ((x+WIDTH/2 - Paddle.getX()) * (180.0/Paddle.getWIDTH()));
            vx = -1 * Math.cos(Math.toRadians(dir))*10;
        }
    }
    // draws ball onto screen
    public void draw(Graphics g) {
        g.drawImage(ballImage, (int) x, (int) y, null);
    }
}
