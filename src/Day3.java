import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day3 {
    private List<String> map;

    public static void main(String[] args) throws IOException {
        new Day3().start();
    }

    private void start() throws IOException {
        map = Files.readAllLines(new File("input3.txt").toPath());
        part1();
        part2();
    }

    private void part1() {
        int count = treeCount(3, 1);
        System.out.println("Part1: " + count + " trees");
    }

    private void part2()
    {
        int count = treeCount(1, 1) *
                treeCount(3, 1) *
                treeCount(5, 1) *
                treeCount(7, 1) *
                treeCount(1, 2);
        System.out.println("Part2: " + count);
    }

    private int treeCount(int xSlope, int ySlope) {
        int count = 0;
        for (int x = 0, y = 0; y < map.size(); x += xSlope, y += ySlope) {
            if (isTree(x, y)) {
                count++;
            }
        }
        System.out.println(count);
        return count;
    }

    private boolean isTree(int x,int y)
    {
        String line = map.get(y % map.size());
        int pos = x % line.length();
        return line.charAt(pos) == '#';
    }
}
