package collections;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

public class Employee implements Comparable<Employee> {
    private Integer id;
    private String firstName;
    private String lastName;
    private String institution;
    private String position;
    private Integer salary;

    public Employee(Integer id, String firstName, String lastName, String institution, String position, Integer salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.institution = institution;
        this.position = position;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", institution='" + institution + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        Employee emp = (Employee) o;

        return this.id.equals(emp.id) && this.firstName.equals(emp.firstName) && this.lastName.equals(emp.institution) && this.position.equals(emp.institution) && this.salary.equals(emp.salary);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + this.firstName.hashCode() + this.lastName.hashCode() + this.institution.hashCode() + this.position.hashCode() + this.salary.hashCode() ;
    }

    @Override
    public int compareTo(Employee other) {
        if (this.id.compareTo(other.id) != 0) {
            return this.id.compareTo(other.id);
        }

        if (this.lastName.compareTo(other.lastName) != 0) {
            return this.lastName.compareTo(other.lastName);
        }

        if (this.firstName.compareTo(other.firstName) != 0) {
            return this.firstName.compareTo(other.firstName);
        }

        if (this.salary.compareTo(other.salary) != 0) {
            return this.salary.compareTo(other.salary);
        }

        return 0;
    }
}
