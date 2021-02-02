package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IChecklistInteractor;
import mx.app.fashionme.pojo.Characteristic;
import mx.app.fashionme.presenter.interfaces.IChecklistPresenter;
import mx.app.fashionme.view.interfaces.IChecklistView;

public class ChecklistPresenter implements IChecklistPresenter, IChecklistInteractor.ChecklistListener {

    private IChecklistView view;
    private IChecklistInteractor interactor;
    private Context context;

    public ChecklistPresenter(IChecklistView view, IChecklistInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getChecklist(int idJourney) {
        interactor.getDataFromAPi(context, idJourney, this);
    }

    @Override
    public void saveChecklist(ListView listView, int idJourney) {
        view.showProgressDialog(true);
        interactor.saveChecklist(listView, idJourney, context, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Checklist/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onGetJourney(ArrayList<Characteristic> list) {
        if (view != null) {
            view.setAdapterChecklist(list);
        }
    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.showError(error);
        }
    }

    @Override
    public void onChecklistSaved() {
        if (view != null) {
            view.showProgressDialog(false);
            Toast.makeText(context, "Guardado!", Toast.LENGTH_SHORT).show();
        }
    }
}
