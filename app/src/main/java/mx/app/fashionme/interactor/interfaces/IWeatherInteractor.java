package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import mx.app.fashionme.pojo.WeatherResp;

public interface IWeatherInteractor {
    void checkConditionsWeather(WeatherListener listener);
    void checkPermissions(Activity activity, WeatherListener listener);
    void checkShowExplanation(Activity activity, Context context, WeatherListener listener);
    void checkGPSStatus(Activity activity, WeatherListener listener);
    void getLocation(Activity activity, WeatherListener listener);

    void getWeatherFromOpenWeatherAPI(double latitude, double longitude, Context context, WeatherListener listener);

    void stopLocationUpdates(Activity activity);


    interface WeatherListener {
        void onCheckVersion(boolean isNewVersion);
        void onCheckPermissions(boolean showExplication);
        void onCheckPermissions();
        void onCheckExplanation();
        void onCheckGPSStatus(boolean gpsIsEnabled);
        void onGetLocationFinished(double latitude, double longitude);

        void onSuccessGetWeather(WeatherResp body);
        void onError(String error);

    }
}
