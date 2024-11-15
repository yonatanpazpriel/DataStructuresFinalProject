package core;
import java.util.Random;

public class World {

    // room obj
    // hallway obj
    // world initializer

    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private static final Random random = new Random();

    private int[][] world;
    private int currentLocation;
    //private WeightedQuickUnionUF nodes;
    private int size;


    public World () {
        this.world = new int[HEIGHT][WIDTH];
        //this.nodes = nodes;
        this.size = 0;
        createWorld();
    }

    ///generates random X coordinate or random X length
    private int generateX() {
        return random.nextInt(WIDTH);
    }
    private int generateHorizontalLengths(int maxSize) {
        return random.nextInt(maxSize);
    }

    //generates random Y coordinate or random Y length
    private int generateY() {
        return random.nextInt(HEIGHT);
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
        //size += 1;
    }

    private int countMarkedLocs() {
        int count = 0;
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private NextOriginNode generateRoom(NextOriginNode node) {
        int roomWidth = generateHorizontalLengths(8) + 1;
        int roomHeight = generateVerticalLengths(8) + 1;
        int x = node.x;
        int y = node.y;
        int direction = node.direction;

        for (int i = 0; i <= roomWidth; i++) {
            for (int j = 0; j <= roomHeight; j++) {
                if (convertY(y + j) >= HEIGHT - 1 || x + i >= WIDTH - 1) {
                    break;
                }
                markLocation(x + i, convertY(y + j), 2);
            }
        }

        int nextXValue = 0;
        int nextYValue = 0;
        int newDirection = generateRandomDirection();

        if (newDirection == 0) { // up
            nextXValue = random.nextInt(roomWidth) + x;
            nextYValue = y + roomHeight+1;
        } else if (newDirection == 1) { // right
            nextXValue = x + roomWidth+1;
            nextYValue = random.nextInt(roomHeight) + y;
        } else if (newDirection == 2) {// down
            nextXValue = random.nextInt(roomWidth) + x;
            nextYValue = y-1;
        } else if (newDirection == 3) { // left
            nextXValue = x-1;
            nextYValue = random.nextInt(roomHeight) + y;
        }
        return new NextOriginNode(nextXValue, nextYValue, newDirection);
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
        int currHeight = generateVerticalLengths(20);
        return generateVerticalHallwayHelper(y, currHeight, x, direction);
    }

    private NextOriginNode generateVerticalHallwayHelper(int currY, int lengthLeft, int x, int direction) {
        if (currY >= HEIGHT - 1 || currY < 0 || x >= WIDTH || x < 0||(x==0||x==WIDTH-1)) {
            return new NextOriginNode(x, currY, direction);
        } else if (lengthLeft == 0 || world[convertY(currY)][x] == 1) {
            return new NextOriginNode(x, currY, direction);
        } else if (direction == 0) {
            markLocation(x,convertY(currY), 1);
            generateVerticalHallwayHelper(currY - 1, lengthLeft - 1, x, direction);
        } else if (direction == 2) {
            markLocation(x,convertY(currY), 1);
            generateVerticalHallwayHelper(currY + 1, lengthLeft - 1, x, direction);
        }
        return new NextOriginNode(x, currY, direction);
    }

    ///creates hallways along the width of the world!
    private NextOriginNode generateHorizontalHallway(int x, int y, int direction) {
         ///generates hallway toward 'direction' starting at (x,y)
        int currLength = generateHorizontalLengths(20);
        return generateHorizontalHallwayHelper(x, currLength, y, direction);
    }

    private NextOriginNode generateHorizontalHallwayHelper(int currX, int lengthLeft, int y, int direction) {
        if (y >= HEIGHT || y < 0 || currX >= WIDTH - 1 || currX < 0||(convertY(y)==0||convertY(y)==HEIGHT-1)) {
            return new NextOriginNode(currX, y, direction);
        } else if (lengthLeft == 0 || world[convertY(y)][currX] == 1) {
            return new NextOriginNode(currX, y, direction);
        } else if (direction == 1) { // right
            markLocation(currX, convertY(y), 1);
            generateHorizontalHallwayHelper(currX + 1, lengthLeft - 1, y, direction);
        } else if (direction == 3) { // left
            markLocation(currX, convertY(y), 1);
            generateHorizontalHallwayHelper(currX - 1, lengthLeft - 1, y, direction);
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

    ///callsfillRooms first
    public int[][] createWorld(){
        NextOriginNode nextCoord = new NextOriginNode(random.nextInt(WIDTH), random.nextInt(HEIGHT), 0);
        while (size < 0.4 * WIDTH * HEIGHT) {
            nextCoord = generateRoom(nextCoord);
            nextCoord = generateHallway(nextCoord);
        }
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
