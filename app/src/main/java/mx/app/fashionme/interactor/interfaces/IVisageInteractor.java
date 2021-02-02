package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Visage;

public interface IVisageInteractor {

    void getData(Context context, VisageListener listener);

    interface VisageListener {

        void onSuccessGetData(ArrayList<Visage> data);

        void onErrorGetData(String error);
    }
}
