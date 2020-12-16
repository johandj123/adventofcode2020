import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {
    private Map<String, List<Range>> fieldMap = new HashMap<>();
    private List<Range> rangeList = new ArrayList<>();
    private List<Integer> myTicket = new ArrayList<>();
    private List<List<Integer>> nearbyTickets = new ArrayList<>();
    private List<List<Integer>> nearbyValidTicket = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day16().start();

    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void second() {
        int columnCount = myTicket.size();
        Map<String, Set<Integer>> possibleColumnMap = new HashMap<>();
        for (Map.Entry<String, List<Range>> entry : fieldMap.entrySet()) {
            Set<Integer> valid = IntStream.range(0, columnCount)
                    .filter(i -> columnInRange(entry.getValue(), i))
                    .boxed()
                    .collect(Collectors.toSet());
            possibleColumnMap.put(entry.getKey(), valid);
        }

        Map<String, Integer> assignment = new HashMap<>();
        Set<Integer> toBeAssigned = IntStream.range(0, columnCount).boxed().collect(Collectors.toCollection(TreeSet::new));
        while (!toBeAssigned.isEmpty()) {
            for (int i : toBeAssigned) {
                int count = 0;
                String foundKey = null;
                for (String key : possibleColumnMap.keySet()) {
                    if (possibleColumnMap.get(key).contains(i)) {
                        count++;
                        foundKey = key;
                    }
                }
                if (count == 1) {
                    assignment.put(foundKey, i);
                    toBeAssigned.remove(i);
                    possibleColumnMap.remove(foundKey);
                    break;
                }
            }
        }

        long result = 1L;
        for (Map.Entry<String, Integer> entry : assignment.entrySet()) {
            if (entry.getKey().startsWith("departure")) {
                long value = myTicket.get(entry.getValue());
                result *= value;
            }
        }
        System.out.println("Second: " + result);
    }

    private boolean columnInRange(List<Range> ranges, int index) {
        for (List<Integer> ticket : nearbyValidTicket) {
            int value = ticket.get(index);
            if (!isValidValue(ranges, value)) {
                return false;
            }
        }
        return true;
    }

    private void first() {
        int sum = 0;
        for (List<Integer> ticket : nearbyTickets) {
            boolean valid = true;
            for (Integer value : ticket) {
                if (!isValidValue(value)) {
                    sum += value;
                    valid = false;
                }
            }
            if (valid) {
                nearbyValidTicket.add(ticket);
            }
        }
        System.out.println("First: " + sum);
    }

    private void readInput() throws IOException {
        String input = Files.readString(new File("input16.txt").toPath());
        String[] parts = input.split("\n\n");

        parseFields(parts[0]);
        parseMyTicket(parts[1]);
        parseNearbyTickets(parts[2]);
    }

    private boolean isValidValue(int value) {
        return rangeList.stream().anyMatch(range -> range.inRange(value));
    }

    private boolean isValidValue(List<Range> ranges, int value) {
        return ranges.stream().anyMatch(range -> range.inRange(value));
    }

    private void parseMyTicket(String part) {
        String[] tickets = part.split("\n");
        myTicket = parseTicket(tickets[1]);
    }

    private void parseNearbyTickets(String part) {
        String[] tickets = part.split("\n");
        for (int i = 1; i < tickets.length; i++) {
            List<Integer> ticketList = parseTicket(tickets[i]);
            nearbyTickets.add(ticketList);
        }
    }

    private List<Integer> parseTicket(String ticket) {
        String[] values = ticket.split(",");
        return Arrays.stream(values).map(Integer::parseInt).collect(Collectors.toList());
    }

    private void parseFields(String part) {
        String[] fields = part.split("\n");
        for (String field : fields) {
            String[] parts = field.split(":");
            String name = parts[0];
            String[] ranges = parts[1].split("or");
            List<Range> fieldMapRangeList = fieldMap.computeIfAbsent(name, key -> new ArrayList<>());
            for (String range : ranges) {
                String rangeTrimmed = range.trim();
                String[] rangeParts = rangeTrimmed.split("-");
                int from = Integer.parseInt(rangeParts[0]);
                int to = Integer.parseInt(rangeParts[1]);
                fieldMapRangeList.add(new Range(from, to));
                rangeList.add(new Range(from, to));
            }
        }
    }

    static class Range {
        final int from;
        final int to;

        Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

        boolean inRange(int value) {
            return from <= value && value <= to;
        }

        @Override
        public String toString() {
            return from + "-" + to;
        }
    }
}
