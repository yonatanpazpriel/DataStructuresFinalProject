package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.awt .*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InteractiveWorld2 {
    private static final int WIDTH = World2.WIDTH;
    private static final int HEIGHT = World2.HEIGHT;
    private TETile[][] world;
    private int currX;
    private int currY;
    private static Random random;
    private static Random charactersAmount;

    public InteractiveWorld2(TETile[][] world, int startX, int startY) {
        this.world = world;
        this.currX = startX;
        this.currY = startY;
        world[currX][currY] = Tileset.AVATAR;
        System.out.println("Avatar @: (" + currX + ", " + currY + ")");
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }

    public InteractiveWorld2(TETile[][] world) {
        random = new Random(WIDTH);
        charactersAmount = new Random(15);
        this.world = world;
        createAvatar();
        createTokens();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
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
    private void generateRandomCharacter(int x, int y) {
        for (int i = x; i <= WIDTH - 2; i++) {
            for (int j = y; j <= HEIGHT - 2; j++) {
                if (world[i][j] == Tileset.CELL) {
                    world[i][j] = Tileset.GRASS;
                    System.out.println("Grass @: (" + i + ", " + j + ")");
                    return; // Exit after placing one character
                }
            }
        }
    }

    private void createTokens() {
        int amount = charactersAmount.nextInt(11) + 4; // Ensure charactersAmount is between 4 and 14
        int i = 0;
        while (i < amount) {
            int x = random.nextInt(WIDTH - 2) + 1; // Generate random x within [1, WIDTH-2]
            int y = random.nextInt(HEIGHT - 2) + 1; // Generate random y within [1, HEIGHT-2]
            generateRandomCharacter(x, y);
            i++;
        }
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

    public InteractiveWorld2 startPlaying() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
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
                }

        }
        ter.renderFrame(world);
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

    private void moveUp(int currX, int currY) {
        if (world[currX][currY + 1] == Tileset.CELL) {
            world[currX][currY + 1] =  Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currY++;
        } else if (world[currX][currY + 1] == Tileset.GRASS) {
            SmallWorld curr = new SmallWorld(this);
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
        } else if (world[currX][currY - 1] == Tileset.GRASS) {
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
        } else if  (world[currX + 1][currY] == Tileset.GRASS) {
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
        } else if (world[currX - 1][currY] == Tileset.GRASS) {
            world[currX - 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            this.currX--;
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

}



