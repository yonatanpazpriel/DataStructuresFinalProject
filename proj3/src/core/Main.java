package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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


    public static void main(String[] args) {

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
            previousWorld.startPlaying();
        }

        else if ( (Character.toLowerCase(response) == "q".toCharArray()[0])) {
            System.exit(0);
        }


    }

}