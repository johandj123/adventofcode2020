import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input2.txt").toPath());
        int valid = 0;

        for (String line : lines) {
            if (isValid(line)) {
                System.out.println("VALID: " + line);
                valid++;
            } else {
                System.out.println("INVALID: " + line);
            }
        }

        System.out.println();
        System.out.println("Total (1): " + valid);
        System.out.println();

        int valid2 = 0;

        for (String line : lines) {
            if (isValid2(line)) {
                System.out.println("VALID: " + line);
                valid2++;
            } else {
                System.out.println("INVALID: " + line);
            }
        }

        System.out.println();
        System.out.println("Total (2): " + valid2);
    }

    static boolean isValid(String line)
    {
        int colonIndex = line.indexOf(": ");
        String policy = line.substring(0, colonIndex);
        String password = line.substring(colonIndex + 2);

        int spaceIndex = policy.indexOf(' ');
        String counts = policy.substring(0, spaceIndex);
        String character = policy.substring(spaceIndex + 1);

        int dashIndex = counts.indexOf('-');
        int min = Integer.parseInt(counts.substring(0, dashIndex));
        int max = Integer.parseInt(counts.substring(dashIndex + 1));

        int count = 0;
        while (true) {
            int index = password.indexOf(character);
            if (index == -1) {
                break;
            }
            count++;
            password = password.substring(index + character.length());
        }

        return min <= count && count <= max;
    }

    static boolean isValid2(String line)
    {
        int colonIndex = line.indexOf(": ");
        String policy = line.substring(0, colonIndex);
        String password = line.substring(colonIndex + 2);

        int spaceIndex = policy.indexOf(' ');
        String counts = policy.substring(0, spaceIndex);
        String character = policy.substring(spaceIndex + 1);

        int dashIndex = counts.indexOf('-');
        int index1 = Integer.parseInt(counts.substring(0, dashIndex)) - 1;
        int index2 = Integer.parseInt(counts.substring(dashIndex + 1)) - 1;

        int count = 0;
        if (character.equals(password.substring(index1, index1 + 1))) {
            count++;
        }
        if (character.equals(password.substring(index2, index2 + 1))) {
            count++;
        }
        return count == 1;
    }
}
