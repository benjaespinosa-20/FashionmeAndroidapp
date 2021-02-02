package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.WayDressing;

public interface IWayDressingInteractor {

    void getWaysFromAPI(Context context, OnGetWaysFinishedListener callback);

    interface OnGetWaysFinishedListener {
        void onSuccessWays(ArrayList<WayDressing> ways);
        void onErrorWays(String err);
    }
}
