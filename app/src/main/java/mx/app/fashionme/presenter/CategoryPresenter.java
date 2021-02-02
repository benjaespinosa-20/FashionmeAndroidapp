package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.ICategoryInteractor;
import mx.app.fashionme.pojo.Category;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.ICategoryPresenter;
import mx.app.fashionme.view.BodyActivity;
import mx.app.fashionme.view.CharacteristicActivity;
import mx.app.fashionme.view.ColorTestActivity;
import mx.app.fashionme.view.interfaces.ICategoryView;

/**
 * Created by heriberto on 13/03/18.
 */

public class CategoryPresenter implements ICategoryPresenter, ICategoryInteractor.OnGetCategoriesFishishedListener {

    private ICategoryView view;
    private ICategoryInteractor interactor;
    private Context context;
    private Activity activity;

    public CategoryPresenter(ICategoryView view, ICategoryInteractor interactor, Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void getCategories() {
        if (view != null) {
            view.showProgress(true);
        }
        interactor.getDataFromAPI(context, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle params = new Bundle();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SessionPrefs.PREFS_FILE_NAME, 0);
        boolean isClosetIdeal = sharedPreferences.getBoolean(SessionPrefs.KEY_CLOSET_IDEAL, false);
        if (isClosetIdeal){
            params.putString("screen_name", "Closet ideal/Categorias/Android");
        } else {
            params.putString("screen_name", "Closet FMW/Categorias/Android");
        }
        firebaseAnalytics.logEvent("open_screen", params);
    }

    @Override
    public void onSuccess(ArrayList<Category> categories) {
        if (view != null){
            view.showProgress(false);
            if (categories.size() == 0){
                view.showEmptyList();
                return;
            }

            view.generateLinearLayoutVertical();
            view.initializeAdapter(view.createAdapter(categories));
        }
    }

    @Override
    public void onError(String error) {
        if (view != null){
            view.showProgress(false);
            view.showError(error);
        }
    }

    @Override
    public void finishAndBodyPage() {
        activity.startActivity(new Intent(activity, BodyActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finish();
    }

    @Override
    public void finishAndCharacteristicsPage() {
        activity.startActivity(new Intent(activity, CharacteristicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finish();
    }

    @Override
    public void finishAndColorPage() {
        activity.startActivity(new Intent(activity, ColorTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finish();
    }
}
