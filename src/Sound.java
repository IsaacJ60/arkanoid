/*
Sound.java
Isaac Jiang
Sound loads and contains methods to play music and sound effects
 */

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

public class Sound extends JFrame implements ActionListener {
    SoundEffect sound;
    // all soundeffects and music
    private static SoundEffect playmusic, nextscreen, menumusic, ballactive, winmusic,
            skipmusic, loselife, die, hit, typing, powerup, transform, wallbounce, hitblock, laser, explode;
    public Sound() {
        // loading all music and sounds
        playmusic = new SoundEffect("src/sound/track1.wav");
        menumusic = new SoundEffect("src/sound/menumusic.wav");
        nextscreen = new SoundEffect("src/sound/nextscreen1.wav");
        ballactive = new SoundEffect("src/sound/ballactive.wav");
        winmusic = new SoundEffect("src/sound/winmusic.wav");
        skipmusic = new SoundEffect("src/sound/skip.wav");
        loselife = new SoundEffect("src/sound/loselife.wav");
        hit = new SoundEffect("src/sound/bounce.wav");
        die = new SoundEffect("src/sound/die.wav");
        typing = new SoundEffect("src/sound/typing.wav");
        powerup = new SoundEffect("src/sound/powerup.wav");
        transform = new SoundEffect("src/sound/transform.wav");
        wallbounce = new SoundEffect("src/sound/wallbounce.wav");
        hitblock = new SoundEffect("src/sound/hit.wav");
        laser = new SoundEffect("src/sound/laser.wav");
        explode = new SoundEffect("src/sound/explode.wav");
    }
    public void actionPerformed(ActionEvent ae){
        sound.play();
    }
    public static void main(String args[]){
        new Sound().setVisible(true);
    }
    // looping or stopping game music
    public static void gameMusic(String s) {
        if (s.equals("STOP")) {
            playmusic.stop();
        } else {
            playmusic.stop();
            playmusic.loop();
        }
    }
    // looping or stopping menu music
    public static void menuMusic(String s) {
        if (s.equals("STOP")) {
            menumusic.stop();
        } else {
            menumusic.stop();
            menumusic.loop();
        }
    }
    // loop or stop win screen music
    public static void winMusic(String s) {
        if (s.equals("STOP")) {
            winmusic.stop();
        } else {
            winmusic.stop();
            winmusic.loop();
        }
    }
    // loop or stop typing music
    public static void typingMusic(String s) {
        if (s.equals("STOP")) {
            typing.stop();
        } else {
            typing.stop();
            typing.loop();
        }
    }
    // play lose life sound effect
    public static void loselife() {
        loselife.stop();
        loselife.play();
    }
    // ball bounce off wall
    public static void wallBounce() {
        wallbounce.stop();
        wallbounce.play();
    }
    // player death sound effect
    public static void die() {
        die.stop();
        die.play();
    }
    // hit block sound
    public static void hit() {
        hit.stop();
        hit.play();
    }
    // sound effect for skipping line of text during story
    public static void skipText() {
        skipmusic.stop();
        skipmusic.play();
    }
    // sound effect for entering next screen
    public static void nextScreen() {
        nextscreen.stop();
        nextscreen.play();
    }
    // ball becoming active
    public static void ballActive() {
        ballactive.stop();
        ballactive.play();
    }
    // soudn effect for when entering laser powerup mode
    public static void transform() {
        transform.stop();
        transform.play();
    }
    // sound for gaining powerup
    public static void powerup() {
        powerup.stop();
        powerup.play();
    }
    // sound for hitting block
    public static void hitblock() {
        hitblock.stop();
        hitblock.play();
    }
    // sound for laser
    public static void laser() {
        laser.stop();
        laser.play();
    }
    // sound for exploding block by laser
    public static void explosion() {
        explode.stop();
        explode.play();
    }
}

class SoundEffect{
    private Clip c;
    public SoundEffect(String filename){
        setClip(filename);
    }
    public void setClip(String filename){
        try{
            File f = new File(filename);
            c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(f));
        } catch(Exception e){ System.out.println("error"); }
    }
    // play sound one time
    public void play(){
        c.setFramePosition(0);
        c.start();
    }
    // looping music instead of just playing it once
    public void loop() {
        c.setFramePosition(0);
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }
    // stopping music
    public void stop(){
        c.stop();
    }
}