package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;

/**
 * Created by heriberto on 5/04/18.
 */

public interface IClotheInteractor {

    void getDataFromAPi(Context context, int subcategoryId, boolean isClosetIdeal, OnGetClotheFinishedListener callback);

    interface OnGetClotheFinishedListener {
        void onSuccess(ArrayList<Clothe> clothes);
        void onError(String err);
    }
}
