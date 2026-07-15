import java.util.*;

public class Employee {

    private  int id;
    private String name;
    private double salary;

    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public Employee() {
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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", salary=" + salary +"]";
    }

    public static void main(String[] args) {
        Employee emp1 = new Employee(1, "John Doe", 50000.0);
        Employee emp2 = new Employee(2, "Jane Smith", 60000.0);
        Employee emp3 = new Employee(2, "Jane Smith", 60000.0);
        Employee emp4 = new Employee(2, "Jane Smith", 60000.0);

        System.out.println(emp1);
        System.out.println(emp2);

        List<Employee> employees = new ArrayList<>();
        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);

        Collections.sort(employees, (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()) );
        //employees.forEach(System.out::println);

        Set<Employee> employeeSet = new HashSet<>(employees);
        employeeSet.add(emp4);

        employeeSet.forEach(System.out::println);

        Comparator<Employee> comparator = (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary());
        employees.sort(comparator);
        employees.forEach(System.out::println);
    }
}
