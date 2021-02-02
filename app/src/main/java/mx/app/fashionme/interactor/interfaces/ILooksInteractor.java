package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Look;

public interface ILooksInteractor {

    void getDataLooksFromDB(Context context, OnGetLooksFinishedListener listener);
    void saveDateToLook(Context context, Look look, String date, OnSaveDateFinishedListener listener);

    interface OnGetLooksFinishedListener {
        void onSuccess(ArrayList<Look> looks);
        void onError(String error);
    }

    interface OnSaveDateFinishedListener {
        void onSuccessSavedDate();
        void onErrorSavedDate(String err);
    }
}
