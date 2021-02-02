package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IJourneyInteractor;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JourneyInteractor implements IJourneyInteractor {
    private static final String TAG = JourneyInteractor.class.getSimpleName();

    @Override
    public void getDataJourneys(Context context, final JourneyListener listener) {
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<ArrayList<Journey>>> call = endpointsApi.getJourneys();

        call.enqueue(new Callback<Base<ArrayList<Journey>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Journey>>> call, Response<Base<ArrayList<Journey>>> response) {
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
                listener.onGetJourneys(response.body().getData());

            }

            @Override
            public void onFailure(Call<Base<ArrayList<Journey>>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }
}
