private void eraseDeadEnds() {
    boolean anyDeadEnds = true;
    while (anyDeadEnds) {
        anyDeadEnds = false;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[j][i] != 0 && deadEndChecker(i, j)) {
                    world[j][i] == 0;
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