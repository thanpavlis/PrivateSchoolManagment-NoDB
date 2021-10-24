package privateschool.io;

import privateschool.entities.Trainer;
import privateschool.entities.Student;
import privateschool.entities.Assignment;
import privateschool.entities.Course;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import privateschool.DBMS.DBMS;
import privateschool.entities.EntitiesRelationship;

public abstract class Input {

    private final static DBMS db = DBMS.getDBMS();
    private final static Scanner scan = new Scanner(System.in);

    public static int howInputData() {
        String choice;
        int ch;
        do {
            Printer.divider('=', 0);
            System.out.println(Printer.red("ΕΙΣΑΓΩΓΗ ΔΕΔΟΜΕΝΩΝ:"));
            System.out.println("1-ΠΛΗΚΤΡΟΛΟΓΙΟ");
            System.out.println("2-ΑΠΟ ΑΡΧΕΙΟ data.txt");
            System.out.println("0-Έξοδος");
            System.out.print("Δώσε αριθμό: ");
            choice = scan.nextLine().trim();
            ch = isNumberInLimits(choice, 0, 2);
            System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
        return ch;
    }

    public static void inputKeyboardData() {//kalw tis methodous gia thn eisagwgh twn dedomenwn apo to pliktrologio
        addCourses();
        addTrainers();
        addStudents();
        addAssignments();
        addStudentsPerCourse();
        addTrainersPerCourse();
        addAssignmentsPerCourse();
        addAssignmentsPerStudent();
        mainMenu();
    }

    public static void addCourses() {
        //EISAGWGH TWN COURSES
        int pl, i;
        boolean valid;
        String plithos, title, stream, type, start_date, end_date;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσα courses θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osa courses epelexe o xrhsths na prosthesei sthn arrayList
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε τίτλο για το " + i + "ο μάθημα: ");
                title = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(title.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (title.isEmpty());
            do {
                System.out.println("Δώσε το stream για το " + i + "ο μάθημα: ");
                stream = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(stream.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (stream.isEmpty());
            do {
                System.out.println("Δώσε τον τύπο για το " + i + "ο μάθημα: ");
                type = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(type.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (type.isEmpty());
            do {//elegxos hmeromhnias start_date
                System.out.println("Δώσε την ημερομηνία έναρξης για το " + i + "ο μάθημα (να είναι της μορφής dd/mm/yyyy): ");
                start_date = scan.nextLine().trim();
                valid = isValidDateStart(start_date);
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (!valid);
            do {//elegxos hmeromhnias end_date
                System.out.println("Δώσε την ημερομηνία λήξης για το " + i + "ο μάθημα (να είναι της μορφής dd/mm/yyyy): ");
                end_date = scan.nextLine().trim();
                valid = isValidDate(end_date);
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
                if (valid) {//an h hmeromhnia lixhs einai valid tote thn sigkrinw me thn enarxhs
                    valid = isEndDateAfterStart(start_date, end_date);
                    System.out.print((!valid) ? Printer.red("Η ημερομηνία λήξης δεν μπορεί να είναι ίδια ή πιο παλιά από την έναρξης !\n") : "");
                }
            } while (!valid);
            //h insterToDB kanei insert se oles tis listes analoga me to object και τοlabel pou tha tou dwsw
            db.insertToDB(new Course(title, stream, type, LocalDate.parse(start_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(end_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))), "");
            System.out.println(Printer.green("Το " + i + "ο μάθημα αποθηκεύτηκε επιτυχώς !"));
        }
    }

    public static void addTrainers() {
        //EISAGWGH TWN TRAINERS
        int pl, i;
        String plithos, firstName, lastName, subject;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσους trainers θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osous trainers epelexe o xrhsths na prosthesei sthn arrayList
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε όνομα για το " + i + "ο εκπαιδευτή: ");
                firstName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(firstName.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (firstName.isEmpty());
            do {
                System.out.println("Δώσε το επώνυμο για το " + i + "ο εκπαιδευτή: ");
                lastName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(lastName.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (lastName.isEmpty());
            do {
                System.out.println("Δώσε το θέμα για το " + i + "ο εκπαιδευτή: ");
                subject = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(subject.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (subject.isEmpty());
            //h insterToDB kanei insert se oles tis listes analoga me to object και τοlabel pou tha tou dwsw
            db.insertToDB(new Trainer(firstName, lastName, subject), "");
            System.out.println(Printer.green("Ο " + i + "ος εκπαιδευτής αποθηκεύτηκε επιτυχώς !"));
        }
    }

    public static void addStudents() {
        //EISAGWGH TWN STUDENTS
        boolean valid;
        int pl, i, tuitionFees;
        String fees, plithos, firstName, lastName, dateOfBirth;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσους students θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osous students epelexe o xrhsths na prosthesei sthn arrayList
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε όνομα για το " + i + "ο μαθητή: ");
                firstName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(firstName.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (firstName.isEmpty());
            do {
                System.out.println("Δώσε επώνυμο για το " + i + "ο μαθητή: ");
                lastName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(lastName.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (lastName.isEmpty());
            do {//elegxos hmeromhnias dateOfBirth
                System.out.println("Δώσε ημερομηνία γέννησης για το " + i + "ο μαθητή (να είναι της μορφής dd/mm/yyyy): ");
                dateOfBirth = scan.nextLine().trim();
                valid = isValidDateBirth(dateOfBirth);
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (!valid);
            do {
                System.out.println("Δώσε δίδακτρα για το " + i + "ο μαθητή: ");
                fees = scan.nextLine().trim();
                tuitionFees = isNumberBiggerThanLimit(fees, 0);
                System.out.print((tuitionFees == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (tuitionFees == -1);
            //h insterToDB kanei insert se oles tis listes analoga me to object και τοlabel pou tha tou dwsw
            db.insertToDB(new Student(firstName, lastName, LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy")), tuitionFees), "");
            System.out.println(Printer.green("Ο " + i + "ος μαθητής αποθηκεύτηκε επιτυχώς !"));
        }
    }

    public static void addAssignments() {
        //EISAGWGH TWN ASSIGNMENTS
        boolean valid;
        int pl, i, oralMark, totalMark;
        String mark, plithos, title, description, subDateTime;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσα assignemnts θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osous students epelexe o xrhsths na prosthesei
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε τίτλο για την " + i + "η εργασία: ");
                title = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(title.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (title.isEmpty());
            do {
                System.out.println("Δώσε περιγραφή για την " + i + "η εργασία: ");
                description = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(description.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (description.isEmpty());
            do {//elegxos hmeromhnias subDateTime
                System.out.println("Δώσε ημερομηνία και ώρα υποβολής για την " + i + "η εργασία (να είναι της μορφής dd/mm/yyyy HH:mm:ss): ");
                subDateTime = scan.nextLine().trim().replaceAll("\\s+", " ");
                valid = isValidDateTime(subDateTime);//elexei an einai se morfh pou exw orisei ws dateTime
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (!valid);
            do {
                System.out.println("Ο συνολικός βαθμός της εργασίας είναι με μέγιστο το 100.");
                System.out.println("π.χ. Αν δώσεις προφορικό 20/100, τότε αυτόματα το γραπτό μετράει για 80/100.");
                System.out.println("Δώσε το πόσο μετράει ο προφορικός βαθμός (στα 100) για την " + i + "η εργασία (τα προφορικά μπορεί να είναι και 0/100): ");
                mark = scan.nextLine().trim();
                oralMark = isNumberInLimits(mark, 0, 100);//mporei to oralMark na mhn paizei kapoio rolo
                System.out.print((oralMark == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (oralMark == -1);
            System.out.println("Εφόσον ο προφορικός μετράει για " + oralMark + "/100, o γραπτός βαθμός μετράει για " + (100 - oralMark) + "/100");
            totalMark = 100 - oralMark;
            //h insterToDB kanei insert se oles tis listes analoga me to object kai to label pou tha tou dwsw
            db.insertToDB(new Assignment(title, description, LocalDateTime.parse(subDateTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), oralMark, totalMark), "");
            System.out.println(Printer.green("Η " + i + "η εργασία αποθηκεύτηκε επιτυχώς !"));
        }
    }

    public static void addStudentsPerCourse() {
        //EISAGWGH TWN SYSXETISEWN studentsPerCourse
        int pl, i;
        boolean valid;
        String[] parts;
        String relationship, plithos;
        Printer.divider('=', 0);
        db.printEntitiesRelationship("studentsPerCourse");
        db.printListOfAll("courses");
        db.printListOfAll("students");
        if (db.getSizeOf("courses") > 0 && db.getSizeOf("students") > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις studentsPerCourse."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσα μαθήματα θέλεις να εισάγεις μαθητές (ο αριθμός δεν μπορεί να ξεπερνάει τα μαθήματα που έχεις εισάγει), δηλαδή " + db.getSizeOf("courses") + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, db.getSizeOf("courses"));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Μαθητών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkAddRelationshipIds("studentsPerCourse", relationship);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[0]), idsFromBrackets("[" + parts[1] + "]")), "studentsPerCourse");//kalw thn idsFromBrackets gia na mou epistrepsei ta ids prosthetw tis []
                    System.out.println(Printer.green("Η " + i + "η συσχέτιση studentsPerCourse αποθηκεύτηκε επιτυχώς !"));
                }
            }
        }
    }

    public static void addTrainersPerCourse() {
        //EISAGWGH TWN SYSXETISEWN trainersPerCourse
        int pl, i;
        boolean valid;
        String[] parts;
        String relationship, plithos;
        Printer.divider('=', 0);
        db.printEntitiesRelationship("trainersPerCourse");
        db.printListOfAll("courses");
        db.printListOfAll("trainers");
        if (db.getSizeOf("courses") > 0 && db.getSizeOf("trainers") > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις trainersPerCourse."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσα μαθήματα θέλεις να εισάγεις εκπαιδευτές (ο αριθμός δεν μπορεί να ξεπερνάει τα μαθήματα που έχεις εισάγει), δηλαδή " + db.getSizeOf("courses") + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, db.getSizeOf("courses"));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εκπαιδευτών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkAddRelationshipIds("trainersPerCourse", relationship);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[0]), idsFromBrackets("[" + parts[1] + "]")), "trainersPerCourse");//kalw thn idsFromBrackets gia na mou epistrepsei ta ids prosthetw tis []
                    System.out.println(Printer.green("Η " + i + "η συσχέτιση trainersPerCourse αποθηκεύτηκε επιτυχώς !"));
                }
            }
        }
    }

    public static void addAssignmentsPerCourse() {
        //EISAGWGH TWN SYSXETISEWN assignmentsPerCourse
        int pl, i;
        boolean valid;
        String[] parts;
        String relationship, plithos;
        Printer.divider('=', 0);
        db.printEntitiesRelationship("assignmentsPerCourse");
        db.printListOfAll("courses");
        db.printListOfAll("assignments");
        if (db.getSizeOf("courses") > 0 && db.getSizeOf("assignments") > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις assignmentsPerCourse."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσα μαθήματα θέλεις να εισάγεις εργασίες (ο αριθμός δεν μπορεί να ξεπερνάει τα μαθήματα που έχεις εισάγει), δηλαδή " + db.getSizeOf("courses") + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, db.getSizeOf("courses"));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkAddRelationshipIds("assignmentsPerCourse", relationship);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[0]), idsFromBrackets("[" + parts[1] + "]")), "assignmentsPerCourse");//kalw thn idsFromBrackets gia na mou epistrepsei ta ids prosthetw tis []
                    System.out.println(Printer.green("Η " + i + "η συσχέτιση assignmentsPerCourse αποθηκεύτηκε επιτυχώς !"));
                }
            }
        }
    }

    public static void addAssignmentsPerStudent() {
        int pl, i;
        boolean valid;
        String[] parts;
        String relationship, plithos;
        //EISAGWGH TWN SYSXETISEWN assignmentsPerStudent
        Printer.divider('=', 0);
        db.printEntitiesRelationship("assignmentsPerStudent");
        db.printListOfAll("students");
        db.printListOfAll("assignments");
        if (db.getSizeOf("students") > 0 && db.getSizeOf("assignments") > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις assignmentsPerStudent."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσους μαθητές θέλεις να εισάγεις εργασίες (ο αριθμός δεν μπορεί να ξεπερνάει τους μαθητές που έχεις εισάγει), δηλαδή " + db.getSizeOf("students") + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, db.getSizeOf("students"));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθητή (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkAddRelationshipIds("assignmentsPerStudent", relationship);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[0]), idsFromBrackets("[" + parts[1] + "]")), "assignmentsPerStudent");//kalw thn idsFromBrackets gia na mou epistrepsei ta ids prosthetw tis []
                    System.out.println(Printer.green("Η " + i + "η συσχέτιση assignmentsPerStudent αποθηκεύτηκε επιτυχώς !"));
                }
            }
        }
    }

    public static void inputTxtData() {
        String fileName = "data.txt";
        try {
            Scanner sc = new Scanner(new File(fileName));
            String line;
            String[] parts;
            while (sc.hasNextLine()) {//diabasma arxeiou data.txt
                line = sc.nextLine().trim();
                parts = line.split(",");//diaxwrismos grammhs me ton xarakthra ,
                for (int i = 0; i < parts.length; i++) {//trimarw ta kena kathe string pou xwrizetai me , kai epishs an yparxoun polla kena metaxy lexewn antikathistantai me ena keno " "
                    parts[i] = parts[i].trim().replaceAll("\\s+", " ");
                }
                switch (parts[0]) {//h prwth lexh ths kathe grammh mou leei se ti anaferetai h kathe grammh, to id ths kathe grammhs den to xrhsimopoiw to ebala gia na fainontai oi sisxetiseis pio eukola
                    case "course":
                        db.insertToDB(new Course(parts[2], parts[3], parts[4], LocalDate.parse(parts[5], DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(parts[6], DateTimeFormatter.ofPattern("dd/MM/yyyy"))), "");
                        break;
                    case "trainer":
                        db.insertToDB(new Trainer(parts[2], parts[3], parts[4]), "");
                        break;
                    case "student":
                        db.insertToDB(new Student(parts[2], parts[3], LocalDate.parse(parts[4], DateTimeFormatter.ofPattern("dd/MM/yyyy")), Integer.parseInt(parts[5])), "");
                        break;
                    case "assignment":
                        db.insertToDB(new Assignment(parts[2], parts[3], LocalDateTime.parse(parts[4], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), Integer.parseInt(parts[5]), Integer.parseInt(parts[6])), "");
                        break;
                    case "studentsPerCourse"://insert gia tis sisxetiseis
                        db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[1]), idsFromBrackets(parts[2])), "studentsPerCourse");
                        break;
                    case "trainersPerCourse":
                        db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[1]), idsFromBrackets(parts[2])), "trainersPerCourse");
                        break;
                    case "assignmentsPerCourse":
                        db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[1]), idsFromBrackets(parts[2])), "assignmentsPerCourse");
                        break;
                    default:
                        db.insertToDB(new EntitiesRelationship(Integer.parseInt(parts[1]), idsFromBrackets(parts[2])), "assignmentsPerStudent");
                }
            }
            sc.close();//kleisimo rohs pros arxeio
            Printer.divider('=', 0);
            System.out.println(Printer.green("Το " + fileName + " διαβάστηκε επιτυχώς !"));
        } catch (FileNotFoundException ex) {
            Printer.divider('=', 0);
            System.out.println(Printer.red("Το αρχείο " + fileName + " δεν βρέθηκε !"));
            System.exit(0);
        }
        mainMenu();
    }

    public static void mainMenu() {
        String choice;
        int ch;
        do {
            do {
                Printer.divider('=', 0);
                System.out.println(Printer.red("ΕΚΤΥΠΩΣΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("1--ΟΛΟΙ ΟΙ ΜΑΘΗΤΕΣ");
                System.out.println("2--ΟΛΟΙ ΟΙ ΕΚΠΑΙΔΕΥΤΕΣ");
                System.out.println("3--ΟΛΑ ΤΑ ΜΑΘΗΜΑΤΑ");
                System.out.println("4--ΟΛΕΣ ΟΙ ΕΡΓΑΣΙΕΣ");
                System.out.println("5--ΟΛΟΙ ΟΙ ΜΑΘΗΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("6--ΟΛΟΙ ΟΙ ΕΚΠΑΙΔΕΥΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("7--ΟΛΕΣ ΟΙ ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("8--ΟΛΕΣ ΟΙ ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΤΗ");
                System.out.println("9--ΜΑΘΗΤΕΣ ΠΟΥ ΑΝΗΚΟΥΝ ΣΕ ΠΕΡΙΣΣΟΤΕΡΑ ΑΠΟ ΕΝΑ ΜΑΘΗΜΑΤΑ");
                System.out.println("10-ΜΑΘΗΤΕΣ ΠΟΥ ΠΡΕΠΕΙ ΝΑ ΠΑΡΑΔΩΣΟΥΝ ΤΟΥΛΑΧΙΣΤΟΝ ΜΙΑ ΕΡΓΑΣΙΑ ΜΕΣΑ ΣΤΗΝ ΕΒΟΔΟΜΑΔΑ ΠΟΥ ΑΝΗΚΕΙ Η ΗΜΕΡΟΜΗΝΙΑ ΠΟΥ ΘΑ ΔΩΣΕΙΣ");
                System.out.println("11--ΕΞΑΓΩΓΗ ΣΤΙΓΜΙΟΤΥΠΟΥ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ ΣΕ ΑΝΑΓΝΩΣΙΜΟ ΑΡΧΕΙΟ PrivateSchoolDB_dd-MM-yyyy_HH_mm_ss.txt");
                System.out.println(Printer.red("ΠΡΟΣΘΗΚΗ - ΔΙΑΓΡΑΦΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("12--ΠΡΟΣΘΗΚΗ ΔΕΔΟΜΕΝΩΝ");
                System.out.println("13--ΔΙΑΓΡΑΦΗ ΔΕΔΟΜΕΝΩΝ");
                System.out.println("0--Έξοδος");
                System.out.print("Δώσε αριθμό: ");
                choice = scan.nextLine();
                choice = choice.trim();
                ch = isNumberInLimits(choice, 0, 13);
                System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
            switch (ch) {//analoga me thn epilogh tou xrhsth
                case 1://ektypwsh olwn twn mathitwn
                    db.printListOfAll("students");
                    break;
                case 2://etkypwsh olwn twn ekpaideutwn
                    db.printListOfAll("trainers");
                    break;
                case 3://ektypwsh olwn twn mathimatwn
                    db.printListOfAll("courses");
                    break;
                case 4://ektypwsh olws twn assignaments
                    db.printListOfAll("assignments");
                    break;
                case 5://oloi oi mathites ana mathima
                    db.printListOfAll("studentsPerCourse");
                    break;
                case 6://oloi oi ekpaideutes an mathima
                    db.printListOfAll("trainersPerCourse");
                    break;
                case 7://oles oi ergasies ana mathima
                    db.printListOfAll("assignmentsPerCourse");
                    break;
                case 8://oles oi ergasies ana mathith
                    db.printListOfAll("assignmentsPerStudent");
                    break;
                case 9://oloi oi mathites pou anhkoun se perissotera apo ena mathimata
                    if (db.getSizeOf("studentsPerCourse") > 0) {
                        db.printListOfAll("studentsToMoreThanOneCourse");
                    } else {
                        System.out.println(Printer.red("Δεν υπάρχουν συσχετίσεις studentsPerCourse !"));
                    }
                    break;
                case 10://ektypwsh mathitwn pou exoun na paradwsoun toulaxiston mia ergasia mesa sthn ebdomada pou anhkei h hmeromhnia pou tha dwsei o xrhsths
                    String date;
                    boolean valid;
                    if (db.getSizeOf("assignmentsPerStudent") > 0) {
                        do {
                            System.out.println("Δώσε ημερομηνία (dd/mm/yyyy) για την οποία θα εκυπωθούν οι μαθητές, οι οποίοι πρέπει να\n"
                                    + "παραδώσουν τουλάχιστον μια εργασία, εντός της εβδομάδας που ανήκει η ημερομηνία αυτή:");
                            date = scan.nextLine().trim();
                            valid = isValidDate(date);
                            System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
                        } while (!valid);
                        db.printListOfAll(date);//stelnw kateutheian thn hmeromhnia xwris kapoio label
                    } else {
                        System.out.println(Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerStudent !"));
                    }
                    break;
                case 11://export bashs se .txt arxeio
                    db.exportReadableDB();
                    break;
                case 12://prosthiki dedomenwn
                    addMenu();
                    break;
                case 13://diagrafh dedomenwn
                    removeMenu();
                    break;
                default:
                    System.out.println(Printer.red("Έξοδος προγράμματος!"));
                    System.exit(0);
            }
        } while (ch != 0);
    }

    public static void addMenu() {
        String choice;
        int ch;
        do {
            do {
                Printer.divider('=', 0);
                System.out.println(Printer.red("ΠΡΟΣΘΗΚΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("1--ΜΑΘΗΤΕΣ");
                System.out.println("2--ΕΚΠΑΙΔΕΥΤΕΣ");
                System.out.println("3--ΜΑΘΗΜΑΤΑ");
                System.out.println("4--ΕΡΓΑΣΙΕΣ");
                System.out.println("5--ΜΑΘΗΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("6--ΕΚΠΑΙΔΕΥΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("7--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("8--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΤΗ");
                System.out.println("0--ΠΙΣΩ");
                System.out.print("Δώσε αριθμό: ");
                choice = scan.nextLine();
                choice = choice.trim();
                ch = isNumberInLimits(choice, 0, 8);
                System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
            switch (ch) {//analoga me thn epilogh tou xrhsth
                case 1://prosthiki mathitwn
                    db.printListOfAll("students");
                    addStudents();
                    break;
                case 2://prosthiki ekpaideutwn
                    db.printListOfAll("trainers");
                    addTrainers();
                    break;
                case 3://prosthiki mathimatwn
                    db.printListOfAll("courses");
                    addCourses();
                    break;
                case 4://prosthiki assignments
                    db.printListOfAll("assignments");
                    addAssignments();
                    break;
                case 5://prosthiki mathites ana mathima
                    addStudentsPerCourse();
                    break;
                case 6://prosthiki ekpaideutes an mathima
                    addTrainersPerCourse();
                    break;
                case 7://prosthiki ergasies ana mathima
                    addAssignmentsPerCourse();
                    break;
                case 8://prosthiki ergasies ana mathith
                    addAssignmentsPerStudent();
                    break;
                default://pisw
                    System.out.println(Printer.red("ΠΙΣΩ"));
            }
        } while (ch != 0);
    }

    public static void removeMenu() {
        String choice;
        int ch;
        do {
            do {
                Printer.divider('=', 0);
                System.out.println(Printer.red("ΔΙΑΓΡΑΦΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("1--ΜΑΘΗΤΕΣ");
                System.out.println("2--ΕΚΠΑΙΔΕΥΤΕΣ");
                System.out.println("3--ΜΑΘΗΜΑΤΑ");
                System.out.println("4--ΕΡΓΑΣΙΕΣ");
                System.out.println("5--ΜΑΘΗΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("6--ΕΚΠΑΙΔΕΥΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("7--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("8--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΤΗ");
                System.out.println("0--ΠΙΣΩ");
                System.out.print("Δώσε αριθμό: ");
                choice = scan.nextLine();
                choice = choice.trim();
                ch = isNumberInLimits(choice, 0, 8);
                System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
            switch (ch) {//analoga me thn epilogh tou xrhsth
                case 1://diagrafh mathitwn
                    removeStudents();
                    break;
                case 2://diagrafh ekpaideutwn
                    removeTrainers();
                    break;
                case 3://diagrafh mathimatwn
                    removeCourses();
                    break;
                case 4://diagrafh assignments
                    removeAssignments();
                    break;
                case 5://diagrafh mathites ana mathima
                    removeStudentsPerCourse();
                    break;
                case 6://diagrafh ekpaideutes an mathima
                    removeTrainersPerCourse();
                    break;
                case 7://diagrafh ergasies ana mathima
                    removeAssignmentsPerCourse();
                    break;
                case 8://diagrafh ergasies ana mathith
                    removeAssignmentsPerStudent();
                    break;
                default://pisw
                    System.out.println(Printer.red("ΠΙΣΩ"));
            }
        } while (ch != 0);
    }

    public static void removeStudents() {
        //DIAGRAFH MATHITWN
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printListOfAll("students");
        if (db.getSizeOf("students") > 0) {
            do {
                System.out.println("Δώσε τα id_Μαθητών(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("students", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("students", relationship);
                System.out.println(Printer.green("Η διαγραφή μαθητών έγινε επιτυχώς !"));
            }
        }
    }

    public static void removeTrainers() {
        //DIAGRAFH EKPAIDEYTWN
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printListOfAll("trainers");
        if (db.getSizeOf("trainers") > 0) {
            do {
                System.out.println("Δώσε τα id_Εκπαιδευτών(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("trainers", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("trainers", relationship);
                System.out.println(Printer.green("Η διαγραφή εκπαιδευτών έγινε επιτυχώς !"));
            }
        }
    }

    public static void removeCourses() {
        //DIAGRAFH MATHIMATWN
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printListOfAll("courses");
        if (db.getSizeOf("courses") > 0) {
            do {
                System.out.println("Δώσε τα id_Μαθημάτων(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("courses", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("courses", relationship);
                System.out.println(Printer.green("Η διαγραφή μαθημάτων έγινε επιτυχώς !"));
            }
        }
    }

    public static void removeAssignments() {
        //DIAGRAFH ERGASIWN
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printListOfAll("assignments");
        if (db.getSizeOf("assignments") > 0) {
            do {
                System.out.println("Δώσε τα id_Μαθητών(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("assignments", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("assignments", relationship);
                System.out.println(Printer.green("Η διαγραφή εργασιών έγινε επιτυχώς !"));
            }
        }
    }

    public static void removeStudentsPerCourse() {
        //DIAGRAFH SYSXETISEWN studentsPerCourse
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printEntitiesRelationship("studentsPerCourse");
        db.printListOfAll("courses");
        db.printListOfAll("students");
        if (db.getSizeOf("studentsPerCourse") > 0) {//exw sysxetiseis gia diagrafh     
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Μαθητών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("studentsPerCourse", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("studentsPerCourse", relationship);
                System.out.println(Printer.green("Η διαγραφή της συσχέτισης studentsPerCourse έγινε επιτυχώς !"));
            }
        }
    }

    public static void removeTrainersPerCourse() {
        //DIAGRAFH SYSXETISEWN trainersPerCourse
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printEntitiesRelationship("trainersPerCourse");
        db.printListOfAll("courses");
        db.printListOfAll("trainers");
        if (db.getSizeOf("trainersPerCourse") > 0) {
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εκπαιδευτών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("trainersPerCourse", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("trainersPerCourse", relationship);
                System.out.println(Printer.green("Η διαγραφή της συσχέτισης trainersPerCourse έγινε επιτυχώς !"));
            }
        }
    }

    public static void removeAssignmentsPerCourse() {
        //DIAGRAFH SYSXETISEWN assignmentsPerCourse
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printEntitiesRelationship("assignmentsPerCourse");
        db.printListOfAll("courses");
        db.printListOfAll("assignments");
        if (db.getSizeOf("assignmentsPerCourse") > 0) {
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("assignmentsPerCourse", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("assignmentsPerCourse", relationship);
                System.out.println(Printer.green("Η διαγραφή της συσχέτισης assignmentsPerCourse έγινε επιτυχώς !"));
            }
        }
    }

    public static void removeAssignmentsPerStudent() {
        //DIAGRAFH SYSXETISEWN assignmentsPerStudent
        boolean valid;
        String relationship;
        Printer.divider('=', 0);
        db.printEntitiesRelationship("assignmentsPerStudent");
        db.printListOfAll("students");
        db.printListOfAll("assignments");
        if (db.getSizeOf("assignmentsPerStudent") > 0) {
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθητή (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : db.checkRemoveIds("assignmentsPerStudent", relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                db.deleteFromDB("assignmentsPerStudent", relationship);
                System.out.println(Printer.green("Η διαγραφή της συσχέτισης assignmentsPerStudent έγινε επιτυχώς !"));
            }
        }
    }

    public static boolean isValidDateBirth(String date) {//elegxei thn morfh ths 
        boolean valid = true;
        LocalDate d;
        try {
            d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if ((LocalDate.now().getYear() - d.getYear() <= 18) || (LocalDate.now().getYear() - d.getYear() >= 60)) {//ilikia apo [18 . . . 60]
                valid = false;
                System.out.println(Printer.red("Η ηλικία πρέπει να είναι από 18 έως και 60 χρονών !"));
            }
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    public static boolean isValidDateStart(String date) {
        boolean valid = true;
        LocalDate d;
        try {
            d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (d.isBefore(LocalDate.now())) {
                System.out.println(Printer.red("Η ημερομηνία έναρξης δεν μπορεί να είναι πριν από την σημερινή !"));
                valid = false;
            }
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    public static boolean isValidDate(String date) {
        boolean valid = true;
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    public static boolean isValidDateTime(String dateTime) {//elegxei thn morfh ths
        boolean valid = true;
        LocalDateTime d;
        try {
            d = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            if (d.isBefore(LocalDateTime.now())) {//den mporei h ypobolh na einai prin apo twra          
                valid = false;
                System.out.println(Printer.red("Δεν γίνεται η ημερομηνία υποβολής να είναι πιο πριν από την σημερινή !"));
            }
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    public static boolean isEndDateAfterStart(String start_date, String end_date) {//elegxei an h end_date einai pio meta apo thn start_date
        boolean valid = true;
        LocalDate start = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate end = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (end.isBefore(start) || end.isEqual(start)) {//an to end einai prin to start h' an einai idia
            valid = false;
        }
        return valid;
    }

    public static int isNumberInLimits(String number, int limitA, int limitB) {//elegxei an to number einai arithmos kai brisketai sto [limitA,limitB] an isxyei ton epistrefei alliws epistrefei -1
        int num;
        try {
            num = Integer.parseInt(number);
            if ((num < limitA) || (num > limitB)) {//exw apo ta limits
                num = -1;
            }
        } catch (NumberFormatException e) {//den einai arithmos
            num = -1;
        }
        return num;
    }

    public static int isNumberBiggerThanLimit(String number, int limitA) {//elegxei an to number einai arithmos kai einai megalyteros tou limitA alliws epistrefei -1
        int num;
        try {
            num = Integer.parseInt(number);
            if (num <= limitA) {//an einai katw apo to limitA
                num = -1;
            }
        } catch (NumberFormatException e) {//den einai arithmos
            num = -1;
        }
        return num;
    }

    public static ArrayList<Integer> idsFromBrackets(String s) {
        ArrayList<Integer> ids = new ArrayList<>();
        String[] parts;
        StringBuilder sb = new StringBuilder(s);
        sb = sb.deleteCharAt(0);//afairw to [
        sb = sb.deleteCharAt(sb.length() - 1);//afairw to ]
        parts = sb.toString().split("-");//kanw split me to -
        for (String temp : parts) {//metatroph se int twn ids kai eisagwgh sthn list
            ids.add(Integer.parseInt(temp));
        }
        return ids;
    }
}
