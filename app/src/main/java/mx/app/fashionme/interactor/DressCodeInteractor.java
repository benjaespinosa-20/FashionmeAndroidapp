package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IDressCodeInteractor;
import mx.app.fashionme.pojo.DressCode;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DressCodeInteractor implements IDressCodeInteractor {

    public static final String TAG = DressCodeInteractor.class.getSimpleName();

    @Override
    public void getDressCode(final Context context, int idDressCode, final OnGetDressCodeFinishedListener callback) {
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<WayDressing>> callResponse = endpointsApi.getDressCodeById(idDressCode);

        callResponse.enqueue(new Callback<Base<WayDressing>>() {
            @Override
            public void onResponse(Call<Base<WayDressing>> call, Response<Base<WayDressing>> response) {
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
                        callback.onFail(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<WayDressing>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onFail(context.getResources().getString(R.string.error_network));
            }
        });
    }

    @Override
    public void checkFavDressCode(Context context, final int idDressCode, final DressCodeListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<WayDressing>> call = endpointsApi.getFavoriteByUserByIdWayDressing(userId, idDressCode, "way-dressing");

        call.enqueue(new Callback<Base<WayDressing>>() {
            @Override
            public void onResponse(Call<Base<WayDressing>> call, Response<Base<WayDressing>> response) {
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

                if (response.body().getData().getId() != idDressCode){
                    listener.onCheckFav(false);
                    return;
                }
                listener.onCheckFav(true);
            }

            @Override
            public void onFailure(Call<Base<WayDressing>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
                listener.onCheckFav(false);
            }
        });
    }

    @Override
    public void removeFavByIdDressCode(Context context, final int idDressCode, final DressCodeListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<WayDressing>> wayDressingCall = endpointsApi.removeFavoriteByUserByWayDress(userId, idDressCode, "way-dressing");

        wayDressingCall.enqueue(new Callback<Base<WayDressing>>() {
            @Override
            public void onResponse(Call<Base<WayDressing>> call, Response<Base<WayDressing>> response) {
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

                if (response.body().getData().getId() != idDressCode){
                    listener.onError("No se pudo quitar el favorito");
                    return;
                }

                listener.onRemoveTrendFav();
            }

            @Override
            public void onFailure(Call<Base<WayDressing>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }

    @Override
    public void addToFav(Context context, final int idDressCode, final DressCodeListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        WayDressing wayDressing = new WayDressing();
        wayDressing.setType("way-dressing");
        wayDressing.setFavorite(idDressCode);

        Call<Base<WayDressing>> call = endpointsApi.setFavoriteWayDressByUser(userId, wayDressing);

        call.enqueue(new Callback<Base<WayDressing>>() {
            @Override
            public void onResponse(Call<Base<WayDressing>> call, Response<Base<WayDressing>> response) {
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
                if (response.body().getData().getId() != idDressCode){
                    listener.onError("No se pudo agregar a favoritos");
                    return;
                }

                listener.onAddTrendFav();
            }

            @Override
            public void onFailure(Call<Base<WayDressing>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }
}
