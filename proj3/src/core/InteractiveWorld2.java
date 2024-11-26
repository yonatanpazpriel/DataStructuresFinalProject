package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InteractiveWorld2 {
    private static final int WIDTH = World2.WIDTH;
    private static final int HEIGHT = World2.HEIGHT;
    private TETile[][] world;
    private TETile[][] darkWorld;
    private TETile[][] lightWorld;
    private int currX;
    private int currY;
    private static Random random;
    private static Random charactersAmount;
    private boolean darknessOn;
    private SmallWorld smallworld;

    public InteractiveWorld2(TETile[][] world, int startX, int startY) {
        this.world = world;
        this.lightWorld = copy(world);
        this.darkWorld = darkness(world, startX, startY);
        this.darknessOn = false;
        smallworld = new SmallWorld(new TETile[WIDTH][HEIGHT]);
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
        smallworld = new SmallWorld(new TETile[WIDTH][HEIGHT]);
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

    private TETile[][] darkness(TETile[][] world, int x, int y) {

        TETile[][] temp = copy(world);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                double distance = Math.sqrt((j - x)*(j - x) + (i - y)*(i - y));
                if (distance > 5 ) {//&& j > 1 && i > 1) {
                    temp[j][i] = Tileset.NOTHING;
                } else if (distance <= 5 && j > 1 && i > 1) {
                    temp[j][i] = world[j][i];
                }
            }
        }
        return temp;
    }

    public InteractiveWorld2 startPlaying() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        darknessOn = false;
        char c;
        char[] previousTwoWords = new char[2];
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                if (!darknessOn) { lightWorld = copy(world); }
                darkWorld = darkness(lightWorld, currX, currY);

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
                        if (darknessOn) {
                            world = darkness(lightWorld, currX, currY);
                        }
                        ter.renderFrame(world);
                        break;
                    case 'a':
                        moveLeft(currX, currY);
                        if (darknessOn) {
                            world = darkness(lightWorld, currX, currY);
                        }
                        ter.renderFrame(world);
                        break;
                    case 's':
                        moveDown(currX, currY);
                        if (darknessOn) {
                            world = darkness(lightWorld, currX, currY);
                        }
                        ter.renderFrame(world);
                        break;
                    case 'd':
                        moveRight(currX, currY);
                        if (darknessOn) {
                            world = darkness(lightWorld, currX, currY);
                        }
                        ter.renderFrame(world);
                        break;
                    case 'm':
                        if (darknessOn) { // turn darkness off
                            world = copy(lightWorld);
                            darknessOn = false;
                        }
                        else { // turn darkness on
                            world = copy(darkWorld);
                            darknessOn = true;
                        }
                        ter.renderFrame(world);
                        break;
                }
                if (quit) {

                    String toSave = worldToString(lightWorld);
                    saveToFile(toSave);
                    System.exit(0);
                }

        }
        ter.renderFrame(world);
    }

    }

    private TETile[][] copy(TETile[][] world) {
        TETile[][] copy = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                copy[x][y] = world[x][y];
            }
        }
        return copy;
    }


    private String worldToString(TETile[][] worldArg) {
        String s = "";
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                s = s + worldArg[x][y].character();
            }
            s += "\n";
        }
        return s;
    }

    private void moveUp(int currX, int currY) {
        if (world[currX][currY + 1] == Tileset.CELL) {
            world[currX][currY + 1] =  Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX][currY + 1] =  Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
            this.currY++;
        } else if (world[currX][currY + 1] == Tileset.GRASS) {
            smallworld.play();
            world[currX][currY + 1] =  Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX][currY + 1] =  Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
            this.currY++;
        }
    }

    private void moveDown(int currX, int currY) {
        if (world[currX][currY - 1] == Tileset.CELL) {
            world[currX][currY - 1] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX][currY - 1] = Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
            this.currY--;
        } else if (world[currX][currY - 1] == Tileset.GRASS) {
            smallworld.play();
            world[currX][currY - 1] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX][currY - 1] = Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
            this.currY--;
        }
    }
    private void moveRight(int currX, int currY) {
        if (world[currX + 1][currY] == Tileset.CELL) {
            world[currX + 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX + 1][currY] = Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
            this.currX++;
        } else if (world[currX + 1][currY] == Tileset.GRASS) {
            smallworld.play();
            world[currX + 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX + 1][currY] = Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
            this.currX++;
        }
    }
    private void moveLeft(int currX, int currY) {
        if (world[currX - 1][currY] == Tileset.CELL) {
            world[currX - 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX - 1][currY] = Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
            this.currX--;
        } else if (world[currX - 1][currY] == Tileset.GRASS) {
            smallworld.play();
            world[currX - 1][currY] = Tileset.AVATAR;
            world[currX][currY] = Tileset.CELL;
            lightWorld[currX - 1][currY] = Tileset.AVATAR;
            lightWorld[currX][currY] = Tileset.CELL;
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



