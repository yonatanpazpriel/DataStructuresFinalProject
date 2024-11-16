package core;
import java.sql.Time;
import java.util.HashMap;
import java.util.Random;
import java.time.Instant;
import java.time.Duration;

public class World {

    // room obj
    // hallway obj
    // world initializer

    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;
    private static final Random random = new Random();


    private int[][] world;
    private int size;
    private int topRightQuad;
    private int topLeftQuad;
    private int bottomRightQuad;
    private int bottomLeftQuad;




    public World() {
        this.world = new int[HEIGHT][WIDTH];
        this.size = 0;
        //createWorld();
    }

    ///generates random X coordinate or random X length
    private int generateX() {
        return random.nextInt(WIDTH-1);
    }
    private int generateHorizontalLengths(int maxSize) {
        return random.nextInt(maxSize);
    }

    //generates random Y coordinate or random Y length
    private int generateY() {
        return random.nextInt(HEIGHT-1);
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
        if (y <= 1 || x  >= WIDTH - 2 || y >= HEIGHT - 2 || x <= 1) {
            return;
        }
        if (world[x][y] == 0) {
            size++;
            System.out.println("Marking new tile " + mark + " at (" + x + ", " + y + "). Current size: " + size);
            world[x][y] = mark;
        }
    }


    private NextOriginNode generateRoom(NextOriginNode node) {
        int roomWidth = generateHorizontalLengths(12) + 2;
        int roomHeight = generateVerticalLengths(12) + 2;
        int x = node.x;
        int y = node.y;
        int direction = node.direction;

        NextOriginNode finalCoordinates = roomGenerationSelector(roomWidth, roomHeight, x, y, direction);
        int finalX = finalCoordinates.x;
        int finalY = finalCoordinates.y;

        int nextXValue = 0;
        int nextYValue = 0;
        int newDirection = generateRandomDirection();

        if (newDirection == 0) { // up
            nextYValue = Math.min(finalY, y);
            int tempX = Math.min(finalX, x);
            nextXValue = tempX + random.nextInt(roomWidth);
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
        for (int i = 0; i < roomWidth; i++) {
            if (breaker) {
                break;
            }
            for (int j = 0; j < roomHeight; j++) {
                if (convertY(y + j) >= HEIGHT - 2|| x + i >= WIDTH - 1 || x + i <= 1 || y + j <= 1) {
                    System.out.println("RRD");
                    breaker = true;
                    break;
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
        for (int i = 0; i < roomWidth; i++) {
            if (breaker) {
                break;
            }
            for (int j = 0; j < roomHeight; j++) {
                if (convertY(y - j) >= HEIGHT - 2|| x + i >= WIDTH - 1 || x + i <= 1 || y - j <= 1) {
                    System.out.println("RRU");
                    breaker = true;
                    break;
                }
                if(world[y-j][x+i] != 0) {
                    tally += 1;
                }
                markLocation(x + i, convertY(y - j), 2);
                currX = x+i;
                currY = y-j;
                /*
                if (tally >= .1 * roomWidth*roomHeight) {
                    break;
                }

                 */
            }
        }

        return new NextOriginNode(currX, currY, direction);
    }
    private NextOriginNode generateRoomLeftDown(int roomWidth, int roomHeight, int x, int y, int direction) {
        int tally = 0;
        int currX = x;
        int currY = y;
        boolean breaker = false;
        for (int i = 0; i < roomWidth; i++) {
            if (breaker) {
                break;
            }
            for (int j = 0; j < roomHeight; j++) {
                if (convertY(y + j) >= HEIGHT - 2|| x - i >= WIDTH - 1 || x - i <= 1 || y + j <= 1) {
                    System.out.println("RLD");
                    breaker = true;
                    break;
                }

                if(world[y+j][x-i] != 0) {
                    tally += 1;
                }
                markLocation(x - i, convertY(y + j), 2);
                currX = x-i;
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
    private NextOriginNode generateRoomLeftUp(int roomWidth, int roomHeight, int x, int y, int direction) {
        int currX = x;
        int currY = y;
        int tally = 0;
        boolean breaker = false;
        for (int i = 0; i < roomWidth; i++) {
            if (breaker) {
                break;
            }
            if (x - i <= 1||x>=WIDTH-2) break; // Check for out-of-bounds in the outer loop
            for (int j = 0; j < roomHeight; j++) {
                if (convertY(y - j) >= HEIGHT - 2|| x - i >= WIDTH - 1 || x - i <= 1 || y - j <= 1) {
                    System.out.println("RLU");
                    breaker = true;
                    break; // Check for out-of-bounds in the inner loop
                }
                int newX = x - i;
                int newY = convertY(y - j);
                if(world[x][y] != 0) {
                    tally += 1;
                }
                markLocation(newX, newY, 2);
                currX = newX;
                currY = newY;
                /*
                if (tally >= .1 * roomWidth*roomHeight) {
                    break;
                }

                 */
            }
        }
        return new NextOriginNode(currX, currY, direction);
    }
/*
    private int fixHeight(int roomHeight, int start, int direction) {
        if (direction == 0) {
            while (start + roomHeight > HEIGHT - 2) {
                roomHeight--;
            }
        }

    }
    */

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
        /// generates hallway toward 'direction' starting at (x,y)
        int currHeight = generateVerticalLengths(25);
        return generateVerticalHallwayHelper(y, currHeight, x, direction);
    }

    private NextOriginNode generateVerticalHallwayHelper(int currY, int lengthLeft, int x, int direction) {
        // Ensure we don't go out of bounds
        if (currY <= 2 || currY >= HEIGHT - 2 || x <= 1 || x >= WIDTH - 2) {
            return new NextOriginNode(x, currY, direction);
        }

        // If no length is left, stop recursion
        if (lengthLeft == 0 ) {
            return new NextOriginNode(x, currY, direction);
        }

        // Mark the current location as hallway
        markLocation(x, convertY(currY), 1);

        // Recurse based on direction
        if (direction == 0) { // Moving up
            return generateVerticalHallwayHelper(currY - 1, lengthLeft - 1, x, direction);
        } else if (direction == 2) { // Moving down
            return generateVerticalHallwayHelper(currY + 1, lengthLeft - 1, x, direction);
        }

        // Return the final position
        return new NextOriginNode(x, currY, direction);
    }

    ///creates hallways along the width of the world!
    private NextOriginNode generateHorizontalHallway(int x, int y, int direction) {
        ///generates hallway toward 'direction' starting at (x,y)
        int currLength = generateHorizontalLengths(25);
        return generateHorizontalHallwayHelper(x, currLength, y, direction);
    }

    private NextOriginNode generateHorizontalHallwayHelper(int currX, int lengthLeft, int y, int direction) {
        // Ensure we don't go out of bounds
        if (currX <= 2 || currX >= WIDTH - 2 || y <= 2 || y >= HEIGHT - 2) {
            return new NextOriginNode(currX, y, direction);
        }

        // If no length is left or we encounter a marked tile, stop recursion
        if (lengthLeft == 0 ) {
            return new NextOriginNode(currX, y, direction);
        }

        // Mark the current location as hallway
        markLocation(currX, convertY(y), 1);

        // Recurse based on direction
        if (direction == 1) { // Moving right
            return generateHorizontalHallwayHelper(currX + 1, lengthLeft - 1, y, direction);
        } else if (direction == 3) { // Moving left
            return generateHorizontalHallwayHelper(currX - 1, lengthLeft - 1, y, direction);
        }

        // Return the final position
        return new NextOriginNode(currX, y, direction);
    }


    ///converts standard coordinates to SPEC coordinates
    private int convertY(int y) {
        return y;
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
        if (j + 1 <= HEIGHT && world[j + 1][i] == 0) { // right
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

    ///callsfillRooms first
    public int[][] createWorld() {
        Instant start = Instant.now();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        NextOriginNode nextCoord = new NextOriginNode(random.nextInt(WIDTH/4) + WIDTH/4, random.nextInt(HEIGHT/4) + HEIGHT/4, 0);
        int bonusHallwayChance = 1;
        int roomsCreated = 0;
        int hallsCreated = 0;
        int totalThings = 0;
        int count = 0;
        double percent = (double) size / (WIDTH * HEIGHT);
        while (count < 100 && size < 0.25 * WIDTH * HEIGHT) {//(percent < 0.35 && timeElapsed.toSeconds()<1 && totalThings < 50000) {
            count++;
            System.out.println(count);
            if (bonusHallwayChance == 2) {
                int currDirection = nextCoord.direction + 1;
                //nextCoord = generateHallway(new NextOriginNode(nextCoord.x, nextCoord.y, currDirection % 4));
                hallsCreated++;
            }
            nextCoord = generateRoom(nextCoord);
            roomsCreated++;
            nextCoord = generateHallway(nextCoord);
            hallsCreated++;

            bonusHallwayChance = random.nextInt(4);
            end = Instant.now();
            timeElapsed = Duration.between(start, end);
            totalThings = hallsCreated + roomsCreated;
            percent = (double) size / (WIDTH * HEIGHT);
        }

        if (totalThings >= 500) {
            System.out.println("stopped because of total things count: " + totalThings);
            System.out.println("size was: " +  percent);
            System.out.println("# hallways: " +  hallsCreated);
            System.out.println("# rooms: " +  roomsCreated);
        }

        if ((double) size / (WIDTH * HEIGHT) < 0.35) {
            System.out.println("didn't stop because of percent, time was: " + timeElapsed.toMillis() / 1000);
            System.out.println("size was: " +  (double) size / (WIDTH * HEIGHT));
            System.out.println("# hallways: " +  hallsCreated);
            System.out.println("# rooms: " +  roomsCreated);
        }
        if (timeElapsed.toSeconds()<5) {
            System.out.println("didn't stop because of time: " + (double) size / (WIDTH * HEIGHT));
            System.out.println("time was: " + timeElapsed.toMillis() / 1000);
            System.out.println("# hallways: " +  hallsCreated);
            System.out.println("# rooms: " +  roomsCreated);
        }
        createWalls();
        return world;
    }


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
}