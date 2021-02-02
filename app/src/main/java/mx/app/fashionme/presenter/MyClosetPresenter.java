package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import mx.app.fashionme.interactor.interfaces.IMyClosetInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.IMyClosetPresenter;
import mx.app.fashionme.view.interfaces.IMyClosetView;

public class MyClosetPresenter implements IMyClosetPresenter, IMyClosetInteractor.MyClosetListener {

    private IMyClosetView view;
    private IMyClosetInteractor interactor;
    private Context context;

    public MyClosetPresenter(IMyClosetInteractor interactor, Context context) {
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void setView(IMyClosetView view) {
        this.view = view;
    }

    @Override
    public void getData() {
        int userId = SessionPrefs.get(context).getUserId();
        interactor.getClothesByUser(userId, this);
    }

    @Override
    public void onErrorGetData(String error) {
        if (view != null) {
            view.showError(error);
        }
    }

    @Override
    public void updateData(List<Clothe> data) {
        if (view != null) {
            view.updateData(data);
        }
    }
}
