package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IClientProfileInteractor;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientProfileInteractor implements IClientProfileInteractor {

    private static final String TAG = "ClientProfileInt";

    @Override
    public void getUser(Context context, String clientEmail, ClientProfileListener listener) {


        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointGetUser = apiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<User>>> call = endpointGetUser.getUserByEmail(clientEmail);

        call.enqueue(new Callback<Base<ArrayList<User>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<User>>> call, Response<Base<ArrayList<User>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onErrorGetData(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.getMessage());
                            listener.onErrorGetData(context.getString(R.string.algo_salio_mal));
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorGetData(apiError != null ? apiError.getError():context.getString(R.string.algo_salio_mal));
                        return;
                    }
                }
                //callback.onGetUID(response.body().getData());
                if (response.body().getData().isEmpty()) {
                    listener.onErrorGetData("No se encontro el usuario");
                    return;
                }
                listener.onGetUser(response.body().getData().get(0));
            }

            @Override
            public void onFailure(Call<Base<ArrayList<User>>> call, Throwable t) {
                Log.e(TAG, "ERROR CONEXION", t);
                listener.onErrorGetData(context.getString(R.string.error_network));
            }
        });

    }
}
