package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.ICharacteristicInteractor;
import mx.app.fashionme.pojo.Characteristic;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.restApi.model.CharacteristicResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by desarrollo1 on 20/03/18.
 */

public class CharacteristicInteractor implements ICharacteristicInteractor {

    public static final String TAG = CharacteristicInteractor.class.getSimpleName();

    @Override
    public void getDataFromApi(final OnGetData callback) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<Characteristic>>> characteristicResponseCall = endpointsApi.getCharacteristics();
        characteristicResponseCall.enqueue(new Callback<Base<ArrayList<Characteristic>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Characteristic>>> call, Response<Base<ArrayList<Characteristic>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")){
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
                        callback.onError(apiError != null ? apiError.getError() : "Algo salió mal");
                        return;
                    }

                }

                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Characteristic>>> call, Throwable t) {
                t.printStackTrace();
                callback.onError(t.getMessage());
            }
        });
    }

    @Override
    public void sendToServer(ListView listView, final Context context, final OnSendDataFinishedListener callback) {

        int countChars = listView.getAdapter().getCount();
        int ids[] = new int[countChars];

        for (int i = 0; i < countChars; i++) {
            CheckBox checkBox = listView.getAdapter().getView(i, null, listView).findViewById(R.id.chkSelection);
            if (checkBox.isChecked()) {
                Characteristic characteristic = (Characteristic) listView.getAdapter().getItem(i);
                ids[i] = characteristic.getId();
            }
        }

        ArrayList<Integer> idsList = new ArrayList<>();

        for (int id : ids) {
            if (id != 0)
                idsList.add(id);
        }

        if (idsList.size() == 0){
            callback.onErrorSendData("Debes elegir al menos un rasgo");
            return;
        }

        // Send list

        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<CharacteristicResponse> characteristicResponseCall = endpointsApi.sendCharacteristics(idsList, userId);

        characteristicResponseCall.enqueue(new Callback<CharacteristicResponse>() {
            @Override
            public void onResponse(Call<CharacteristicResponse> call, Response<CharacteristicResponse> response) {
                // Procesar errores
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onErrorSendData(response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            callback.onErrorSendData("Algo salio mal, intenta mas tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onErrorSendData(apiError != null ? apiError.getError() : null);
                        return;
                    }
                }
                SessionPrefs.get(context).saveCharacteristics();
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<CharacteristicResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                t.printStackTrace();
                callback.onErrorSendData("Algo salio mal, intenta mas tarde");
            }
        });
    }
}
