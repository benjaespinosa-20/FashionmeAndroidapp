package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;

public interface IShoppingInteractor {

    void getDataFromDB(Context context, ClotheShoppingListener listener);

    interface ClotheShoppingListener {
        void onGetCart(ArrayList<Clothe> clothes);
        void onError(String err);
    }
}
