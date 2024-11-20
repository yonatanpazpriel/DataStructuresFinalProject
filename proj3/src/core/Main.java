package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import static core.World2.HEIGHT;
import static core.World2.WIDTH;


public class Main {
    public static void print2DArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {          // Iterate over rows
            for (int j = 0; j < array[i].length; j++) {    // Iterate over columns in each row
                System.out.print(array[i][j] + "\t");      // Print each element with a tab space
            }
            System.out.println();                          // Move to the next line after each row
        }
    }
    public static void main(String[] args) {


        /*
        Random random = new Random();
        long SEED = 7647956535394261435l;

        Character nlq = InteractiveMenu.buildAndAcceptNLQ();

        // build your own world!
        World2 x = new World2(SEED);
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

         */

        Frame frame = new Frame("AWT Example");
        Button button = new Button("Click Me");

        button.addActionListener(e -> System.out.println("Button clicked!"));

        frame.add(button); // Add button to the frame
        frame.setSize(300, 200); // Set frame size
        frame.setLayout(new FlowLayout()); // Set layout manager
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
         });

    }

}