import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day23 {
    private Map<Integer, Cup> cupMap = new HashMap<>();

    public static void main(String[] args) {
        new Day23().first();
        new Day23().second();
    }

    private void first() {
        Cup currentCup = createCups("364297581", false);
        for (int i = 0; i < 100; i++) {
            currentCup = simulateMove(currentCup);
        }
        System.out.println("First: " + labels(cupMap.get(1)).substring(1));
    }

    private void second() {
        Cup currentCup = createCups("364297581", true);
        for (int i = 0; i < 10000000; i++) {
            currentCup = simulateMove(currentCup);
        }
        Cup oneCup = cupMap.get(1);
        Cup firstCup = oneCup.next;
        Cup secondCup = firstCup.next;
        long result = ((long)firstCup.value) * ((long)secondCup.value);
        System.out.println("Second: " + result);
    }

    private String labels(Cup startCup) {
        StringBuilder sb = new StringBuilder();
        Cup currentCup = startCup;
        do {
            sb.append(currentCup.value);
            currentCup = currentCup.next;
        } while (currentCup != startCup);
        return sb.toString();
    }

    private Cup simulateMove(Cup currentCup) {
        List<Cup> takenCups = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            takenCups.add(takeCupAfter(currentCup));
        }

        int value = currentCup.value;
        do {
            value--;
            if (value == 0) {
                value = cupMap.size();
            }
        } while (!cupMap.get(value).inserted);

        Cup destinationCup = cupMap.get(value);
        for (int i = 0; i < 3; i++) {
            Cup insertCup = takenCups.get(i);
            insertAfter(destinationCup, insertCup);
            destinationCup = insertCup;
        }
        return currentCup.next;
    }

    private Cup takeCupAfter(Cup currentCup) {
        Cup takeCup = currentCup.next;
        currentCup.next = takeCup.next;
        takeCup.inserted = false;
        return takeCup;
    }

    private void insertAfter(Cup destinationCup, Cup insertCup) {
        Cup afterCup = destinationCup.next;
        destinationCup.next = insertCup;
        insertCup.next = afterCup;
        insertCup.inserted = true;
    }

    private Cup createCups(String pattern, boolean additional) {
        List<Cup> cups = new ArrayList<>();
        for (int i = 0; i < pattern.length(); i++) {
            int value = pattern.charAt(i) - '0';
            Cup cup = new Cup(value);
            cups.add(cup);
            cupMap.put(value, cup);
        }
        if (additional) {
            for (int value = 10; value <= 1000000; value++) {
                Cup cup = new Cup(value);
                cups.add(cup);
                cupMap.put(value, cup);
            }
        }
        for (int i = 0; i < cups.size(); i++) {
            Cup thisCup = cups.get(i);
            thisCup.next = cups.get((i + 1) % cups.size());
        }
        return cups.get(0);
    }

    static class Cup {
        int value;
        Cup next;
        boolean inserted = true;

        public Cup(int value) {
            this.value = value;
        }

        @Override
        public String
        toString() {
            return "Cup{" + value + '}';
        }
    }
}
