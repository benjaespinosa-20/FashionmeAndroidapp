package mx.app.fashionme.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Look;

public class LooksConstructor {
    private Context context;

    public LooksConstructor(Context context) {
        this.context = context;
    }

    public long saveLook(Look look) {
        DataBase db = new DataBase(context);
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseConstants.TABLE_LOOK_URI, look.getUri());

        return db.insertLook(contentValues);
    }

    public ArrayList<Look> getDataLooks() {
        DataBase db = new DataBase(context);
        return db.getLooks();
    }

    public ArrayList<Look> getDataLooksWithDate() {
        DataBase db = new DataBase(context);
        return db.getLooksWithDate();
    }

    public void updateDateLook(Look look) {
        DataBase db = new DataBase(context);
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseConstants.TABLE_LOOK_DATE, look.getDate());

        db.updateLook(contentValues, look.getId());
    }

    public ArrayList<Look> getLookByDate(String date) {
        DataBase db = new DataBase(context);
        return db.getLookByDate(date);
    }
}
