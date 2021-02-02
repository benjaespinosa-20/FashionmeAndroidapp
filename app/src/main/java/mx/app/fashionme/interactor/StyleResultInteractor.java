package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import mx.app.fashionme.interactor.interfaces.IStyleResultInteractor;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StyleResultInteractor implements IStyleResultInteractor {

    private static final String TAG = StyleResultInteractor.class.getSimpleName();

    @Override
    public void getResult(final Context context, final OnGetStyleFinished callback) {
        int userId = SessionPrefs.get(context).getUserId();
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<User>> userCall = endpointsApi.getUserById(userId);
        userCall.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        callback.onError("Algo salio mal, intenta más tarde");
                        try {
                            Log.e(TAG, response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError.getError());
                        Log.e(TAG, apiError.getError());
                        Log.e(TAG, "Code: " + apiError.getCode());
                        return;
                    }
                }

                SessionPrefs.get(context).saveStyleUser(
                        response.body().getData().getStyle().getData().getSpanish().getName(),
                        response.body().getData().getStyle().getData().getEnglish().getName());
                callback.onSuccess(response.body().getData().getStyle().getData());
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onError("Error en la conexión");

            }
        });

    }
}
