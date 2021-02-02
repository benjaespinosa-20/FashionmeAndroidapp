package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IMakeupInteractor;
import mx.app.fashionme.pojo.Makeup;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeupInteractor implements IMakeupInteractor {

    private static final String TAG = "Makeup";

    @Override
    public void getData(Context context, String typeMH, MakeupListener listener) {

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<ArrayList<Makeup>>> call;

        if (typeMH.equals(Constants.VALUE_MH_M)) {
             call = endpointsApi.getMakeupsByUser(
                    SessionPrefs.get(context).getUserId()
            );
        } else if (typeMH.equals(Constants.VALUE_MH_H)){
            call = endpointsApi.getHairByUser(
                    SessionPrefs.get(context).getUserId()
            );
        } else {
            call = null;
            listener.onErrorGetData("Ocurrio un error");
            return;
        }


        call.enqueue(new Callback<Base<ArrayList<Makeup>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Makeup>>> call, Response<Base<ArrayList<Makeup>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onErrorGetData(response.errorBody().string());
                            Log.e(TAG, "error request=\n" + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, "IO EXCEPTION error", e.fillInStackTrace());
                            listener.onErrorGetData(context.getString(R.string.algo_salio_mal));
                            return;
                        }
                    } else if (response.code() == 412){
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorGetData(apiError.getError());
                        return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorGetData(apiError != null ? apiError.getError():context.getString(R.string.algo_salio_mal));
                        Log.e(TAG, "error request=\n" + apiError);
                        return;
                    }
                }

                listener.onSuccessGetData(response.body().getData(), typeMH);
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Makeup>>> call, Throwable t) {
                Log.e(TAG, "Error request connection", t);
                listener.onErrorGetData(context.getString(R.string.error_network));
            }
        });
    }
}
