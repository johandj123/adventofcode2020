import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day24 {
    private Set<Position> black = new HashSet<>();

    public static void main(String[] args) throws IOException {
        new Day24().start();
    }

    private void start() throws IOException {
        List<String> lines = Files.readAllLines(new File("input24.txt").toPath());

        for (String line : lines) {
            findAndFlip(line);
        }
        System.out.println("First: " + black.size());

        for (int i = 0; i < 100; i++) {
            simulate();
        }
        System.out.println("Second: " + black.size());
    }


    private void simulate() {
        Set<Position> active = determineActiveSet();
        Set<Position> originalBlack = new HashSet<>(black);
        for (Position position : active) {
            int blackNeighbours = countBlackNeighbours(originalBlack, position);
            if (originalBlack.contains(position)) {
                if (blackNeighbours == 0 || blackNeighbours > 2) {
                    black.remove(position);
                }
            } else {
                if (blackNeighbours == 2) {
                    black.add(position);
                }
            }
        }
    }

    private int countBlackNeighbours(Set<Position> originalBlack, Position startPosition) {
        return (int) startPosition.neighbours().stream()
                .filter(originalBlack::contains)
                .count();
    }

    private Set<Position> determineActiveSet() {
        Set<Position> active = new HashSet<>(black);
        for (Position position : black) {
            active.addAll(position.neighbours());
        }
        return active;
    }

    private void findAndFlip(String line) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == 'w') {
                x -= 2;
            } else if (c == 'e') {
                x += 2;
            } else if (c == 'n') {
                y -= 4;
                c = line.charAt(++i);
                if (c == 'w') {
                    x--;
                } else if (c == 'e') {
                    x++;
                }
            } else if (c == 's') {
                y += 4;
                c = line.charAt(++i);
                if (c == 'w') {
                    x--;
                } else if (c == 'e') {
                    x++;
                }
            }
        }
        flip(x, y);
    }

    private void flip(int x, int y) {
        Position position = new Position(x, y);
        if (black.contains(position)) {
            black.remove(position);
        } else {
            black.add(position);
        }
    }

    static class Position {
        int x;
        int y;

        public Position() {
        }

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public List<Position> neighbours() {
            List<Position> result = new ArrayList<>();
            result.add(new Position(x - 2, y));
            result.add(new Position(x + 2, y));
            result.add(new Position(x - 1, y - 4));
            result.add(new Position(x + 1, y - 4));
            result.add(new Position(x - 1, y + 4));
            result.add(new Position(x + 1, y + 4));
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
