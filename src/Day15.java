import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 {
    public static void main(String[] args) throws IOException {
        List<Integer> list = readInput();

        calculate(new ArrayList<>(list), 2020);
        calculate(new ArrayList<>(list), 30000000);
    }

    private static void calculate(List<Integer> list, int index) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size() - 1; i++) {
            map.put(list.get(i), i);
        }

        while (list.size() < index) {
            int lastNumber = list.get(list.size() - 1);
            int nextNumber = 0;
            if (map.containsKey(lastNumber)) {
                nextNumber = list.size() - map.get(lastNumber) - 1;
            }
            map.put(lastNumber, list.size() - 1);
            list.add(nextNumber);
        }

        System.out.println(list.get(list.size() - 1));
    }

    private static List<Integer> readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input15.txt").toPath());
        String[] values = lines.get(0).split(",");
        return Arrays.stream(values).map(Integer::parseInt).collect(Collectors.toList());
    }
}
