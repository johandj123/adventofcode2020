import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 {
    public static void main(String[] args) throws IOException {
        List<List<String>> groups = readInput();

        System.out.println("1: " + someAnsweredYes(groups));
        System.out.println("2: " + allAnsweredYes(groups));
    }

    private static int someAnsweredYes(List<List<String>> groups) {
        int count = 0;
        for (List<String> group : groups) {
            Set<Character> content = new HashSet<>();
            for (String person : group) {
                for (char c : person.toCharArray()) {
                    content.add(c);
                }
            }
            count += content.size();
        }
        return count;
    }

    private static int allAnsweredYes(List<List<String>> groups) {
        int count = 0;
        for (List<String> group : groups) {
            Set<Character> content = new HashSet<>();
            for (String person : group) {
                for (char c : person.toCharArray()) {
                    content.add(c);
                }
            }
            for (char c : content) {
                String str = Character.toString(c);
                boolean allYes = group.stream().allMatch(person -> person.contains(str));
                if (allYes) {
                    count++;
                }
            }
        }
        return count;
    }

    private static List<List<String>> readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input6.txt").toPath());
        List<List<String>> groups = new ArrayList<>();
        List<String> current = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                groups.add(current);
                current = new ArrayList<>();
            } else {
                current.add(line);
            }
        }
        if (!current.isEmpty()) {
            groups.add(current);
        }
        return groups;
    }
}
