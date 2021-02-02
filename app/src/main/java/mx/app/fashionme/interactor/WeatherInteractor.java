package mx.app.fashionme.interactor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IWeatherInteractor;
import mx.app.fashionme.pojo.WeatherResp;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherInteractor implements IWeatherInteractor {

    private static final String TAG = WeatherInteractor.class.getSimpleName();
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void checkConditionsWeather(WeatherListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listener.onCheckVersion(true);
        } else {
            listener.onCheckVersion(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkPermissions(Activity activity, WeatherListener listener) {
        int statusPermission = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (statusPermission != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                listener.onCheckPermissions(true);
            } else {
                listener.onCheckPermissions( false);
            }
        } else {
            listener.onCheckPermissions();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkShowExplanation(Activity activity, Context context, WeatherListener listener) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(activity, "Como denegaste el acceso a la ubicación, no se mostrará el clima.", Toast.LENGTH_LONG).show();
        } else {
            listener.onCheckExplanation();
        }
    }

    @Override
    public void checkGPSStatus(Activity activity, WeatherListener listener) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                listener.onCheckGPSStatus(true);
            } else {
                listener.onCheckGPSStatus(false);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getLocation(Activity activity, WeatherListener listener) {

        Location l = getLastKnownLocation(activity);

        if (l != null) {
            listener.onGetLocationFinished(l.getLatitude(), l.getLongitude());
            return;
        }

        locationManager.requestLocationUpdates("gps", 10000*60, 5, getLocationListener(activity, listener));
    }

    private LocationListener getLocationListener(Activity activity, WeatherListener listener) {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                listener.onGetLocationFinished(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(i);
            }
        };

        return locationListener;
    }

    @Override
    public void getWeatherFromOpenWeatherAPI(double latitude, double longitude, final Context context, final WeatherListener listener) {

        Map<String, String> data = new HashMap<>();
        data.put("lang", "es");
        data.put("units", "metric");
        data.put("APPID", "85214a0e47b64a6c5d6327f3d8d5d567");
        data.put("lon", String.valueOf(longitude));
        data.put("lat", String.valueOf(latitude));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionOpenWeatherApi();
        Call<WeatherResp> weatherRespCall = endpointsApi.getWeather(data);

        weatherRespCall.enqueue(new Callback<WeatherResp>() {
            @Override
            public void onResponse(Call<WeatherResp> call, Response<WeatherResp> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.d(TAG, "Error --text--" + response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
//                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
//                        callback.onFail(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        Log.d(TAG, "Error response body");
                        return;
                    }
                }
                Log.d(TAG, response.body().toString());
                listener.onSuccessGetWeather(response.body());
            }

            @Override
            public void onFailure(Call<WeatherResp> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                t.printStackTrace();
                listener.onError(context.getResources().getString(R.string.error_network));
            }
        });

    }

    @Override
    public void stopLocationUpdates(Activity activity) {
        if (locationManager != null){
            locationManager.removeUpdates(getLocationListener(activity, null));
        }
    }

    private Location getLastKnownLocation(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }

            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
