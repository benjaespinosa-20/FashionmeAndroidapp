package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Category;

/**
 * Created by heriberto on 19/03/18.
 */

public interface ICategoryInteractor {

    void getDataFromAPI(Context context, OnGetCategoriesFishishedListener callback);

    interface OnGetCategoriesFishishedListener {
        void onSuccess(ArrayList<Category> categories);
        void onError(String error);

        void finishAndBodyPage();

        void finishAndCharacteristicsPage();

        void finishAndColorPage();
    }
}
