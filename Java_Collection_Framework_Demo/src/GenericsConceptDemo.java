import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericsConceptDemo {
    public static void run() {
        DemoPrinter.section("9. Generics and Type Safety");

        List<String> names = new ArrayList<>();
        names.add("Kamal");
        names.add("Nimal");

        //names.add(10); // This will cause a compile-time error because names is a List<Integer>

        String firstName = names.get(0);
        System.out.println("Generic List<String> gives String directly: " + firstName);

        List<Integer> numbers = Arrays.asList(10, 20, 30);
        printNumbers(numbers);

        List<Double> prices = Arrays.asList(10.5, 20.75, 30.0);
        printNumbers(prices);

        List<Object> objects = new ArrayList<>();
        addSampleValues(objects);
        System.out.println("Lower bounded wildcard result: " + objects);
    }

    private static void printNumbers(List<? extends Number> numbers) {
        System.out.println("Upper bounded wildcard can read numbers: " + numbers);
    }

    private static void addSampleValues(List<? super Integer> values) {
        values.add(100);
        values.add(200);
    }
}
