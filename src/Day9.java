import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<Long> input = readInput();
        calculate(input);
    }

    private static List<Long> readInput() throws IOException {
        return Files.readAllLines(new File("input9.txt").toPath())
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private static void calculate(List<Long> input) {
        for (int i = 25; i < input.size(); i++) {
            List<Long> pre = input.subList(i - 25, i);
            if (!sumFound(pre, input.get(i))) {
                System.out.println("First: " + input.get(i));
                findSegmentSum(input, input.get(i));
            }
        }
    }

    private static void findSegmentSum(List<Long> input, long value) {
        outer:
        for (int i = 0; i < input.size() - 1; i++) {
            long sum = input.get(i);
            for (int j = i + 1; j < input.size(); j++) {
                sum += input.get(j);
                if (sum == value) {
                    List<Long> sub = input.subList(i, j + 1);
                    long min = sub.stream().min(Comparator.naturalOrder()).get();
                    long max = sub.stream().max(Comparator.naturalOrder()).get();
                    System.out.println("Second: " + min + " + " + max + " = " + (min + max));
                } else if (sum > value) {
                    continue outer;
                }
            }
        }
    }

    private static boolean sumFound(List<Long> pre, long value) {
        for (int i = 0; i < pre.size(); i++) {
            for (int j = i + 1; j < pre.size(); j++) {
                if (pre.get(i) + pre.get(j) == value) {
                    return true;
                }
            }
        }
        return false;
    }
}
