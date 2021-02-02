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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.app.fashionme.R;
import mx.app.fashionme.db.FavConstructor;
import mx.app.fashionme.interactor.interfaces.IRecommendationInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.pojo.WeatherResp;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationInteractor implements IRecommendationInteractor {

    public static final String TAG = RecommendationInteractor.class.getSimpleName();
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void getRecommendationsFromAPI(int condition, double temp, Context context, boolean isMyCloset, final RecommendationListener listener) {

//        String temp = null;

        /*if (idTemp >= 200 && idTemp <=232) {
            temp = "Thunderstorm";
        } else if (idTemp >= 300 && idTemp <= 321) {
            temp = "Drizzle";
        } else if (idTemp >= 500 && idTemp <= 531) {
            temp = "Rain";
        } else if (idTemp >= 600 && idTemp <= 622) {
            temp = "Snow";
        } else if (idTemp >= 701 && idTemp <= 781) {
            temp = "Atmosphere";
        } else if (idTemp == 800) {
            temp = "Clear";
        } else if (idTemp >= 801 && idTemp <= 804) {
            temp = "Clouds";
        }*/

        int userId = SessionPrefs.get(context).getUserId();

        Map<String, String> data = new HashMap<>();

        data.put("conditionId", String.valueOf(condition));
        data.put("temp", String.valueOf(temp));

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<ArrayList<Clothe>>> call;

        if (isMyCloset) {
            call = endpointsApi.getRecommendationsByUserClothes(userId, data);
        } else {
            call = endpointsApi.getRecommendations(userId, data);
        }

        if (call != null) {
            call.enqueue(new Callback<Base<ArrayList<Clothe>>>() {
                @Override
                public void onResponse(Call<Base<ArrayList<Clothe>>> call, Response<Base<ArrayList<Clothe>>> response) {
                    if (!response.isSuccessful()) {
                        if (response.errorBody().contentType().type().equals("text")) {
                            try {
                                listener.onGetRecommendationError(response.errorBody().string());
                                return;
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                listener.onGetRecommendationError("Algo salió mal, intenta más tarde");
                                return;
                            }
                        } else {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            listener.onGetRecommendationError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                            return;
                        }
                    }
                    listener.onGetRecommendationSuccess(response.body().getData());
                }

                @Override
                public void onFailure(Call<Base<ArrayList<Clothe>>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    t.printStackTrace();
                    listener.onGetRecommendationError(t.getMessage());
                }
            });
        } else {
            listener.onGetRecommendationError("Algo salió mal, intenta más tarde");
        }
    }

    @Override
    public void addAllToCart(Context context, ArrayList<Clothe> clothesAll, RecommendationListener listener) {

        for (Clothe clothe:clothesAll) {


            FavConstructor constructor = new FavConstructor(context);
            constructor.addToShoppingCart(clothe);
        }

        listener.onAddedToCart();
    }

    @Override
    public void checkConditionsPermissions(RecommendationListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listener.onCheckVersion(true);
        } else {
            listener.onCheckVersion(false);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkPermissions(Activity activity, RecommendationListener listener) {
        int statusPermission = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (statusPermission != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                listener.onCheckPermissions(true);
            } else {
                listener.onCheckPermissions(false);
            }
        } else {
            listener.onCheckPermissions();
        }
    }

    @Override
    public void checkGPSStatus(Activity activity, RecommendationListener listener) {
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
    public void getLocation(Activity activity, RecommendationListener listener) {

        Location l = getLastKnowLocation(activity);

        if (l != null) {
            listener.onGetLocationFinished(l.getLatitude(), l.getLongitude());
            return;
        }

        locationManager.requestLocationUpdates("gps", 5000, 500, getLocationListener(activity, listener));
    }

    private LocationListener getLocationListener(Activity activity, RecommendationListener listener) {
        if (locationListener == null) {
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
        }

        return locationListener;
    }

    private Location getLastKnowLocation(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider: providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null){
                continue;
            }

            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void getWeatherFromOpenWeatherAPI(double latitude, double longitude, Context context, RecommendationListener listener) {

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
                            listener.onGetRecommendationError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
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
                listener.onGetRecommendationError(context.getResources().getString(R.string.error_network));
            }
        });
    }

    @Override
    public void stopLocationUpdates(Activity activity) {
        if (locationManager != null)
            locationManager.removeUpdates(getLocationListener(activity, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkShowExplanation(Activity activity, Context context, RecommendationListener listener) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(activity, "Como denegaste el acceso a la ubicación, no se mostrará el clima.", Toast.LENGTH_LONG).show();
        } else {
            listener.onCheckExplanation();
        }
    }

    private void getConditions(Context context, RecommendationListener listener) {

        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

        Call<Base<User>> call = endpointsApi.getUserById(userId);

        call.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.w(TAG, "onResponse not successful: "  + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.w(TAG, "onResoonse not successful: " + apiError.getError());
                    }
                } else {
                    if (response.body().getData().getBody() == null) {
                        listener.finishAndBodyPage();
                        return;
                    }

                    if (response.body().getData().getCharacteristics().getData().size() == 0) {
                        listener.finishAndCharacteristicsPage();
                        return;
                    }

                    if (response.body().getData().getColor() == null) {
                        listener.finishAndColorPage();
                    }

                }
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.w(TAG, ">>>onFailure()", t);
                listener.onError("Error en la conexión");
            }
        });

    }
}
