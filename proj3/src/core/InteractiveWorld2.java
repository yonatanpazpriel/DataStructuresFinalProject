package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.awt .*;

public class InteractiveWorld2 {
    private static final int WIDTH = World2.WIDTH;
    private static final int HEIGHT = World2.HEIGHT;
    private TETile[][] world;
    private int startX;
    private int startY;

    public InteractiveWorld2(long seed) {
        world = createWorld(seed);
        boolean breaker = true;
        for (int x = 0; x < WIDTH && breaker; x++) {
            for (int y = 0; y < HEIGHT && breaker; y++) {
                if (world[x][y] == Tileset.CELL) {
                    startX = x;
                    startY = y;
                    world[startX][startY] = Tileset.AVATAR;
                    System.out.println("Avatar @: (" + startX + ", " + startY + ")");
                    breaker = false;
                    break;
                }
            }
        }
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);

    }

    public static void fill(TETile[][] world, int x, int y, char c) {
        TETile tile = new TETile(c, Color.cyan, Color.black, " ", 0);
        world[x][y] = tile;
    }

    public static void play(TETile[][] world) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        char c;
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == "l".toCharArray()[0] || c == "n".toCharArray()[0] || c == "q".toCharArray()[0]) {
                    //
                }
            }
            ter.renderFrame(world);
        }
    }


    public static TETile[][] createWorld(long longSeed) {
        World2 x = new World2(longSeed);
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
        return world;
    }
    }


