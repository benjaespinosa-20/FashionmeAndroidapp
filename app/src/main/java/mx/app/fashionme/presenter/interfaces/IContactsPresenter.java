package mx.app.fashionme.presenter.interfaces;

import mx.app.fashionme.pojo.ChatAssessorClient;

public interface IContactsPresenter {

    void getContacts();

    void openChat(ChatAssessorClient chatAssessorClient);
}
