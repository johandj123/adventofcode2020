import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day25 {
    private long cardPublicKey;
    private long doorPublicKey;

    public static void main(String[] args) throws IOException {
        new Day25().start();
    }

    private int determineLoopSize(long subjectNumber, long key)
    {
        long value = 1L;
        int loopCount = 0;
        do {
            value *= subjectNumber;
            value %= 20201227L;
            loopCount++;
        } while (value != key);
        return loopCount;
    }

    private long transform(long subjectNumber, int loopSize)
    {
        long value = 1L;
        for (int i = 0; i < loopSize; i++) {
            value *= subjectNumber;
            value %= 20201227L;
        }
        return value;
    }

    private void start() throws IOException {
        readInput();

        int cardLoopSize = determineLoopSize(7L, cardPublicKey);
        int doorLoopSize = determineLoopSize(7L, doorPublicKey);

        long encryptionKey1 = transform(doorPublicKey, cardLoopSize);
        long encryptionKey2 = transform(cardPublicKey, doorLoopSize);
        System.out.println("Encryption key: " + encryptionKey1 + " / " + encryptionKey2 + " (both numbers should be the same)");
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input25.txt").toPath());
        cardPublicKey = Long.parseLong(lines.get(0));
        doorPublicKey = Long.parseLong(lines.get(1));
    }
}
