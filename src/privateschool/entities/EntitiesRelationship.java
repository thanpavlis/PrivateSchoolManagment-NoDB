package privateschool.entities;

import java.util.ArrayList;
import java.util.Collections;

public class EntitiesRelationship {//anaparista tis sxeseis metaxy twn diaforwn ontothtwn

    private int rowId;// to rowId einai to trexwn id kathe fora ths listas sthn opoia tha prostethei to antikeimeno

    private final int entitieId;//einai to id ths mias ontothtas thn opoia thelw na enwsw me polles p.x to id tou Course deixnei se pollous Students
    private ArrayList<Integer> ids;//einai ta ids (polla) pou sindeontai me thn mia ontothta id gia paradeigma ta ids twn Student pou anhkoun sto mathima me to sygkekrimeno id

    public EntitiesRelationship(int entitieId, ArrayList<Integer> ids) {
        this.entitieId = entitieId;
        this.ids = ids;
        Collections.sort(ids);//sortarisma ths listas twn ids
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getEntitieId() {
        return entitieId;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void addListIds(int id) {
        ids.add(id);
        Collections.sort(ids);//sortarisma ths lista twn ids meta apo prosthiki neou id
    }

    public void removeListIds(int id) {
        int i = 0;
        boolean found = false;
        while ((i < ids.size()) && !found) {
            if (ids.get(i) == id) {
                ids.remove(i);
                found = true;
            }
            i++;
        }
        Collections.sort(ids);//sortarisma ths lista twn ids meta apo prosthiki neou id
    }

    @Override
    public String toString() {
        return "EntitiesRelationship{" + "rowId=" + rowId + ", entitieId=" + entitieId + ", ids=" + ids + '}';
    }
}
