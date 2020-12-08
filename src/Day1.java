import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input1.txt").toPath());
        Set<Integer> values = lines.stream().map(Integer::parseInt).collect(Collectors.toSet());
        for (int value : values) {
            int otherValue = 2020 - value;
            if (values.contains(otherValue) && value <= otherValue) {
                System.out.println(value + "*" + otherValue + "=" + (value * otherValue));
            }
        }

        for (int value1 : values) {
            for (int value2 : values) {
                int value3 = 2020 - value2 - value1;
                if (values.contains(value3)) {
                    System.out.println(value1 + "*" + value2 + "*" + value3 + "=" + (value1 * value2 * value3));
                }
            }
        }
    }
}
