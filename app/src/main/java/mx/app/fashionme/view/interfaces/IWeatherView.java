package mx.app.fashionme.view.interfaces;

import android.location.LocationManager;

public interface IWeatherView {

    void setUpPresenterWeather();
    void showExplication();
    void showNeverAskDialog();
    void showAlertGPS();

    //void checkPermissionsStatus();
    //void getWeather(double latitude, double longitude);


    void setWeather(int resource, double temp, String desc, String city, String country);
    void showErrors(String err);

    void showProgress();
    void hideProgress();
}
