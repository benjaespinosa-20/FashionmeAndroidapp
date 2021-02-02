package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.ISubcategoryInteractor;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.ISubcategoryPresenter;
import mx.app.fashionme.view.interfaces.ISubcategoryView;

/**
 * Created by heriberto on 13/03/18.
 */

public class SubcategoryPresenter implements ISubcategoryPresenter, ISubcategoryInteractor.OnGetSubcategoriesFinishedListener {

    private ISubcategoryView view;
    private ISubcategoryInteractor interactor;
    private Context context;
    private Activity activity;

    public SubcategoryPresenter(ISubcategoryView view, ISubcategoryInteractor interactor, Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void getSubcategories(int categoryId) {
        interactor.getDataFromAPI(context, categoryId, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SessionPrefs.PREFS_FILE_NAME, 0);
        Bundle bundle = new Bundle();

        boolean isClosetIdeal = sharedPreferences.getBoolean(SessionPrefs.KEY_CLOSET_IDEAL, false);
        if (isClosetIdeal){
            bundle.putString("screen_name", "Closet ideal/Subcategorias/Android");
        } else {
            bundle.putString("screen_name", "Closet FME/Subcategorias/Android");
        }

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccess(ArrayList<Subcategory> subcategories) {
        if (view != null) {
            if (subcategories.size() == 0) {
                view.showEmptyList();
                return;
            }

            view.generateLinearLayoutVertical();
            view.initializeAdapter(view.createAdapter(subcategories));
        }

    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }
}
