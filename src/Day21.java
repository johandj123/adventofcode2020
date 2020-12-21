import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day21 {
    public static void main(String[] args) throws IOException {
        List<Line> lines = Files.readAllLines(new File("input21.txt").toPath())
                .stream()
                .map(Line::new)
                .collect(Collectors.toList());
        Set<String> allIngredients = lines.stream()
                .flatMap(line -> line.ingredients.stream())
                .collect(Collectors.toSet());
        Set<String> allAllergens = lines.stream()
                .flatMap(line -> line.allergens.stream())
                .collect(Collectors.toSet());

        Set<String> potentialAllergenIngredients = new HashSet<>();
        Map<String, List<String>> allergenToIngredientsMap = new HashMap<>();
        for (String allergen : allAllergens) {
            List<Line> linesWithAllergen = lines.stream()
                    .filter(line -> line.allergens.contains(allergen))
                    .collect(Collectors.toList());
            Set<String> allPossibleIngredients = linesWithAllergen
                    .stream()
                    .flatMap(line -> line.ingredients.stream())
                    .collect(Collectors.toSet());
            for (String ingredient : allPossibleIngredients) {
                boolean inAllLines = linesWithAllergen.stream()
                        .allMatch(line -> line.ingredients.contains(ingredient));
                if (inAllLines) {
                    potentialAllergenIngredients.add(ingredient);
                    allergenToIngredientsMap.computeIfAbsent(allergen, key -> new ArrayList<>()).add(ingredient);
                }
            }
        }

        int count = 0;
        for (Line line : lines) {
            for (String ingredient : line.ingredients) {
                if (!potentialAllergenIngredients.contains(ingredient)) {
                    count++;
                }
            }
        }
        System.out.println("First: " + count);

        Map<String, String> uniqueAllergenToIngredientMap = new TreeMap<>();
        while (uniqueAllergenToIngredientMap.size() < allergenToIngredientsMap.size()) {
            for (Map.Entry<String, List<String>> entry : allergenToIngredientsMap.entrySet()) {
                if (entry.getValue().size() == 1) {
                    String ingredient = entry.getValue().get(0);
                    uniqueAllergenToIngredientMap.put(entry.getKey(), ingredient);
                    for (Map.Entry<String, List<String>> entry2 : allergenToIngredientsMap.entrySet()) {
                        entry2.getValue().remove(ingredient);
                    }
                    break;
                }
            }
        }
        List<String> ingredientList = new ArrayList<>();
        for (Map.Entry<String, String> entry : uniqueAllergenToIngredientMap.entrySet()) {
            ingredientList.add(entry.getValue());
        }
        System.out.println("Second: " + ingredientList.stream().collect(Collectors.joining(",")));
    }

    static class Line {
        public static final String CONTAINS = " (contains ";
        Set<String> ingredients;
        Set<String> allergens;

        Line(String line) {
            int i = line.indexOf(CONTAINS);
            String ingredientString = line.substring(0, i);
            String[] ingredientArray = ingredientString.split(" ");
            ingredients = new HashSet<>(Arrays.asList(ingredientArray));
            String allergenString = line.substring(i + CONTAINS.length(), line.length() - 1);
            String[] allergenArray = allergenString.split(", ");
            allergens = new HashSet<>(Arrays.asList(allergenArray));
        }
    }
}
