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
    private int currX;
    private int currY;

    public InteractiveWorld2(TETile[][] world) {
        this.world = world;
        boolean breaker = true;
        for (int x = 0; x < WIDTH && breaker; x++) {
            for (int y = 0; y < HEIGHT && breaker; y++) {
                if (world[x][y] == Tileset.CELL) {
                    currX = x;
                    currY = y;
                    world[currX][currY] = Tileset.AVATAR;
                    System.out.println("Avatar @: (" + currX + ", " + currY + ")");
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

    public void startPlaying() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        char c;
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                switch (c) {
                    case 'w':
                        moveUp(currX, currY);
                        System.out.println("move up");
                        ter.renderFrame(world);
                        break;
                    case 'a':
                        moveLeft(currX, currY);
                        ter.renderFrame(world);
                        break;
                    case 's':
                        moveDown(currX, currY);
                        ter.renderFrame(world);
                        break;
                    case 'd':
                        moveRight(currX, currY);
                        ter.renderFrame(world);
                        break;
                    case 'q':
                        System.out.println("exiting");
                        System.exit(0);
                        break;
                    default:
                        break;
                }

        }
        ter.renderFrame(world);
    }

    }

    private void moveUp(int currX, int currY) {
        if (world[currX][currY + 1] == Tileset.CELL) {
            world[currX][currY + 1] =  Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currY++;
        }
    }

    private void moveDown(int currX, int currY) {
        if (world[currX][currY - 1] == Tileset.CELL) {
            world[currX][currY - 1] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currY--;
        }
    }
    private void moveRight(int currX, int currY) {
        if (world[currX + 1][currY] == Tileset.CELL) {
            world[currX + 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currX++;
        }
    }
    private void moveLeft(int currX, int currY) {
        if (world[currX - 1][currY] == Tileset.CELL) {
            world[currX - 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currX--;
        }
    }
}



