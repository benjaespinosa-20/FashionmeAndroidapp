package mx.app.fashionme.db;

import android.content.Context;

public class ChecklistConstructor {
    private Context context;

    public ChecklistConstructor(Context context) {
        this.context = context;
    }

    public void checklistInsert(DataBase db, int idJourney, int idItem) {
        db.insertChecklist(idJourney, idItem);
    }

    public void checklistDeleteAll(DataBase db, int idJourney) {
        db.deleteChecklist(idJourney);
    }

    public boolean checkedItem(DataBase db, int idJourney, int idItem) {
        return db.existItem(idJourney, idItem);
    }
}
