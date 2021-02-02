package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import mx.app.fashionme.interactor.interfaces.ITipInteractor;
import mx.app.fashionme.pojo.Tip;
import mx.app.fashionme.presenter.interfaces.ITipPresenter;
import mx.app.fashionme.view.interfaces.ITipView;

/**
 * Created by heriberto on 26/03/18.
 */

public class TipPresenter implements ITipPresenter, ITipInteractor.OnGetTipListener, ITipInteractor.TipListener {

    public static final String TAG = TipPresenter.class.getSimpleName();
    private ITipView view;
    private ITipInteractor interactor;
    private Context context;

    public TipPresenter(ITipView view, ITipInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getTip(int idTip) {
        view.showLoading(true);
        view.showEmpty(false);
        interactor.getTip(context, idTip, this);
    }

    @Override
    public void checkFav(int idTip) {
        interactor.checkFav(context, idTip, this);
    }

    @Override
    public void removeFavTip(int idTip) {
        interactor.removeFavByIdTip(context, idTip, this);
    }

    @Override
    public void addFavTip(int idTip) {
        interactor.addToFav(context, idTip, this);
    }

    @Override
    public void setAnalytics(Activity activity) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Tips/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccess(Tip tip) {
        if (view != null) {

            view.showLoading(false);

            if (tip == null) {
                view.showEmpty(true);
                return;
            }

            view.generateLinearLayoutVerticalTip();
            view.initializeTipAdapter(view.createTipAdapter(tip));
            checkFav(tip.getId());
        }
    }

    @Override
    public void onFail(String error) {
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
