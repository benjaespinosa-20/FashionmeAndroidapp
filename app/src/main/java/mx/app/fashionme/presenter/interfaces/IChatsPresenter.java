package mx.app.fashionme.presenter.interfaces;

import mx.app.fashionme.pojo.ChatAssessorClient;

public interface IChatsPresenter {
    void getPendingChats();
    void acceptChat(ChatAssessorClient chatAssessorClient);
}
