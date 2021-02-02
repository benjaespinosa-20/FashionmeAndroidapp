package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IJourneyInteractor;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.presenter.interfaces.IJourneyPresenter;
import mx.app.fashionme.view.interfaces.IJourneyView;

public class JourneyPresenter implements IJourneyPresenter, IJourneyInteractor.JourneyListener {

    private IJourneyView view;
    private IJourneyInteractor interactor;
    private Context context;

    public JourneyPresenter(IJourneyView view, IJourneyInteractor interactor, Context context) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
    }

    @Override
    public void getJourneys() {
        interactor.getDataJourneys(context, this);
    }

    @Override
    public void setAnalytics(Activity activity) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Viajes/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onGetJourneys(ArrayList<Journey> journeys) {
        if (view != null) {
            if (journeys.size() == 0) {
                view.showEmptyList();
                return;
            }

            view.generateLinearLayoutJourney();
            view.initializeJourneyListAdapter(view.createAdapter(journeys));
        }
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }

    @Override
    public void onGetJourneyById(Journey journey) {
        /*NOT USE HERE*/
    }

    @Override
    public void onCheckFav(boolean isFav) {
        /*NOT USE HERE*/
    }

    @Override
    public void onAddJourneyFav() {
        /*NOT USE HERE*/
    }

    @Override
    public void onRemoveJourneyFav() {
        /*NOT USE HERE*/
    }

    @Override
    public void showToast(String message) {
        /*NOT USE HERE*/
    }

    @Override
    public void onSetDialog(MaterialDialog.Builder dialog, ArrayList<String> names) {
        /*NOT USE HERE*/
    }
}
