import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day7 {

    public static final String CONTAIN = " contain ";
    public static final String SHINY_GOLD = "shiny gold";

    public static void main(String[] args) throws IOException {
        Map<String, List<Item>> input = readInput();
        System.out.println("First: " + first(input));
        System.out.println("Second: " + second(input));
    }

    private static int first(Map<String, List<Item>> input) {
        Map<String, Set<String>> reversed = new HashMap<>();
        for (Map.Entry<String, List<Item>> entry : input.entrySet()) {
            reversed.computeIfAbsent(entry.getKey(), key -> new HashSet<>());
            for (Item item : entry.getValue()) {
                reversed.computeIfAbsent(item.name, key -> new HashSet<>()).add(entry.getKey());
            }
        }

        Set<String> result = new HashSet<>();
        traverseReversed(reversed, result, SHINY_GOLD);
        result.remove(SHINY_GOLD);

        return result.size();
    }

    private static void traverseReversed(Map<String, Set<String>> reversed, Set<String> result, String name) {
        if (!result.contains(name)) {
            result.add(name);
            for (String next : reversed.get(name)) {
                traverseReversed(reversed, result, next);
            }
        }
    }

    private static Map<String, List<Item>> readInput() throws IOException {
        Map<String, List<Item>> input = new HashMap<>();
        List<String> lines = Files.readAllLines(new File("input7.txt").toPath());
        for (String line : lines) {
            line = line.replace("contains", "contain").replace("bags", "bag").replace(".", "");
            int index = line.indexOf(CONTAIN);
            String name = line.substring(0, index).replace(" bag", "");
            String content = line.substring(index + CONTAIN.length());
            List<Item> items = new ArrayList<>();
            if (!"no other bag".equals(content)) {
                String[] parts = content.split(", ");
                for (String part : parts) {
                    part = part.replace(" bag", "");
                    index = part.indexOf(' ');
                    Item item = new Item();
                    item.count = Integer.parseInt(part.substring(0, index));
                    item.name = part.substring(index + 1);
                    items.add(item);
                }
            }
            input.put(name, items);
        }
        return input;
    }

    private static int second(Map<String, List<Item>> input) {
        int total = traverse(input, SHINY_GOLD);
        return total - 1;
    }

    private static int traverse(Map<String, List<Item>> input, String name) {
        int result = 1;
        for (Item item : input.get(name)) {
            result += item.count * traverse(input, item.name);
        }
        return result;
    }

    private static class Item {
        int count;
        String name;

        @Override
        public String toString() {
            return "Item{" +
                    "count=" + count +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
