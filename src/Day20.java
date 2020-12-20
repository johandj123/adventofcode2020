import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20 {
    private Map<Integer, Tile> tiles = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new Day20().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
        //testOrientationsWithP();
    }

    private void testOrientationsWithP() {
        List<String> list = new ArrayList<>();
        list.add("#### ");
        list.add("#   #");
        list.add("#### ");
        list.add("#    ");
        list.add("#    ");
        Tile tile = new Tile(list);
        for (List<String> orientation : tile.orientations) {
            for (String line : orientation) {
                System.out.println(line);
            }
            System.out.println("============");
        }
    }

    private void first() {
        long result = 1L;
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            if (entry.getValue().isBorder()) {
                result *= entry.getKey();
            }
        }
        System.out.println("First: " + result);
    }

    private void second() {
        Set<Integer> placedIds = new HashSet<>();
        PlacedTile[][] placed = new PlacedTile[12][12];
        boolean complete = placeNextTile(placedIds, placed, 0, 0);
        if (complete) {
            List<String> fullPicture = new ArrayList<>();
            for (int y = 0; y < 12; y++) {
                List<List<String>> inners = new ArrayList<>();
                for (int x = 0; x < 12; x++) {
                    inners.add(placed[y][x].getInner());
                }
                for (int j = 0; j < inners.get(0).size(); j++) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < inners.size(); i++) {
                        sb.append(inners.get(i).get(j));
                    }
                    fullPicture.add(sb.toString());
                }
            }

            List<String> pattern = new ArrayList<>();
            pattern.add("                  # ");
            pattern.add("#    ##    ##    ###");
            pattern.add(" #  #  #  #  #  #   ");
            Tile fullPictureTile = new Tile(fullPicture);
            for (List<String> orientation : fullPictureTile.orientations) {
                List<String> marked = new ArrayList<>(orientation);
                int count = 0;
                for (int y = 0; y <= orientation.size() - pattern.size(); y++) {
                    for (int x = 0; x <= orientation.get(0).length() - pattern.get(0).length(); x++) {
                        if (isPatternAt(orientation, pattern, x, y)) {
                            markPatternAt(marked, pattern, x, y);
                            count++;
                        }
                    }
                }
                if (count > 0) {
                    System.out.println("Second: " + countHashes(marked));
                }
            }
        }
    }

    private boolean isPatternAt(List<String> fullPicture, List<String> pattern, int x, int y) {
        for (int dy = 0; dy < pattern.size(); dy++) {
            for (int dx = 0; dx < pattern.get(0).length(); dx++) {
                if (pattern.get(dy).charAt(dx) == '#') {
                    if (fullPicture.get(y + dy).charAt(x + dx) != '#') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void markPatternAt(List<String> fullPicture, List<String> pattern, int x, int y) {
        for (int dy = 0; dy < pattern.size(); dy++) {
            for (int dx = 0; dx < pattern.get(0).length(); dx++) {
                if (pattern.get(dy).charAt(dx) == '#') {
                    String originalLine = fullPicture.get(y + dy);
                    int position = x + dx;
                    String newLine = originalLine.substring(0, position) + "O" + originalLine.substring(position + 1);
                    fullPicture.set(y + dy, newLine);
                }
            }
        }
    }

    private int countHashes(List<String> fullPicture) {
        int count = 0;
        for (String line : fullPicture) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean placeNextTile(Set<Integer> placedIds, PlacedTile[][] placed, int x, int y) {
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            if (!placedIds.contains(entry.getKey())) {
                Tile tile = entry.getValue();
                placedIds.add(entry.getKey());
                for (List<String> orientation : tile.orientations) {
                    placed[y][x] = new PlacedTile(orientation);
                    if (canPlaceTile(placed, x, y)) {
                        int nx = x + 1;
                        int ny = y;
                        if (nx == 12) {
                            nx = 0;
                            ny++;
                        }
                        if (ny == 12) {
                            return true;
                        }
                        if (placeNextTile(placedIds, placed, nx, ny)) {
                            return true;
                        }
                    }
                    placed[y][x] = null;
                }
                placedIds.remove(entry.getKey());
            }
        }
        return false;
    }

    private boolean canPlaceTile(PlacedTile[][] placed, int x, int y) {
        if (x > 0) {
            if (!placed[y][x - 1].getRight().equals(placed[y][x].getLeft())) {
                return false;
            }
        }
        if (y > 0) {
            if (!placed[y - 1][x].getBottom().equals(placed[y][x].getTop())) {
                return false;
            }
        }
        return true;
    }

    private void readInput() throws IOException {
        Pattern pattern = Pattern.compile("Tile (\\d+):");
        String input = Files.readString(new File("input20.txt").toPath());
        String[] parts = input.split("\n\n");
        for (String part : parts) {
            String[] lines = part.split("\n");
            Matcher matcher = pattern.matcher(lines[0]);
            if (!matcher.matches()) {
                System.err.println("Failed to parse tile id line: " + lines[0]);
            }
            int id = Integer.parseInt(matcher.group(1));
            List<String> content = Arrays.asList(lines).subList(1, lines.length);
            tiles.put(id, new Tile(content));
        }
    }

    static class PlacedTile {
        List<String> content;

        public PlacedTile(List<String> content) {
            this.content = content;
        }

        public String getTop() {
            return content.get(0);
        }

        public String getBottom() {
            return content.get(content.size() - 1);
        }

        public String getLeft() {
            StringBuilder sb = new StringBuilder();
            for (String line : content) {
                sb.append(line.charAt(0));
            }
            return sb.toString();
        }

        public String getRight() {
            StringBuilder sb = new StringBuilder();
            for (String line : content) {
                sb.append(line.charAt(line.length() - 1));
            }
            return sb.toString();
        }

        public List<String> getInner() {
            List<String> result = new ArrayList<>();
            for (int i = 1; i < content.size() - 1; i++) {
                String line = content.get(i);
                result.add(line.substring(1, line.length() - 1));
            }
            return result;
        }
    }

    class Tile {
        List<String> content;
        List<List<String>> orientations;

        public Tile(List<String> content) {
            this.content = content;
            this.orientations = calculateOrientations();
        }

        private List<String> tileSides() {
            List<String> sidesWithoutFlip = new ArrayList<>();
            sidesWithoutFlip.add(content.get(0));
            sidesWithoutFlip.add(content.get(content.size() - 1));
            StringBuilder left = new StringBuilder();
            StringBuilder right = new StringBuilder();
            for (String line : content) {
                left.append(line.charAt(0));
                right.append(line.charAt(line.length() - 1));
            }
            sidesWithoutFlip.add(left.toString());
            sidesWithoutFlip.add(right.toString());
            List<String> sides = new ArrayList<>();
            for (String side : sidesWithoutFlip) {
                sides.add(side);
                StringBuilder sb = new StringBuilder(side);
                sb.reverse();
                sides.add(sb.toString());
            }
            return sides;
        }

        public boolean isBorder() {
            Set<String> otherSet = otherTileBorders();
            Set<String> set = new HashSet<>(tileSides());
            return countCommonElements(set, otherSet) == 4;
        }

        private int countCommonElements(Set<String> a, Set<String> b) {
            int count = 0;
            for (String s : a) {
                if (b.contains(s)) {
                    count++;
                }
            }
            return count;
        }

        private Set<String> otherTileBorders() {
            Set<String> otherSet = new HashSet<>();
            for (Tile otherTile : tiles.values()) {
                if (!equals(otherTile)) {
                    otherSet.addAll(otherTile.tileSides());
                }
            }
            return otherSet;
        }

        private List<List<String>> calculateOrientations() {
            List<List<String>> result = new ArrayList<>();
            collectRotations(result, content);
            collectRotations(result, flip());
            return result;
        }

        private void collectRotations(List<List<String>> result, List<String> input) {
            result.add(input);
            List<String> temp = rotate(input);
            result.add(temp);
            temp = rotate(temp);
            result.add(temp);
            temp = rotate(temp);
            result.add(temp);
        }

        private List<String> flip() {
            List<String> result = new ArrayList<>();
            for (String line : content) {
                StringBuilder sb = new StringBuilder(line);
                sb.reverse();
                result.add(sb.toString());
            }
            return result;
        }

        private List<String> rotate(List<String> original) {
            List<String> result = new ArrayList<>();
            for (int x = 0; x < original.size(); x++) {
                StringBuilder sb = new StringBuilder();
                for (int y = original.size() - 1; y >= 0; y--) {
                    sb.append(original.get(y).charAt(x));
                }
                result.add(sb.toString());
            }
            return result;
        }
    }
}
