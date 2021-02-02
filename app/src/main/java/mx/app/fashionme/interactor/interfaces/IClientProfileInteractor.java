package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import mx.app.fashionme.pojo.User;


public interface IClientProfileInteractor {
    void getUser(Context context, String clientEmail, ClientProfileListener listener);

    interface ClientProfileListener {

        void onErrorGetData(String error);

        void onGetUser(User user);
    }
}
