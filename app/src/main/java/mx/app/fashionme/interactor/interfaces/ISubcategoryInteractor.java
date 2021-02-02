package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Subcategory;

/**
 * Created by heriberto on 5/04/18.
 */

public interface ISubcategoryInteractor {

    void getDataFromAPI(Context context, int categoryId, OnGetSubcategoriesFinishedListener callback);

    interface OnGetSubcategoriesFinishedListener {
        void onSuccess(ArrayList<Subcategory> subcategories);
        void onError(String err);
    }
}
