package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import mx.app.fashionme.interactor.interfaces.ITrendInteractor;
import mx.app.fashionme.pojo.Trend;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 26/03/18.
 */

public class TrendInteractor implements ITrendInteractor {

    public static final String TAG = TrendInteractor.class.getSimpleName();

    @Override
    public void getTrend(Context context, int idTrend, final OnGetTrendListener callback) {

        if (idTrend > 0) {

            RestApiAdapter apiAdapter = new RestApiAdapter();
            EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
            Call<Base<Trend>> trendResponse = endpointsApi.getTrendById(idTrend);

            trendResponse.enqueue(new Callback<Base<Trend>>() {
                @Override
                public void onResponse(Call<Base<Trend>> call, Response<Base<Trend>> response) {
                    if (!response.isSuccessful()) {
                        if (response.errorBody().contentType().type().equals("text")) {
                            try {
                                callback.onFailure(response.errorBody().string());
                                return;
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                callback.onFailure("Algo salió mal, intenta más tarde");
                                return;
                            }
                        } else {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            callback.onFailure(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                            return;
                        }
                    }
                    callback.onSuccess(response.body().getData());
                }

                @Override
                public void onFailure(Call<Base<Trend>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    t.printStackTrace();
                    callback.onFailure("Error de conexión, intenta más tarde");
                }
            });

            return;
        }

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<Trend>> trendResponseCall = endpointsApi.getTrend();

        trendResponseCall.enqueue(new Callback<Base<Trend>>() {
            @Override
            public void onResponse(Call<Base<Trend>> call, Response<Base<Trend>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onFailure(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            callback.onFailure("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onFailure(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<Trend>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onFailure("Error de conexión, intenta más tarde");
            }
        });
    }

    @Override
    public void checkFav(Context context, final int idTrend, final TrendListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<Trend>> call = endpointsApi.getFavoriteByUserByIdTrend(userId, idTrend, "trend");

        call.enqueue(new Callback<Base<Trend>>() {
            @Override
            public void onResponse(Call<Base<Trend>> call, Response<Base<Trend>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onError(response.errorBody().string());
                            listener.onCheckFav(false);
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.onError("Algo salió mal, intenta más tarde");
                            listener.onCheckFav(false);
                            return;
                        }
                    } else {
                        if (response.code() != 404){
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        }
                        listener.onCheckFav(false);
                        return;
                    }
                }

                if (response.body().getData().getId() != idTrend){
                    listener.onCheckFav(false);
                    return;
                }
                listener.onCheckFav(true);
            }

            @Override
            public void onFailure(Call<Base<Trend>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
                listener.onCheckFav(false);
            }
        });

    }

    @Override
    public void addToFav(Context context, final int idTrend, final TrendListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Trend trend = new Trend();
        trend.setType("trend");
        trend.setFavorite(idTrend);

        Call<Base<Trend>> call = endpointsApi.setFavoriteTrendByUser(userId, trend);

        call.enqueue(new Callback<Base<Trend>>() {
            @Override
            public void onResponse(Call<Base<Trend>> call, Response<Base<Trend>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                if (response.body().getData().getId() != idTrend){
                    listener.onError("No se pudo agregar a favoritos");
                    return;
                }

                listener.onAddTrendFav();
            }

            @Override
            public void onFailure(Call<Base<Trend>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });

    }

    @Override
    public void removerFavByIdTrend(Context context, final int idTrend, final TrendListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<Trend>> call = endpointsApi.removeFavoriteByUserByTrend(userId, idTrend, "trend");

        call.enqueue(new Callback<Base<Trend>>() {
            @Override
            public void onResponse(Call<Base<Trend>> call, Response<Base<Trend>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                if (response.body().getData().getId() != idTrend){
                    listener.onError("No se pudo quitar el favorito");
                    return;
                }

                listener.onRemoveTrendFav();
            }

            @Override
            public void onFailure(Call<Base<Trend>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });

    }
}
