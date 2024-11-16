package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import static core.World.HEIGHT;
import static core.World.WIDTH;

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
        int SEED = 0;
        // build your own world!
        World x = new World();
        int [][] yippee = x.createWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i< WIDTH; i++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[i][y] = Tileset.NOTHING;
                if (yippee[i][y] == 1 || yippee[i][y] == 2) {
                    world[i][y] = Tileset.GRASS;
                } else if (yippee[i][y] == 3) {
                    world[i][y] = Tileset.WALL;
                }
            }
        }
        ter.renderFrame(world);
    }
}
