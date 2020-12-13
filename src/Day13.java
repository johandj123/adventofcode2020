import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
    private int now;
    private List<Integer> busIds;
    private List<Integer> busIdsWithBlanks;

    public static void main(String[] args) throws IOException {
        new Day13().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input13.txt").toPath());
        now = Integer.parseInt(lines.get(0));
        busIds = Arrays.stream(lines.get(1).split(","))
                .filter(x -> !"x".equals(x))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        busIdsWithBlanks = Arrays.stream(lines.get(1).split(","))
                .map(x -> "x".equals(x) ? "-1" : x)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private void second() {
        List<Equation> equations = new ArrayList<>();
        for (int i = 0; i < busIdsWithBlanks.size(); i++) {
            int busId = busIdsWithBlanks.get(i);
            if (busId != -1) {
                equations.add(new Equation(-i, busId));
            }
        }

        long mproduct = equations
                .stream()
                .map(equation -> equation.m)
                .reduce(1L, (a, b) -> a * b);

        for (Equation equation : equations) {
            equation.n = mproduct / equation.m;
            equation.w = egcd(equation.n, equation.m);
            if (equation.w < 0) {
                equation.w += equation.m;
            }
        }

        long solution = equations
                .stream()
                .map(equation -> equation.c * equation.n * equation.w)
                .reduce(0L, Long::sum);
        solution = properModulo(solution, mproduct);

        System.out.println("Second: " + solution);
    }

    private static long egcd(long a1,long b1)
    {
        long a = a1;
        long b = b1;

        long x = 0;
        long y = 1;
        long u = 1;
        long v = 0;
        while (a != 0) {
            long q = b / a;
            long r = properModulo(b, a);
            long m = x - u * q;
            long n = y - v * q;

            b = a;
            a = r;
            x = u;
            y = v;
            u = m;
            v = n;
        }

        if (b != 1) {
            System.err.println("!" + b);
        }

        return x;
    }

    private static long properModulo(long a,long b)
    {
        b = Math.abs(b);
        if (a < 0) {
            return b - ((-a) % b);
        } else {
            return a % b;
        }
    }

    private void first() {
        int minWait = Integer.MAX_VALUE;
        int minBusId = -1;
        for (int busId : busIds) {
            int wait = busId - (now % busId);
            if (wait < minWait) {
                minWait = wait;
                minBusId = busId;
            }
        }
        System.out.println("First: " + minWait * minBusId);
    }

    static class Equation
    {
        public Equation(long c, long m) {
            this.c = c;
            this.m = m;
        }

        long c;
        long m;
        long n;
        long w;
    }
}
