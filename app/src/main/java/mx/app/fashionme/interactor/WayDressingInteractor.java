package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IWayDressingInteractor;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WayDressingInteractor implements IWayDressingInteractor {
    public static final String TAG = WayDressingInteractor.class.getSimpleName();

    @Override
    public void getWaysFromAPI(Context context, final OnGetWaysFinishedListener callback) {

        RestApiAdapter adapter = new RestApiAdapter();
        EndpointsApi endpointsApi = adapter.establecerConexionRestApi();
        Call<Base<ArrayList<WayDressing>>> responseCall = endpointsApi.getWaysDressing();

        responseCall.enqueue(new Callback<Base<ArrayList<WayDressing>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<WayDressing>>> call, Response<Base<ArrayList<WayDressing>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onErrorWays(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            callback.onErrorWays("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onErrorWays(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                callback.onSuccessWays(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<ArrayList<WayDressing>>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onErrorWays(t.getMessage());
            }
        });
    }
}
