package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.presenter.ChatsPresenter;

public interface IChatsInteractor {
    void getData(Context context, ChatsListener listener);

    void updateChat(Context context, ChatAssessorClient chatAssessorClient, ChatsListener listener);

    interface ChatsListener {
        void onGetData(ArrayList<ChatAssessorClient> data);
        void onErrorGetData(String error);

        void onErrorUpdateChat(String error);

        void onSuccessUpdateChat(ChatAssessorClient data);
    }
}
