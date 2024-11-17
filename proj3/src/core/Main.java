package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

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
    /*
    public static void main(String[] args) {
        int SEED = 0;
        // build your own world!
        World2 x = new World2(SEED);
        int [][] yippee = x.world;//.createWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i< WIDTH; i++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[y][i] = Tileset.NOTHING;
                if (yippee[y][i] == 1 || yippee[y][i] == 2) {
                    world[y][i] = Tileset.GRASS;
                } else if (yippee[y][i] == 3) {
                    world[y][i] = Tileset.WALL;
                }
            }
        }
        ter.renderFrame(world);
    }

     */

    public static void main(String[] args) {
        WorldTake2 x = new WorldTake2(5);
        int [][] yippee = x.getWorld();
        print2DArray(yippee);
    }
}
