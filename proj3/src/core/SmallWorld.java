package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import static core.World2.HEIGHT;
import static core.World2.WIDTH;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SmallWorld {

    private TETile[][] world;
    private Random random;
    private int currX = 0;
    private int currY = 0;
    private int amountCoinsLeft = 0;
    private InteractiveWorld2 previousWorld;
    public SmallWorld(InteractiveWorld2 previousWorld) {
        this.previousWorld = previousWorld;
        this.world = new TETile[HEIGHT][WIDTH];
        this.random = new Random(WIDTH);
        resetScreen();
        buildHeader();
        createAvatar();
        addCoins();
        play();
    }

    private void resetScreen() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.world[y][x] = Tileset.NOTHING;
                this.world[y][x] = Tileset.CELL;
            }
        }
    }

    private void buildHeader() {
        char[] header = "You have 10s to chop trees.".toCharArray();
        int x1 = (WIDTH / 2) - header.length / 2;
        int headerY = HEIGHT - (HEIGHT / 4);
        for (char c : header) {
            fill(this.world, x1, headerY, c);
            x1++;
        }
    }

    private void addCoins() {
        int amount = 8;
        int i = 0;
        while (i <= amount) {
            int currX = generateHorizontalLengths();
            int currY = generateVerticalLengths();
            if (world[currY][currX] != Tileset.TREE && world[currY][currX] != Tileset.AVATAR) {
                world[currY][currX] = Tileset.TREE;
                i--;
            }
        }
        this.amountCoinsLeft = 8;
    }

    private void createAvatar() {
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
    }

    private int generateHorizontalLengths() {
        return random.nextInt(HEIGHT);
    }
    private int generateVerticalLengths() {
        return random.nextInt(WIDTH);
    }


    private void fill(TETile[][] world, int x, int y, char c) {
        TETile tile = new TETile(c, Color.cyan, Color.black, " ", 0);
        world[y][x] = tile;
    }



    public void play() {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        char c;
        char[] previousTwoWords = new char[2];
        long startTime = System.currentTimeMillis();
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
                } if(this.amountCoinsLeft == 0) {
                    return;
                } if(System.currentTimeMillis() - startTime >= 10000){
                    failureScreen();
                }

            }
            ter.renderFrame(world);
        }

    }

    private void failureScreen(){
        char[] header = "Good game bro".toCharArray();
        int x1 = (WIDTH / 2) - header.length / 2;
        int headerY = HEIGHT - (HEIGHT / 4);
        for (char c : header) {
            fill(this.world, x1, headerY, c);
            x1++;
        }
        long startTime = System.currentTimeMillis();
        System.exit(0);
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


    private static void saveToFile(String data) {
        try (FileWriter writer = new FileWriter("savedWorld.txt")) {
            System.out.println("Saving file");
            writer.write(data);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private String worldToString(TETile[][] world) {
        String s = "";
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                s = s + world[x][y].character();
            }
            s += "\n";
        }
        return s;
    }





}
