import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortingConceptDemo {
    public static void run() {
        DemoPrinter.section("7. Sorting with Comparable and Comparator");

        List<Student> students = new ArrayList<>();
        students.add(new Student(3, "Kamal", 3.4));
        students.add(new Student(1, "Amali", 3.9));
        students.add(new Student(2, "Nimal", 3.6));

        students.sort(null);
        System.out.println("Natural order using Comparable by id:");
        printStudents(students);

        students.sort(Comparator.comparing(Student::getName));
        System.out.println("Comparator order by name:");
        printStudents(students);

        students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        System.out.println("Comparator order by GPA descending:");
        printStudents(students);
    }

    private static void printStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println("  " + student);
        }
    }
}
