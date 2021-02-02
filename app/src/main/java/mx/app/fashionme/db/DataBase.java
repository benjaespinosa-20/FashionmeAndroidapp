package mx.app.fashionme.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.English;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.pojo.Spanish;

/**
 * Created by heriberto on 13/03/18.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String TAG = DataBase.class.getSimpleName();

    private Context context;

    public DataBase(Context context) {
        super(context, DataBaseConstants.DATABASE_NAME, null, DataBaseConstants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryLookTableCreate = "CREATE TABLE "+ DataBaseConstants.TABLE_LOOK +"(" +
                DataBaseConstants.TABLE_LOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataBaseConstants.TABLE_LOOK_URI + " TEXT," +
                DataBaseConstants.TABLE_LOOK_DATE + " TEXT " +
                ")";

//        String queryFavoritesTableCreate = "CREATE TABLE "+ DataBaseConstants.TABLE_FAVS +"(" +
//                DataBaseConstants.TABLE_FAVS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                DataBaseConstants.TABLE_FAVS_ID_CLOTHE + " INTEGER," +
//                DataBaseConstants.TABLE_FAVS_NAME_CLOTHE + " TEXT," +
//                DataBaseConstants.TABLE_FAVS_IMAGE_CLOTHE + " TEXT " +
//                ")";

        String queryShoppingTableCreate = "CREATE TABLE "+ DataBaseConstants.TABLE_SHOPPING +"(" +
                DataBaseConstants.TABLE_SHOPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataBaseConstants.TABLE_SHOPPING_ID_CLOTHE + " INTEGER," +
                DataBaseConstants.TABLE_SHOPPING_NAME_CLOTHE + " TEXT," +
                DataBaseConstants.TABLE_SHOPPING_NAME_CLOTHE_ENGLISH + " TEXT," +
                DataBaseConstants.TABLE_SHOPPING_URL_CLOTHE + " TEXT," +
                DataBaseConstants.TABLE_SHOPPING_IMAGE_CLOTHE + " TEXT " +
                ")";

        String queryChecklistTableCreate = "CREATE TABLE " + DataBaseConstants.TABLE_CHECKLIST + "(" +
                DataBaseConstants.TABLE_CHECKLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataBaseConstants.TABLE_CHECKLIST_ID_JOURNEY + " INTEGER," +
                DataBaseConstants.TABLE_CHECKLIST_ID_ITEM + " INTEGER " +
                ")";

        db.execSQL(queryLookTableCreate);
        //db.execSQL(queryFavoritesTableCreate);
        db.execSQL(queryShoppingTableCreate);
        db.execSQL(queryChecklistTableCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstants.TABLE_LOOK);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstants.TABLE_FAVS);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstants.TABLE_SHOPPING);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstants.TABLE_CHECKLIST);
        //db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstants.TABLE_ANOTHER_NAME);
        onCreate(db);
    }


    public ArrayList<Look> getLooks() {
        ArrayList<Look> looks = new ArrayList<>();

        String querySelect = "SELECT * FROM " + DataBaseConstants.TABLE_LOOK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(querySelect, null);

        while (registros.moveToNext()) {
            Look current = new Look();
            current.setId(registros.getInt(0));
            current.setUri(registros.getString(1));
            current.setDate(registros.getString(2));
            //actual.setSOME(registros.getInt(2));

            looks.add(current);
        }

        db.close();

        return looks;
    }

    public ArrayList<Look> getLooksWithDate() {
        ArrayList<Look> looks = new ArrayList<>();

        String query = "SELECT * FROM " + DataBaseConstants.TABLE_LOOK + " WHERE " + DataBaseConstants.TABLE_LOOK_DATE + " != NULL";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(query, null);

        while (registros.moveToNext()) {
            Look currentLook = new Look();
            currentLook.setId(registros.getInt(0));
            currentLook.setUri(registros.getString(1));
            currentLook.setDate(registros.getString(2));
            looks.add(currentLook);
        }

        db.close();

        return looks;
    }

    public long insertLook(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(DataBaseConstants.TABLE_LOOK, null, values);
        db.close();
        return id;
    }

    public void updateLook(ContentValues contentValues, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DataBaseConstants.TABLE_LOOK, contentValues, DataBaseConstants.TABLE_LOOK_ID + "="+id, null);
    }

    public ArrayList<Look> getLookByDate(String date) {
        ArrayList<Look> looks = new ArrayList<>();

        String query = "SELECT * FROM " + DataBaseConstants.TABLE_LOOK + " WHERE " + DataBaseConstants.TABLE_LOOK_DATE + " =\"" + date + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(query, null);

        while (registros.moveToNext()) {
            Look currentLook = new Look();
            currentLook.setId(registros.getInt(0));
            currentLook.setUri(registros.getString(1));
            currentLook.setDate(registros.getString(2));
            looks.add(currentLook);
        }

        db.close();

        return looks;
    }

    public boolean existFav(int idClothe) {
        String querySelect = "SELECT * FROM " + DataBaseConstants.TABLE_FAVS + " WHERE " + DataBaseConstants.TABLE_FAVS_ID_CLOTHE + " =" + idClothe;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(querySelect, null);

        return registros != null && registros.getCount() > 0;
    }

    public boolean existInCart(int idClothe) {
        String querySelect = "SELECT * FROM " + DataBaseConstants.TABLE_SHOPPING + " WHERE " + DataBaseConstants.TABLE_FAVS_ID_CLOTHE + " =" + idClothe;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        return cursor != null && cursor.getCount() > 0;
    }

    public long insertClotheFav(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(DataBaseConstants.TABLE_FAVS, null, contentValues);
        db.close();
        return id;
    }

    public boolean deleteFav(int idClothe) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DataBaseConstants.TABLE_FAVS,
                DataBaseConstants.TABLE_FAVS_ID_CLOTHE + "=" + idClothe, null) > 0;
    }

    public long insertClotheToShoppingCart(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(DataBaseConstants.TABLE_SHOPPING, null, values);
        db.close();
        return id;
    }

    public boolean deleteCart(int idClothe) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DataBaseConstants.TABLE_SHOPPING,
                DataBaseConstants.TABLE_SHOPPING_ID_CLOTHE + "=" + idClothe, null) > 0;
    }

    public ArrayList<Clothe> getFavorites() {
        ArrayList<Clothe> clothes = new ArrayList<>();
        Spanish spanish = new Spanish();

        String querySelect = "SELECT * FROM " + DataBaseConstants.TABLE_FAVS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(querySelect, null);

        while (registros.moveToNext()) {
            Clothe current = new Clothe();
            current.setId(registros.getInt(1));
            spanish.setName(registros.getString(2));
            current.setSpanish(spanish);
            current.setUrlImage(registros.getString(3));

            clothes.add(current);
        }

        db.close();

        return clothes;
    }

    public ArrayList<Clothe> getShoppingCart() {
        ArrayList<Clothe> clothes = new ArrayList<>();
        Spanish spanish = new Spanish();
        English english = new English();

        String querySelect = "SELECT * FROM " + DataBaseConstants.TABLE_SHOPPING;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(querySelect, null);

        while (registros.moveToNext()) {
            Clothe current = new Clothe();
            current.setId(registros.getInt(1));

            spanish.setName(registros.getString(2));
            current.setSpanish(spanish);

            english.setName(registros.getString(3));
            current.setEnglish(english);

            current.setClotheLink(registros.getString(4));
            current.setUrlImage(registros.getString(5));

            current.setInCar(true);

            clothes.add(current);
        }

        db.close();

        return clothes;
    }

    public void insertChecklist(int idJourney, int idItem) {
        if (!existItem(idJourney, idItem)) {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DataBaseConstants.TABLE_CHECKLIST_ID_JOURNEY, idJourney);
            values.put(DataBaseConstants.TABLE_CHECKLIST_ID_ITEM, idItem);
            database.insert(DataBaseConstants.TABLE_CHECKLIST, null, values);

            database.close();
        }
    }

    public boolean existItem(int idJourney, int idItem) {

        String[] params = {String.valueOf(idJourney), String.valueOf(idItem)};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseConstants.TABLE_CHECKLIST +
                " WHERE id_journey = ? AND id_item = ?", params);

        return cursor.getCount() > 0;
    }

    public void deleteChecklist(int idJourney) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataBaseConstants.TABLE_CHECKLIST,
                DataBaseConstants.TABLE_CHECKLIST_ID_JOURNEY + "=" + idJourney, null);
    }
}
