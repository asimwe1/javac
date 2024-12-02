package collections;

import java.util.*;

public class Programs extends Employee {
    public Programs(Integer id, String firstName, String lastName, String institution, String position, Integer salary) {
        super(id, firstName, lastName, institution, position, salary);
    }

    public static void main(String[] args) {
        HashSet<Object> emp = new HashSet<>();

        emp.add(new Employee(1, "eric", "landry", "RCA", "developer", 5500000));
        emp.add(new Employee(2, "paulin", "niyobyose", "RCA", "cleaner", 20000));
        emp.add(new Employee(3, "prosper", "tenhag", "RCA", "analyst", 204000));
        emp.add(new Employee(4, "mary", "jose", "RCA", "adviser", 20000));
        emp.add(new Employee(5, "mike", "nibyose", "RCA", "trader", 20000));
        emp.add(new Employee(2, "paulin", "niyobyose", "RCA", "leaner", 20000));

        emp.add(new Employee(4, "gisa", "fred", "rca", "pm", 24053430));
        emp.add(new Employee(4, "gisa", "fred", "rca", "pm", 24053430));

//        Collections.sort(emp);

        for (Object emps : emp) {
            System.out.println(emps);
        }
    }
}
