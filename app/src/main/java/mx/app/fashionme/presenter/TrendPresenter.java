package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import mx.app.fashionme.interactor.interfaces.ITrendInteractor;
import mx.app.fashionme.pojo.Trend;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.ITrendPresenter;
import mx.app.fashionme.view.interfaces.ITrendView;

/**
 * Created by heriberto on 26/03/18.
 */

public class TrendPresenter implements ITrendPresenter, ITrendInteractor.OnGetTrendListener, ITrendInteractor.TrendListener {

    private static final String TAG = TrendPresenter.class.getSimpleName();
    private ITrendView view;
    private ITrendInteractor interactor;
    private Context context;


    public TrendPresenter(ITrendView view, ITrendInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getTrend(int idTrend) {
        view.showLoading(true);
        view.showEmpty(false);
        interactor.getTrend(context, idTrend, this);
    }

    @Override
    public void checkFav(int idTrend) {
        interactor.checkFav(context, idTrend, this);
    }

    @Override
    public void removeFavTrend(int idTrend) {
        interactor.removerFavByIdTrend(context, idTrend, this);
    }

    @Override
    public void addTrendToFav(int idTrend) {
        interactor.addToFav(context, idTrend, this);
    }

    @Override
    public void setAnalytics(Activity activity) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Tendencias/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccess(Trend trend) {
        if (view != null) {
            view.showLoading(false);

            if (trend == null) {
                view.showEmpty(true);
                return;
            }

            view.generateLinearLayoutVertical();
            view.initializeAdapter(view.createAdapter(trend));
            checkFav(trend.getId());
        }
    }

    @Override
    public void onFailure(String error) {
        if (view != null) {
            view.showLoading(false);
            view.showError(error);
        }
    }

    @Override
    public void onCheckFav(boolean isFav) {
        if (view != null) {
            view.setButtonFav(isFav);
        }
    }

    @Override
    public void onAddTrendFav() {
        if (view != null) {
            view.favAdded();
            Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveTrendFav() {
        if (view != null) {
            view.favRemoved();
            Toast.makeText(context, "Removido de favoritos", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.showError(error);
        }
    }
}
