package privateschool.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import privateschool.DBMS.DBMS;
import privateschool.io.Printer;

public class Course {

    private final int id;
    private String title;
    private String stream;
    private String type;
    private LocalDate start_date;
    private LocalDate end_date;
    private static int currentIndex = 1;//gia to trexwn kathe fora id

    public Course(String title, String stream, String type, LocalDate start_date, LocalDate end_date) {
        this.id = currentIndex++;
        this.title = title;
        this.stream = stream;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
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

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return start_date;
    }

    public void setStartDate(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEndDate() {
        return end_date;
    }

    public void setEndDate(LocalDate end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return String.format(Printer.header(Course.class), id, title, stream, type, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(start_date), DateTimeFormatter.ofPattern("dd/MM/yyyy").format(end_date));
    }

    public static void printCourses(HashMap<Integer, Course> courses) {
        System.out.println(DBMS.getDBMS().getwriteToFile() ? "Λίστα Μαθημάτων:" : Printer.red("Λίστα Μαθημάτων:"));
        String dividers = Printer.arrayDividers(Course.class);//einai oi diakekommenes tou pinaka -------------
        System.out.println(dividers);
        System.out.println(String.format(Printer.header(Course.class), "Id", "Title", "Stream", "Type", "Start_Date", "End_Date"));
        System.out.println(dividers);
        for (Course c : courses.values()) {
            System.out.println(c);
        }
        System.out.println(dividers);
    }
}
