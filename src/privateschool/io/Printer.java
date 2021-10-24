package privateschool.io;

import privateschool.entities.Trainer;
import privateschool.entities.Student;
import privateschool.entities.Assignment;
import privateschool.entities.Course;
import java.util.Arrays;
import privateschool.DBMS.DBMS;

public abstract class Printer {//thn ebala abstract gia na mhn mporei na ftiaxtei antikeimeno ths to opoio den exei nohma

    public static int[] printWidths(Class c) {//einai to width gia thn ektypwsh kathe sthlhs analoga poios thn kalese
        int[] width;
        if (c == Course.class) {//an klithike apo thn klash Course
            width = new int[]{6, 25, 30, 30, 15, 20};
        } else if (c == Trainer.class) {//an klithike apo thn klash Trainer
            width = new int[]{6, 30, 30, 31};
        } else if (c == Student.class) {//an klithike apo thn klash Student
            width = new int[]{6, 30, 30, 15, 15};
        } else {//an klithike apo thn klash Assignment
            width = new int[]{6, 20, 40, 26, 10, 10};
        }
        return width;
    }

    public static String header(Class c) {// to header allazei analga me to poios thn kalese
        int width[] = printWidths(c);
        String line;
        if (c == Course.class) {
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|%5$-" + width[4] + "s|%6$-" + width[5] + "s|";
        } else if (c == Trainer.class) {
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|";
        } else if (c == Student.class) {
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|%5$-" + width[4] + "s|";
        } else {//einai gia to Assignment
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|%5$-" + width[4] + "s|%6$-" + width[5] + "s|";
        }
        return line;
    }

    public static String arrayDividers(Class c) {//epistrfei tis diakekommenes grammes tou pinaka analoga me ta widths
        int width[] = printWidths(c);//ta width epistrefontai analoga me to poia klash kalese thn printWidths
        String outlines[] = new String[width.length];
        String line;
        for (int i = 0; i < outlines.length; i++) {//ftiaxnei tis paules ------- gia kathe sthlh analoga me ta widths
            char[] chars = new char[width[i]];
            Arrays.fill(chars, '-');
            outlines[i] = new String(chars);
        }
        if (c == Course.class) {
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3], outlines[4], outlines[5]);
        } else if (c == Trainer.class) {
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3]);
        } else if (c == Student.class) {
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3], outlines[4]);
        } else {//einai gia to Assignment
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3], outlines[4], outlines[5]);
        }
        return line;
    }

    public static void divider(char c, int size) {//einai ta diaxwristika prints 
        String divider;
        int width = (size == 0) ? 200 : size;//an size=0 default timh 200
        char[] chars = new char[width];
        Arrays.fill(chars, c);
        divider = new String(chars);
        System.out.println(divider);
    }

    public static void printRelationshipsTableHeader(String label) {
        String dividers;
        switch (label) {
            case "oneCourse":
                Printer.divider('=', 0);
                System.out.println(DBMS.getDBMS().getwriteToFile() ? "Το μάθημα:" : red("Το μάθημα:"));
                dividers = Printer.arrayDividers(Course.class);//einai oi diakekommenes tou pinaka -------------
                System.out.println(dividers);
                System.out.println(String.format(Printer.header(Course.class), "Id", "Title", "Stream", "Type", "Start_Date", "End_Date"));
                System.out.println(dividers);
                break;
            case "manyStudent":
                System.out.println(DBMS.getDBMS().getwriteToFile() ? "Παρακολουθούν οι εξής μαθητές:" : red("Παρακολουθούν οι εξής μαθητές:"));
                dividers = Printer.arrayDividers(Student.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(Printer.header(Student.class), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
                System.out.println(dividers);
                break;
            case "manyTrainer":
                System.out.println(DBMS.getDBMS().getwriteToFile() ? "Διδάσκουν οι εξής εκπαιδευτές:" : red("Διδάσκουν οι εξής εκπαιδευτές:"));
                dividers = Printer.arrayDividers(Trainer.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(Printer.header(Trainer.class), "Id", "FirstName", "LastName", "Subject"));
                System.out.println(dividers);
                break;
            case "manyAssignment":
                System.out.println(DBMS.getDBMS().getwriteToFile() ? "Έχει τις εξής εργασίες:" : red("Έχει τις εξής εργασίες:"));
                dividers = Printer.arrayDividers(Assignment.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(Printer.header(Assignment.class), "Id", "Title", "Description", "SubDateTime", "OralMark", "TotalMark"));
                System.out.println(dividers);
                break;
            case "oneStudent"://oneStudent
                Printer.divider('=', 0);
                System.out.println(DBMS.getDBMS().getwriteToFile() ? "Ο μαθητής:" : red("Ο μαθητής:"));
                dividers = Printer.arrayDividers(Student.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(Printer.header(Student.class), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
                System.out.println(dividers);
                break;
            case "studentsToMoreThanOneCourse":
                Printer.divider('=', 0);
                System.out.println(red("Οι μαθητές που ανήκουν σε περισσότερα από ένα μαθήματα είναι:"));
                dividers = Printer.arrayDividers(Student.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(Printer.header(Student.class), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
                System.out.println(dividers);
                break;
            default://einai to minima gia ta assignments ths sygkekrimenhs ebdomadas (stelnw tis hmeromhnies xwrismenes me ,)
                String[] dates = label.split(",");
                System.out.println(red("Έχει να παραδώσει στο διάστημα από " + dates[0] + " έως και " + dates[1] + " τις εξής εργασίες:"));
                dividers = Printer.arrayDividers(Assignment.class);//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(Printer.header(Assignment.class), "Id", "Title", "Description", "SubDateTime", "OralMark", "TotalMark"));
                System.out.println(dividers);
        }
    }

    public static String red(String s) {
        return "\u001B[31m" + s + "\u001B[0m";
    }

    public static String green(String s) {
        return "\u001B[32m" + s + "\u001B[0m";
    }

}
