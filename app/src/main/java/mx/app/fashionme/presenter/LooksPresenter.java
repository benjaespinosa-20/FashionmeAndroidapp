package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.ILooksInteractor;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.presenter.interfaces.ILooksPresenter;
import mx.app.fashionme.view.interfaces.ILooksView;

public class LooksPresenter implements ILooksPresenter, ILooksInteractor.OnGetLooksFinishedListener, ILooksInteractor.OnSaveDateFinishedListener {

    private static final String TAG = LooksPresenter.class.getSimpleName();

    private ILooksView view;
    private ILooksInteractor interactor;
    private Context context;

    public LooksPresenter(ILooksView view, ILooksInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void onSuccess(ArrayList<Look> looks) {
        if (view != null){
            if (looks.size() == 0){
                view.showEmptyLooks();
                return;
            }

            view.generateGridLayout(2);
            view.initializeAdapterLooks(view.createAdapterLooks(looks));
        }
    }

    @Override
    public void onError(String error) {
        if (view != null){
            view.showError(error);
        }
    }

    @Override
    public void getLooks() {
        interactor.getDataLooksFromDB(context, this);
    }

    @Override
    public void saveDateLook(Look look, String date) {
        interactor.saveDateToLook(context, look, date, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Mis looks/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccessSavedDate() {
        Log.i(TAG, "Success");
        if (view != null) {
            view.rvChange();
        }
    }

    @Override
    public void onErrorSavedDate(String err) {
        Log.e(TAG, err);
    }
}
