import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day4 {
    public static final List<String> REQUIRED_KEYS = List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    public static final List<String> EYE_COLORS = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input4.txt").toPath());
        List<Map<String, String>> passports = parse(lines);

        long count = passports.stream().filter(Day4::isValid).count();
        System.out.println("Valid passports: " + count);

        long count2 = passports.stream().filter(Day4::isExtendedValid).count();
        System.out.println("Part 2 valid passports: " + count2);
    }

    private static List<Map<String, String>> parse(List<String> lines) {
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> current = new HashMap<>();
        for (String line : lines) {
            if (line.isBlank()) {
                if (!current.isEmpty()) {
                    result.add(current);
                    current = new HashMap<>();
                }
            } else {
                parseLine(line, current);
            }
        }
        if (!current.isEmpty()) {
            result.add(current);
        }
        return result;
    }

    private static void parseLine(String line, Map<String, String> current) {
        String[] parts = line.split(" ");
        for (String part : parts) {
            String[] keyvalue = part.split(":");
            current.put(keyvalue[0], keyvalue[1]);
        }
    }

    private static boolean isValid(Map<String, String> passport) {
        return REQUIRED_KEYS.stream().allMatch(passport::containsKey);
    }

    private static boolean isExtendedValid(Map<String, String> passport) {
        if (!isValid(passport)) return false;
        String byr = passport.get("byr");
        if (!byr.matches("\\d{4}") ||
                byr.compareTo("1920") < 0 || byr.compareTo("2002") > 0) return false;
        String iyr = passport.get("iyr");
        if (!iyr.matches("\\d{4}") ||
                iyr.compareTo("2010") < 0 || iyr.compareTo("2020") > 0) return false;
        String eyr = passport.get("eyr");
        if (!eyr.matches("\\d{4}") ||
                eyr.compareTo("2020") < 0 || eyr.compareTo("2030") > 0) return false;
        if (!isValidHeight(passport.get("hgt"))) return false;
        String hcl = passport.get("hcl");
        if (!hcl.matches("#[0-9a-f]{6}")) return false;
        if (!EYE_COLORS.contains(passport.get("ecl"))) return false;
        String pid = passport.get("pid");
        if (!pid.matches("\\d{9}")) return false;
        return true;
    }

    private static boolean isValidHeight(String height) {
        if (height.endsWith("cm")) {
            int value = Integer.parseInt(height.substring(0, height.length() - 2));
            return 150 <= value && value <= 193;
        } else if (height.endsWith("in")) {
            int value = Integer.parseInt(height.substring(0, height.length() - 2));
            return 59 <= value && value <= 76;
        } else {
            return false;
        }
    }
}
