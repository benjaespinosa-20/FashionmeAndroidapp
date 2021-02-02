package mx.app.fashionme.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IRecommendationInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.WeatherResp;
import mx.app.fashionme.presenter.interfaces.IRecommendationPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.BodyActivity;
import mx.app.fashionme.view.CharacteristicActivity;
import mx.app.fashionme.view.ColorTestActivity;
import mx.app.fashionme.view.MyClosetActivity;
import mx.app.fashionme.view.RecommendationActivity;
import mx.app.fashionme.view.interfaces.IRecommendationView;

public class RecommendationPresenter implements IRecommendationPresenter, IRecommendationInteractor.RecommendationListener {
    private static final String TAG = RecommendationPresenter.class.getSimpleName();

    private IRecommendationView view;
    private IRecommendationInteractor interactor;
    private Context context;
    private Activity activity;

    public RecommendationPresenter(IRecommendationView view, IRecommendationInteractor interactor,
                                   Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void saveAllToCart(ArrayList<Clothe> clothesAll) {
        interactor.addAllToCart(context, clothesAll, this);
    }

    @Override
    public void checkConditions() {
        if (view != null) {
            view.showProgressBar(true);
        }
        interactor.checkConditionsPermissions(this);

    }

    @Override
    public void showExplanation() {
        interactor.checkShowExplanation(activity, context, this);
    }

    @Override
    public void getWeather(double latitude, double longitude) {
        interactor.getWeatherFromOpenWeatherAPI(latitude, longitude, context, this);
    }

    @Override
    public void stopLocationUpdates() {
        interactor.stopLocationUpdates(activity);
    }

    @Override
    public void setSAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Recomendacion/Android");
        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onGetRecommendationSuccess(ArrayList<Clothe> clothes) {
        if (view != null) {

            view.showProgressBar(false);

            if (clothes.size() == 0) {
                view.showEmptyList();
                return;
            }

            view.generateGridLayout(Utils.COLUMNS_RECOMMENDATIONS);
            view.initializeAdapter(view.createAdapter(clothes));
        }
    }

    @Override
    public void onGetRecommendationError(String error) {
        if (view != null) {
            view.showProgressBar(false);
            view.showError(error);
        }
    }

    @Override
    public void onAddedToCart() {
        if (view != null) {
            view.removeFabCart();
        }
        Toast.makeText(context, "Agregadas al carrito", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckVersion(boolean isNewVersion) {
        if (isNewVersion) {
            interactor.checkPermissions(activity, this);
        } else {
            interactor.checkGPSStatus(activity, this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckPermissions(boolean showExplication) {
        if (showExplication) {
            if (view != null) {
                view.showExplication();
            }
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RecommendationActivity.REQUEST_GPS_PERMISSION_CODE);
        }
    }

    @Override
    public void onCheckPermissions() {
        interactor.checkGPSStatus(activity, this);
    }

    @Override
    public void onCheckExplanation() {
        if (view != null) {
            view.showNeverAskDialog();
        }
    }

    @Override
    public void onCheckGPSStatus(boolean gpsIsEnabled) {
        if (!gpsIsEnabled) {
            if (view != null) {
                view.showAlertGPS();
            }
            return;
        }
        interactor.getLocation(activity, this);
    }

    @Override
    public void onGetLocationFinished(double latitude, double longitude) {
        interactor.getWeatherFromOpenWeatherAPI(latitude, longitude, context, this);
    }

    @Override
    public void onSuccessGetWeather(WeatherResp body) {

        boolean myCloset = false;
        Intent intent = activity.getIntent();
        if (intent != null) {
            myCloset = intent.getBooleanExtra(MyClosetActivity.MY_CLOSET, false);
        }

        int condition = body.getWeather().get(0).getId();
        double temp = body.getMain().getTemp();

        interactor.getRecommendationsFromAPI(condition, temp, context, myCloset,this);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
