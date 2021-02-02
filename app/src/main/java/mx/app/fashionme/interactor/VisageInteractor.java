package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IVisageInteractor;
import mx.app.fashionme.pojo.Visage;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisageInteractor implements IVisageInteractor {

    private static final String TAG = "Visage";

    @Override
    public void getData(Context context, VisageListener listener) {

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<Visage>>> call = endpointsApi.getVisageByUser(
                SessionPrefs.get(context).getUserId(),
                SessionPrefs.get(context).getGenre());

        call.enqueue(new Callback<Base<ArrayList<Visage>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Visage>>> call, Response<Base<ArrayList<Visage>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onErrorGetData(response.errorBody().string());
                            Log.e(TAG, "error request=\n" + response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, "IO EXCEPTION error", e.getCause());
                            listener.onErrorGetData(context.getString(R.string.algo_salio_mal));
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorGetData(apiError != null ? apiError.getError():context.getString(R.string.algo_salio_mal));
                        Log.e(TAG, "error request=\n" + apiError);
                        return;
                    }
                }

                listener.onSuccessGetData(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Visage>>> call, Throwable t) {
                Log.e(TAG, "Error request connection", t);
                listener.onErrorGetData(context.getString(R.string.error_network));
            }
        });
    }
}
