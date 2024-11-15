private void createWalls() {
    for (int i = 0; i < WIDTH; i++) {
        for (int j = 0; j < HEIGHT; j++) {
            if (world[j][i] != 0) { // pick floor tiles
                wallHelper(i, j); // surround floor tiles with walls
            }
        }
    }
}

private void wallHelper(int i, int j) {
    if (i + 1 < WIDTH && world[j][i + 1] == 0) { // right edge: index valid, square is empty
        world[j][i + 1] = 3;
    }
    if (i - 1 >= 0 && world[j][i - 1] == 0) { // left edge
        world[j][i - 1] = 3;
    }
    if (j - 1 >= 0 && world[j - 1][i] == 0) { // top edge
        world[j - 1][i] = 3;
    }
    if (j + 1 >= HEIGHT && world[j - 1][i] == 0) { // bottom edge
        world[j - 1][i] = 3;
    }
}