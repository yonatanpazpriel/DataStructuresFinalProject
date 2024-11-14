package core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Random;

public class World {
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
        generateHallways(10, 10);
    }

    ///generates random X coordinate or random X length
    private int generateX() {
        return random.nextInt(WIDTH);
    }
    private int generateHorizontalLengths() {
        return random.nextInt(20);
    }

    //generates random Y coordinate or random Y length
    private int generateY() {
        return random.nextInt(HEIGHT);
    }

    private int generateVerticalLengths() {
        return random.nextInt(20);
    }



    /*marks that location with a certain type of object.
    Key is as follows:
    0 = blank = Tile.NOTHING
    1 = hallway (same as rooms on the final result but we will distinguish them for now)
    2 = room
    3 = wall

     */
    private void markLocation(int x, int y, int mark) {
        world[y][x] = mark;
    }

    private void generateHallways(int h, int v) {
        generateHorizontalHallways(h);
        generateVerticalHallways(v);
    }
    ///creates hallways along the height of the world!
    private void generateVerticalHallways(int amount){
        for (int i = 0; i < amount; i++)  { ///generates AMOUNT hallways
            int currY = generateY();
            int currHeight = generateVerticalLengths();
            int currX = generateX();
            generateVerticalHallwayHelper(currY, currHeight, currX);
        }
    }

    private void generateVerticalHallwayHelper(int currY, int lengthLeft, int x) {
        if (currY >= HEIGHT - 1 || currY < 0 || x >= WIDTH || x < 0||(x==0||x==WIDTH-1)) {
            return;
        } else if (lengthLeft == 0 || world[convertY(currY)][x] == 1) {
            return;
        } else {
            markLocation(x,convertY(currY), 1);
            generateVerticalHallwayHelper(currY + 1, lengthLeft - 1, x);
        }
    }
    ///creates hallways along the width of the world!
    private void generateHorizontalHallways(int amount) {
        for (int i = 0; i < amount; i++) { ///generates AMOUNT hallways
            int currX = generateX();
            int currLength = generateHorizontalLengths();
            int currY = generateY();
            generateHorizontalHallwayHelper(currX, currLength, currY);
        }
    }

    private void generateHorizontalHallwayHelper(int currX, int lengthLeft, int y) {
        if (y >= HEIGHT || y < 0 || currX >= WIDTH - 1 || currX < 0||(convertY(y)==0||convertY(y)==HEIGHT-1)) {
            return;
        } else if (lengthLeft == 0 || world[convertY(y)][currX] == 1) {
            return;
        } else {
            markLocation(currX, convertY(y), 1);
            generateHorizontalHallwayHelper(currX + 1, lengthLeft - 1, y);
        }
    }
    ///converts standard coordinates to SPEC coordinates
    private int convertY(int y) {
        return HEIGHT-1-y;
    }


    //recursively calls fillRooms, unknown name recursion
    private void fillHallways(int length) {
        ///single for loop to fill that much space with floor tiles
    }

    ///recursively calls fillHallways, unknown name recursion
    private void fillRooms(int width, int height) {
        //double for loop to fill that much space with floor tiles
    }

    public int[][] getWorld() {
        return world;
    }

    ///callsfillRooms first
    private void createWorld(){

    }
}
