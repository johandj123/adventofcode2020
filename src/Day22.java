import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day22 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File("input22.txt").toPath());
        String[] parts = input.split("\n\n");
        first(parts);
        second(parts);
    }

    private static void first(String[] parts) {
        Deque<Integer> first = createDeck(parts[0]);
        Deque<Integer> second = createDeck(parts[1]);
        playGame(first, second);
        System.out.println("First: " + calculateScore(first));
    }

    private static void second(String[] parts) {
        Deque<Integer> first = createDeck(parts[0]);
        Deque<Integer> second = createDeck(parts[1]);
        playRecursiveGame(first, second);
        System.out.println("Second: " + calculateScore(first));
    }

    private static int calculateScore(Deque<Integer> first) {
        int score = 0;
        int factor = 1;
        while (!first.isEmpty()) {
            score += (factor * first.pollLast());
            factor++;
        }
        return score;
    }

    private static void playGame(Deque<Integer> first, Deque<Integer> second) {
        while (!first.isEmpty() && !second.isEmpty()) {
            int firstCard = first.pollFirst();
            int secondCard = second.pollFirst();
            if (firstCard > secondCard) {
                first.addLast(firstCard);
                first.addLast(secondCard);
            } else {
                second.addLast(secondCard);
                second.addLast(firstCard);
            }
        }
    }

    private static int playRecursiveGame(Deque<Integer> first, Deque<Integer> second)
    {
        Set<State> states = new HashSet<>();
        while (!first.isEmpty() && !second.isEmpty()) {
            State state = new State(first, second);
            if (states.contains(state)) {
                return 1;
            }
            states.add(state);

            int firstCard = first.pollFirst();
            int secondCard = second.pollFirst();
            int winner;
            if (first.size() >= firstCard && second.size() >= secondCard) {
                Deque<Integer> subFirst = new ArrayDeque<>(new ArrayList<>(first).subList(0, firstCard));
                Deque<Integer> subSecond = new ArrayDeque<>(new ArrayList<>(second).subList(0, secondCard));
                winner = playRecursiveGame(subFirst, subSecond);
            } else {
                winner = (firstCard > secondCard) ? 1 : 2;
            }
            if (winner == 1) {
                first.addLast(firstCard);
                first.addLast(secondCard);
            } else {
                second.addLast(secondCard);
                second.addLast(firstCard);
            }
        }
        return second.isEmpty() ? 1 : 2;
    }

    private static Deque<Integer> createDeck(String inputPart) {
        Deque<Integer> result = new ArrayDeque<>();
        String[] parts = inputPart.split("\n");
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            Integer value = Integer.parseInt(part);
            result.addLast(value);
        }
        return result;
    }

    static class State {
        List<Integer> first;
        List<Integer> second;

        public State(Deque<Integer> first, Deque<Integer> second) {
            this.first = new ArrayList<>(first);
            this.second = new ArrayList<>(second);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return first.equals(state.first) &&
                    second.equals(state.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}
