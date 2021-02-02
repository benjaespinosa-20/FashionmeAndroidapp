package mx.app.fashionme.view.interfaces;

import java.util.List;

import mx.app.fashionme.pojo.DataRegisterClotheViewModel;

public interface IDataRegisterClotheView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void updateData(List<DataRegisterClotheViewModel> data);
}
