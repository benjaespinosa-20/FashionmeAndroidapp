package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.List;

import mx.app.fashionme.pojo.DataRegisterClotheViewModel;

public interface IDataRegisterClotheInteractor {

    void getData(Context context, String type, DataListener listener);

    interface DataListener {
        void onErrorGetData(String error);
        void updateData(List<DataRegisterClotheViewModel> data);

    }
}
