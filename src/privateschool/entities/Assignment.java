package privateschool.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import privateschool.DBMS.DBMS;
import privateschool.io.Printer;

public class Assignment {

    private final int id;
    private String title;
    private String description;
    private LocalDateTime subDateTime;
    private int oralMark;
    private int totalMark;
    private static int currentIndex = 1;

    public Assignment(String title, String description, LocalDateTime subDateTime, int oralMark, int totalMark) {
        this.id = currentIndex++;
        this.title = title;
        this.description = description;
        this.subDateTime = subDateTime;
        this.oralMark = oralMark;
        this.totalMark = totalMark;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getSubDateTime() {
        return subDateTime;
    }

    public void setSubDateTime(LocalDateTime subDateTime) {
        this.subDateTime = subDateTime;
    }

    public int getOralMark() {
        return oralMark;
    }

    public void setOralMark(int oralMark) {
        this.oralMark = oralMark;
    }

    public int getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(int totalMark) {
        this.totalMark = totalMark;
    }

    @Override
    public String toString() {
        return String.format(Printer.header(Assignment.class), id, title, description, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(subDateTime), oralMark, totalMark);
    }

    public static void printAssignments(HashMap<Integer, Assignment> assignments) {
        System.out.println(DBMS.getDBMS().getwriteToFile() ? "Λίστα Εργασιών:" : Printer.red("Λίστα Εργασιών:"));
        String dividers = Printer.arrayDividers(Assignment.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
        System.out.println(dividers);
        System.out.println(String.format(Printer.header(Assignment.class), "Id", "Title", "Description", "SubDateTime", "OralMark", "TotalMark"));
        System.out.println(dividers);
        for (Assignment a : assignments.values()) {
            System.out.println(a);
        }
        System.out.println(dividers);
    }
}
