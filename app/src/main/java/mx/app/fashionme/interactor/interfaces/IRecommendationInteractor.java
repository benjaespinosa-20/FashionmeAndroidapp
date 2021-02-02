package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.WeatherResp;

public interface IRecommendationInteractor {
    void getRecommendationsFromAPI(int condition, double temp, Context context, boolean isMyCloset, RecommendationListener listener);

    void addAllToCart(Context context, ArrayList<Clothe> clothesAll, RecommendationListener listener);

    void checkConditionsPermissions(RecommendationListener listener);

    void checkPermissions(Activity activity, RecommendationListener listener);

    void checkShowExplanation(Activity activity, Context context, RecommendationListener listener);

    void checkGPSStatus(Activity activity, RecommendationListener listener);

    void getLocation(Activity activity, RecommendationListener listener);

    void getWeatherFromOpenWeatherAPI(double latitude, double longitude, Context context, RecommendationListener listener);

    void stopLocationUpdates(Activity activity);

    interface RecommendationListener {
        void onGetRecommendationSuccess(ArrayList<Clothe> clothes);
        void onGetRecommendationError(String error);
        void onAddedToCart();

        void onCheckVersion(boolean isNewVersion);
        void onCheckPermissions(boolean showExplication);
        void onCheckPermissions();
        void onCheckExplanation();
        void onCheckGPSStatus(boolean gpsIsEnabled);
        void onGetLocationFinished(double latitude, double longitude);

        void onSuccessGetWeather(WeatherResp body);

        void onError(String message);

        void finishAndBodyPage();

        void finishAndCharacteristicsPage();

        void finishAndColorPage();
    }
}
