package mx.app.fashionme.interactor.interfaces;

import java.util.List;

import mx.app.fashionme.pojo.Clothe;

public interface IMyClosetInteractor {

    void getClothesByUser(int userId, MyClosetListener listener);

    interface MyClosetListener {
        void onErrorGetData(String error);
        void updateData(List<Clothe> data);
    }
}
