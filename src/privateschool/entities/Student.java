package privateschool.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import privateschool.DBMS.DBMS;
import privateschool.io.Printer;

public class Student {

    private final int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int tuitionFees;
    public static int currentIndex = 1;

    public Student(String firstName, String lastName, LocalDate dateOfBirth, int tuitionFees) {
        this.id = currentIndex++;//prwta xrhsimopoiw kai meta auxanw to id gia na xrhsimopoihthei gia ton epomeno
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.tuitionFees = tuitionFees;
    }

    public int getId() {
        return id;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getTuitionFees() {
        return tuitionFees;
    }

    public void setTuitionFees(int tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

    @Override
    public String toString() {
        return String.format(Printer.header(Student.class), id, firstName, lastName, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dateOfBirth), tuitionFees);
    }

    public static void printStudents(HashMap<Integer, Student> students) {
        System.out.println(DBMS.getDBMS().getwriteToFile() ? "Λίστα Μαθητών:" : Printer.red("Λίστα Μαθητών:"));
        String dividers = Printer.arrayDividers(Student.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
        System.out.println(dividers);
        System.out.println(String.format(Printer.header(Student.class), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
        System.out.println(dividers);
        for (Student s : students.values()) {
            System.out.println(s);
        }
        System.out.println(dividers);
    }
}
