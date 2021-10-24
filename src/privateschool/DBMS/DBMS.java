
package privateschool.DBMS;

import privateschool.entities.EntitiesRelationship;
import privateschool.io.Printer;
import privateschool.io.Input;
import privateschool.entities.Trainer;
import privateschool.entities.Student;
import privateschool.entities.Assignment;
import privateschool.entities.Course;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBMS {

    private static DBMS dbms;//mporei na yparxei mono ena antikeimeno typou DBMS, alliws epistrefetai panta to reference se auto
    private boolean writeToFile = false;//thn xrhsimopoiw gia thn eggrafh sto arxeio 
    //kathe fora pou prosthetw antikeimeno sthn antistoixh lista dinw kai to diko tou id
    private int currentStudentsPerCourseId;
    private int currentTrainersPerCourseId;
    private int currentAssignmentsPerCourseId;
    private int currentAssignmentsPerStudentId;

    //oles oi anexarthtes ontothtes pou zhtaei h askhsh to key einai to id 
    private HashMap<Integer, Course> courses;
    private HashMap<Integer, Trainer> trainers;
    private HashMap<Integer, Student> students;
    private HashMap<Integer, Assignment> assignments;

    //oles oi sxeseis metaxy twn ontothtwn pou zhtaei h askhsh 
    private HashMap<Integer, EntitiesRelationship> studentsPerCourse;
    private HashMap<Integer, EntitiesRelationship> trainersPerCourse;
    private HashMap<Integer, EntitiesRelationship> assignmentsPerCourse;
    private HashMap<Integer, EntitiesRelationship> assignmentsPerStudent;

    private DBMS() {
        //oles oi anexarthtes ontothtes pou zhtaei h askhsh
        courses = new HashMap<>();
        trainers = new HashMap<>();
        students = new HashMap<>();
        assignments = new HashMap<>();

        //oles oi sxeseis metaxy twn ontothtwn pou zhtaei h askhsh
        studentsPerCourse = new HashMap<>();
        trainersPerCourse = new HashMap<>();
        assignmentsPerCourse = new HashMap<>();
        assignmentsPerStudent = new HashMap<>();

        //arxikopoihsh twn ids
        currentStudentsPerCourseId = 1;
        currentTrainersPerCourseId = 1;
        currentAssignmentsPerCourseId = 1;
        currentAssignmentsPerStudentId = 1;
    }

    public static DBMS getDBMS() {
        if (dbms == null) {
            dbms = new DBMS();
        }
        return dbms;
    }

    public boolean getwriteToFile() {
        return writeToFile;
    }

    public void initializeDB() {
        switch (Input.howInputData()) {
            case 1://eisagwgh apo to pliktrologio
                Input.inputKeyboardData();
                break;
            case 2://eisagwgh apo to data.txt
                Input.inputTxtData();
                break;
            default://exodos
                System.out.println(Printer.red("Έξοδος προγράμματος!"));
                System.exit(0);
        }
    }

    public void insertToDB(Object o, String label) {//analoga me to se poia lista tha kanw insert to label to xreiazomai gia tis listes me tis sisxetiseis
        if (o instanceof Course) {
            courses.put(((Course) o).getId(), (Course) o);
        } else if (o instanceof Trainer) {
            trainers.put(((Trainer) o).getId(), (Trainer) o);
        } else if (o instanceof Student) {
            students.put(((Student) o).getId(), (Student) o);
        } else if (o instanceof Assignment) {
            assignments.put(((Assignment) o).getId(), (Assignment) o);
        } else {//sisxetish
            Iterator<EntitiesRelationship> iter;
            boolean found = false;
            EntitiesRelationship eRel;
            switch (label) {
                case "studentsPerCourse":
                    iter = studentsPerCourse.values().iterator();
                    break;
                case "trainersPerCourse":
                    iter = trainersPerCourse.values().iterator();
                    break;
                case "assignmentsPerCourse":
                    iter = assignmentsPerCourse.values().iterator();
                    break;
                default://assignmentPerStudent
                    iter = assignmentsPerStudent.values().iterator();
            }
            while (iter.hasNext() && found == false) {
                eRel = iter.next();
                if (eRel.getEntitieId() == ((EntitiesRelationship) o).getEntitieId()) {//yparxei hdh kataxwrhsh me to id tou Student
                    found = true;
                    for (int i : ((EntitiesRelationship) o).getIds()) {//gia kathe id pou thelw na prosthesw
                        eRel.addListIds(i);//prosthetw ta ids sthn grammh ths hash pou yparxei gia na mhn exw diploeggrafes
                    }
                }
            }
            switch (label) {
                case "studentsPerCourse":
                    if (found == false) {//se periptwsh pou h sisxetish gia to sigkekrimeno mathima den iparxei hdh
                        ((EntitiesRelationship) o).setRowId(currentStudentsPerCourseId);//setarw to antistoixo rowId analoga me to se poia list tha kanw insert
                        studentsPerCourse.put(currentStudentsPerCourseId++, (EntitiesRelationship) o);//xrhsimopoiw to RowId kai to auxanw kata ena gia to epomeno instert
                    }
                    break;
                case "trainersPerCourse":
                    if (found == false) {//se periptwsh pou h sisxetish gia to sigkekrimeno mathima den iparxei hdh
                        ((EntitiesRelationship) o).setRowId(currentTrainersPerCourseId);
                        trainersPerCourse.put(currentTrainersPerCourseId++, (EntitiesRelationship) o);
                    }
                    break;
                case "assignmentsPerCourse":
                    if (found == false) {//se periptwsh pou h sisxetish gia to sigkekrimeno mathima den iparxei hdh
                        ((EntitiesRelationship) o).setRowId(currentAssignmentsPerCourseId);
                        assignmentsPerCourse.put(currentAssignmentsPerCourseId++, (EntitiesRelationship) o);
                    }
                    break;
                default://assignmentsPerStudent
                    if (found == false) {//se periptwsh pou h sisxetish gia to sigkekrimeno student den iparxei hdh
                        ((EntitiesRelationship) o).setRowId(currentAssignmentsPerStudentId);
                        assignmentsPerStudent.put(currentAssignmentsPerStudentId++, (EntitiesRelationship) o);
                    }
            }

        }
    }

    public int getSizeOf(String label) {
        int s;
        switch (label) {
            case "courses":
                s = courses.size();
                break;
            case "trainers":
                s = trainers.size();
                break;
            case "students":
                s = students.size();
                break;
            case "assignments":
                s = assignments.size();
                break;
            case "studentsPerCourse":
                s = studentsPerCourse.size();
                break;
            case "trainersPerCourse":
                s = trainersPerCourse.size();
                break;
            case "assignmentsPerCourse":
                s = assignmentsPerCourse.size();
                break;
            default://assignmentsPerStudent
                s = assignmentsPerStudent.size();
                break;

        }
        return s;
    }

    public boolean checkAddRelationshipIds(String label, String relationship) {
        String[] parts;
        String[] parts2;
        int[] refIds;
        int entitieId, i, j;
        EntitiesRelationship eRel;
        Iterator<EntitiesRelationship> iter = null;
        parts = relationship.split("->");
        boolean isValid = true;
        if (parts.length == 2) {//an den spaei se dio parts analoga me thn morfh pou ypedeixa kanw return false
            entitieId = Input.isNumberBiggerThanLimit(parts[0], 0);//to parts[0] einai to entitieId analoga thn periptwsh, tsekarw an einai arithmos kai megalyteros tou 0
            if (entitieId == -1) {
                isValid = false;
            } else {
                parts2 = parts[1].split("-");//polla h ena id p.x. 1-2-3
                refIds = new int[parts2.length];
                i = 0;
                while ((i < parts2.length) && isValid) {//gia kathe id tha psaxw na dw an einai arithmos kai megalyteros tou 0
                    if (Input.isNumberBiggerThanLimit(parts2[i], 0) != -1) {
                        refIds[i] = Input.isNumberBiggerThanLimit(parts2[i], 0);
                        i++;
                    } else {
                        isValid = false;
                    }
                }
                if (isValid && (refIds.length >= 2)) {//tsekarw an o xrhsths edwse dipla ids
                    i = 0;
                    while ((i < refIds.length) && isValid) {
                        j = i + 1;
                        while ((j < refIds.length) && isValid) {
                            if (refIds[i] == refIds[j]) {//yparxei koino stoixei ston pinaka
                                isValid = false;
                            }
                            j++;
                        }
                        i++;
                    }
                }
                if (isValid) {//exei perasei tous parapanw elegxous
                    switch (label) { //π.χ. 10->1-2-3-4 => entitiesId->refIds
                        case "studentsPerCourse":
                            if (courses.containsKey(entitieId)) {//ean to entitieid -> id tou Course yparxei 
                                for (int refId : refIds) {//ean kapoio id tou Student den yparxei to isValid ginetai false
                                    if (!students.containsKey(refId)) {
                                        isValid = false;
                                    }
                                }
                            } else {
                                isValid = false;
                            }
                            if (isValid) {//arxikopoiw ton iterator wste meta to switch na kanw elegxo gia to an gia kapoio apot a refIds yparxei hdh sysxetish
                                iter = studentsPerCourse.values().iterator();
                            }
                            break;
                        case "trainersPerCourse":
                            if (courses.containsKey(entitieId)) {//ean to entitieid -> id tou Course yparxei 
                                for (int refId : refIds) {//ean kapoio id tou Trainer den yparxei to isValid ginetai false
                                    if (!trainers.containsKey(refId)) {
                                        isValid = false;
                                    }
                                }
                            } else {
                                isValid = false;
                            }
                            if (isValid) {//arxikopoiw ton iterator wste meta to switch na kanw elegxo gia to an gia kapoio apot a refIds yparxei hdh sysxetish
                                iter = trainersPerCourse.values().iterator();
                            }
                            break;
                        case "assignmentsPerCourse":
                            if (courses.containsKey(entitieId)) {//ean to entitieid -> id tou Course yparxei 
                                for (int refId : refIds) {//ean kapoio id tou Assignment den yparxei to isValid ginetai false
                                    if (!assignments.containsKey(refId)) {
                                        isValid = false;
                                    }
                                }
                            } else {
                                isValid = false;
                            }
                            if (isValid) {//arxikopoiw ton iterator wste meta to switch na kanw elegxo gia to an gia kapoio apot a refIds yparxei hdh sysxetish
                                iter = assignmentsPerCourse.values().iterator();
                            }
                            break;
                        default://assignmentsPerStudent
                            if (students.containsKey(entitieId)) {//ean to entitieid -> id tou Student yparxei 
                                for (int refId : refIds) {//ean kapoio id tou Assignment den yparxei to isValid ginetai false
                                    if (!assignments.containsKey(refId)) {
                                        isValid = false;
                                    }
                                }
                            } else {
                                isValid = false;
                            }
                            if (isValid) {//arxikopoiw ton iterator wste meta to switch na kanw elegxo gia to an gia kapoio apot a refIds yparxei hdh sysxetish
                                iter = assignmentsPerStudent.values().iterator();
                            }
                    }
                }
                if (isValid) {
                    while (iter.hasNext() && isValid) {//edw elegxw an gia kapoio apo ta ids yparxei hdh h sysxetish
                        eRel = iter.next();//gia kathe entitieRelationship analoga apo to switch
                        if (eRel.getEntitieId() == entitieId) {//an to id yparxei hdh kataxwrhmeno
                            i = 0;
                            while ((i < refIds.length) && isValid) {
                                if (eRel.getIds().contains(refIds[i])) {//an h sysxetish yparxei hdh gia to trexwn refId
                                    isValid = false;
                                    System.out.println(Printer.red("Η συσχέτιση υπάρχει ήδη για κάποιο από τα id που έδωσες !"));
                                }
                                i++;
                            }
                        }
                    }
                }
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

    public boolean checkRemoveIds(String label, String relationship) {
        String[] parts;
        String[] parts2;
        int[] refIds;
        int entitieId, i, j;
        EntitiesRelationship eRel;
        Iterator<EntitiesRelationship> iter;
        boolean isValid = true, found = false;
        if (label.equals("students") || label.equals("trainers") || label.equals("courses") || label.equals("assignments")) {//autes einai oi anexarthtes ontothtes
            parts = relationship.split("-");//p.x. 1-2-3
            refIds = new int[parts.length];
            i = 0;
            while ((i < parts.length) && isValid) {//gia kathe id tha psaxw na dw an einai arithmos kai megalyteros tou 0
                if (Input.isNumberBiggerThanLimit(parts[i], 0) != -1) {
                    refIds[i] = Input.isNumberBiggerThanLimit(parts[i], 0);
                    i++;
                } else {
                    isValid = false;
                }
            }
            if (isValid && (refIds.length >= 2)) {//tsekarw an o xrhsths edwse dipla ids
                i = 0;
                while ((i < refIds.length) && isValid) {
                    j = i + 1;
                    while ((j < refIds.length) && isValid) {
                        if (refIds[i] == refIds[j]) {//yparxei koino stoixei ston pinaka
                            isValid = false;
                        }
                        j++;
                    }
                    i++;
                }
            }
            if (isValid) {//exei perasei tous parapanw elegxous
                switch (label) { //π.χ. refIds => 1-2-3 
                    case "students":
                        for (int id : refIds) {
                            if (!students.containsKey(id)) {//an kapoio apo ta refIds den yparxei
                                isValid = false;
                            }
                        }
                        break;
                    case "trainers":
                        for (int id : refIds) {
                            if (!trainers.containsKey(id)) {//an kapoio apo ta refIds den yparxei
                                isValid = false;
                            }
                        }
                        break;
                    case "courses":
                        for (int id : refIds) {
                            if (!courses.containsKey(id)) {//an kapoio apo ta refIds den yparxei
                                isValid = false;
                            }
                        }
                        break;
                    default://assignments
                        for (int id : refIds) {
                            if (!assignments.containsKey(id)) {//an kapoio apo ta refIds den yparxei
                                isValid = false;
                            }
                        }
                }
            }
        } else {//alliws prepei na afairesw kapoia apo tis sisxetiseis ths morfhs entitie_id - > refIds p.x. 10->1-2-3
            parts = relationship.split("->");
            if (parts.length == 2) {//an den spaei se dio parts analoga me thn morfh pou ypedeixa kanw return false
                entitieId = Input.isNumberBiggerThanLimit(parts[0], 0);//to parts[0] einai to entitieId analoga thn periptwsh, tsekarw an einai arithmos kai megalyteros tou 0
                if (entitieId == -1) {
                    isValid = false;
                } else {
                    parts2 = parts[1].split("-");//polla h ena id p.x. 1-2-3
                    refIds = new int[parts2.length];
                    i = 0;
                    while ((i < parts2.length) && isValid) {//gia kathe id tha psaxw na dw an einai arithmos kai megalyteros tou 0
                        if (Input.isNumberBiggerThanLimit(parts2[i], 0) != -1) {
                            refIds[i] = Input.isNumberBiggerThanLimit(parts2[i], 0);
                            i++;
                        } else {
                            isValid = false;
                        }
                    }
                    if (isValid && (refIds.length >= 2)) {//tsekarw an o xrhsths edwse dipla ids
                        i = 0;
                        while ((i < refIds.length) && isValid) {
                            j = i + 1;
                            while ((j < refIds.length) && isValid) {
                                if (refIds[i] == refIds[j]) {//yparxei koino stoixei ston pinaka
                                    isValid = false;
                                }
                                j++;
                            }
                            i++;
                        }
                    }
                    if (isValid) {//exei perasei tous parapanw elegxous
                        switch (label) {//analoga se poia sxesh thelw na psaxw gia na vrw an oi sysxetiseis pou edwse o xrhsths yparxoun
                            case "studentsPerCourse":
                                iter = studentsPerCourse.values().iterator();
                                break;
                            case "trainersPerCourse":
                                iter = trainersPerCourse.values().iterator();
                                break;
                            case "assignmentsPerCourse":
                                iter = assignmentsPerCourse.values().iterator();
                                break;
                            default://assignmentsPerStudent   
                                iter = assignmentsPerStudent.values().iterator();
                        }
                        while (iter.hasNext() && isValid) { //π.χ. 10->1-2-3-4 => entitieId -> refIds
                            eRel = iter.next();
                            if (eRel.getEntitieId() == entitieId) {//ean to entitieId -> id yparxei kataxwrhmeno sthn antistoixh sysxetish
                                found = true;//gia na xerw an exw vrei to mathima
                                for (int refId : refIds) {//gia ola ta ids pou thelw na diagrapsw
                                    if (!eRel.getIds().contains(refId)) {//ean den periexei estw kai ena apo ta id pou thelw na diagrapsw
                                        isValid = false;
                                    }
                                }
                            }
                        }
                    }
                    isValid = isValid && found;//to found to thelw gia na xerw an yparxei kataxwrhsh me to entitieId() pou zhthse o xrhsths
                }
            } else {
                isValid = false;
            }
        }
        return isValid;
    }

    public void deleteFromDB(String label, String ids) {
        ArrayList<Integer> idsRemove = new ArrayList<>();
        Iterator<EntitiesRelationship> iter;
        EntitiesRelationship eRel;
        String[] idS = null;
        String[] parts;
        String[] parts2;
        int entitieId = 0, i;
        int[] idRe = null;
        if (label.equals("students") || label.equals("courses") || label.equals("trainers") || label.equals("assignments")) {//anexarthtes ontothtes ids ths morfhs p.x. 1-2-3
            idS = ids.split("-");
        } else {//einai ths morfhs entitieId -> refIds
            parts = ids.split("->");
            parts2 = parts[1].split("-");
            entitieId = Integer.parseInt(parts[0]);
            idRe = new int[parts2.length];
            i = 0;
            while (i < parts2.length) {
                idRe[i] = Integer.parseInt(parts2[i]);
                i++;
            }
        }
        switch (label) {
            case "students"://diagrafh mathitwn, ektos apo ton pinaka twn mathitwn, prepei na psaxw se oles tis sxeseis an xrhsimopoieitai o kathe mathith pou thelw na diagrapsw wste na ton svhsw kai apo ekei         
                for (String id : idS) {//diagrafw tous mathites
                    students.remove(Integer.parseInt(id));
                }
                iter = assignmentsPerStudent.values().iterator();
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {//diagrafw ta assignments tou kathe mathith pou diegrapsa prin
                        if (eRel.getEntitieId() == Integer.parseInt(id)) {
                            idsRemove.add(eRel.getRowId());
                        }
                    }
                }
                for (int rID : idsRemove) {
                    assignmentsPerStudent.remove(rID);
                }
                idsRemove.clear();//adeiasma twn ids gia diagrafh
                iter = studentsPerCourse.values().iterator();
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {
                        if (eRel.getIds().contains(Integer.parseInt(id))) {//an parakolouthouse to mathima 
                            eRel.removeListIds(Integer.parseInt(id));
                        }
                        if (eRel.getIds().isEmpty()) {//se periptwsh pou den exei allous mathites to mathima tote den yparxei logos na yparxei h sysxetish
                            idsRemove.add(eRel.getRowId());//ta rowIds() pou tha afairesw apo tis sisxetiseis wste na mhn yparxei sysxetish xwris kanena mathith
                        }
                    }
                }
                for (int rID : idsRemove) {
                    studentsPerCourse.remove(rID);
                }
                break;
            case "trainers"://diagrafh ekpaideutwn
                for (String id : idS) {//diagrafw tous ekpaideutes
                    trainers.remove(Integer.parseInt(id));
                }
                iter = trainersPerCourse.values().iterator();
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {
                        if (eRel.getIds().contains(Integer.parseInt(id))) {//an didaskei to mathima
                            eRel.removeListIds(Integer.parseInt(id));
                        }
                        if (eRel.getIds().isEmpty()) {//se periptwsh pou den exei allous ekpaideutes to mathima tote den yparxei logos na yparxei h sysxetish
                            idsRemove.add(eRel.getRowId());//ta rowIds() pou tha afairesw apo tis sisxetiseis wste na mhn yparxei sysxetish xwris kanena mathith
                        }
                    }
                }
                for (int rID : idsRemove) {
                    trainersPerCourse.remove(rID);
                }
                break;
            case "courses":
                for (String id : idS) {//diagrafw ta mathimata
                    courses.remove(Integer.parseInt(id));
                }
                iter = studentsPerCourse.values().iterator();//diagrafh sysxetishs mathima kai mathites
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {
                        if (eRel.getEntitieId() == Integer.parseInt(id)) {
                            idsRemove.add(eRel.getRowId());
                        }
                    }
                }
                for (int rID : idsRemove) {
                    studentsPerCourse.remove(rID);
                }
                idsRemove.clear();//adeiasma listas sthn opoia krataw ta ids gia diagrafh
                iter = trainersPerCourse.values().iterator();//diagrafh sysxetishs mathima kai ekpaideutwn
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {
                        if (eRel.getEntitieId() == Integer.parseInt(id)) {
                            idsRemove.add(eRel.getRowId());
                        }
                    }
                }
                for (int rID : idsRemove) {
                    trainersPerCourse.remove(rID);
                }
                idsRemove.clear();
                iter = assignmentsPerCourse.values().iterator();//diagrafh mathima kai ergasiwn
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {//diagrafw ta assignments tou kathe mathith pou diegrapsa prin
                        if (eRel.getEntitieId() == Integer.parseInt(id)) {
                            idsRemove.add(eRel.getRowId());
                        }
                    }
                }
                for (int rID : idsRemove) {
                    assignmentsPerCourse.remove(rID);
                }
                break;
            case "assignments":
                for (String id : idS) {//diagrafw tis ergasies
                    assignments.remove(Integer.parseInt(id));
                }
                iter = assignmentsPerCourse.values().iterator();//diagrafw tis ergasies pou zhthse o xrthsths ean anhkan se kapoio mathima
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {
                        if (eRel.getIds().contains(Integer.parseInt(id))) {//an to mathima eixe thn ergasia
                            eRel.removeListIds(Integer.parseInt(id));
                        }
                        if (eRel.getIds().isEmpty()) {//se periptwsh pou den exei alles ergasies to mathima tote den yparxei logos na yparxei h sysxetish
                            idsRemove.add(eRel.getRowId());//ta rowIds() pou tha afairesw apo tis sisxetiseis wste na mhn yparxei sysxetish xwris kamia ergasia
                        }
                    }
                }
                for (int rID : idsRemove) {
                    assignmentsPerCourse.remove(rID);
                }
                idsRemove.clear();
                iter = assignmentsPerStudent.values().iterator();//diagrafw tis ergasies pou zhthse o xrthsths ean anhkan se kapoio mathith
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (String id : idS) {
                        if (eRel.getIds().contains(Integer.parseInt(id))) {//an o mathiths eixe thn ergasia
                            eRel.removeListIds(Integer.parseInt(id));
                        }
                        if (eRel.getIds().isEmpty()) {//se periptwsh pou den exei alles ergasies o mathiths tote den yparxei logos na yparxei h sysxetish
                            idsRemove.add(eRel.getRowId());//ta rowIds() pou tha afairesw apo tis sisxetiseis wste na mhn yparxei sysxetish xwris kamia ergasia
                        }
                    }
                }
                for (int rID : idsRemove) {
                    assignmentsPerStudent.remove(rID);
                }
                break;
            default://thelw na afairesw sysxetish
                switch (label) {
                    case "studentsPerCourse":
                        iter = studentsPerCourse.values().iterator();
                        break;
                    case "trainersPerCourse":
                        iter = trainersPerCourse.values().iterator();
                        break;
                    case "assignmentsPerCourse":
                        iter = assignmentsPerCourse.values().iterator();
                        break;
                    default://assignmentsPerStudent                   
                        iter = assignmentsPerStudent.values().iterator();
                }
                while (iter.hasNext()) {
                    eRel = iter.next();
                    for (int id : idRe) {
                        if (eRel.getEntitieId() == entitieId) {//an einai to entitieId() gia th sysxetish pou me endiaferei einai idio me auto pou edwse o xrhsths
                            if (eRel.getIds().contains(id)) {//an to id pou thelei o xrhsths na afairesei yparxei sthn lista tote to afairw
                                eRel.removeListIds(id);
                            }
                            if (eRel.getIds().isEmpty()) {//se periptwsh pou den exei alles ergasies o mathiths tote den yparxei logos na yparxei h sysxetish
                                idsRemove.add(eRel.getRowId());//ta rowIds() pou tha afairesw apo tis sisxetiseis wste na mhn yparxei sysxetish xwris kamia ergasia
                            }
                        }
                    }
                }
                switch (label) {//an exei meinie kapoia sysxetish mono me to entitie id, exw krathsei ta rowIds() twn grammwn kai tis afairw
                    case "studentsPerCourse":
                        for (int rID : idsRemove) {
                            studentsPerCourse.remove(rID);
                        }
                        break;
                    case "trainersPerCourse":
                        for (int rID : idsRemove) {
                            trainersPerCourse.remove(rID);
                        }
                        break;
                    case "assignmentsPerCourse":
                        for (int rID : idsRemove) {
                            assignmentsPerCourse.remove(rID);
                        }
                        break;
                    default:
                        //assignmentsPerStudent
                        for (int rID : idsRemove) {
                            assignmentsPerStudent.remove(rID);
                        }
                }
        }
    }

    public void printListOfAll(String value) {//analoga to ti thelw na ektipwsw
        switch (value) {
            case "courses":
                if (courses.size() > 0) {
                    Course.printCourses(courses);
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν μαθήματα !" : Printer.red("Δεν υπάρχουν μαθήματα !"));
                }
                break;
            case "trainers":
                if (trainers.size() > 0) {
                    Trainer.printTrainers(trainers);
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν εκπαιδευτές !" : Printer.red("Δεν υπάρχουν εκπαιδευτές !"));
                }
                break;
            case "students":
                if (students.size() > 0) {
                    Student.printStudents(students);
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν μαθητές !" : Printer.red("Δεν υπάρχουν μαθητές !"));
                }
                break;
            case "assignments":
                if (assignments.size() > 0) {
                    Assignment.printAssignments(assignments);
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν εργασίες !" : Printer.red("Δεν υπάρχουν εργασίες !"));
                }
                break;
            case "studentsPerCourse":
                if (studentsPerCourse.size() > 0) {
                    printEntitiesRelationship("studentsPerCourse");
                    System.out.println(writeToFile ? "Οι συσχετίσεις studentsPerCourse είναι:" : Printer.red("Οι συσχετίσεις studentsPerCourse είναι:"));
                    for (EntitiesRelationship e : studentsPerCourse.values()) {//gia kathe object entitiesRelationship (sisxetish) sth hashMap studentsPerCourse
                        Printer.printRelationshipsTableHeader("oneCourse");//header tou course
                        System.out.println(courses.get(e.getEntitieId()));//afou exw hashMap den xreiazetai na psaxw na vrw to mathima, to zhtaw me key to id tou            
                        System.out.println(Printer.arrayDividers(Course.class));//kleisimo tou pinaka  
                        Printer.printRelationshipsTableHeader("manyStudent");//header twn Students pou prakolouthoun to mathima
                        for (Integer sid : e.getIds()) {//gia kathe id tou student pou anhkei sto mathima, den xreiazetai na ton psaxw me loop, efoson ton zhtaw me key to id tou
                            System.out.println(students.get(sid));
                            System.out.println(Printer.arrayDividers(Student.class));
                        }
                    }
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις studentsPerCourse !" : Printer.red("Δεν υπάρχουν συσχετίσεις studentsPerCourse !"));
                }
                break;
            case "trainersPerCourse":
                if (trainersPerCourse.size() > 0) {
                    printEntitiesRelationship("trainersPerCourse");
                    System.out.println(writeToFile ? "Οι συσχετίσεις trainersPerCourse είναι:" : Printer.red("Οι συσχετίσεις trainersPerCourse είναι:"));
                    for (EntitiesRelationship e : trainersPerCourse.values()) {//gia kathe sisxetish sth hashMap trainersPerCourse
                        Printer.printRelationshipsTableHeader("oneCourse");
                        System.out.println(courses.get(e.getEntitieId()));
                        System.out.println(Printer.arrayDividers(Course.class));
                        Printer.printRelationshipsTableHeader("manyTrainer");
                        for (Integer tid : e.getIds()) {//to e.getIds() epistrefei arraylist olwn twn ids twn trainers
                            System.out.println(trainers.get(tid));
                            System.out.println(Printer.arrayDividers(Trainer.class));
                        }
                    }
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις trainersPerCourse !" : Printer.red("Δεν υπάρχουν συσχετίσεις trainersPerCourse !"));
                }
                break;
            case "assignmentsPerCourse":
                if (assignmentsPerCourse.size() > 0) {
                    printEntitiesRelationship("assignmentsPerCourse");
                    System.out.println(writeToFile ? "Οι συσχετίσεις assignmentsPerCourse είναι:" : Printer.red("Οι συσχετίσεις assignmentsPerCourse είναι:"));
                    for (EntitiesRelationship e : assignmentsPerCourse.values()) {//gia kathe sisxetish sth hashMap assignmentsPerCourse
                        Printer.printRelationshipsTableHeader("oneCourse");
                        System.out.println(courses.get(e.getEntitieId()));
                        System.out.println(Printer.arrayDividers(Course.class));
                        Printer.printRelationshipsTableHeader("manyAssignment");
                        for (Integer aid : e.getIds()) {
                            System.out.println(assignments.get(aid));
                            System.out.println(Printer.arrayDividers(Assignment.class));
                        }
                    }
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις assignmentsPerCourse !" : Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerCourse !"));
                }
                break;
            case "assignmentsPerStudent":
                if (assignmentsPerStudent.size() > 0) {
                    printEntitiesRelationship("assignmentsPerStudent");
                    System.out.println(writeToFile ? "Οι συσχετίσεις assignmentsPerStudent είναι:" : Printer.red("Οι συσχετίσεις assignmentsPerStudent είναι:"));
                    for (EntitiesRelationship e : assignmentsPerStudent.values()) {
                        Printer.printRelationshipsTableHeader("oneStudent");
                        System.out.println(students.get(e.getEntitieId()));
                        System.out.println(Printer.arrayDividers(Student.class));
                        Printer.printRelationshipsTableHeader("manyAssignment");
                        for (Integer aid : e.getIds()) {
                            System.out.println(assignments.get(aid));
                            System.out.println(Printer.arrayDividers(Assignment.class));
                        }
                    }
                } else {
                    System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις assignmentsPerStudent !" : Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerStudent !"));
                }
                break;
            case "studentsToMoreThanOneCourse":
                int counter;
                boolean printHeader = true;
                for (Student s : students.values()) {//gia kathe mathith tha psaxw sthn sisxetish studentsPerCourse gia ton an to id tou iparxei perissoteres apo mia fores, dld emfanizetai toulaxiston dyo fores se diaforetikes sysxetiseis
                    Iterator<EntitiesRelationship> e = studentsPerCourse.values().iterator();
                    counter = 0;//metraei se posa mathimata vriskoume to id tou trexwn student ths for
                    while (e.hasNext() && (counter < 2)) {//gia kathe sisxetish Course->Students pou exoume, to ebala se while wste na stamathsei molis ton brei se dio toulaxiston mathimata
                        if (e.next().getIds().contains(s.getId())) {//an h arraylist me ta ids twn students perixei to id tou trexwn mathith
                            counter++;
                        }
                    }
                    if (counter == 2) {//anhkei se perissotera apo ena
                        if (printHeader) {
                            Printer.printRelationshipsTableHeader("studentsToMoreThanOneCourse");
                            printHeader = false;
                        }
                        System.out.println(s);
                        System.out.println(Printer.arrayDividers(Student.class));
                    }
                }
                if (printHeader) {
                    System.out.println(Printer.red("Δεν υπάρχουν μαθητές να ανήκουν σε περισσότερα από ένα μαθήματα !"));
                }
                break;
            default://teleutaia epilogh einai h 10, sthn opoia stelnw kateutheian thn hmeromhnia
                LocalDate asDate;
                int minusDays;
                boolean printStudentOnce;
                boolean atLeastOne = false;
                LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));//kanw localDate thn hmeromhnia pou diavasa
                minusDays = date.getDayOfWeek().getValue() - 1;//ypologizw poses meres prepei na afairesw gia na peftw panta sthn Deutera
                LocalDate startPeriod = date.minusDays(minusDays);//to start_period ths ebdomadas einai panta h deutera
                LocalDate endPeriod = startPeriod.plusDays(6);//to end_period einai Deutera+6 meres = oloklhrh h ebdomada
                System.out.println(Printer.red("Η ημερομηνία που έδωσες ανήκει στην εβδομάδα, από " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(startPeriod) + " έως " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(endPeriod) + " και"));
                System.out.println(Printer.red("μέσα σε αυτή την εβδομάδα έχουν να παραδώσουν εργασίες οι εξής μαθητές:"));
                for (EntitiesRelationship e : assignmentsPerStudent.values()) {//kathe student gia ton opoio exw sisxetish dld exw anathesei ergasies             
                    printStudentOnce = false;//gia na elegxw an exei ektypwthei h oxi to header tou Student to opoio thelw momo mia fora
                    for (int aid : e.getIds()) {//gia kathe assignment tha elegxw thn hmeromhnia paradoshs an einai mesa sthn ebdomada, an vrw estw kai ena pou einai stamataw na psaxnw tis ipoloipes ergasies          
                        asDate = assignments.get(aid).getSubDateTime().toLocalDate();//epeidh sto assignment exw valei pedio dateTime edw pairnw mono to date
                        if ((asDate.isAfter(startPeriod) && asDate.isBefore(endPeriod)) || asDate.isEqual(startPeriod) || asDate.isEqual(endPeriod)) {//to h ergasia einai entos ths ebdomadas
                            if (!printStudentOnce) {//h if auth trexei mia fora gia kathe student an exw estw kai mia ergasia mesa sthn ebdomada thn hmeromhnias
                                Printer.printRelationshipsTableHeader("oneStudent");
                                System.out.println(students.get(e.getEntitieId()));
                                System.out.println(Printer.arrayDividers(Student.class));
                                Printer.printRelationshipsTableHeader(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(startPeriod) + "," + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(endPeriod));//stelnw tis hmeromhnies startPeriod,endPeriod xwrismenes me ,
                                printStudentOnce = true;
                                atLeastOne = true;
                            }
                            System.out.println(assignments.get(aid));//auta ta dio sout einai exw apo thn if etsi wste an exw polles ergasies na ektypwthoun oles
                            System.out.println(Printer.arrayDividers(Assignment.class));
                        }
                    }
                }
                if (!atLeastOne) {//den yparxei kanenas mathiths pou na prepei na paradwsei ergasia entos ths ebdomadas
                    System.out.println(Printer.red("Δεν βρέθηκε κανένας μαθητής !"));
                }
        }
    }

    public void printEntitiesRelationship(String value) {
        switch (value) {
            case "studentsPerCourse":
                if (studentsPerCourse.size() > 0) {
                    System.out.println(writeToFile ? "Οι συσχετίσεις studentsPerCourse σε μορφή id είναι:" : Printer.red("Οι συσχετίσεις studentsPerCourse σε μορφή id είναι:"));
                    Printer.divider('-', 50);
                    for (EntitiesRelationship e : studentsPerCourse.values()) {//gia kathe object entitiesRelationship (sisxetish) sth hashMap studentsPerCourse
                        System.out.println(e);
                    }
                } else {
                    System.out.println(Printer.red("Δεν υπάρχουν συσχετίσεις studentsPerCourse !"));
                }
                break;
            case "trainersPerCourse":
                if (trainersPerCourse.size() > 0) {
                    System.out.println(writeToFile ? "Οι συσχετίσεις trainersPerCourse σε μορφή id είναι:" : Printer.red("Οι συσχετίσεις trainersPerCourse σε μορφή id είναι:"));
                    Printer.divider('-', 50);
                    for (EntitiesRelationship e : trainersPerCourse.values()) {
                        System.out.println(e);
                    }
                } else {
                    System.out.println(Printer.red("Δεν υπάρχουν συσχετίσεις trainersPerCourse !"));
                }
                break;
            case "assignmentsPerCourse":
                if (assignmentsPerCourse.size() > 0) {
                    System.out.println(writeToFile ? "Οι συσχετίσεις assignmentsPerCourse σε μορφή id είναι:" : Printer.red("Οι συσχετίσεις assignmentsPerCourse σε μορφή id είναι:"));
                    Printer.divider('-', 53);
                    for (EntitiesRelationship e : assignmentsPerCourse.values()) {
                        System.out.println(e);
                    }
                } else {
                    System.out.println(Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerCourse !"));
                }
                break;
            default://assignmentsPerStudent
                if (assignmentsPerStudent.size() > 0) {
                    System.out.println(writeToFile ? "Οι συσχετίσεις assignmentsPerStudent σε μορφή id είναι:" : Printer.red("Οι συσχετίσεις assignmentsPerStudent σε μορφή id είναι:"));
                    Printer.divider('-', 54);
                    for (EntitiesRelationship e : assignmentsPerStudent.values()) {
                        System.out.println(e);
                    }
                } else {
                    System.out.println(Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerStudent !"));
                }
        }
    }

    public void exportReadableDB() {
        File f;
        LocalDateTime date;
        PrintStream out, console = System.out;
        String[] label = {"courses", "students", "trainers", "assignments", "studentsPerCourse", "trainersPerCourse", "assignmentsPerCourse", "assignmentsPerStudent",};
        String day, month, year, hour, minute, second, fileName, folder, now;
        try {
            now = LocalDateTime.now().toString();
            folder = "PrivateSchoolDBExports";
            writeToFile = true;//thn xrisimopoiw wste na xerw pote thelw na grapsw se arxeio gia na vgalw to xrwma apo tis kefalides
            day = now.substring(8, 10);//diaxwrizw mera, mhna, xrono, wra, lepta, deuterolepta
            month = now.substring(5, 7);
            year = now.substring(0, 4);
            hour = now.substring(11, 13);
            minute = now.substring(14, 16);
            second = now.substring(17, 19);
            now = day + "-" + month + "-" + year + "_" + hour + "_" + minute + "_" + second;
            date = LocalDateTime.parse(now, DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss"));
            fileName = "PrivateSchoolDB_" + DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss").format(date) + ".txt";
            f = new File(folder);//ftiaxnw ton fakelo PrivateSchoolDBExports ston opoio tha apothikeuontai ta stigmiotypa ths bashs
            if (!f.exists()) {
                f.mkdir();
            }
            out = new PrintStream(new File(folder + "\\" + fileName));
            System.setOut(out);//pleon h roh einai pros to arxeio
            Printer.divider('*', 0);
            for (String l : label) {//ektipwsh olwn twn sxesewn ths bashs
                printListOfAll(l);
                Printer.divider('*', 0);
            }
            System.setOut(console);//afou grapsw to arxeio h roh xanagyrizei sthn konsola     
            writeToFile = false;
            System.out.println(Printer.green("Το στιγμιότυπο της βάσης δεδομένων έγινε επιτυχώς export στο αρχείο : " + Printer.red(fileName)));
        } catch (FileNotFoundException ex) {
            System.out.println(Printer.red("Μη αποδεκτό όνομα αρχείου !"));
        }
    }
}
