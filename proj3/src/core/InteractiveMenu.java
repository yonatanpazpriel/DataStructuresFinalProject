package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InteractiveMenu {

    private static final int WIDTH = World2.WIDTH;
    private static final int HEIGHT = World2.HEIGHT;
    private static int headerY;
    public static void fill(TETile[][] world, int x, int y, char c) {
        TETile tile = new TETile(c, Color.cyan, Color.black, " ", 0);
        world[x][y] = tile;
    }

    public static void resetScreen(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void buildHeader(TETile[][] world) {
        char[] header = "CS61B: BYOW".toCharArray();
        int x1 = (WIDTH / 2) - header.length / 2;
        headerY = HEIGHT - (HEIGHT / 4);
        for (char c : header) {
            fill(world, x1, headerY, c);
            x1++;
        }
    }

    public static void buildStartMenu(TETile[][] world) {
        buildHeader(world);
        char[] newGame = "(N) New Game".toCharArray();
        int x2 = (WIDTH / 2) - newGame.length / 2;
        int y2 = headerY - 5;
        for (char c : newGame) {
            TETile currChar = new TETile(c, Color.blue, Color.pink, "CS61B: BYOW", 0);
            world[x2][y2] = currChar;
            x2++;
        }

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
    }

    public static Character buildAndAcceptNLQ() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        resetScreen(world);
        buildStartMenu(world);

        char c;
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == "n".toCharArray()[0]) {
                    callNew(world);
                }
                if (c == "l".toCharArray()[0] || c == "n".toCharArray()[0] || c == "q".toCharArray()[0]) {

                    return c;
                }
            }
            ter.renderFrame(world);
        }
    }

    public static void callNew(TETile[][] world) {

        char c;
        resetScreen(world);
        buildHeader(world);
        int x = WIDTH / 2;
        List<Character> seed = new ArrayList<>();
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (Character.isDigit(c)) {
                    seed.add(c);
                }
                // keep seed centered ? not necessary lowk
            }
            //ter.renderFrame(world);
        }
    }
}
