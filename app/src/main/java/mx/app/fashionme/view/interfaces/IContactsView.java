package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.ContactsAdapter;
import mx.app.fashionme.pojo.ChatAssessorClient;

public interface IContactsView {

    void generateLinearLayout();

    ContactsAdapter createAdapter();

    void initializeAdapter(ContactsAdapter adapter, ArrayList<ChatAssessorClient> clients);

    void emptyList();

    void showOffline();

    void showError(String error);

    void showProgress();

    void hideProgress();

    void showLinearLayoutError();

    void hideLinearLayoutError();

    void showList();

    void hideList();
}
