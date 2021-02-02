package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IJourneyInteractor;
import mx.app.fashionme.interactor.interfaces.IShowJourneyInteractor;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.presenter.interfaces.IShowJourneyPresenter;
import mx.app.fashionme.view.interfaces.IShowJourneyView;

public class ShowJourneyPresenter implements IShowJourneyPresenter, IJourneyInteractor.JourneyListener {

    public static final String TAG = ShowJourneyPresenter.class.getSimpleName();
    private IShowJourneyView view;
    private IShowJourneyInteractor interactor;
    private Context context;


    public ShowJourneyPresenter(IShowJourneyView view, IShowJourneyInteractor interactor, Context context) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
    }

    @Override
    public void getJourney(int idJourney) {
        view.showLoading(true);
        interactor.getJourney(context, idJourney, this);
    }

    @Override
    public void checkFav(int idJourney) {
        interactor.checkFav(context, idJourney, this);
    }

    @Override
    public void removeFav(int idJourney) {
        interactor.removeFav(context, idJourney, this);
    }

    @Override
    public void addFav(int idJourney) {
        interactor.addToFav(context, idJourney, this);
    }

    @Override
    public void openDialogChecklist(Journey journey, MaterialDialog.Builder dialog) {
        interactor.openDialog(context, journey, dialog, this);
    }

    @Override
    public void checkListSelected(ArrayList<String> checklistSelected) {
        interactor.saveChecklist(checklistSelected);
    }

    @Override
    public void openActivityChecklist(Journey journey) {
        interactor.openActivity(context, journey, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Viaje/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onGetJourneys(ArrayList<Journey> journeys) {
        /*NOT USE HERE*/
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showLoading(false);
            view.showError(err);
        }
    }

    @Override
    public void onGetJourneyById(Journey journey) {
        if (view != null) {
            view.showLoading(false);
            view.generateLinearLayout();
            view.initializeAdapter(view.createAdapter(journey));
        }
    }

    @Override
    public void onCheckFav(boolean isFav) {
        if (view != null) {
            view.setButtonFav(isFav);
        }
    }

    @Override
    public void onAddJourneyFav() {
        if (view != null) {
            view.favAdded();
            Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveJourneyFav() {
        if (view != null) {
            view.favRemoved();
            Toast.makeText(context, "Removido de favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetDialog(MaterialDialog.Builder dialog, ArrayList<String> names) {
        if (view != null) {
            view.showDialogChecklist(dialog, names);
        }
    }
}
