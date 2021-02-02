package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.ITipInteractor;
import mx.app.fashionme.pojo.Tip;
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

public class TipInteractor implements ITipInteractor {

    public static final String TAG = TipInteractor.class.getSimpleName();

    @Override
    public void getTip(final Context context, int idTip, final OnGetTipListener callback) {

        if (idTip > 0){
            RestApiAdapter restApiAdapter = new RestApiAdapter();
            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<Base<Tip>> tipResponse = endpointsApi.getTipById(idTip);

            tipResponse.enqueue(new Callback<Base<Tip>>() {
                @Override
                public void onResponse(Call<Base<Tip>> call, Response<Base<Tip>> response) {
                    if (!response.isSuccessful()) {
                        if (response.errorBody().contentType().type().equals("text")) {
                            try {
                                callback.onFail(response.errorBody().string());
                                return;
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                callback.onFail("Algo salió mal, intenta más tarde");
                                return;
                            }
                        } else {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            Log.e(TAG, apiError.getError());
                            callback.onFail(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                            return;
                        }
                    }

                    callback.onSuccess(response.body().getData());
                }

                @Override
                public void onFailure(Call<Base<Tip>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    t.printStackTrace();
                    callback.onFail(context.getResources().getString(R.string.error_network));
                }
            });

            return;
        }

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<Tip>> tipResponseCall = endpointsApi.getTip();

        tipResponseCall.enqueue(new Callback<Base<Tip>>() {
            @Override
            public void onResponse(Call<Base<Tip>> call, Response<Base<Tip>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onFail(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            callback.onFail("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.e(TAG, apiError.getError());
                        callback.onFail(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<Tip>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onFail(context.getResources().getString(R.string.error_network));
            }
        });
    }

    @Override
    public void checkFav(Context context, final int idTip, final TipListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<Tip>> call = endpointsApi.getFavoriteByUserByIdTip(userId, idTip, "tip");

        call.enqueue(new Callback<Base<Tip>>() {
            @Override
            public void onResponse(Call<Base<Tip>> call, Response<Base<Tip>> response) {
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

                if (response.body().getData().getId() != idTip){
                    listener.onCheckFav(false);
                    return;
                }
                listener.onCheckFav(true);
            }

            @Override
            public void onFailure(Call<Base<Tip>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
                listener.onCheckFav(false);
            }
        });
    }

    @Override
    public void removeFavByIdTip(Context context, final int idTip, final TipListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<Tip>> call = endpointsApi.removeFavoriteByUserByTip(userId, idTip, "tip");

        call.enqueue(new Callback<Base<Tip>>() {
            @Override
            public void onResponse(Call<Base<Tip>> call, Response<Base<Tip>> response) {
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
                        Log.e(TAG, apiError.getError());
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                if (response.body().getData().getId() != idTip){
                    listener.onError("No se pudo quitar el favorito");
                    return;
                }

                listener.onRemoveTrendFav();
            }

            @Override
            public void onFailure(Call<Base<Tip>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }

    @Override
    public void addToFav(Context context, final int idTip, final TipListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Tip tip = new Tip();
        tip.setType("tip");
        tip.setFavorite(idTip);

        Call<Base<Tip>> call = endpointsApi.setFavoriteTipByUser(userId, tip);

        call.enqueue(new Callback<Base<Tip>>() {
            @Override
            public void onResponse(Call<Base<Tip>> call, Response<Base<Tip>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.e(TAG, response.errorBody().string());
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
                        Log.e(TAG, apiError.getError());
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                if (response.body().getData().getId() != idTip){
                    listener.onError("No se pudo agregar a favoritos");
                    return;
                }

                listener.onAddTrendFav();
            }

            @Override
            public void onFailure(Call<Base<Tip>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }
}
