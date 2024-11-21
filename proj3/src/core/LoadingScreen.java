package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.LinkedList;

public class LoadingScreen {
    private static final int WIDTH = World2.WIDTH;
    private static final int HEIGHT = World2.HEIGHT;
    private int headerY;
    private Character response;

    public LoadingScreen(TETile[][] world) {
        headerY = 0;
        resetScreen(world);
        buildHeader(world);
        buildStartMenu(world);
    }
    private void resetScreen(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    private void fill(TETile[][] world, int x, int y, char c) {
        TETile tile = new TETile(c, Color.cyan, Color.black, " ", 0);
        world[x][y] = tile;
    }


    private void buildHeader(TETile[][] world) {
        char[] header = "CS61B: BYOW".toCharArray();
        int x1 = (WIDTH / 2) - header.length / 2;
        headerY = HEIGHT - (HEIGHT / 4);
        for (char c : header) {
            fill(world, x1, headerY, c);
            x1++;
        }
    }

    private void buildStartMenu(TETile[][] world) {
        char[] newGame = "Enter seed followed by S".toCharArray();
        int x2 = (WIDTH / 2) - newGame.length / 2;
        int y2 = headerY - 10;
        for (char c : newGame) {
            fill(world, x2, y2, c);
            x2++;
        }


        /*
        char[] saveGame = "(L) Load Game".toCharArray();
        int x3 = (WIDTH / 2) - saveGame.length / 2;
        int y3 = y2 - 2;
        for (char c : saveGame) {
            TETile currChar = new TETile(c, Color.blue, Color.pink, "CS61B: BYOW", 0);
            world[x3][y3] = currChar;
            x3++;
        }

        char[] quitGame = "(Q) Quit Game".toCharArray();
        int x4 = (WIDTH / 2) - quitGame.length / 2;
        int y4 = y3 - 2;
        for (char c : quitGame) {
            TETile currChar = new TETile(c, Color.blue, Color.pink, "CS61B: BYOW", 0);
            world[x4][y4] = currChar;
            x4++;
        }
         */
    }

    public LinkedList<Character> buildAndAcceptSeed(TETile[][] world) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        LinkedList<Character> seed = new LinkedList<>();
        int x = (WIDTH / 2);
        int y = headerY - 20;

        char c;
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();

                /*
                if (c == "n".toCharArray()[0]) {
                    callNew(world);
                }
                 */
                if (Character.isDigit(c) ) {
                    seed.add(c);
                    x = (WIDTH)/2 - seed.size()/2;
                    int xCurr = x;

                    for (char currNumber: seed) {
                        fill(world, xCurr, y, currNumber);
                        xCurr += 1;
                    }
                } else if (c == "s".toCharArray()[0] || c == "S".toCharArray()[0]) {
                    return seed;
                }
                else {
                    return null;
                }
            }
            ter.renderFrame(world);
        }
    }
}
