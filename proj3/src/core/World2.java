package core;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;
import java.time.Instant;
import java.time.Duration;

public class World2 {

    // room obj
    // hallway obj
    // world initializer

    public static final int WIDTH = 47;
    public static final int HEIGHT = 47;
    private static Random random;


    public int[][] world;
    private int previousHallwayDirection;
    private int currentLocation;
    //private WeightedQuickUnionUF nodes;
    private int size;


    public World2(long SEED) {
        this.world = new int[HEIGHT][WIDTH];
        random = new Random(SEED);
        this.size = 0;
        Instant start = Instant.now();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        previousHallwayDirection = 0;
        NextOriginNode nextCoord = new NextOriginNode(random.nextInt(WIDTH/4) + WIDTH/4, random.nextInt(HEIGHT/4) + HEIGHT/4, 0);
        int count = 0;
        while (size < .32*WIDTH*HEIGHT ){//&& size < 0.4 * WIDTH * HEIGHT&& timeElapsed.toMillis()<10000) {
            count++;
            nextCoord = generateRoom(nextCoord);
            nextCoord = generateHallway(nextCoord);
            end = Instant.now();
            timeElapsed = Duration.between(start, end);
        }
        generateRoom(nextCoord);
        eraseDeadEnds();
        createWalls();
        //return world;
    }

    public TETile[][] createWorld() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] worldTiles = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i< WIDTH; i++) {
            for (int y = 0; y < HEIGHT; y++) {
                worldTiles[y][i] = Tileset.NOTHING;
                if (world[y][i] == 1) {
                    worldTiles[y][i] = Tileset.CELL;
                } if (world[y][i] == 2) {
                    worldTiles[y][i] = Tileset.CELL;
                }else if (world[y][i] == 3) {
                    worldTiles[y][i] = Tileset.WALL;
                }
            }
        }
        ter.renderFrame(worldTiles);
        return worldTiles;
    }

    private int generateHorizontalLengths(int maxSize) {
        return random.nextInt(maxSize);
    }

    private int generateVerticalLengths(int maxSize) {
        return random.nextInt(maxSize);
    }

    /*marks that location with a certain type of object.
    Key is as follows:
    0 = blank = Tile.NOTHING
    1 = hallway (same as rooms on the final result, but we will distinguish them for now)
    2 = room
    3 = wall
     */
    private void markLocation(int x, int y, int mark) {
        if (world[y][x] ==0) {
            size+=1;
        };
        world[y][x] = mark;
    }

    private NextOriginNode generateRoom(NextOriginNode node) {
        int roomWidth = generateHorizontalLengths(5) + 2;
        int roomHeight = generateVerticalLengths(5) + 2;
        int x = node.x;
        int y = node.y;
        int direction = node.direction;

        NextOriginNode finalCoordinates = roomGenerationSelector(roomWidth, roomHeight, x, y, direction);
        int finalX = finalCoordinates.x;
        int finalY = finalCoordinates.y;

        int nextXValue = 0;
        int nextYValue = 0;
        int newDirection = generateRandomDirection();
        while (previousHallwayDirection == (newDirection + 2) % 4) {
            newDirection = generateRandomDirection();
        }
        if (finalX != x && finalY != y) {
            if (newDirection == 0) { // up
                nextYValue = Math.min(finalY, y);
                int tempX = Math.min(finalX, x);
                nextXValue = tempX + random.nextInt(roomWidth); ////this is an important line---when we reach the end, we get suck because of this
            } else if (newDirection == 1) { // right
                nextXValue = Math.max(finalX, x);
                int tempY = Math.min(finalY, y);
                nextYValue = tempY + random.nextInt(roomHeight);
            } else if (newDirection == 2) {// down
                nextYValue = Math.max(finalY, y);
                int tempX = Math.min(finalX, x);
                nextXValue = tempX + random.nextInt(roomWidth);
            } else if (newDirection == 3) { // left
                nextXValue = Math.min(finalX, x);
                int tempY = Math.min(finalY, y);
                nextYValue = tempY + random.nextInt(roomHeight);
            }
        }
        if (nextXValue > WIDTH -2 || nextXValue < 1) {
            nextXValue = finalX;

        } if (nextYValue < 1 || nextYValue > WIDTH - 2) {
            nextYValue = finalY;
        }
        return new NextOriginNode(nextXValue, nextYValue, newDirection);
    }

    private NextOriginNode roomGenerationSelector(int roomWidth, int roomHeight, int x, int y, int direction) { ///selects which way to generate the room given direction of the previous hallway
        int rndm = random.nextInt(2);
        if (direction == 1) {
            if (rndm == 1) {
                return generateRoomRightUp(roomWidth, roomHeight, x, y, direction);
            } else {
                return generateRoomRightDown(roomWidth, roomHeight, x, y, direction);
            }
        } else if (direction == 3) {
            if (rndm == 1) {
                return generateRoomLeftUp(roomWidth, roomHeight, x, y, direction);
            } else {
                return generateRoomLeftDown(roomWidth, roomHeight, x, y, direction);
            }
        } else if(direction == 0) {
            if (rndm == 1) {
                return generateRoomRightUp(roomWidth, roomHeight, x, y, direction);
            } else {
                return generateRoomLeftUp(roomWidth, roomHeight, x, y, direction);
            }
        } else {
            if (rndm == 1) {
                return generateRoomRightDown(roomWidth, roomHeight, x, y, direction);
            } else {
                return generateRoomLeftDown(roomWidth, roomHeight, x, y, direction);
            }
        }
    }

    private NextOriginNode generateRoomRightDown(int roomWidth, int roomHeight, int x, int y, int direction){
        int tally = 0;
        int currX = x;
        int currY = y;
        boolean breaker = false;
        for (int i = 0; i <= roomWidth; i++) {
            if (breaker) { break; }

            for (int j = 0; j <= roomHeight; j++) {
                if (convertY(y + j) >= HEIGHT - 2|| x + i >= WIDTH - 2 || y + j < 2 || x + i < 2) {
                    breaker = true;
                    break;
                }

                if(world[y+j][x+i] != 0) {
                    tally += 1;
                }

                markLocation(x + i, convertY(y + j), 2);
                currX = x+i;
                currY = y+j;
                /*
                if (tally >= .1 * roomWidth*roomHeight) {
                    break;
                }

                 */
            }
        }

        return new NextOriginNode(currX, currY, direction);
    }
    private NextOriginNode generateRoomRightUp (int roomWidth, int roomHeight, int x, int y, int direction) {
        int tally = 0;
        int currX = x;
        int currY = y;
        boolean breaker = false;
        for (int i = 0; i <= roomWidth; i++) {
            if (breaker) { break; }
            for (int j = 0; j <= roomHeight; j++) {
                if (y-j <= 1 || x + i >= WIDTH - 2||y - j >=HEIGHT-2 || x + i <= 2) {
                    break;
                }
                if(world[y-j][x+i] != 0) {
                    tally += 1;
                }
                markLocation(x + i, convertY(y - j), 2);
                currX = x+i;
                currY = y-j;

                if (tally >= .1 * roomWidth*roomHeight) {
                    break;
                }


            }
        }

        return new NextOriginNode(currX, currY, direction);
    }
    private NextOriginNode generateRoomLeftDown(int roomWidth, int roomHeight, int x, int y, int direction) {
        int tally = 0;
        int currX = x;
        int currY = y;
        for (int i = 0; i <= roomWidth; i++) {
            for (int j = 0; j <= roomHeight; j++) {
                if (convertY(y + j) >= HEIGHT - 2 || x - i <= 2 ||x>=WIDTH - 2||y + j >= HEIGHT-2) {
                    break;
                }
                if(world[y+j][x-i] != 0) {
                    tally += 1;
                }
                markLocation(x - i, convertY(y + j), 2);
                currX = x-i;
                currY = y+j;

                if (tally >= .1 * roomWidth*roomHeight) {
                    break;
                }


            }
        }


        return new NextOriginNode(currX, currY, direction);
    }
    private NextOriginNode generateRoomLeftUp(int roomWidth, int roomHeight, int x, int y, int direction) {
        int currX = x;
        int currY = y;
        int tally = 0;
        for (int i = 0; i <= roomWidth; i++) {
            if (x - i <= 1||x - i >=WIDTH-2) break; // Check for out-of-bounds in the outer loop
            for (int j = 0; j <= roomHeight; j++) {
                if (y - j <= 2 || y >= HEIGHT - 2) break; // Check for out-of-bounds in the inner loop
                int newX = x - i;
                int newY = convertY(y - j);
                if(world[newY][newX] != 0) {
                    tally ++;
                }
                markLocation(newX, newY, 2);
                currX = newX;
                currY = newY;

                if (tally >= .1 * roomWidth*roomHeight) {
                    break;
                }


            }
        }

        return new NextOriginNode(currX, currY, direction);
    }

    // need to pick (x,y) from room - assume direction, x,y passed into genHallways is valid
    private NextOriginNode generateHallway(NextOriginNode node) {
        int x = node.x;
        int y = node.y;
        int direction = node.direction;

        if (direction == 0 || direction == 2) { // up or down
            return generateVerticalHallway(x, y, direction);
        } else { // left or right
            return generateHorizontalHallway(x, y, direction);
        }


    }
    ///creates hallways along the height of the world!
    private NextOriginNode generateVerticalHallway(int x, int y, int direction){
        ///generates hallway toward 'direction' starting at (x,y)
        int currHeight = generateVerticalLengths(11) + 2;
        return generateVerticalHallwayHelper(y, currHeight, x, direction);
    }

    private NextOriginNode generateVerticalHallwayHelper(int currY, int lengthLeft, int x, int direction) {
        // Ensure we don't go out of bounds

        int originalY = currY;
        while (lengthLeft > 0 && x >= 1 && x <= WIDTH-2) {
            lengthLeft--;
            if (direction == 0 && currY > 1) { // Moving up
                currY--;
                markLocation(x, currY, 1);

            } else if (direction == 2 && currY < HEIGHT-2) { // Moving down
                currY++;
                markLocation(x, currY, 1);
            } else {
                break;
            }
        }
        if (originalY != currY) {
            previousHallwayDirection = direction;
        }
        return new NextOriginNode(x, currY, direction);
    }

    ///creates hallways along the width of the world!
    private NextOriginNode generateHorizontalHallway(int x, int y, int direction) {
        ///generates hallway toward 'direction' starting at (x,y)
        int currLength = generateHorizontalLengths(11) + 2;
        return generateHorizontalHallwayHelper(x, currLength, y, direction);
    }

    private NextOriginNode generateHorizontalHallwayHelper(int currX, int lengthLeft, int y, int direction) {
        int originalX = currX;
        while (lengthLeft > 0 && y >= 1 && y <= WIDTH-2) {
            lengthLeft--;
            if (direction == 1 && currX < WIDTH-2) { // Moving right
                currX++;
                markLocation(currX, y, 1);

            } else if (direction == 3 && currX > 1) { // Moving left
                currX--;
                markLocation(currX, y, 1);
            } else {
                break;
            }
        }
        if (originalX != currX) {
            previousHallwayDirection = direction;
        }
        return new NextOriginNode(currX, y, direction);
    }

    ///converts standard coordinates to SPEC coordinates
    private int convertY(int y) {
        return y;
    }

    public int[][] getWorld() {
        return world;
    }

    /* returns random direction for hallway generation:
     0 = up
     1 = right
     2 = down
     3 = left
     */
    private int generateRandomDirection() {
        return random.nextInt(4);
    }

    private void createWalls() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[j][i] == 1 || world[j][i] == 2) { // pick floor tiles
                    wallHelper(i, j); // surround floor tiles with walls
                }
            }
        }
    }

    private void wallHelper(int i, int j) {

        if (j + 1 < HEIGHT && world[j + 1][i] == 0) { // right
            world[j + 1][i] = 3;
        }
        if (i - 1 >= 0 && world[j][i - 1] == 0) { //  bottom
            world[j][i - 1] = 3;
        }
        if (j - 1 >= 0 && world[j - 1][i] == 0) { // left
            world[j - 1][i] = 3;
        }
        if (i + 1 < WIDTH && world[j][i + 1] == 0) { // top
            world[j][i + 1] = 3;
        }
    }



    /*
    public int[][] createWorld(){
        Instant start = Instant.now();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        NextOriginNode nextCoord = new NextOriginNode(random.nextInt(WIDTH/4) + WIDTH/4, random.nextInt(HEIGHT/4) + HEIGHT/4, 0);
        int count = 0;
        while (count < 100 ){//&& size < 0.4 * WIDTH * HEIGHT&& timeElapsed.toMillis()<10000) {
            count++;
            nextCoord = generateRoom(nextCoord);
            nextCoord = generateHallway(nextCoord);
            end = Instant.now();
            timeElapsed = Duration.between(start, end);
        }
        createWalls();
        return world;
    }
    \
     */




    private class NextOriginNode {
        int x;
        int y;
        public int direction;

        // Use to start next room/hallway
        public NextOriginNode(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }
    private void eraseDeadEnds() {
        boolean anyDeadEnds = true;
        while (anyDeadEnds) {
            anyDeadEnds = false;
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    if (world[j][i] != 0 && deadEndChecker(i, j)) {
                        world[j][i] = 0;
                        anyDeadEnds = true;
                    }
                }
            }
        }
    }

    private boolean deadEndChecker(int i, int j) {
        int neighbors = 0;
        if (j + 1 <= HEIGHT && world[j + 1][i] != 0) { // right
            neighbors++;
        }
        if (i - 1 >= 0 && world[j][i - 1] != 0) { //  bottom
            neighbors++;
        }
        if (j - 1 >= 0 && world[j - 1][i] != 0) { // left
            neighbors++;
        }
        if (i + 1 < WIDTH && world[j][i + 1] != 0) { // top
            neighbors++;
        }
        return neighbors < 2;
    }
}