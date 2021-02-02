package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IClotheInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 5/04/18.
 */

public class ClotheInteractor implements IClotheInteractor {

    public static final String TAG = ClotheInteractor.class.getSimpleName();

    @Override
    public void getDataFromAPi(Context context, int subcategoryId, boolean isClosetIdeal, final OnGetClotheFinishedListener callback) {
        
        if (isClosetIdeal){
            int userId = SessionPrefs.get(context).getUserId();
            String genre = SessionPrefs.get(context).getGenre().toLowerCase();

            RestApiAdapter restApiAdapter = new RestApiAdapter();
            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

            Call<Base<ArrayList<Clothe>>> clotheCall;

            switch (genre) {
                case "woman":
                    clotheCall = endpointsApi.getClothesWoman(subcategoryId, userId);
                    break;
                case "man":
                    clotheCall = endpointsApi.getClothesMan(subcategoryId, userId);
                    break;
                default:
                    clotheCall = endpointsApi.getClothes(subcategoryId, userId);
                    break;
            }

            clotheCall.enqueue(new Callback<Base<ArrayList<Clothe>>>() {
                @Override
                public void onResponse(Call<Base<ArrayList<Clothe>>> call, Response<Base<ArrayList<Clothe>>> response) {
                    if (!response.isSuccessful()) {
                        if (response.errorBody().contentType().type().equals("text")) {
                            try {
                                callback.onError(response.errorBody().string());
                                return;
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                callback.onError("Algo salió mal, intenta más tarde");
                                return;
                            }
                        } else {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            callback.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                            return;
                        }
                    }
                    callback.onSuccess(response.body().getData());
                }

                @Override
                public void onFailure(Call<Base<ArrayList<Clothe>>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    t.printStackTrace();
                    callback.onError(t.getMessage());
                }
            });
        } else {
            String genre = SessionPrefs.get(context).getGenre().toLowerCase();

            RestApiAdapter restApiAdapter = new RestApiAdapter();
            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

            Call<Base<ArrayList<Clothe>>> clotheCall;

            switch (genre) {
                case "woman":
                    clotheCall = endpointsApi.getClothesWoman(subcategoryId);
                    break;
                case "man":
                    clotheCall = endpointsApi.getClothesMan(subcategoryId);
                    break;
                    default:
                        clotheCall = endpointsApi.getClothes(genre);
                        break;
            }

            clotheCall.enqueue(new Callback<Base<ArrayList<Clothe>>>() {
                @Override
                public void onResponse(Call<Base<ArrayList<Clothe>>> call, Response<Base<ArrayList<Clothe>>> response) {
                    if (!response.isSuccessful()) {
                        if (response.errorBody().contentType().type().equals("text")) {
                            try {
                                callback.onError(response.errorBody().string());
                                return;
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                callback.onError("Algo salió mal, intenta más tarde");
                                return;
                            }
                        } else {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            callback.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                            return;
                        }
                    }
                    callback.onSuccess(response.body().getData());
                }

                @Override
                public void onFailure(Call<Base<ArrayList<Clothe>>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    t.printStackTrace();
                    callback.onError(t.getMessage());
                }
            });
        }
    }
}
