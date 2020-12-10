import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();
        firstPart(input);
        secondPart(input);
    }

    private static void firstPart(List<Integer> input) throws IOException {
        int d1 = 0;
        int d3 = 0;
        for (int i = 0; i < input.size() - 1; i++) {
            int difference = input.get(i + 1) - input.get(i);
            if (difference == 1) {
                d1++;
            } else if (difference == 3) {
                d3++;
            }
        }
        System.out.println("First part: " + d1 * d3);
    }

    private static void secondPart(List<Integer> input) {
        Map<Integer, Node> map = new TreeMap<>();
        for (int value : input) {
            map.put(value, new Node(value));
        }
        for (Map.Entry<Integer, Node> entry : map.entrySet()) {
            for (int i = entry.getValue().value + 1; i <= entry.getValue().value + 3; i++) {
                if (map.containsKey(i)) {
                    Node current = entry.getValue();
                    Node next = map.get(i);
                    current.children.add(next);
                    next.parent.add(current);
                }
            }
        }
        Node startNode = map.get(input.get(0));
        Node endNode = map.get(input.get(input.size() - 1));

        for (Map.Entry<Integer, Node> entry : map.entrySet()) {
            Node current = entry.getValue();
            if (current == startNode) {
                startNode.count = BigInteger.ONE;
            } else {
                BigInteger sum = BigInteger.ZERO;
                for (Node prev : current.parent) {
                    sum = sum.add(prev.count);
                }
                current.count = sum;
            }
        }

        System.out.println("Second: " + endNode.count);
    }

    private static List<Integer> readInput() throws IOException {
        List<Integer> input1 = Files.readAllLines(new File("input10.txt").toPath())
                .stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
        List<Integer> input = new ArrayList<>();
        input.add(0);
        input.addAll(input1);
        input.add(input.get(input.size() - 1) + 3);
        return input;
    }

    static class Node {
        private int value;
        private List<Node> children = new ArrayList<>();
        private List<Node> parent = new ArrayList<>();
        private BigInteger count;

        public Node(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", children=" + children.stream().map(node -> node.value).collect(Collectors.toList()) +
                    '}';
        }
    }
}
