package core;

import tileengine.TETile;
import tileengine.Tileset;

import static java.lang.Character.isDigit;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {

        char[] inputArray = input.toCharArray();
        String seed = "";
        int i = 1;
        if (inputArray[0] == "n".toCharArray()[0] || inputArray[0] == "N".toCharArray()[0]) {
            i = 1;
            while (isDigit(inputArray[i]) && inputArray[i] != "s".toCharArray()[0]) {
                if (isDigit(inputArray[i])) {
                    seed += inputArray[i];
                }
                i++;
            }
        }

        TETile[][] world = new World2(Long.parseLong(seed)).createWorld();
        InteractiveWorld2 iw2 = new InteractiveWorld2(world);
        if (inputArray[i] == "w".toCharArray()[0]) {
            int x = 0;
        }

        iw2.startPlaying();
        while (i < inputArray.length) {
            i++;
        }
        return world;
    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
