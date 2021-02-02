package mx.app.fashionme.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;

public class FavConstructor {
    private Context context;

    public FavConstructor(Context context) {
        this.context = context;
    }

    public boolean favExist(int idClothe) {
        DataBase dataBase = new DataBase(context);
        return dataBase.existFav(idClothe);
    }

    public boolean shoppingExist(int idClothe) {
        DataBase db = new DataBase(context);
        return db.existInCart(idClothe);
    }

    public long addClotheFav(Clothe clothe) {
        DataBase db = new DataBase(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseConstants.TABLE_FAVS_ID_CLOTHE, clothe.getId());
        contentValues.put(DataBaseConstants.TABLE_FAVS_NAME_CLOTHE, clothe.getSpanish().getName());
        contentValues.put(DataBaseConstants.TABLE_FAVS_IMAGE_CLOTHE, clothe.getUrlImage());

        return db.insertClotheFav(contentValues);
    }

    public boolean removeFavByClotheId(int idClothe) {
        DataBase db = new DataBase(context);
        return db.deleteFav(idClothe);
    }

    public long addToShoppingCart(Clothe clothe) {
        DataBase db = new DataBase(context);
        ContentValues values = new ContentValues();
        values.put(DataBaseConstants.TABLE_SHOPPING_ID_CLOTHE, clothe.getId());
        values.put(DataBaseConstants.TABLE_SHOPPING_NAME_CLOTHE, clothe.getSpanish().getName());
        values.put(DataBaseConstants.TABLE_SHOPPING_NAME_CLOTHE_ENGLISH, clothe.getEnglish().getName());
        values.put(DataBaseConstants.TABLE_SHOPPING_URL_CLOTHE, clothe.getClotheLink());
        values.put(DataBaseConstants.TABLE_SHOPPING_IMAGE_CLOTHE, clothe.getUrlImage());

        return db.insertClotheToShoppingCart(values);
    }

    public boolean removeCartShoppingByIdClothe(int idClothe) {
        DataBase db = new DataBase(context);
        return db.deleteCart(idClothe);
    }

    public ArrayList<Clothe> getClothesFavorites() {
        DataBase db = new DataBase(context);
        return db.getFavorites();
    }

    public ArrayList<Clothe> getClothesOnCart() {
        DataBase db = new DataBase(context);
        return db.getShoppingCart();
    }
}
