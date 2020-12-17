import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day17 {
    public static void main(String[] args) throws IOException {
        new Day17().start();
    }

    private void start() throws IOException {
        first();
        second();
    }

    private void first() throws IOException {
        Set<Position> currentGrid = readInput();
        for (int i = 0; i < 6; i++) {
            currentGrid = evolve(currentGrid);
        }
        System.out.println("First: " + currentGrid.size());
    }

    private void second() throws IOException {
        Set<Position> currentGrid = readInput();
        for (int i = 0; i < 6; i++) {
            currentGrid = evolve4d(currentGrid);
        }
        System.out.println("Second: " + currentGrid.size());
    }

    private Set<Position> readInput() throws IOException {
        Set<Position> result = new HashSet<>();
        List<String> lines = Files.readAllLines(new File("input17.txt").toPath());
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    result.add(new Position(x, y, 0));
                }
            }
        }
        return result;
    }

    private Set<Position> evolve(Set<Position> grid) {
        Position min = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Position max = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Position position : grid) {
            min = Position.min(min, position);
            max = Position.max(max, position);
        }
        Set<Position> result = new HashSet<>();
        for (int z = min.z - 1; z <= max.z + 1; z++) {
            for (int y = min.y - 1; y <= max.y + 1; y++) {
                for (int x = min.x - 1; x <= max.x + 1; x++) {
                    Position position = new Position(x, y, z);
                    int neighbours = countNeighbours(grid, position);
                    if (grid.contains(position)) {
                        if (neighbours == 2 || neighbours == 3) {
                            result.add(position);
                        }
                    } else {
                        if (neighbours == 3) {
                            result.add(position);
                        }
                    }
                }
            }
        }
        return result;
    }

    private int countNeighbours(Set<Position> grid, Position position) {
        int result = 0;
        for (int dz = -1; dz <= 1; dz++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    if (dx != 0 || dy != 0 || dz != 0) {
                        Position target = new Position(position.x + dx, position.y + dy, position.z + dz);
                        if (grid.contains(target)) {
                            result++;
                        }
                    }
                }
            }
        }
        return result;
    }

    private Set<Position> evolve4d(Set<Position> grid) {
        Position min = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Position max = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Position position : grid) {
            min = Position.min(min, position);
            max = Position.max(max, position);
        }
        Set<Position> result = new HashSet<>();
        for (int w = min.w - 1; w <= max.w + 1; w++) {
            for (int z = min.z - 1; z <= max.z + 1; z++) {
                for (int y = min.y - 1; y <= max.y + 1; y++) {
                    for (int x = min.x - 1; x <= max.x + 1; x++) {
                        Position position = new Position(x, y, z, w);
                        int neighbours = countNeighbours4d(grid, position);
                        if (grid.contains(position)) {
                            if (neighbours == 2 || neighbours == 3) {
                                result.add(position);
                            }
                        } else {
                            if (neighbours == 3) {
                                result.add(position);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private int countNeighbours4d(Set<Position> grid, Position position) {
        int result = 0;
        for (int dw = -1; dw <= 1; dw++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx != 0 || dy != 0 || dz != 0 || dw != 0) {
                            Position target = new Position(position.x + dx, position.y + dy, position.z + dz, position.w + dw);
                            if (grid.contains(target)) {
                                result++;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    static class Position {
        final int x;
        final int y;
        final int z;
        final int w;

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = 0;
        }

        public Position(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y &&
                    z == position.z &&
                    w == position.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, w);
        }

        @Override
        public String toString() {
            return String.format("(%d;%d;%d;%d)", x, y, z, w);
        }

        public static Position max(Position a, Position b) {
            return new Position(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z), Math.max(a.w, b.w));
        }

        public static Position min(Position a, Position b) {
            return new Position(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z), Math.min(a.w, b.w));
        }
    }
}
