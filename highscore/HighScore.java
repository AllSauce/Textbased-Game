import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class shows the data of players and their highscore.
 * @author Krenar Manxhuka
 * @version 2020.11.05
 */
public class HighScore {

    /**
     * Extracts data from a .txt file, on players, and prints their name,
     * country and highscore.
     * @param filename which allows the method to take data on
     * players from the .txt file used.
     */
    public static void printHighScores(String filename) {
        BufferedReader inputStream = null;

        ArrayList<Player> players = new ArrayList<>();

        try {
            inputStream = new BufferedReader(new FileReader(filename));
            String line;
            String[] playerData;
            while ((line = inputStream.readLine()) != null) {
                playerData = line.split(",");
                int score = Integer.parseInt(playerData[2]);

                Player player = new Player(playerData[0], playerData[1], score);
                players.add(player);
                System.out.println(player.toString());
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public static void main(String[] args) {
        printHighScores("scores.txt");
    }
}
