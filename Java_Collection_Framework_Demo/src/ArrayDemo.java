import java.util.Vector;

public class ArrayDemo {

    Customer[] customers=new Customer[10];
    Vector<Customer> customers1=new Vector<>();


    public static void main(String[] args) {

        MyArrayList<Customer> myCustomers = new MyArrayList<>();

        myCustomers.add(new Customer("1", "John", "john@example.com"));
        myCustomers.add(new Customer("2", "Jane", "jane@example.com"));
        myCustomers.add(new Customer("3", "Bob", "bob@example.com"));

        System.out.println(myCustomers.get(0));

       for(int i=0; i<myCustomers.size(); i++){
           myCustomers.get(i);
           System.out.println(myCustomers.get(i));
       }
    }
}
