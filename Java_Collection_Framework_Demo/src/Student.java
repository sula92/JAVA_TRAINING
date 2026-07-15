import java.util.*;

public class Student implements Comparable<Student> {
    private final int id;
    private final String name;
    private final double gpa;

    public Student(int id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Student)) {
            return false;
        }
        Student student = (Student) object;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', gpa=" + gpa + "}";
    }

    public static void main(String[] args) {
        Student student1 = new Student(1, "Alice", 3.8);
        Student student2 = new Student(2, "Bob", 3.5);
        Student student3 = new Student(1, "Charlie", 3.9);

        System.out.println(student1);
        System.out.println(student2);
        System.out.println(student3);

        System.out.println("student1 equals student2: " + student1.equals(student2));
        System.out.println("student1 equals student3: " + student1.equals(student3));

        System.out.println("Comparing student1 and student2: " + student1.compareTo(student2));
        System.out.println("Comparing student1 and student3: " + student1.compareTo(student3));

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        System.out.println("Students sorted by ID:");

        Set<Student> studentSet = new HashSet<>(students);
        studentSet.add(new Student(2, "David", 3.6));
        studentSet.add(new Student(3, "Eve", 3.7));
        studentSet.add(new Student(4, "Frank", 3.8));
        studentSet.add(new Student(4, "Grace", 3.9)); // Duplicate ID, should not be added
        studentSet.add(new Student(3, "nimal", 3.9)); // Duplicate ID, should not be added

        System.out.println("Unique students:");
        studentSet.forEach(System.out::println);

        students.sort(Comparator.naturalOrder());
        students.forEach(System.out::println);

    }
}
