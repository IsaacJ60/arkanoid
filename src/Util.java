/*
Util.java
Isaac Jiang
contains utility functions such as getting random integer, adding a highscore, and loading fonts
 */

import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Util {
    // returns random integer
    public static int randint(int low, int high){
        return (int)(Math.random()*(high-low+1)+low);
    }
    // checks if highscore should be added to highscore file
    public static void highScore(String name, int score) {
        String[] s = new String[3];
        int[] n = new int[3];
        try {
            Scanner f = new Scanner(new BufferedReader(new FileReader("src/images/highscore.txt")));
            // goes through file to get highscores
            for (int i = 0; i < 3; i++) {
                s[i] = f.nextLine();
                n[i] = Integer.parseInt(f.nextLine());
            }
            for (int i = 0; i < 3; i++) {
                if (score >= n[i]) { // if the new score is higher or same as any current highscore, move highscores down
                    for (int j = 2; j > i; j--) {
                        n[j] = n[j-1];
                        s[j] = s[j-1];
                    }
                    n[i] = score; // stick new highscore into array
                    s[i] = name;
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex + "dummy");
        }

        try {
            // write new highscore back to highscore file
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("src/images/highscore.txt")));
            for (int i = 0; i < 3; i++) {
                out.println(s[i]);
                out.println(n[i]);
            }
            out.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    // loading all fonts
    public static void loadFonts() {
        // normal-sized font
        String fName = "Monocraft.otf";
        InputStream is = GamePanel.class.getResourceAsStream(fName);
        try {
            assert is != null;
            GamePanel.fontLocal = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(32f);
        } catch(IOException ex){
            System.out.println(ex);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
        // large font
        String fName1 = "Monocraft.otf";
        InputStream is1 = GamePanel.class.getResourceAsStream(fName1);
        try {
            GamePanel.fontLocalBig = Font.createFont(Font.TRUETYPE_FONT, is1).deriveFont(50f);
        } catch(IOException ex){
            System.out.println(ex);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
        // highscore font
        String fName2 = "Monocraft.otf";
        InputStream is2 = GamePanel.class.getResourceAsStream(fName2);
        try {
            GamePanel.fontLocalHS = Font.createFont(Font.TRUETYPE_FONT, is2).deriveFont(35f);
        } catch(IOException ex){
            System.out.println(ex);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
        // small font
        String fName3 = "Monocraft.otf";
        InputStream is3 = GamePanel.class.getResourceAsStream(fName3);
        try {
            GamePanel.fontLocalSmall = Font.createFont(Font.TRUETYPE_FONT, is3).deriveFont(16f);
        } catch(IOException ex){
            System.out.println(ex);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
        // font for story
        String fName4 = "Monocraft.otf";
        InputStream is4 = GamePanel.class.getResourceAsStream(fName4);
        try {
            GamePanel.fontLocalStory = Font.createFont(Font.TRUETYPE_FONT, is4).deriveFont(22f);
        } catch(IOException ex){
            System.out.println(ex);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }
}