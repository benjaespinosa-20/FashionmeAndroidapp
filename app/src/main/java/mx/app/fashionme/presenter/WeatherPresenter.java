package mx.app.fashionme.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IWeatherInteractor;
import mx.app.fashionme.pojo.WeatherResp;
import mx.app.fashionme.presenter.interfaces.IWeatherPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.WeatherActivity;
import mx.app.fashionme.view.interfaces.IWeatherView;

public class WeatherPresenter implements IWeatherPresenter, IWeatherInteractor.WeatherListener {

    private IWeatherView view;
    private IWeatherInteractor interactor;
    private Context context;
    private Activity activity;

    public WeatherPresenter(IWeatherView view, IWeatherInteractor interactor,
                            Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void checkConditions() {
        if (view != null){
            view.showProgress();
        }
        interactor.checkConditionsWeather(this);
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
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, WeatherActivity.REQUEST_GPS_PERMISSION_CODE);
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
    public void onSuccessGetWeather(WeatherResp weatherResp) {
        if (view != null) {
            view.hideProgress();
            String icon = weatherResp.getWeather().get(0).getIcon();
            int resource = 0;

            switch (icon) {
                case "01d":
                    resource = R.drawable.ic_sol_144;
                    break;
                case "01n":
                    resource = R.drawable.ic_sol_144;
                    break;
                case "02d":
                    resource = R.drawable.ic_partly_cloudy_100;
                    break;
                case "02n":
                    resource = R.drawable.ic_partly_cloudy_100;
                    break;
                case "03d":
                    resource = R.drawable.ic_clouds_100;
                    break;
                case "03n":
                    resource = R.drawable.ic_clouds_100;
                    break;
                case "04d":
                    resource = R.drawable.ic_nubes_dispersas_100;
                    break;
                case "04n":
                    resource = R.drawable.ic_nubes_dispersas_100;
                    break;
                case "09d":
                    resource = R.drawable.ic_lluvia_ligera_100;
                    break;
                case "09n":
                    resource = R.drawable.ic_lluvia_ligera_100;
                    break;
                case "10d":
                    resource = R.drawable.ic_lluvia_100;
                    break;
                case "10n":
                    resource = R.drawable.ic_lluvia_100;
                    break;
                case "11d":
                    resource = R.drawable.ic_tormenta_100;
                    break;
                case "11n":
                    resource = R.drawable.ic_tormenta_100;
                    break;
                case "13d":
                    resource = R.drawable.ic_nieve_100;
                    break;
                case "13n":
                    resource = R.drawable.ic_nieve_100;
                    break;
                case "50d":
                    resource = R.drawable.ic_viento_100;
                    break;
                case "50n":
                    resource = R.drawable.ic_viento_100;
                    break;
                default:
                    resource = R.drawable.ic_sol_144;
                    break;
            }

            view.setWeather(resource,
                    weatherResp.getMain().getTemp(),
                    Utils.toCapitalName(weatherResp.getWeather().get(0).getDescription()),
                    weatherResp.getName(),
                    weatherResp.getSys().getCountry());
        }
    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.hideProgress();
            view.showErrors(error);
        }
    }
}
