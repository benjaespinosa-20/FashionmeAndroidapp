package mx.app.fashionme.view.interfaces;

import java.util.List;

import mx.app.fashionme.pojo.Clothe;

public interface IMyClosetView {

    void updateData(List<Clothe> data);

    void showError(String error);
}
