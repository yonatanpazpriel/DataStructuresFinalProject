package core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashMap;
import java.util.Random;

public class WorldTake2 {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;

    private Random random;
    private int[][] world;
    private int size;
    private WeightedQuickUnionUF rooms;

    public WorldTake2(int seed) {
        this.world = new int[HEIGHT][WIDTH];
        this.size = 0;
        this.random = new Random(seed);
        generateRooms(10);
    }

    private int generateX() {
        return random.nextInt(WIDTH-1);
    }
    private int generateHorizontalLengths(int maxSize) {
        return random.nextInt(maxSize);
    }

    private int generateY() {
        return random.nextInt(HEIGHT-1);
    }
    private int generateVerticalLengths(int maxSize) {
        return random.nextInt(maxSize);
    }

    private void markLocation(int x, int y, int mark) {
        if (y <= 1 || x  >= WIDTH - 2 || y >= HEIGHT - 2 || x <= 1) {
            return;
        }
        if (world[y][x] == 0) {
            size++;
            System.out.println("Marking new tile " + mark + " at (" + x + ", " + y + "). Current size: " + size);
            world[y][x] = mark;
        }
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

    private void generateRooms(int amountRooms) {
        int i = 0;
        while (i < amountRooms) {
            int x1 = generateX();
            int x2 = generateX();
            int y1 = generateY();
            int y2 = generateY();
            fillRoom(x1, x2, y1, y2);
            i += 1;
        }
    }

    private void fillRoom(int startX, int startY, int finalX, int finalY) {
        int leftX = Math.min(startX, finalX);
        int rightX = Math.max(startX, finalX);
        int topY = Math.min(startY, finalY);
        int bottomY = Math.max(startY, finalY);
        int countFilled = 0;
        int area = (rightX - leftX) * (bottomY - topY);

        for (int i = leftX; i < rightX; i++) {
            for (int j = topY; j < bottomY; j++) {
                markLocation(i, j, 2);
            }
        }
    }
    public int[][] getWorld() {
        return world;
    }

}
