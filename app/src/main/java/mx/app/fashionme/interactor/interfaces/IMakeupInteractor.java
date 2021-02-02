package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Makeup;

public interface IMakeupInteractor {

    void getData(Context context, String typeMH, MakeupListener listener);

    interface MakeupListener {
        void onSuccessGetData(ArrayList<Makeup> data, String typeMH);
        void onErrorGetData(String error);
    }
}
