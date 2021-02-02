package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IWayDressingInteractor;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.presenter.interfaces.IWayDressingPresenter;
import mx.app.fashionme.view.interfaces.IWayDressingView;

public class WayDressingPresenter implements IWayDressingPresenter, IWayDressingInteractor.OnGetWaysFinishedListener {

    private IWayDressingView view;
    private IWayDressingInteractor interactor;
    private Context context;

    public WayDressingPresenter(IWayDressingView view, IWayDressingInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getWays() {
        interactor.getWaysFromAPI(context, this);
    }

    @Override
    public void setAnalytics(Activity activity) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Codigo de vestimenta/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccessWays(ArrayList<WayDressing> ways) {
        if (view != null) {
            if (ways.size() == 0) {
                view.showEmptyListWays();
                return;
            }

            view.generateWayDressingLinearLayoutVertical();
            view.initializeWayDressingAdapter(view.createWayDressingAdapter(ways));
        }
    }

    @Override
    public void onErrorWays(String err) {
        if (view != null) {
            view.showErrorWays(err);
        }
    }
}
