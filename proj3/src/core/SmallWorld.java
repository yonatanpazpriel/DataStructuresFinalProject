package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SmallWorld {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private TETile[][] world;
    private Random random;
    private int currX = 0;
    private int currY = 0;
    private int amountCoinsLeft = 0;
    public SmallWorld(TETile[][] world) {
        this.world = world;
        this.random = new Random(WIDTH);

    }

    private void resetScreen() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void buildHeader() {
        char[] header = "COLLECT ALL THE COINS!".toCharArray();
        int x1 = (WIDTH / 2) - header.length / 2;
        int headerY = HEIGHT - (HEIGHT / 4) + 3;
        for (char c : header) {
            fill(this.world, x1, headerY, c);
            x1++;
        }
    }

    private void fillScreen() {
        for (int x = 10; x < 30; x++) {
            for (int y = 10; y < 30d; y++) {
                this.world[x][y] = Tileset.CELL;
            }
        }
    }

    private void addCoins() {
        int amount = 5;
        int i = 1;
        while (i <= amount) {
            int currX = generateHorizontalLengths();
            int currY = generateVerticalLengths();
            if (world[currX][currY] == Tileset.CELL) {
                world[currX][currY] = Tileset.TREE;
                i++;
            }
        }
        this.amountCoinsLeft = 5;
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
        world[x][y] = tile;
    }

    public void play() {
        resetScreen();
        buildHeader();
        fillScreen();
        createAvatar();
        addCoins();
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
                    TETile[][] victory = victoryScreen();
                    long time = System.currentTimeMillis();
                    while (System.currentTimeMillis() - time < 1000) {
                        ter.renderFrame(victory);
                    }
                    while (StdDraw.hasNextKeyTyped()) {
                        StdDraw.nextKeyTyped();
                    }
                    boolean keyPressed = false;
                    while (!keyPressed) {
                        ter.renderFrame(victory);
                        if (StdDraw.hasNextKeyTyped()) {
                            keyPressed = true;
                        }
                    }
                    return;

                } if(System.currentTimeMillis() - startTime >= 10000){
                    long time = System.currentTimeMillis();
                    TETile[][] failure = failureScreen();
                    while (System.currentTimeMillis() - time < 1000) {
                        ter.renderFrame(failure);
                    }
                    while (StdDraw.hasNextKeyTyped()) {
                        StdDraw.nextKeyTyped();
                    }
                    boolean keyPressed = false;
                    while (!keyPressed) {
                        ter.renderFrame(failure);
                        if (StdDraw.hasNextKeyTyped()) {
                            keyPressed = true;
                        }
                    }
                    return;
                }

            }
            ter.renderFrame(world);
        }

    }

    private TETile[][] victoryScreen() {
        TETile[][] victoryScreen = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                victoryScreen[i][j] = Tileset.NOTHING;
            }
        }
        char[] header = "You win!".toCharArray();
        int x1 = (WIDTH / 2) - header.length / 2;
        int headerY = HEIGHT / 2 + 2;
        for (char c : header) {
            fill(victoryScreen, x1, headerY, c);
            x1++;
        }
        header = "Press any key to return to main world.".toCharArray();
        int x2 = (WIDTH / 2) - header.length / 2;
        headerY = headerY - 4;
        for (char f : header) {
            fill(victoryScreen, x2, headerY + 1, f);
            x2++;
        }

        return victoryScreen;
    }

    private TETile[][] failureScreen(){
        TETile[][] failureScreen = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                failureScreen[i][j] = Tileset.NOTHING;
            }
        }


        char[] header = "You lose!".toCharArray();
        int x1 = (WIDTH / 2) - header.length / 2;
        int headerY = HEIGHT / 2 + 2;
        for (char c : header) {
            fill(failureScreen, x1, headerY, c);
            x1++;
        }
        header = "Press any key to return to main world.".toCharArray();
        int x2 = (WIDTH / 2) - header.length / 2;
        headerY = headerY - 4;
        for (char f : header) {
            fill(failureScreen, x2, headerY + 1, f);
            x2++;
        }

        return failureScreen;
    }

    private void moveUp(int currX, int currY) {
        if (currY < HEIGHT - 1) {
            if (world[currX][currY + 1] == Tileset.CELL) {
                world[currX][currY + 1] = Tileset.AVATAR;
                world[currX][currY] = Tileset.CELL;
                this.currY++;
            } else if (world[currX][currY + 1] == Tileset.TREE) {
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
        } else if (world[currX][currY - 1] == Tileset.TREE) {
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
        } else if  (world[currX + 1][currY] == Tileset.TREE) {
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
        } else if (world[currX - 1][currY] == Tileset.TREE) {
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