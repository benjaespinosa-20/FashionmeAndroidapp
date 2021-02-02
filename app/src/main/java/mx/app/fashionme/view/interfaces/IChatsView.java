package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.ChatsAdapter;
import mx.app.fashionme.pojo.ChatAssessorClient;

public interface IChatsView {

    void generateLinearLayout();

    ChatsAdapter createAdapter();

    void initializeAdapter(ChatsAdapter adapter, ArrayList<ChatAssessorClient> chatAssessorClients);

    void emptyList();

    void showOffline();

    void showError(String error);

    void showProgress();

    void hideProgress();

    void showLinearLayoutError();

    void hideLinearLayoutError();

    void hideList();

    void showList();
}
