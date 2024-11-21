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
    public static void print2DArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {          // Iterate over rows
            for (int j = 0; j < array[i].length; j++) {    // Iterate over columns in each row
                System.out.print(array[i][j] + "\t");      // Print each element with a tab space
            }
            System.out.println();                          // Move to the next line after each row
        }
    }
    public static void main(String[] args) {


        /*
        Random random = new Random();
        long SEED = 7647956535394261435l;

        Character nlq = InteractiveMenu.buildAndAcceptNLQ();

        // build your own world!
        World2 x = new World2(SEED);
        int [][] yippee = x.world;//.createWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i< WIDTH; i++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[y][i] = Tileset.NOTHING;
                if (yippee[y][i] == 1) {
                    world[y][i] = Tileset.CELL;
                } if (yippee[y][i] == 2) {
                    world[y][i] = Tileset.CELL;
                }else if (yippee[y][i] == 3) {
                    world[y][i] = Tileset.WALL;
                }
            }
        }
        ter.renderFrame(world);

         */
        /*
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        InteractiveMenu initialScreen = new InteractiveMenu(world);
        Character response = initialScreen.buildAndAcceptNLQ(world);
        System.out.print(response);

         */
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        /*
        Builds initial Screen and stores in RESPONSE
         */
        InteractiveMenu initialScreen = new InteractiveMenu(world);
        Character response = initialScreen.buildAndAcceptNLQ(world);
        /*
        uses response to determine actions
        1. user types n; use loadingscreen object to store and render seed
        2. user types L. UNIMPLEMENTED
        3. user type Q. UNIMPLEMENTED
         */

        if (Character.toLowerCase(response) == "n".toCharArray()[0]) {
            LoadingScreen secondScreen = new LoadingScreen(world);
            LinkedList<Character> seed = secondScreen.buildAndAcceptSeed(world);
            ///System.out.print(seed);
        }


    }

}