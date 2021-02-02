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

import mx.app.fashionme.interactor.interfaces.IVisageInteractor;
import mx.app.fashionme.pojo.Visage;
import mx.app.fashionme.presenter.interfaces.IVisagePresenter;
import mx.app.fashionme.view.interfaces.IVisageView;

public class VisagePresenter implements IVisagePresenter, IVisageInteractor.VisageListener {

    private static final String TAG = "Visage";
    public static final String TYPE_KEY = "type";
    public static final String TYPE_VALUE = "visage";
    public static final String OBJ_SPANISH = "spanish";
    public static final String OBJ_ENGLISH = "english";
    public static final String IMAGES_VISAGE = "images";

    private Context context;
    private Activity activity;
    private IVisageView view;
    private IVisageInteractor interactor;

    public VisagePresenter(Context context, Activity activity,
                           IVisageView view, IVisageInteractor interactor) {
        this.context    = context;
        this.activity   = activity;
        this.view       = view;
        this.interactor = interactor;
    }

    @Override
    public void getVisages() {
        if (view != null){
            view.showProgress();
            view.hideLinearLayoutError();
            if (!isOnline()) {
                view.hideProgress();
                view.hideRecyclerView();
                view.showLinearLayoutError();
                view.offline();
            } else {
                view.showRecyclerView();
                interactor.getData(context, this);
            }
        }
    }

    @Override
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVisages();
                if (view != null){
                    view.setRefreshingToSwipe(false);
                }
            }
        };
    }

    @Override
    public View.OnClickListener setOnClickListenerTryAgain() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVisages();
            }
        };
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Visagismo/Android");
        firebaseAnalytics.logEvent("open_screen", bundle);
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
    public void onSuccessGetData(ArrayList<Visage> data) {
        if (view != null) {
            view.hideProgress();
            view.hideLinearLayoutError();

            if (data.size() == 0) {
                view.showLinearLayoutError();
                view.emptyList();
            }
            view.setItems(data);
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
