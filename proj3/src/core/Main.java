package core;

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

        // build your own world!
        World x = new World();
        int [][] yippee = x.getWorld();
        print2DArray(yippee);

    }
}
