package collectionsComplete;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Student {

    private int id;
    private String name;
    private double gpa;

    public Student(int id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gpa);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        return id == other.id && name.equals(other.name) && gpa == other.gpa;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", gpa=" + gpa +"]";
    }

    public static void main(String[] args) {
        Student s1=new Student(1,"John",3.5);
        Student s2=new Student(2,"Alice",3.8);
        Student s3=new Student(1,"John",3.5);
        System.out.println(s1);
        System.out.println(s2);

        System.out.println(s1==s2);
        System.out.println(s1.equals(s2));
        System.out.println("..............................");
        System.out.println(s1==s3);
        System.out.println(s1.equals(s3));

        Set<Student> set = new HashSet<>();
        set.add(s1);
        set.add(s2);
        set.add(s3);
        System.out.println(set);
    }
}
