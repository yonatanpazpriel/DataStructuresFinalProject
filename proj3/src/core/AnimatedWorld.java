package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;

public class AnimatedWorld {
    public static final int WIDTH = World2.WIDTH;
    public static final int HEIGHT = World2.HEIGHT;
    private TETile[][] world;

    public AnimatedWorld(TETile[][] world) {
        this.world = world;
        placeCharacter();
    }
    private void fill(TETile[][] world, int x, int y) {;
        world[y][x] = Tileset.AVATAR;
    }

    private void placeCharacter() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        int x = WIDTH/2;
        int y = HEIGHT/2;
        for (int i = 0; i<WIDTH; i++) {
            for (int j =0; j<HEIGHT; j++){
                if (world[y][x] == Tileset.CELL) {
                    fill(world, x, y);
                }
            }
        }
    }
}
