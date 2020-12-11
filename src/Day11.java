import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.BiFunction;

public class Day11 {
    private char state[][];
    private int width;
    private int height;

    public static void main(String[] args) throws IOException {
        System.out.println("First: " + new Day11().first());
        System.out.println("Second: " + new Day11().second());
    }

    private int first() throws IOException {
        return start(this::adjacentOccupied, 4);
    }

    private int second() throws IOException {
        return start(this::adjacentOccupiedLine, 5);
    }

    private int start(BiFunction<Integer, Integer, Integer> adjacentOccupiedFunction, int adjacentLowerBound) throws IOException {
        readInput();
        boolean updated = true;
        while (updated) {
            updated = performUpdate(adjacentOccupiedFunction, adjacentLowerBound);
        }
        return countOccupied();
    }

    private boolean performUpdate(BiFunction<Integer, Integer, Integer> adjacentOccupiedFunction, int adjacentLowerBound) {
        boolean updated = false;
        char[][] nextstate = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (state[y][x] == 'L' && adjacentOccupiedFunction.apply(x, y) == 0) {
                    nextstate[y][x] = '#';
                    updated = true;
                } else if (state[y][x] == '#' && adjacentOccupiedFunction.apply(x, y) >= adjacentLowerBound) {
                    nextstate[y][x] = 'L';
                    updated = true;
                } else {
                    nextstate[y][x] = state[y][x];
                }
            }
        }
        state = nextstate;
        return updated;
    }

    private int adjacentOccupied(int x, int y) {
        int result = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if ((dx != 0 || dy != 0) && get(x + dx, y + dy) == '#') {
                    result++;
                }
            }
        }
        return result;
    }

    private int adjacentOccupiedLine(int x, int y) {
        int result = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if ((dx != 0 || dy != 0) && adjacentOccupiedLine(x, y, dx, dy)) {
                    result++;
                }
            }
        }
        return result;
    }

    private boolean adjacentOccupiedLine(int x, int y, int dx, int dy) {
        while (true) {
            x += dx;
            y += dy;
            char c = get(x, y);
            if (c == '#') return true;
            if (c == 'L' || c == '\0') return false;
        }
    }

    private int countOccupied() {
        int result = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (state[y][x] == '#') {
                    result++;
                }
            }
        }
        return result;
    }

    private void readInput() throws IOException {
        List<String> input = Files.readAllLines(new File("input11.txt").toPath());
        width = input.get(0).length();
        height = input.size();
        state = new char[height][width];
        for (int y = 0; y < height; y++) {
            String s = input.get(y);
            for (int x = 0; x < width; x++) {
                state[y][x] = s.charAt(x);
            }
        }
    }

    private char get(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return '\0';
        }
        return state[y][x];
    }
}
