package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import static core.World2.HEIGHT;
import static core.World2.WIDTH;


public class Main {
    private static InteractiveWorld2 previousWorld;

    public static void print2DArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {          // Iterate over rows
            for (int j = 0; j < array[i].length; j++) {    // Iterate over columns in each row
                System.out.print(array[i][j] + "\t");      // Print each element with a tab space
            }
            System.out.println();                          // Move to the next line after each row
        }
    }


    public static void main(String[] args) throws Exception {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        //Builds initial Screen and stores in RESPONSE
        InteractiveMenu initialScreen = new InteractiveMenu(world);
        Character response = initialScreen.buildAndAcceptNLQ(world);

        /*
        uses response to determine actions
        1. user types n; use loadingscreen object to store and render seed. displays world.
        2. user types L. UNIMPLEMENTED
        3. user type Q. Quits program, closes windows
         */

        if (Character.toLowerCase(response) == "n".toCharArray()[0]) {
            LoadingScreen secondScreen = new LoadingScreen(world);
            LinkedList<Character> seedAcceptor = secondScreen.buildAndAcceptSeed(world);
            String seed = "";
            for (Character digit : seedAcceptor) { seed += digit; }
            long seedLong = Long.parseLong(seed);
            World2 tempWorld = new World2(seedLong);
            world = tempWorld.createWorld();
            InteractiveWorld2 interactive = new InteractiveWorld2(world);
            previousWorld = interactive.startPlaying();
        }

        else if (Character.toLowerCase(response) == "l".toCharArray()[0]) {
            // fileLoader returns InteractiveWorld object reconstructed from savedWorld.txt
            fileLoader().startPlaying();
        }
        else if ( (Character.toLowerCase(response) == "q".toCharArray()[0])) {
            System.exit(0);
        }
    }

    private static InteractiveWorld2 fileLoader() throws Exception {
        String filePath = "savedWorld.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int avatarX = 0;
            int avatarY = 0;
            String[] lines = reader.lines().toArray(String[]::new);

            // Step 2: Create a 2D array
            int rows = lines.length;
            int cols = lines[0].length(); // Assuming all rows have the same length
            TETile[][] reconstructedWorld = new TETile[rows][cols];
            char[][] charGrid = new char[rows][cols];
            // Step 3: Fill the 2D array
            for (int i = 0; i < rows; i++) {
                charGrid[i] = lines[i].toCharArray();
                int x = 0;
                for (char c : charGrid[i]) {
                    if (c == "#".toCharArray()[0]) {
                        reconstructedWorld[i][x] = Tileset.WALL;
                    } else if (c == "█".toCharArray()[0]) {
                        reconstructedWorld[i][x] = Tileset.CELL;
                    } else if (c == " ".toCharArray()[0]) {
                        reconstructedWorld[i][x] = Tileset.NOTHING;
                    } else if (c == "@".toCharArray()[0]) {
                        avatarX = i;
                        avatarY = x;
                        System.out.println("Avatar " + avatarX + ", " + avatarY);
                        reconstructedWorld[i][x] = Tileset.CELL;
                    }
                    x++;
                }
            }
            return new InteractiveWorld2(reconstructedWorld, avatarX, avatarY);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        throw new Exception("No world loaded.");
    }

}