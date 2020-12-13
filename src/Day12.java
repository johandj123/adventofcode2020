import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<Move> input = Files.readAllLines(new File("input12.txt").toPath())
                .stream()
                .map(Move::new)
                .collect(Collectors.toList());

        first(input);
        second(input);
    }

    private static void first(List<Move> input) {
        int x = 0;
        int y = 0;
        int dx = 1;
        int dy = 0;
        for (Move move : input) {
            switch (move.action) {
                case 'N':
                    y += move.value;
                    break;
                case 'S':
                    y -= move.value;
                    break;
                case 'E':
                    x += move.value;
                    break;
                case 'W':
                    x -= move.value;
                    break;
                case 'L':
                    if ((move.value % 90) == 0) {
                        for (int i = 0; i < move.value / 90; i++) {
                            int dx2 = -dy;
                            int dy2 = dx;
                            dx = dx2;
                            dy = dy2;
                        }
                    } else {
                        System.err.println("L" + move.value);
                    }
                    break;
                case 'R':
                    if ((move.value % 90) == 0) {
                        for (int i = 0; i < move.value / 90; i++) {
                            int dx2 = dy;
                            int dy2 = -dx;
                            dx = dx2;
                            dy = dy2;
                        }
                    } else {
                        System.err.println("R" + move.value);
                    }
                    break;
                case 'F':
                    x += (move.value * dx);
                    y += (move.value * dy);
                    break;
                default:
                    System.err.println(move.action + "" + move.value);
                    break;
            }
//            System.out.println(move.action + "" + move.value + " => " + x + ";" + y + " dir " + dx + ";" + dy);
        }

        System.out.println("First: " + (Math.abs(x) + Math.abs(y)));
    }

    private static void second(List<Move> input) {
        int x = 0;
        int y = 0;
        int wx = 10;
        int wy = 1;
        int dx = 1;
        int dy = 0;
        for (Move move : input) {
            switch (move.action) {
                case 'N':
                    wy += move.value;
                    break;
                case 'S':
                    wy -= move.value;
                    break;
                case 'E':
                    wx += move.value;
                    break;
                case 'W':
                    wx -= move.value;
                    break;
                case 'L':
                    if ((move.value % 90) == 0) {
                        for (int i = 0; i < move.value / 90; i++) {
                            int wx2 = -wy;
                            int wy2 = wx;
                            wx = wx2;
                            wy = wy2;
                        }
                    } else {
                        System.err.println("L" + move.value);
                    }
                    break;
                case 'R':
                    if ((move.value % 90) == 0) {
                        for (int i = 0; i < move.value / 90; i++) {
                            int wx2 = wy;
                            int wy2 = -wx;
                            wx = wx2;
                            wy = wy2;
                        }
                    } else {
                        System.err.println("R" + move.value);
                    }
                    break;
                case 'F':
                    x += (move.value * wx);
                    y += (move.value * wy);
                    break;
                default:
                    System.err.println(move.action + "" + move.value);
                    break;
            }
            System.out.println(move.action + "" + move.value + " => " + x + ";" + y + " dir " + dx + ";" + dy + " wayp " + wx + ";" + wy);
        }

        System.out.println("Second: " + (Math.abs(x) + Math.abs(y)));
    }

    static class Move
    {
        Move(String input)
        {
            action = input.charAt(0);
            value = Integer.parseInt(input.substring(1));
        }

        char action;
        int value;
    }
}
