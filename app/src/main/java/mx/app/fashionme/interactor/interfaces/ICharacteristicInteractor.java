package mx.app.fashionme.interactor.interfaces;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Characteristic;

/**
 * Created by desarrollo1 on 20/03/18.
 */

public interface ICharacteristicInteractor {
    void getDataFromApi(OnGetData callback);

    void sendToServer(ListView listView, Context context, OnSendDataFinishedListener callback);

    interface OnGetData{
        void onError(String error);
        void onSuccess(ArrayList<Characteristic> characteristics);
    }

    interface OnSendDataFinishedListener {
        void onErrorSendData(String error);
        void onSuccess();
    }
}
