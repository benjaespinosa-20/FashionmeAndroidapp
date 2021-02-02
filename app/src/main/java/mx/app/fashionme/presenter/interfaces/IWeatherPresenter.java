package mx.app.fashionme.presenter.interfaces;

import android.app.PendingIntent;
import android.location.LocationListener;

public interface IWeatherPresenter {
    void checkConditions();
    void showExplanation();


    void getWeather(double latitude, double longitude);

    void stopLocationUpdates();

    //void getWeather(double latitude, double longitude);
}
