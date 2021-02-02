package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.ChatAssessorClient;

public interface IContactsInteractor {

    void getData(Context context, ContactsListener listener);

    interface ContactsListener {
        void onGetData(ArrayList<ChatAssessorClient> data);

        void onErrorGetData(String error);
    }
}
