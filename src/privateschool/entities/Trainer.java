package privateschool.entities;

import java.util.HashMap;
import privateschool.DBMS.DBMS;
import privateschool.io.Printer;

public class Trainer {

    private final int id;
    private String firstName;
    private String lastName;
    private String subject;
    private static int currentIndex = 1;//gia to trexwn kathe fora id    

    public Trainer(String firstName, String lastName, String subject) {
        this.id = currentIndex++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return String.format(Printer.header(Trainer.class), id, firstName, lastName, subject);
    }

    public static void printTrainers(HashMap<Integer, Trainer> trainers) {
        System.out.println(DBMS.getDBMS().getwriteToFile() ? "Λίστα Εκπαιδευτών:" : Printer.red("Λίστα Εκπαιδευτών:"));
        String dividers = Printer.arrayDividers(Trainer.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
        System.out.println(dividers);
        System.out.println(String.format(Printer.header(Trainer.class), "Id", "FirstName", "LastName", "Subject"));
        System.out.println(dividers);
        for (Trainer t : trainers.values()) {
            System.out.println(t);
        }
        System.out.println(dividers);
    }
}
