/*
Story.java
Isaac Jiang
Controls lore for arkanoid before each level begins. Displays text character by character and then lets player click
to begin the level. Also allowed for player to skip lines and get to game faster if they have already read the lore.
 */
import java.awt.*;
public class Story {
    // flicker variable to control when the "CLICK TO FIGHT" message is displayed
    static int flicker = 0;
    // dialogue
    static String[][] storylines = {{"DOH: Arkanoid... I am coming for you.",
            "YOU: Who is this?! Vaus, eject!",
            "DOH: Your puny paddleboard stands no chance!",
            "YOU: We'll see about that..."},
            {"DOH: Your free trial of living has expired.",
                    "YOU: Seriously? Don't make me do this.",
                    "DOH: Do what? Roll in your GRAVE?",
                    "YOU: Eat lasers, DOUGH!!!"}};
    // variables to keep track of character displayed and line displayed
    private static double charcount;
    private static int linecount;
    // initialize both values to 0

    public Story() {
        charcount = 0;
        linecount = 0;
    }
    // to reset story
    public static void resetStory() {
        linecount = 0;
        charcount = 0;
    }
    // skip lines when replaying
    public static void skipLine() {
        Sound.skipText();
        charcount = storylines[GamePanel.getLevel()][linecount].length()-3;
    }

    // main driver code
    public static void readStory(Graphics g) {
        g.setFont(GamePanel.fontLocalStory);
        // fully display lines if they have already been covered
        for (int i = storylines.length; i >= 0; i--) {
            if (linecount > i) {
                g.drawString(storylines[GamePanel.getLevel()][i], 50, 450 + (i*50));
            }
        }
        // drawing last line
        g.drawString(storylines[GamePanel.getLevel()][linecount].substring(0, (int) charcount),50,450+(50*linecount));
        // if not finished displaying characters increase char count
        if (Math.ceil(charcount) < storylines[GamePanel.getLevel()][linecount].length()-1) {
            charcount+=0.7;
        } else {
            // only reset charcount if not on last line
            if (linecount < 3) {
                charcount = 0;
            }
            // if not last line, increase the linecount to keep displaying lines
            if (linecount < storylines[GamePanel.getLevel()].length-1) {
                linecount++;
            } else {
                // done displaying lines, story is complete
                // flicker display "CLICK TO FIGHT"
                Sound.typingMusic("STOP");
                GamePanel.storyTimeComplete = true;
                if (flicker < 12) {
                    g.drawString("CLICK TO FIGHT", 50, 650);
                } else if (flicker > 24) {
                    flicker = 0;
                }
                flicker++;
            }
        }
    }
}
