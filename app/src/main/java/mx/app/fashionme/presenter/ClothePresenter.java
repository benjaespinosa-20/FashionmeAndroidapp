package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IClotheInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.presenter.interfaces.IClothePresenter;
import mx.app.fashionme.view.interfaces.IClotheView;

/**
 * Created by heriberto on 14/03/18.
 */

public class ClothePresenter implements IClothePresenter, IClotheInteractor.OnGetClotheFinishedListener {

    private IClotheView view;
    private IClotheInteractor interactor;
    private Context context;

    public ClothePresenter(IClotheView view, IClotheInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getClothes(int subcategoryId, boolean isClosetIdeal) {
        interactor.getDataFromAPi(context, subcategoryId, isClosetIdeal, this);
    }

    @Override
    public void setAnalytics(boolean isClosetIdeal) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();

        if (isClosetIdeal){
            bundle.putString("screen_name", "Closet ideal/Ropa/Android");
        } else {
            bundle.putString("screen_name", "Closet FME/Ropa/Android");
        }

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccess(ArrayList<Clothe> clothes) {
        if (view != null) {
            if (clothes.size() == 0) {
                view.showEmptyList();
                return;
            }

            view.generateLinearLayoutVertical();
            view.initializeAdapter(view.createAdapter(clothes));
        }

    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }
}
