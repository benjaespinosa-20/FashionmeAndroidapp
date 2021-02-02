package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IMakeupInteractor;
import mx.app.fashionme.pojo.Makeup;
import mx.app.fashionme.presenter.interfaces.IMakeupPresenter;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.interfaces.IMakeupView;

public class MakeupPresenter implements IMakeupPresenter, IMakeupInteractor.MakeupListener {

    private static final String TAG = "Makeup";
    public static final String TYPE_KEY = "type";
    public static final String TYPE_VALUE = "makeup";
    public static final String TYPE_KEY_M = "do_makeup";
    public static final String TYPE_VALUE_M = "makeup";
    public static final String OBJ_SPANISH = "spanish";
    public static final String OBJ_ENGLISH = "english";
    public static final String IMAGES_VISAGE = "images";
    public static final String VIDEOS_VISAGE = "videos";
    public static final String URLS_VISAGE = "urls";

    private Context context;
    private IMakeupView view;
    private IMakeupInteractor interactor;

    public MakeupPresenter(Context context,
                           IMakeupView view, IMakeupInteractor interactor) {
        this.context    = context;
        this.view       = view;
        this.interactor = interactor;
    }

    @Override
    public void getMakeups() {
        if (view != null) {
            view.showProgress();
            view.hideLinearLayoutError();
            if (!isOnline()) {
                view.hideProgress();
                view.hideRecyclerView();
                view.showLinearLayoutError();
                view.offline();
            } else {
                view.showRecyclerView();
                interactor.getData(context, view.getTypeMH(), this);
            }
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (cm != null) {
            info = cm.getActiveNetworkInfo();
        }
        return info != null && info.isConnected();
    }

    @Override
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMakeups();
                if (view != null) {
                    view.setRefeshingToSwipe(false);
                }
            }
        };
    }

    @Override
    public View.OnClickListener setOnClickListenerTryAgain() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMakeups();
            }
        };
    }

    @Override
    public void setAnalytics(Activity activity) {

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();

        if (view.getTypeMH().equals(Constants.VALUE_MH_M)) {
            bundle.putString("screen_name", "Maquillaje/Android");
        } else if (view.getTypeMH().equals(Constants.VALUE_MH_H)){
            bundle.putString("screen_name", "Cabello/Android");
        }

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccessGetData(ArrayList<Makeup> data, String typeMH) {
        if (view != null){
            view.hideProgress();
            view.hideLinearLayoutError();

            if (data.size() == 0){
                view.showLinearLayoutError();
                view.emptyList();
            }
            view.setItems(data, typeMH);
        }
    }

    @Override
    public void onErrorGetData(String error) {
        if (view != null) {
            view.hideProgress();
            view.hideRecyclerView();
            view.showLinearLayoutError();
            view.error(error);
        }
    }
}
