package mx.app.fashionme.presenter.interfaces;

import java.util.List;

import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.view.interfaces.IDataRegisterClotheView;

public interface IDataRegisterClothePresenter {

    void setView(IDataRegisterClotheView view);

    void loadDataToRegister();

    void selectData(List<DataRegisterClotheViewModel> resultList);
}
