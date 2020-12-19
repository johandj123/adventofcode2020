import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class Day19 {
    private Map<Integer, String> rules = new HashMap<>();
    private List<String> messages = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day19().start();

    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void first() {
        String regex = ruleRegex(rules.get(0));
        Pattern pattern = Pattern.compile(regex);

        int count = 0;
        for (String message : messages) {
            boolean match = pattern.matcher(message).matches();
            if (match) {
                count++;
            }
        }
        System.out.println("First: " + count);
    }

    private void second() {
        rules.put(8, "42 | 42 8");
        rules.put(11, "42 31 | 42 11 31");

        String regex = ruleRegex2(rules.get(0));
        Pattern pattern = Pattern.compile(regex);

        int count = 0;
        for (String message : messages) {
            boolean match = pattern.matcher(message).matches();
            if (match) {
                count++;
            }
        }
        System.out.println("Second: " + count);
    }

    private void readInput() throws IOException {
        String input = Files.readString(new File("input19.txt").toPath());
        String[] parts = input.split("\n\n");

        String[] rulesParts = parts[0].split("\n");
        for (String rulesPart : rulesParts) {
            int i = rulesPart.indexOf(": ");
            int index = Integer.parseInt(rulesPart.substring(0, i));
            String content = rulesPart.substring(i + 2);
            rules.put(index, content);
        }

        String[] messageParts = parts[1].split("\n");
        messages.addAll(Arrays.asList(messageParts));
    }

    private String ruleRegex(String rule)
    {
        if (rule.startsWith("\"")) {
            return rule.substring(1, 2);
        } else {
            StringBuilder sb = new StringBuilder();
            String[] parts = rule.split(" ");
            for (String part : parts) {
                if ("|".equals(part)) {
                    sb.append('|');
                } else {
                    int subRuleIndex = Integer.parseInt(part);
                    String subRule = rules.get(subRuleIndex);
                    sb.append('(').append(ruleRegex(subRule)).append(')');
                }
            }
            return sb.toString();
        }
    }

    private String ruleRegex2(String rule)
    {
        return ruleRegex2(rule, 0);
    }

    private String ruleRegex2(String rule, int depth)
    {
        if (depth > 200) {
            return "x";
        }

        if (rule.startsWith("\"")) {
            return rule.substring(1, 2);
        } else {
            StringBuilder sb = new StringBuilder();
            String[] parts = rule.split(" ");
            for (String part : parts) {
                if ("|".equals(part)) {
                    sb.append('|');
                } else {
                    int subRuleIndex = Integer.parseInt(part);
                    String subRule = rules.get(subRuleIndex);
                    sb.append('(').append(ruleRegex2(subRule, depth + 1)).append(')');
                }
            }
            return sb.toString();
        }
    }
}
