import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 *  This class is the main class of the "Potato House" application.
 *  "Potato House" is a very simple, text based adventure game.  Users
 *  can walk around some rooms. The goal of the game is to get a hidden
 *  golden egg. One can also get completetly stuck in a trap room.
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Krenar Manxhuka
 * @version 2020.11.05
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Room roomOfSinners;
    HashMap<String, Room> worldModel = new HashMap<String, Room>();

    /**
     * Create the game and initialise its internal map.
     * @param inputStream to read the "rooms.txt" file.
     * @param room a room that takes value from the txt file.
     * @param name
     */
    public Game()
    {
        createRoomsFromFile("rooms.txt");
        parser = new Parser();
    }

    /**
     * Takes data from used file and creates the map.
     * It creates a HashMap of the rooms.
     * @param filename which allows the method
     * to take data from the .txt file used.
     */
    private void createRoomsFromFile(String filename){
        BufferedReader inputStream = null;

        try{
          inputStream = new BufferedReader(new FileReader(filename));
          String line;
          String[] roomData;
          while((line = inputStream.readLine()) != null){
            roomData = line.split("-");

            if(roomData[0].equals("Room")){
              String name = roomData[1];
              Room room = new Room(roomData[2]);
              worldModel.put(name, room);
            }
            else if(roomData[0].equals("Exit")){
              worldModel.get(roomData[1]).setExit(roomData[2], worldModel.get(roomData[3]));
            }

            currentRoom = worldModel.get("entrance");
          }
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            winGame();
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to potato house!");
        System.out.println("Somewhere in this house, there is a golden egg! Can you find it?");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
       System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("The potato house has no such path. Use an available path.");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * Print out some help information.
     * Here we print a message to give the player determination and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("Alas, you are lost. But still determined. You wander...");
        System.out.println("inside potato house.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no path there!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * This method is the games win condition.
     * When one reaches the dungeon, they win and the program exits.
     */
    public void winGame(){
      boolean winner = false;
      if(currentRoom == worldModel.get("dungeon")){
        System.out.println("");
        System.out.println("You see a golden egg on a table. You grab it. \nCongratulations! You have found the golden egg! \nThank you for playing!");
        System.exit(0);
      }
    }

    //The main method
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
