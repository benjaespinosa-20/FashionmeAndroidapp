package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Favorite;

public interface IFavoriteInteractor {

    void getDataFromDB(Context context, String type, ClotheFavoritesListener listener);
    void getDataFromAPI(Context context, ClotheFavoritesListener listener);

    interface ClotheFavoritesListener {
        void onGetFavorites(ArrayList<Favorite> favorites);
        void onError(String err);
    }
}
