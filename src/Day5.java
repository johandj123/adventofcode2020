import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day5 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input5.txt").toPath());
        Integer max = lines.stream().map(Day5::seatId).max(Comparator.naturalOrder()).orElse(null);
        System.out.println("Max: " + max);
        SortedSet<Integer> passes = lines.stream().map(Day5::seatId).collect(Collectors.toCollection(TreeSet::new));
        for (int i = 1; i < max; i++) {
            if (passes.contains(i - 1) &&
                    passes.contains(i + 1) &&
                    !passes.contains(i)) {
                System.out.println("My pass: " + i);
            }
        }
    }

    private static int seatId(String input) {
        String translated = input
                .replace('L', '0')
                .replace('R', '1')
                .replace('F', '0')
                .replace('B', '1');
        return Integer.parseInt(translated, 2);
    }
}
