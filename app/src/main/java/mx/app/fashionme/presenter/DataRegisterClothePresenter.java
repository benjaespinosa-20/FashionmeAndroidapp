package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import mx.app.fashionme.interactor.interfaces.IDataRegisterClotheInteractor;
import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.presenter.interfaces.IDataRegisterClothePresenter;
import mx.app.fashionme.view.RegisterClosetActivity;
import mx.app.fashionme.view.interfaces.IDataRegisterClotheView;

public class DataRegisterClothePresenter implements
        IDataRegisterClothePresenter, IDataRegisterClotheInteractor.DataListener {

    static final String TAG = "DataRegisterData";

    private IDataRegisterClotheView view;
    private IDataRegisterClotheInteractor interactor;
    private Context context;
    private Activity activity;

    public DataRegisterClothePresenter(IDataRegisterClotheInteractor interactor,
                                       Context context, Activity activity) {
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void setView(IDataRegisterClotheView view) {
        this.view = view;
    }

    @Override
    public void loadDataToRegister() {
        if (view != null) {
            view.showProgress();
        }

        String type = null;
        Intent i = activity.getIntent();

        if (i != null) {
             type = i.getStringExtra(RegisterClosetActivity.EXTRA_DATA_KEY);
        }

        interactor.getData(context, type, this);
    }

    @Override
    public void selectData(List<DataRegisterClotheViewModel> resultList) {

        Intent i = activity.getIntent();


        if (i != null) {

            ArrayList<DataRegisterClotheViewModel> results = new ArrayList<>();

            for (DataRegisterClotheViewModel data:resultList){
                if (data.isSelected()) {
                    DataRegisterClotheViewModel newData = new DataRegisterClotheViewModel(
                            data.getId(),
                            data.getName()
                    );
                    results.add(newData);
                }
            }

            i.putParcelableArrayListExtra("RESULTADO", results);

            activity.setResult(Activity.RESULT_OK, i);
            activity.finish();

        }
    }

    @Override
    public void onErrorGetData(String error) {
        if (view != null) {
            view.hideProgress();
            view.showError(error);
        }
    }

    @Override
    public void updateData(List<DataRegisterClotheViewModel> data) {
        if (view != null) {
            view.hideProgress();
            view.updateData(data);
        }
    }
}
