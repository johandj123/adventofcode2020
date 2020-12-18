import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input18.txt").toPath());
        List<List<Token>> tokens = lines.stream()
                .map(Day18::tokenize)
                .collect(Collectors.toList());
        long first = tokens.stream()
                .map(Day18::calculateFirstExpression)
                .reduce(0L, Long::sum);
        System.out.println("First: " + first);
        long second = tokens.stream()
                .map(Day18::calculateSecondExpression)
                .reduce(0L, Long::sum);
        System.out.println("Second: " + second);
    }

    private static long calculateFirstExpression(List<Token> tokens) {
        ArrayDeque<Token> queue = new ArrayDeque<>(tokens);
        long result = calculateFirstExpression(queue);
        if (queue.peek() != null) {
            throw new RuntimeException("Parse error");
        }
        return result;
    }

    private static long calculateFirstExpression(Queue<Token> queue) {
        long currentValue = calculateFirstValue(queue);
        while (queue.peek() != null &&
                (Type.PLUS.equals(queue.peek().type) || (Type.STAR.equals(queue.peek().type)))) {
            Token operatorToken = queue.poll();
            if (Type.PLUS.equals(operatorToken.type)) {
                currentValue = currentValue + calculateFirstValue(queue);
            } else if (Type.STAR.equals(operatorToken.type)) {
                currentValue = currentValue * calculateFirstValue(queue);
            }
        }
        return currentValue;
    }

    private static long calculateFirstValue(Queue<Token> queue) {
        Token token = queue.poll();
        if (Type.NUMBER.equals(token.type)) {
            return token.value;
        } else if (Type.BRACKET_OPEN.equals(token.type)) {
            long result = calculateFirstExpression(queue);
            token = queue.poll();
            if (!Type.BRACKET_CLOSE.equals(token.type)) {
                throw new RuntimeException("Parse error");
            }
            return result;
        } else {
            throw new RuntimeException("Parse error");
        }
    }

    private static long calculateSecondExpression(List<Token> tokens) {
        ArrayDeque<Token> queue = new ArrayDeque<>(tokens);
        long result = calculateSecondMultiplication(queue);
        if (queue.peek() != null) {
            throw new RuntimeException("Parse error");
        }
        return result;
    }

    private static long calculateSecondMultiplication(Queue<Token> queue) {
        long currentValue = calculateSecondSum(queue);
        while (queue.peek() != null && Type.STAR.equals(queue.peek().type)) {
            queue.poll();
            long nextValue = calculateSecondSum(queue);
            currentValue = currentValue * nextValue;
        }
        return currentValue;
    }

    private static long calculateSecondSum(Queue<Token> queue) {
        long currentValue = calculateSecondValue(queue);
        while (queue.peek() != null && Type.PLUS.equals(queue.peek().type)) {
            queue.poll();
            currentValue = currentValue + calculateSecondValue(queue);
        }
        return currentValue;
    }

    private static long calculateSecondValue(Queue<Token> queue) {
        Token token = queue.poll();
        if (Type.NUMBER.equals(token.type)) {
            return token.value;
        } else if (Type.BRACKET_OPEN.equals(token.type)) {
            long result = calculateSecondMultiplication(queue);
            token = queue.poll();
            if (!Type.BRACKET_CLOSE.equals(token.type)) {
                throw new RuntimeException("Parse error");
            }
            return result;
        } else {
            throw new RuntimeException("Parse error");
        }
    }

    private static List<Token> tokenize(String input) {
        List<Token> result = new ArrayList<>();
        int position = 0;
        while (position < input.length()) {
            char c = input.charAt(position);
            if (c == '+') {
                result.add(new Token(Type.PLUS));
                position++;
            } else if (c == '*') {
                result.add(new Token(Type.STAR));
                position++;
            } else if (c == '(') {
                result.add(new Token(Type.BRACKET_OPEN));
                position++;
            } else if (c == ')') {
                result.add(new Token(Type.BRACKET_CLOSE));
                position++;
            } else if (c >= '0' && c <= '9') {
                StringBuilder sb = new StringBuilder();
                while (c >= '0' && c <= '9') {
                    sb.append(c);
                    position++;
                    c = position < input.length() ? input.charAt(position) : '\0';
                }
                result.add(new Token(Type.NUMBER, Long.parseLong(sb.toString())));
            } else if (c == ' ') {
                position++;
            } else {
                throw new RuntimeException("Invalid character: " + c);
            }
        }
        return result;
    }

    static class Token {
        public Token(Type type, long value) {
            this.type = type;
            this.value = value;
        }

        public Token(Type type) {
            this(type, 0);
        }

        final Type type;

        final long value;

        @Override
        public String toString() {
            return type + (type == Type.NUMBER ? ("(" + value + ")") : "");
        }
    }

    enum Type {
        PLUS,
        STAR,
        NUMBER,
        BRACKET_OPEN,
        BRACKET_CLOSE
    }
}
