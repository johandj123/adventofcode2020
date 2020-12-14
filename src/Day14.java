import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day14 {
    private long[] memory = new long[65536];
    private Map<Long, Long> memory2 = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new Day14().start();
    }

    private void start() throws IOException {
        List<String> lines = Files.readAllLines(new File("input14.txt").toPath());
        first(lines);
        second(lines);
    }

    private void first(List<String> lines) {
        long changeBits = 0;
        long valueBits = 0;
        for (String line : lines) {
            if (line.startsWith("mask = ")) {
                String mask = line.substring(7);
                changeBits = Long.parseLong(mask.replace('0', '1').replace('X', '0'), 2);
                valueBits = Long.parseLong(mask.replace('X', '0'), 2);
            } else if (line.startsWith("mem[")) {
                int index1 = line.indexOf('[');
                int index2 = line.indexOf(']');
                long address = Long.parseLong(line.substring(index1 + 1, index2));
                long value = Long.parseLong(line.substring(line.indexOf('=') + 2));

                memory[(int) address] = alterValue(value, changeBits, valueBits);
            }
        }

        long sum = Arrays.stream(memory).reduce(0L, Long::sum);
        System.out.println("First: " + sum);
    }

    private void second(List<String> lines) {
        long setBits = 0;
        List<Integer> countBits = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("mask = ")) {
                String mask = line.substring(7);
                setBits = Long.parseLong(mask.replace('X', '0'), 2);
                long countBitsMask = Long.parseLong(mask.replace('1', '0').replace('X', '1'), 2);
                countBits = new ArrayList<>();
                for (int i = 0; i < 36; i++) {
                    if (((countBitsMask >> i) & 1) == 1) {
                        countBits.add(i);
                    }
                }
            } else if (line.startsWith("mem[")) {
                int index1 = line.indexOf('[');
                int index2 = line.indexOf(']');
                long address = Long.parseLong(line.substring(index1 + 1, index2));
                long value = Long.parseLong(line.substring(line.indexOf('=') + 2));

                address = address | setBits;
                for (long i = 0; i < (1L << countBits.size()); i++) {
                    for (int j = 0; j < countBits.size(); j++) {
                        long bitValue = (i >> j) & 1;
                        int destinationBitIndex = countBits.get(j);
                        long destinationMask = 1L << destinationBitIndex;
                        if (bitValue == 0) {
                            address = address & ~destinationMask;
                        } else {
                            address = address | destinationMask;
                        }
                    }
                    memory2.put(address, value);
                }
            }
        }

        long sum = memory2.values().stream().reduce(0L, Long::sum);
        System.out.println("Second: " + sum);
    }

    private long alterValue(long value, long changeBits, long valueBits) {
        return (value & ~changeBits) | (valueBits & changeBits);
    }
}
