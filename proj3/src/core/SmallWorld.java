package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import static core.World2.HEIGHT;
import static core.World2.WIDTH;
import java.awt.*;
import java.util.Random;

public class SmallWorld {

    private TETile[][] world;
    private Random random;
    private int currX = 0;
    private int currY = 0;
    private int amountCoinsLeft = 0;
    public SmallWorld() {
        this.world = new TETile[40][40];
        this.random = new Random(40);
        resetScreen();
        addCoins();
    }

    private void resetScreen() {
        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 40; y++) {
                this.world[y][x] = Tileset.NOTHING;
                this.world[y][x] = Tileset.CELL;
            }
        }
    }

    private void addCoins() {
        int amount = 8;
        int i = 0;
        while (i <= amount) {
            int currX = generateHorizontalLengths();
            int currY = generateVerticalLengths();
            if (world[currY][currX] != Tileset.TREE) {
                world[currY][currX] = Tileset.TREE;
                i--;
            }
        }
        this.amountCoinsLeft = 8;
    }


    private int generateHorizontalLengths() {
        return random.nextInt(40);
    }
    private int generateVerticalLengths() {
        return random.nextInt(40);
    }


    private void fill(TETile[][] world, int x, int y, char c) {
        TETile tile = new TETile(c, Color.cyan, Color.black, " ", 0);
        world[y][x] = tile;
    }



    public InteractiveWorld2 startPlaying() {
        TERenderer ter = new TERenderer();
        ter.initialize(40, 40);
        char c;
        char[] previousTwoWords = new char[2];
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                previousTwoWords[1] = previousTwoWords[0];
                previousTwoWords[0] = c;
                String colonQ = "" + previousTwoWords[1] + previousTwoWords[0];
                boolean quit = false;
                if (colonQ.equals(":Q") || colonQ.equals(":q")) {
                    quit = true;
                }
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
                }
                if (quit) {
                    String toSave = worldToString(world);
                    saveToFile(toSave);
                    System.exit(0);
                } else if(this.amountCoinsLeft == 0) {

                }

            }
            ter.renderFrame(world);
        }

    }

    private void moveUp(int currX, int currY) {
        if (currY < HEIGHT - 1) {
            if (world[currX][currY + 1] == Tileset.CELL) {
                world[currX][currY + 1] = Tileset.AVATAR;
                world[currX][currY] = Tileset.CELL;
                this.currY++;
            } else if (world[currX][currY + 1] == Tileset.GRASS) {
                world[currX][currY + 1] = Tileset.AVATAR;
                world[currX][currY] = Tileset.CELL;
                this.currY++;
                this.amountCoinsLeft-=1;
            }
        }
    }

    private void moveDown(int currX, int currY) {
        if (world[currX][currY - 1] == Tileset.CELL) {
            world[currX][currY - 1] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currY--;
        } else if (world[currX][currY - 1] == Tileset.GRASS) {
            world[currX][currY - 1] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currY--;
            this.amountCoinsLeft-=1;
        }
    }
    private void moveRight(int currX, int currY) {
        if (world[currX + 1][currY] == Tileset.CELL) {
            world[currX + 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currX++;
        } else if  (world[currX + 1][currY] == Tileset.GRASS) {
            world[currX + 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currX++;
            this.amountCoinsLeft-=1;
        }
    }
    private void moveLeft(int currX, int currY) {
        if (world[currX - 1][currY] == Tileset.CELL) {
            world[currX - 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currX--;
        } else if (world[currX - 1][currY] == Tileset.GRASS) {
            world[currX - 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currX--;
            this.amountCoinsLeft-=1;
        }
    }





}
