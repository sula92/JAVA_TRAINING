import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsUtilityDemo {
    public static void run() {
        DemoPrinter.section("8. Collections Utility Class");

        List<Integer> marks = new ArrayList<>();
        marks.add(75);
        marks.add(92);
        marks.add(68);
        marks.add(92);
        marks.add(81);

        System.out.println("Original marks: " + marks);

        Collections.sort(marks);
        System.out.println("Sorted marks: " + marks);

        Collections.reverse(marks);
        System.out.println("Reversed marks: " + marks);

        System.out.println("Max: " + Collections.max(marks));
        System.out.println("Min: " + Collections.min(marks));
        System.out.println("Frequency of 92: " + Collections.frequency(marks, 92));

        Collections.shuffle(marks);
        System.out.println("Shuffled marks: " + marks);

        List<Integer> readOnlyMarks = Collections.unmodifiableList(marks);
        System.out.println("Unmodifiable view: " + readOnlyMarks);
    }
}
